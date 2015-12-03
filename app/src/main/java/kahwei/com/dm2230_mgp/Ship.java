package kahwei.com.dm2230_mgp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by xecli on 11/26/2015.
 */
public class Ship
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

    Ship()
    {

    }

    void Init(float posX, float posY, Resources resources)
    {
        m_defaultPosX = m_positionX = posX;
        m_defaultPosY = m_positionY = posY;
        m_shipTexture = new Bitmap[PowerType.values().length];
        m_power = PowerType.PT_NORMAL;
        m_powerLevel = 1;

        m_shipTexture[PowerType.PT_NORMAL.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_normal);
        m_shipTexture[PowerType.PT_BEAM.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_beam);
        m_shipTexture[PowerType.PT_SPIKE.ordinal()] = BitmapFactory.decodeResource(resources, R.drawable.ship_spike);
    }

    /*
     * Setters
     */
    void SetPositionX(float posX)
    {
        m_positionX = posX;
    }

    void SetPositionY(float posY)
    {
        m_positionY = posY;
    }
    /*
     * Getters
     */
    float GetPositionX()
    {
        return  m_positionX;
    }

    float GetPositionY()
    {
        return m_positionY;
    }

    float GetScaleX()
    {
        return GetShipTexture().getWidth();
    }

    float GetScaleY()
    {
        return GetShipTexture().getHeight();
    }

    Bitmap GetShipTexture()
    {
        return m_shipTexture[m_power.ordinal()];
    }
}
