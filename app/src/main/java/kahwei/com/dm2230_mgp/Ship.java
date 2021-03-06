package kahwei.com.dm2230_mgp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

import kahwei.com.dm2230_mgp.Object.GameObject;
import kahwei.com.dm2230_mgp.Object.Transform;
import kahwei.com.dm2230_mgp.Object.Vector3;
import kahwei.com.dm2230_mgp.Weapon.BeamWeapon;
import kahwei.com.dm2230_mgp.Weapon.Bullet;
import kahwei.com.dm2230_mgp.Weapon.NormalWeapon;
import kahwei.com.dm2230_mgp.Weapon.ShotData;
import kahwei.com.dm2230_mgp.Weapon.SpikeWeapon;
import kahwei.com.dm2230_mgp.Weapon.Weapon;

/**
 * Created by xecli on 11/26/2015.
 */
public class Ship extends GameObject
{
    // Position
    private Vector3 m_defaultPos;

    // Ship Power and Weapons
    public enum PowerType
    {
        PT_NORMAL,
        PT_BEAM,
        PT_SPIKE
    };
    private PowerType m_power;
    private Weapon m_weapon[];

    // Textures
    private Bitmap m_shipTexture[];
    private Bitmap m_rankTexture;
    private SpriteAnimation m_rocketAnim;

    // Life
    private static final int MAX_LIVES = 5;
    private static final int STARTING_LIVES = 2;
    private int m_health;

    Ship()
    {
        super();
    }

    public void Init(Vector3 pos, Resources resources)
    {
        // Initialize variables
        m_defaultPos = pos;
        SetPositionX(pos.x);
        SetPositionY(pos.y);
        m_shipTexture = new Bitmap[PowerType.values().length];
        m_power = PowerType.PT_NORMAL;
        m_health = STARTING_LIVES;

        // Load Ship Textures
        m_shipTexture[PowerType.PT_NORMAL.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_normal);
        m_shipTexture[PowerType.PT_BEAM.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_beam);
        m_shipTexture[PowerType.PT_SPIKE.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_spike);
        m_rocketAnim = new SpriteAnimation(BitmapFactory.decodeResource(resources, R.drawable.rocket_anim), 496, 31, 16, 8);

        // Load Rank Texture
        m_rankTexture = BitmapFactory.decodeResource(resources, R.drawable.rank);

        // Load weapons
        m_weapon = new Weapon[PowerType.values().length];
        m_weapon[PowerType.PT_NORMAL.ordinal()] = new NormalWeapon(resources);
        m_weapon[PowerType.PT_BEAM.ordinal()] = new BeamWeapon(resources);
        m_weapon[PowerType.PT_SPIKE.ordinal()] = new SpikeWeapon(resources);

        super.Init(m_shipTexture[m_power.ordinal()], true, true);
    }

    @Override
    public void Update(final double dt)
    {
        // Update the collision
        super.Update(dt);
        // Update the weapon
        getCurrentWeapon().Update(dt);
        // Update the rocket animation
        m_rocketAnim.update(System.currentTimeMillis());
    }

    /*
     * Setters
     */
    public void SetPositionX(float posX)
    {
        Transform tf = GetTransform();
        tf.m_translate.x = posX;
        SetTransform(tf);
    }

    public void SetPositionY(float posY)
    {
        Transform tf = GetTransform();
        tf.m_translate.y = posY;
        SetTransform(tf);
    }
    /*
     * Getters
     */
    public float GetPositionX()
    {
        return GetTransform().m_translate.x;
    }

    public float GetPositionY()
    {
        return GetTransform().m_translate.y;
    }

    public float GetScaleX()
    {
        return GetMesh().getWidth();
    }

    public float GetScaleY()
    {
        return GetMesh().getHeight();
    }

    private Weapon getCurrentWeapon() { return m_weapon[m_power.ordinal()]; }

    @Override
    public Bitmap GetMesh()
    {
        return m_shipTexture[m_power.ordinal()];
    }

    @Override
    public void Draw(Canvas canvas)
    {
        float shipDrawPosX = GetPositionX() - GetScaleX() * 0.5f;
        float shipDrawPosY = GetPositionY() - GetScaleY() * 0.5f;
        float rankDrawPosY = shipDrawPosY + GetScaleY() * 0.5f;

        // Draw the rocket behind the ship
        m_rocketAnim.setX((int)(GetPositionX() - m_rocketAnim.getSpriteWidth() * 0.5f));
        m_rocketAnim.setY((int) (rankDrawPosY + (m_rankTexture.getHeight() * 0.4f) * (getCurrentWeapon().GetPowerLevel() + 1) + m_rocketAnim.getSpriteHeight() * 0.4f));
        m_rocketAnim.draw(canvas);

        // Draw the Ship
        canvas.drawBitmap(GetMesh(), shipDrawPosX, shipDrawPosY, null);

        for (int rankDrawn = 0; rankDrawn <= getCurrentWeapon().GetPowerLevel(); ++rankDrawn)
        {
            canvas.drawBitmap(m_rankTexture, GetPositionX() - m_rankTexture.getWidth() * 0.5f, rankDrawPosY, null);
            rankDrawPosY += m_rankTexture.getHeight() * 0.4f;
        }
    }

    public void Shoot(ArrayList<Bullet> bullets)
    {
        ArrayList<ShotData> shotDatas = getCurrentWeapon().GetShotDatas();

        // Don't shoot if not enough bullets
        if (bullets.size() < shotDatas.size())
        {
            return;
        }

        // We got enough bullets, now lets shoot them all out
        if (getCurrentWeapon().Shoot())
        {
            int bInfo = 0;

            // Shoot out all the bullets we need
            for (; bInfo < shotDatas.size(); ++ bInfo)
            {
                Bullet bullet = bullets.get(bInfo);
                bullet.Init(getCurrentWeapon().GetBulletTexture(), true, shotDatas.get(bInfo).m_velocity);
                Transform tf = bullet.GetTransform();
                tf.m_translate = GetTransform().m_translate.Add(shotDatas.get(bInfo).m_centerOffset);
                bullet.SetTransform(tf);
            }

            // Deactive all the bullets who were unused
            for (; bInfo < bullets.size(); ++bInfo)
            {
                Bullet bullet = bullets.get(bInfo);
                bullet.SetActive(false);
                bullet.SetRender(false);
            }
        }
    }

    public void DropHealth()
    {
        m_health--;

        if (m_health < 0)
        {
            m_health = 0;
        }
    }

    public void GainHealth()
    {
        m_health++;

        if (m_health > MAX_LIVES)
        {
            m_health = MAX_LIVES;
        }
    }

    public void RankUp()
    {
        getCurrentWeapon().LevelUp();
    }

    public void ChangeWeapon(PowerType pType)
    {
        // Only change if it is the same
        if (pType == m_power)
        {
            return;
        }

        // Set new weapon to old weapon's level
        m_weapon[pType.ordinal()].LevelUp(getCurrentWeapon().GetPowerLevel());
        // Reset this weapon
        getCurrentWeapon().ResetLevel();
        // Switch the weapon
        m_power = pType;
    }

    public void Kill()
    {
        // Decrease Health
        m_health--;

        // Reset to default position
        Transform tf = GetTransform();
        tf.m_translate.Set(m_defaultPos.x, m_defaultPos.y, m_defaultPos.z);
        SetTransform(tf);
    }

    public boolean IsAlive()
    {
        return m_health > 0;
    }

    public int GetHealth()
    {
        return m_health;
    }
}
