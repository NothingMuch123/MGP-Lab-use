package kahwei.com.dm2230_mgp;

/**
 * Created by xecli on 12/6/2015.
 */
public class LifePowerUp extends PowerUp
{
    public void AffectShip(Ship ship)
    {
        ship.GainHealth();
    }
}
