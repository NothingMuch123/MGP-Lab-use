package kahwei.com.dm2230_mgp;

import android.graphics.Bitmap;

import kahwei.com.dm2230_mgp.Object.GameObject;
import kahwei.com.dm2230_mgp.Object.Transform;
import kahwei.com.dm2230_mgp.Object.Vector3;

/**
 * Created by xecli on 12/6/2015.
 */
public abstract class PowerUp extends GameObject
{
    private Vector3 m_velocity;

    public void Init(Bitmap mesh, boolean active, boolean render, Vector3 position, Vector3 velocity)
    {
        super.Init(mesh, active, render);
        Transform tf = GetTransform();
        tf.m_translate = position;
        SetTransform(tf);
        m_velocity = velocity;
    }

    @Override
    public void Update(final double dt)
    {
        super.Update(dt);
        // Update the bullet position
        Transform tf = GetTransform();
        Vector3 diff = m_velocity.ScalarMultiply((float)dt);
        tf.m_translate = tf.m_translate.Add(diff);
        SetTransform(tf);
    }

    // Function that is to be defined by child power ups that defines what the power up does to the ship
    public abstract void AffectShip(Ship ship);
}
