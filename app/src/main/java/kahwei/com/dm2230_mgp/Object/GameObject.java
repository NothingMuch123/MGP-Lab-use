package kahwei.com.dm2230_mgp.Object;

import android.graphics.Bitmap;

/**
 * Created by Koh Fang Shu on 5/12/2015.
 */
public class GameObject extends Object
{
    private Transform m_transform;

    public GameObject()
    {
        super();
        m_transform = new Transform();
    }

    public void Init(Bitmap mesh, boolean active, boolean render)
    {
        super.Init(mesh, active, render);
        m_transform = new Transform();
    }

    public void Update(final double dt)
    {
    }

    public void Reset()
    {
        super.Reset();
        //CCollider::Reset();
        m_transform.Reset();
    }

    public Transform GetTransform()
    {
        return m_transform;
    }
}
