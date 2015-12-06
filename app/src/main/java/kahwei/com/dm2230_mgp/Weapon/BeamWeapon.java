package kahwei.com.dm2230_mgp.Weapon;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import kahwei.com.dm2230_mgp.Object.Vector3;
import kahwei.com.dm2230_mgp.R;

/**
 * Created by xecli on 12/6/2015.
 */
public class BeamWeapon extends Weapon
{
	public BeamWeapon(Resources r)
	{
		super();

		m_bulletTex[0] = m_bulletTex[1] = m_bulletTex[2] = BitmapFactory.decodeResource(r, R.drawable.bullet_beam);

		m_weaponStates[0].Init(800,
				new ShotData(new Vector3(0.0f, -1000.0f, 0.0f), new Vector3()));
		m_weaponStates[1].Init(800,
				new ShotData(new Vector3(0.0f, -1000.0f, 0.0f), new Vector3(-m_bulletTex[1].getWidth() * 0.5f, 0.0f, 0.0f)),
				new ShotData(new Vector3(0.0f, -1000.0f, 0.0f), new Vector3(m_bulletTex[1].getWidth() * 0.5f, 0.0f, 0.0f)));
		m_weaponStates[2].Init(800,
				new ShotData(new Vector3(0.0f, -1000.0f, 0.0f), new Vector3(-m_bulletTex[1].getWidth(), 0.0f, 0.0f)),
				new ShotData(new Vector3(0.0f, -1000.0f, 0.0f), new Vector3()),
				new ShotData(new Vector3(0.0f, -1000.0f, 0.0f), new Vector3(m_bulletTex[1].getWidth(), 0.0f, 0.0f)));

	}
}
