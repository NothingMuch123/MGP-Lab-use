package kahwei.com.dm2230_mgp.Weapon;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import kahwei.com.dm2230_mgp.Object.Vector3;
import kahwei.com.dm2230_mgp.R;

/**
 * Created by xecli on 12/5/2015.
 */
public class NormalWeapon extends Weapon
{
    public NormalWeapon(Resources r)
    {
        super();

        m_bulletTex[0] = m_bulletTex[1] = m_bulletTex[2] = BitmapFactory.decodeResource(r, R.drawable.bullet_normal);

        m_weaponStates[0].Init(100, new ShotData(new Vector3(), new Vector3()));
        m_weaponStates[1].Init(200, new ShotData(new Vector3(), new Vector3()));
        m_weaponStates[2].Init(300, new ShotData(new Vector3(), new Vector3()));
    }
}
