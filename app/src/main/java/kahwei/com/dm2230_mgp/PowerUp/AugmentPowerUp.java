package kahwei.com.dm2230_mgp.PowerUp;

import android.graphics.Bitmap;

import kahwei.com.dm2230_mgp.Object.Transform;
import kahwei.com.dm2230_mgp.Object.Vector3;
import kahwei.com.dm2230_mgp.Ship;

/**
 * Created by xecli on 12/7/2015.
 */
public class AugmentPowerUp extends PowerUp
{
	Ship.PowerType m_powerType;

	public void Init(Bitmap mesh, boolean active, boolean render, Vector3 position, Vector3 velocity, Ship.PowerType powerType)
	{
		super.Init(mesh, active, render, position, velocity);
		m_powerType = powerType;
	}

	@Override
	public void AffectShip(Ship ship)
	{
		ship.ChangeWeapon(m_powerType);
		SetActive(false);
	}
}
