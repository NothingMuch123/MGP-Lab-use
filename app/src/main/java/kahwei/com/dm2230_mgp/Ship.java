package kahwei.com.dm2230_mgp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import kahwei.com.dm2230_mgp.Object.GameObject;

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
    private int m_powerLevel;
    private final int MAX_POWER_LEVEL = 3;
    private Bitmap m_shipTexture[];
    private Bitmap m_rankTexture;

    Ship()
    {
        super();
    }

    public void Init(float posX, float posY, Resources resources)
    {
        m_defaultPosX = m_positionX = posX;
        m_defaultPosY = m_positionY = posY;
        m_shipTexture = new Bitmap[PowerType.values().length];
        m_power = PowerType.PT_NORMAL;
        m_powerLevel = 1;

        m_shipTexture[PowerType.PT_NORMAL.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_normal);
        m_shipTexture[PowerType.PT_BEAM.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_beam);
        m_shipTexture[PowerType.PT_SPIKE.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_spike);

        m_rankTexture = BitmapFactory.decodeResource(resources, R.drawable.rank);
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

        for (int rankDrawn = 0; rankDrawn < m_powerLevel; ++rankDrawn)
        {
            canvas.drawBitmap(m_rankTexture, GetPositionX() - m_rankTexture.getWidth() * 0.5f, rankDrawPosY, null);
            rankDrawPosY += m_rankTexture.getHeight() * 0.4f;
        }
    }
}
