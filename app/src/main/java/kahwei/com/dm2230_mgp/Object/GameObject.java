package kahwei.com.dm2230_mgp.Object;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Koh Fang Shu on 5/12/2015.
 */
public class GameObject extends Object
{
    private Transform m_transform;
    private Collider m_collider;

    public GameObject()
    {
        super();
        m_transform = new Transform();
        m_collider = new Collider();
    }

    public void Init(Bitmap mesh, boolean active, boolean render)
    {
        super.Init(mesh, active, render);
        if (mesh != null)
        {
            m_transform.m_scale.Set(mesh.getWidth(), mesh.getHeight(), 1.f);
        }
        m_collider.Init(Collider.E_COLLIDER_TYPE.CT_AABB, m_transform, Collider.E_Y_START.Y_BOTTOM, true);
    }

    public void Update(final double dt)
    {
        m_collider.Update(m_transform);
    }

    public void Reset()
    {
        super.Reset();
        m_transform.Reset();
    }

    public Transform GetTransform()
    {
        return m_transform;
    }
    public void SetTransform(Transform tf)
    {
        m_transform = tf;
    }
    public void Draw(Canvas canvas)
    {
        if (GetRender())
        {
            Vector3 tf = GetTransform().m_translate;
            Bitmap tex = GetMesh();

            canvas.drawBitmap(tex, tf.x - tex.getWidth() * 0.5f, tf.y - tex.getWidth() * 0.5f, null);
        }
    }

    public final Collider GetCollider()
    {
        return m_collider;
    }

    public boolean CollideWith(GameObject other, double dt)
    {
        return m_collider.CollideWith(other.GetCollider(), dt);
    }
}
