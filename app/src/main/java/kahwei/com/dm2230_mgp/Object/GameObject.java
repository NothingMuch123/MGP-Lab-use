package kahwei.com.dm2230_mgp.Object;

import android.graphics.Bitmap;
import android.graphics.Canvas;

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
    public void SetTransform(Transform tf)
    {
        m_transform = tf;
    }
    public void Draw(Canvas canvas)
    {
        Vector3 tf = GetTransform().GetTranslate();
        Bitmap tex = GetMesh();

        canvas.drawBitmap(tex, tf.x - tex.getWidth() * 0.5f, tf.y - tex.getWidth() * 0.5f, null);
    }
}
