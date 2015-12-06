package kahwei.com.dm2230_mgp.Weapon;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import kahwei.com.dm2230_mgp.Object.Transform;
import kahwei.com.dm2230_mgp.R;
import kahwei.com.dm2230_mgp.Object.Vector3;

/**
 * Created by Koh Fang Shu on 6/12/2015.
 */
public class EnemyWeapon
{
    public static final float S_BULLET_SPEED = 300.f;
    protected ArrayList<ShotData> m_shotDatas;
    protected float m_fireRate; // Firerate of the weapon
    protected float m_deltaShots; // Time since last shot
    protected Bitmap m_bulletTexture; // Texture for the bullet

    public EnemyWeapon()
    {
        m_shotDatas = new ArrayList<ShotData>();
    }

    public void Init(float fireRate, Resources resources)
    {
        m_bulletTexture = BitmapFactory.decodeResource(resources, R.drawable.bullet_enemy);
        m_fireRate = fireRate;
        m_deltaShots = 0.f;
    }

    public void InitShots(Transform transform)
    {
        Vector3 scale = transform.m_scale;
        m_shotDatas.add(new ShotData(new Vector3(0.f, S_BULLET_SPEED, 0.f), new Vector3(scale.x * 0.5f, scale.y, 0.f)));
    }

    public void Update(final double dt)
    {
        if (m_deltaShots < m_fireRate)
        {
            m_deltaShots += dt;
        }
    }

    public void ResetShoot()
    {
        m_deltaShots = 0.f;
    }

    public boolean GetShoot()
    {
        if (m_deltaShots >= m_fireRate)
        {
            return true;
        }
        return false;
    }

    public Bitmap GetBulletTex()
    {
        return m_bulletTexture;
    }

    public ArrayList<ShotData> GetShotData()
    {
        return m_shotDatas;
    }
}