package kahwei.com.dm2230_mgp.PowerUp;

import kahwei.com.dm2230_mgp.Ship;

/**
 * Created by xecli on 12/6/2015.
 */
public class RankPowerUp extends PowerUp
{
    public void AffectShip(Ship ship)
    {
        ship.RankUp();
        SetActive(false);
    }
}
