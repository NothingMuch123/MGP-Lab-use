package kahwei.com.dm2230_mgp.Object;

import android.graphics.Bitmap;

/**
 * Created by Koh Fang Shu on 5/12/2015.
 */
public class Object
{
    private Bitmap m_mesh;
    private boolean m_render, m_active;

    public Object()
    {
        super();
    }

    public void Init(Bitmap mesh, boolean active, boolean render)
    {
        this.m_mesh = mesh;
        this.m_active = active;
        this.m_render = render;
    }

    public void Update(final double dt)
    {
    }

    public void Reset()
    {
        m_mesh.recycle();
        m_active = m_render = false;
    }

    public void SetMesh(Bitmap mesh)
    {
        this.m_mesh = mesh;
    }

    public Bitmap GetMesh()
    {
        return m_mesh;
    }

    public void SetRender(boolean render)
    {
        this.m_render = render;
    }

    public boolean GetRender()
    {
        return m_render;
    }

    public void SetActive(boolean active)
    {
        m_active = active;
    }

    public boolean GetActive()
    {
        return m_active;
    }
}
