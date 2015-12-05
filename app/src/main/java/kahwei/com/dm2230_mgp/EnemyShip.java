package kahwei.com.dm2230_mgp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

import kahwei.com.dm2230_mgp.Object.GameObject;
import kahwei.com.dm2230_mgp.Object.Vector3;

/**
 * Created by Koh Fang Shu on 5/12/2015.
 */
public class EnemyShip extends GameObject
{
    static final float S_OFF_SCREEN_OFFSET = 280.f;
    static final float S_SPEED = 100.f;

    // Bitmap creation
    static final int S_TOTAL_ENEMY_MESHES = 2;
    static Bitmap s_enemy_mesh_list[];

    static void CreateEnemyMesh(Resources resources) // Must be called once at the start before using this class
    {
        s_enemy_mesh_list = new Bitmap[S_TOTAL_ENEMY_MESHES];
        s_enemy_mesh_list[0] = BitmapFactory.decodeResource(resources, R.drawable.enemy1);
        s_enemy_mesh_list[1] = BitmapFactory.decodeResource(resources, R.drawable.enemy2);
    }

    enum E_MOVE_TYPE
    {
        MOVE_NONE, // Do nothing
        MOVE_TO_DEST, // Move to destination and stop
        MOVE_TO_DEST_RETREAT, // Move to destination and stay for a short period of time before moving to default position
        NUM_MOVE,
    };

    E_MOVE_TYPE m_type;
    private Vector3 m_defaultPos;
    private Vector3 m_destination;
    private float m_timeTillDespawn; // -1 means does not despawn
    private Vector3 m_dir;
    private boolean m_reached;

    public EnemyShip()
    {
        super();
        m_defaultPos = new Vector3();
        m_destination = new Vector3();
        m_dir = new Vector3();
        m_dir.Set(0.f, -1.f, 0.f);
        m_type = E_MOVE_TYPE.MOVE_NONE;
        m_timeTillDespawn = -1.f;
        m_reached = false;
    }

    public void Init(int screenWidth, int screenHeight, Resources resources)
    {
        super.Init(getRandomMesh(), true, true);
        generateMovement(screenWidth, screenHeight);
    }

    public void Update(final double dt, int screenWidth, int screenHeight)
    {
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
    }

    public void Reset()
    {
        super.Reset();
        m_defaultPos.SetZero();
        m_destination.SetZero();
        m_dir.Set(0.f, -1.f, 0.f);
        m_type = E_MOVE_TYPE.MOVE_NONE;
        m_timeTillDespawn = -1.f;
        m_reached = false;
    }

    public void Draw(Canvas canvas)
    {
        Vector3 pos = GetTransform().m_translate;
        canvas.drawBitmap(GetMesh(), pos.x, pos.y, null);
    }

    private void generateMovement(int screenWidth, int screenHeight)
    {
        Random random = new Random();

        // Generate of ship position
        Vector3 pos = new Vector3();
        Vector3 scale = GetTransform().m_scale;
        pos.y = random.nextInt((int) (screenHeight * 0.75f) + (int)(S_OFF_SCREEN_OFFSET * 0.5f)) - (int)(S_OFF_SCREEN_OFFSET * 0.5f) - (int)(scale.y * 0.5f);
        if (pos.y < 0.f)
        {
            // Outside of screen for Y axis, can generate anywhere along X axis
            pos.x = random.nextInt(screenWidth + (int)S_OFF_SCREEN_OFFSET);
        }
        else
        {
            // Inside of screen for Y axis, can only generate near the left or right
            int tempX = random.nextInt((int)S_OFF_SCREEN_OFFSET);
            if (tempX < S_OFF_SCREEN_OFFSET * 0.5f)
            {
                // Left side
                pos.x -= tempX - scale.x * 0.5f;
            }
            else
            {
                // Right side
                pos.x += screenWidth + tempX - (S_OFF_SCREEN_OFFSET * 0.5f) + scale.x * 0.5f;
            }
        }
        GetTransform().m_translate = pos;
        m_defaultPos = pos;

        m_destination.Set(random.nextInt(screenWidth), random.nextInt((int) (screenHeight * 0.75f)), 0.f);
        m_dir =  (m_destination.Subtract(GetTransform().m_translate)).Normalized(); // Calculate direction to destination

        m_type = E_MOVE_TYPE.values()[random.nextInt(E_MOVE_TYPE.NUM_MOVE.ordinal() - 1)];

        switch(m_type)
        {
            case MOVE_TO_DEST:
            {
                m_timeTillDespawn = -1.f;
            }
            break;
            case MOVE_TO_DEST_RETREAT:
            {
                m_timeTillDespawn = 10.f;
            }
            break;
        }
    }

    private void move(final double dt)
    {
        if (m_reached) // Reached
        {
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
                GetTransform().m_translate = m_destination;
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
        m_destination = newDestination;
        m_reached = false;
    }
}
