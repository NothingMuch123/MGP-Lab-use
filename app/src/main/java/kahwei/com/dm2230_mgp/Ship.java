package kahwei.com.dm2230_mgp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

import kahwei.com.dm2230_mgp.Object.GameObject;
import kahwei.com.dm2230_mgp.Object.Transform;
import kahwei.com.dm2230_mgp.Object.Vector3;

/**
 * Created by xecli on 11/26/2015.
 */
public class Ship extends GameObject
{
    // Position
    private float m_defaultPosX;
    private float m_defaultPosY;
    private float m_positionX;
    private float m_positionY;

    // Ship Power
    public enum PowerType
    {
        PT_NORMAL,
        PT_BEAM,
        PT_SPIKE
    };
    private PowerType m_power;
    private Bitmap m_shipTexture[];
    private Bitmap m_rankTexture;
    private Weapon m_weapon;

    Ship()
    {
        super();
    }

    public void Init(float posX, float posY, Resources resources)
    {
        // Initialize variables
        m_defaultPosX = m_positionX = posX;
        m_defaultPosY = m_positionY = posY;
        m_shipTexture = new Bitmap[PowerType.values().length];
        m_power = PowerType.PT_NORMAL;

        // Load Ship Textures
        m_shipTexture[PowerType.PT_NORMAL.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_normal);
        m_shipTexture[PowerType.PT_BEAM.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_beam);
        m_shipTexture[PowerType.PT_SPIKE.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_spike);

        // Load Rank Texture
        m_rankTexture = BitmapFactory.decodeResource(resources, R.drawable.rank);

        // Load default weapon
        m_weapon = new NormalWeapon(resources);
    }

    @Override
    public void Update(final double dt)
    {
        m_weapon.Update(dt);
    }

    /*
     * Setters
     */
    public void SetPositionX(float posX)
    {
        m_positionX = posX;
    }

    public void SetPositionY(float posY)
    {
        m_positionY = posY;
    }
    /*
     * Getters
     */
    public float GetPositionX()
    {
        return  m_positionX;
    }

    public float GetPositionY()
    {
        return m_positionY;
    }

    public float GetScaleX()
    {
        return GetMesh().getWidth();
    }

    public float GetScaleY()
    {
        return GetMesh().getHeight();
    }

    @Override
    public Bitmap GetMesh()
    {
        return m_shipTexture[m_power.ordinal()];
    }

    public void Draw(Canvas canvas)
    {
        float shipDrawPosX = GetPositionX() - GetScaleX() * 0.5f;
        float shipDrawPosY = GetPositionY() - GetScaleY() * 0.5f;
        float rankDrawPosY = shipDrawPosY + GetScaleY() * 0.5f;

        // Draw the Ship
        canvas.drawBitmap(GetMesh(), shipDrawPosX, shipDrawPosY, null);

        for (int rankDrawn = 0; rankDrawn < m_weapon.GetPowerLevel(); ++rankDrawn)
        {
            canvas.drawBitmap(m_rankTexture, GetPositionX() - m_rankTexture.getWidth() * 0.5f, rankDrawPosY, null);
            rankDrawPosY += m_rankTexture.getHeight() * 0.4f;
        }
    }

    public void Shoot(Bullet bullet)
    {
        if (m_weapon.Shoot())
        {
            bullet.Init(m_weapon.GetBulletTexture(), true, m_weapon.GetBulletVelocity());
            Transform tf = bullet.GetTransform();
            tf.m_translate.Set(m_positionX, m_positionY);
            bullet.SetTransform(tf);
        }
    }
}
