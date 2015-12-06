package kahwei.com.dm2230_mgp.Weapon;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import kahwei.com.dm2230_mgp.Object.Vector3;
import kahwei.com.dm2230_mgp.R;

/**
 * Created by xecli on 12/6/2015.
 */
public class SpikeWeapon extends Weapon
{
    public SpikeWeapon(Resources r)
    {
        super();

        m_bulletTex[0] = m_bulletTex[1] = m_bulletTex[2] = BitmapFactory.decodeResource(r, R.drawable.bullet_spike);

        m_weaponStates[0].Init(100,
                new ShotData(new Vector3(0.0f, -600.0f, 0.0f), new Vector3()));
        m_weaponStates[1].Init(300,
                new ShotData(new Vector3(0.0f, -850.0f, 0.0f), new Vector3()),
                new ShotData(new Vector3(850.0f, 0.0f, 0.0f), new Vector3()),
                new ShotData(new Vector3(-8500.0f, 0.0f, 0.0f), new Vector3()));
        m_weaponStates[2].Init(500,
                new ShotData(new Vector3(0.0f, -1200.0f, 0.0f), new Vector3()),
                new ShotData(new Vector3(1200.0f, 0.0f, 0.0f), new Vector3()),
                new ShotData(new Vector3(-1200.0f, 0.0f, 0.0f), new Vector3()));

    }
}
