package kahwei.com.dm2230_mgp.Object;

/**
 * Created by Koh Fang Shu on 5/12/2015.
 */
public class Transform
{
    private Vector3 m_translate, m_rotate, m_scale;

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

    public void SetTranslate(Vector3 translate)
    {
        this.m_translate = translate;
    }

    public void SetTranslateX(float x)
    {
        this.m_translate.x = x;
    }

    public void SetTranslateY(float y)
    {
        this.m_translate.y = y;
    }

    public void SetTranslateZ(float z)
    {
        this.m_translate.z = z;
    }

    public Vector3 GetTranslate()
    {
        return m_translate;
    }

    public void SetRotate(Vector3 rotate)
    {
        this.m_rotate = rotate;
    }

    public void SetRotateX(float x)
    {
        this.m_rotate.x = x;
    }

    public void SetRotateY(float y)
    {
        this.m_rotate.y = y;
    }

    public void SetRotateZ(float z)
    {
        this.m_rotate.z = z;
    }

    public Vector3 GetRotate()
    {
        return m_rotate;
    }

    public void SetScale(Vector3 scale)
    {
        this.m_scale = scale;
    }

    public void SetScaleX(float x)
    {
        this.m_scale.x = x;
    }

    public void SetScaleY(float y)
    {
        this.m_scale.y = y;
    }

    public void SetScaleZ(float z)
    {
        this.m_scale.z = z;
    }

    public Vector3 GetScale()
    {
        return m_scale;
    }
}
