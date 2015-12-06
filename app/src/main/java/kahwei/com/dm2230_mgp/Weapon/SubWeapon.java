package kahwei.com.dm2230_mgp.Weapon;

import java.util.ArrayList;

/**
 * Created by xecli on 12/6/2015.
 * ***********************************
 *  A subweapon is the specs of a weapon at each power level
 */
public class SubWeapon
{
    // The time between shots
    private float m_fireRate = 0.0f;
    // List of Bullet shot data for each shot
    protected ArrayList<ShotData> m_shotDatas;

    SubWeapon()
    {
        m_fireRate = 0.0f;
    }

    SubWeapon(float fireRate, ShotData... shotData)
    {
        Init(fireRate, shotData);
    }

    void Init(float fireRate, ShotData... shotData)
    {
        SetFireRate(fireRate);
        m_shotDatas = new ArrayList<ShotData>();

        // Add all the possible ShotData
        for (ShotData s : shotData)
        {
            m_shotDatas.add(s);
        }
    }

    public void SetFireRate(float roundsPerMinute)
    {
        m_fireRate = 1 / (roundsPerMinute / 60);
    }

    public float GetFireRate()
    {
        return m_fireRate;
    }
}
