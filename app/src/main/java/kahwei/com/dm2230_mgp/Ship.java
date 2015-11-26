package kahwei.com.dm2230_mgp;

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

    // Scale
    private float m_scaleX;
    private float m_scaleY;

    Ship()
    {

    }

    void Init(float posX, float posY)
    {
        m_defaultPosX = m_positionX = posX;
        m_defaultPosY = m_positionY = posY;
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
    void SetScaleX(float scaleX)
    {
        m_scaleX = scaleX;
    }

    void SetScaleY(float scaleY)
    {
        m_scaleY = scaleY;
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
        return m_scaleX;
    }

    float GetScaleY()
    {
        return m_scaleY;
    }
}
