package kahwei.com.dm2230_mgp.Weapon;

import android.graphics.Bitmap;

import kahwei.com.dm2230_mgp.Object.GameObject;
import kahwei.com.dm2230_mgp.Object.Transform;
import kahwei.com.dm2230_mgp.Object.Vector3;

/**
 * Created by xecli on 12/5/2015.
 */
public class Bullet extends GameObject
{
    private Vector3 m_velocity;

    public Bullet()
    {
        m_velocity = new Vector3();
        m_velocity.Set(0.0f, 0.0f, 0.0f);
        SetActive(false);
    }

    public void Init(Bitmap mesh, boolean active, Vector3 velocity)
    {
        super.Init(mesh, active, active);
        m_velocity = velocity;
    }

    public void SetVelocity(Vector3 vel)
    {
        m_velocity = vel;
    }

    @Override
    public void Update(final double dt)
    {
        // Update the bullet position
        Transform tf = GetTransform();
        tf.m_translate = tf.m_translate.Add(m_velocity.ScalarMultiply((float)dt));
        SetTransform(tf);
    }
}
