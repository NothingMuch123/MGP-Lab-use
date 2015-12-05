package kahwei.com.dm2230_mgp.Object;

/**
 * Created by Koh Fang Shu on 5/12/2015.
 */
public class Transform
{
    public Vector3 m_translate, m_rotate, m_scale;

    public Transform()
    {
        m_translate = new Vector3();
        m_rotate = new Vector3();
        m_scale = new Vector3();
    }

    public void Init(Vector3 translate, Vector3 rotate, Vector3 scale)
    {
        this.m_translate = translate;
        this.m_rotate = rotate;
        this.m_scale = scale;
    }

    public void Reset()
    {
        m_translate.SetZero();
        m_rotate.SetZero();
        m_scale.Set(1.f, 1.f, 1.f);
    }
}
