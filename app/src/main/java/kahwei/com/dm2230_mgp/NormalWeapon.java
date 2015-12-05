package kahwei.com.dm2230_mgp;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import kahwei.com.dm2230_mgp.Object.Vector3;

/**
 * Created by xecli on 12/5/2015.
 */
public class NormalWeapon extends Weapon
{
    public NormalWeapon(Resources r)
    {
        super();

        m_bulletTex[0] = m_bulletTex[1] = m_bulletTex[2] = BitmapFactory.decodeResource(r, R.drawable.bullet_normal);
        SetFireRate(100, 200, 300);
        Vector3 vel = new Vector3();
        vel.Set(0.0f, -500.0f, 0.0f);
        m_bulletVelocities.add(vel);
    }
}
