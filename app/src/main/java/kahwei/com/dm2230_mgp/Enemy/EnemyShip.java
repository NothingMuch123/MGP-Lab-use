package kahwei.com.dm2230_mgp.Enemy;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

import kahwei.com.dm2230_mgp.Object.GameObject;
import kahwei.com.dm2230_mgp.Object.Transform;
import kahwei.com.dm2230_mgp.Object.Vector3;
import kahwei.com.dm2230_mgp.R;
import kahwei.com.dm2230_mgp.Weapon.Bullet;
import kahwei.com.dm2230_mgp.Weapon.EnemyWeapon;
import kahwei.com.dm2230_mgp.Weapon.ShotData;

/**
 * Created by Koh Fang Shu on 5/12/2015.
 */
public class EnemyShip extends GameObject
{
    static final float S_OFF_SCREEN_OFFSET = 280.f;
    static final float S_SPEED = 300.f;

    // Bitmap creation
    static final int S_TOTAL_ENEMY_MESHES = 2;
    static Bitmap s_enemy_mesh_list[];

    public static void CreateEnemyMesh(Resources resources) // Must be called once at the start before using this class
    {
        s_enemy_mesh_list = new Bitmap[S_TOTAL_ENEMY_MESHES];
        s_enemy_mesh_list[0] = BitmapFactory.decodeResource(resources, R.drawable.enemy1);
        s_enemy_mesh_list[1] = BitmapFactory.decodeResource(resources, R.drawable.enemy2);
    }

    enum E_MOVE_TYPE
    {
        MOVE_NONE, // Do nothing
        MOVE_TO_DEST_RETREAT, // Move to destination and stay for a short period of time before moving to default position
        NUM_MOVE,
    };

    E_MOVE_TYPE m_type;
    private Vector3 m_defaultPos;
    private Vector3 m_destination;
    private ArrayList<Vector3> m_subDestinations;
    private int m_currentSubDestination;
    private float m_timeTillDespawn; // -1 means does not despawn
    private Vector3 m_dir;
    private boolean m_reached;
    EnemyWeapon m_weapon; // Weapon

    public EnemyShip()
    {
        super();
        m_defaultPos = new Vector3();
        m_destination = new Vector3();
        m_subDestinations = new ArrayList<Vector3>();
        m_currentSubDestination = 0;
        m_dir = new Vector3();
        m_dir.Set(0.f, -1.f, 0.f);
        m_type = E_MOVE_TYPE.MOVE_NONE;
        m_timeTillDespawn = -1.f;
        m_reached = false;
        m_weapon = new EnemyWeapon();
    }

    public void Init(int screenWidth, int screenHeight, Resources resources)
    {
        super.Init(getRandomMesh(), true, true);
        generateMovement(screenWidth, screenHeight);
    }

    public void InitWeapon(float fireRate, Resources resources)
    {
        m_weapon.Init(fireRate, resources);
        m_weapon.InitShots(GetTransform());
    }

    public void Update(final double dt, int screenWidth, int screenHeight)
    {
        super.Update(dt);
        // Update the time till enemy ship despawn
        if (m_timeTillDespawn > 0.f)
        {
            m_timeTillDespawn -= dt;
            if (m_timeTillDespawn < 0.f)
            {
                m_timeTillDespawn = 0.f;
                // Changing variables for despawning
                switch(m_type)
                {
                    case MOVE_TO_DEST_RETREAT:
                    {
                        changeDestination(m_defaultPos);
                    }
                    break;
                }
            }
        }

        if (m_timeTillDespawn == 0.f) // Despawn
        {
            if (checkDespawn(screenWidth, screenHeight))
            {
                // Despawn now
                Reset();
            }
            else
            {
                // Can't despawn yet, need to move out of screen to despawn
                move(dt);
            }
        }
        else // Don't despawn
        {
            move(dt);
        }

        m_weapon.Update(dt);
    }

    public void Reset()
    {
        super.Reset();
        m_defaultPos.SetZero();
        m_destination.SetZero();
        m_subDestinations.clear();
        m_currentSubDestination = 0;
        m_dir.Set(0.f, -1.f, 0.f);
        m_type = E_MOVE_TYPE.MOVE_NONE;
        m_timeTillDespawn = -1.f;
        m_reached = false;
    }

    public boolean Shoot(ArrayList<Bullet> bulletList)
    {
        if (m_weapon.GetShoot())
        {
            // Enemy can shoot
            if (bulletList.size() < m_weapon.GetShotData().size())
            {
                return false; // Fail to shoot as not enough bullet in buffer
            }

            int bInfo = 0;
            ArrayList<ShotData> shotDatas = m_weapon.GetShotData();

            // Shoot out all the bullets we need
            for (; bInfo < shotDatas.size(); ++ bInfo)
            {
                Bullet bullet = bulletList.get(bInfo);
                bullet.Init(m_weapon.GetBulletTex(), true, shotDatas.get(bInfo).m_velocity);
                Transform tf = bullet.GetTransform();
                tf.m_translate = GetTransform().m_translate.Add(shotDatas.get(bInfo).m_centerOffset);
                bullet.SetTransform(tf);
            }

            // Deactive all the bullets who were unused
            for (; bInfo < bulletList.size(); ++bInfo)
            {
                Bullet bullet = bulletList.get(bInfo);
                bullet.SetActive(false);
                bullet.SetRender(false);
            }
            m_weapon.ResetShoot();
            return true;
        }
        return false;
    }

