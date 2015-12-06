package kahwei.com.dm2230_mgp.Weapon;

import kahwei.com.dm2230_mgp.Object.Vector3;

/**
 * Created by xecli on 12/6/2015.
 */
public class ShotData
{
    // The direction that this shot will travel relative to the shooter
    public Vector3 m_velocity;
    // This shot's position offset from the center of the shooter
    public Vector3 m_centerOffset;

    ShotData(Vector3 vel)
    {
        m_velocity = vel;
        m_centerOffset = new Vector3();
    }

    ShotData(Vector3 vel, Vector3 centerOffset)
    {
        m_velocity = vel;
        m_centerOffset = centerOffset;
    }
}