    public void Draw(Canvas canvas)
    {
        Vector3 pos = GetTransform().m_translate;
        canvas.drawBitmap(GetMesh(), pos.x, pos.y, null);
    }

    private void generateMovement(int screenWidth, int screenHeight)
    {
        Random random = new Random();

        m_type = E_MOVE_TYPE.values()[random.nextInt(E_MOVE_TYPE.NUM_MOVE.ordinal() - 1) + 1]; // "- 2 + 1" to ensure that MOVE_NONE will never be chose during random

        // Generate of ship position
        Vector3 pos = generateSpawnPoint(screenWidth, screenHeight);
        GetTransform().m_translate.Equal(pos);
        m_defaultPos.Equal(pos);

        Vector3 scale = GetTransform().m_scale;
        switch(m_type)
        {
            case MOVE_TO_DEST_RETREAT:
            {
                m_destination.Equal(generateDestination(screenWidth, screenHeight));
                m_dir.Equal(calcDir(GetTransform().m_translate, m_destination));
                Vector3 newSub = new Vector3(0.f, m_destination.y, 0.f); // Point 1 of sub destination
                m_subDestinations.add(newSub);
                newSub = new Vector3(screenWidth - scale.x, m_destination.y, 0.f); // Point 2 of sub destination
                m_subDestinations.add(newSub);
                m_timeTillDespawn = 30.f;
            }
            break;
        }
    }

    private Vector3 calcDir(Vector3 pos, Vector3 target)
    {
        return (target.Subtract(pos)).Normalized();
    }

    private Vector3 generateSpawnPoint(int screenWidth, int screenHeight)
    {
        Random random = new Random(); // Random
        Vector3 pos = new Vector3(); // Pos of spawn point
        Vector3 scale = GetTransform().m_scale;
        pos.y = random.nextInt((int) (screenHeight * 0.75f) + (int)(S_OFF_SCREEN_OFFSET * 0.5f)) - (int)(S_OFF_SCREEN_OFFSET * 0.5f);
        if (pos.y < 0.f)
        {
            // Outside of screen for Y axis, can generate anywhere along X axis
            pos.x = random.nextInt(screenWidth + (int)S_OFF_SCREEN_OFFSET) - S_OFF_SCREEN_OFFSET * 0.5f;
        }
        else
        {
            // Inside of screen for Y axis, can only generate near the left or right
            int tempX = random.nextInt((int)S_OFF_SCREEN_OFFSET);
            if (tempX < S_OFF_SCREEN_OFFSET * 0.5f)
            {
                // Left side
                pos.x -= (tempX - S_OFF_SCREEN_OFFSET * 0.5f) - scale.x;
            }
            else
            {
                // Right side
                pos.x += screenWidth + tempX - (S_OFF_SCREEN_OFFSET * 0.5f);
            }
        }
        return pos;
    }

    private Vector3 generateDestination(int screenWidth, int screenHeight)
    {
        Random random = new Random();
        Vector3 result = new Vector3();
        Vector3 scale = GetTransform().m_scale;
        result.Set(random.nextInt((int)(screenWidth - scale.x)), random.nextInt((int)(screenHeight * 0.75f - scale.y)), 0.f);
        return result;
    }

    private void move(final double dt)
    {
        if (m_reached && m_timeTillDespawn != 0.f) // Reached
        {
            switch (m_type)
            {
                case MOVE_TO_DEST_RETREAT:
                {
                    if (m_subDestinations.size() > 0)
                    {
                        if (m_destination.isEqual(m_subDestinations.get(m_currentSubDestination))) // If current sub destination assigned and reached
                        {
                            ++m_currentSubDestination;
                            if (m_currentSubDestination == m_subDestinations.size()) // Reached the end of sub destination, reset
                            {
                                m_currentSubDestination = 0;
                            }
                            changeDestination(m_subDestinations.get(m_currentSubDestination));
                        }
                        else // Current sub destination not assigned yet
                        {
                            changeDestination(m_subDestinations.get(m_currentSubDestination));
                        }
                    }
                }
                break;
            }
            return;
        }
        else // Not reached
        {
            Vector3 pos = GetTransform().m_translate;
            float lengthBetweenPointsSquared = (pos.Subtract(m_destination)).LengthSquared();
            float movement = S_SPEED * (float)dt;
            float movementSquared = movement * movement;
            if (movementSquared >= lengthBetweenPointsSquared) // Will reach or pass destination in this frame
            {
                GetTransform().m_translate.Equal(m_destination);
                m_reached = true;
            }
            else
            {
                GetTransform().m_translate.AddTo( m_dir.ScalarMultiply(movement) ); // Move
            }
        }
    }

    private Bitmap getRandomMesh()
    {
        Random random = new Random();
        return s_enemy_mesh_list[random.nextInt(S_TOTAL_ENEMY_MESHES)];
    }

    private boolean checkDespawn(int screenWidth, int screenHeight)
    {
        Vector3 pos = GetTransform().m_translate;
        Vector3 scale = GetTransform().m_scale;
        if (pos.x + scale.x * 0.5f < 0.f || pos.x - scale.x * 0.5f > screenWidth || pos.y - scale.y * 0.5f < 0.f || pos.y - scale.y * 0.5f > screenHeight)
        {
            return true;
        }
        return false;
    }

    private void changeDestination(Vector3 newDestination)
    {
        m_destination.Equal(newDestination);
        m_reached = false;
        m_dir.Equal(calcDir(GetTransform().m_translate, newDestination));
    }
}
