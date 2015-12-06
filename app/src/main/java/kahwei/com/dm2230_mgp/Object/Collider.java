package kahwei.com.dm2230_mgp.Object;

/**
 * Created by Koh Fang Shu on 5/12/2015.
 */
public class Collider
{
    public enum E_COLLIDER_TYPE
    {
        CT_NONE, // No collision
        CT_AABB, // AABB Collision
        CT_DIST, // Distance-based collision
        NUM_CT, // Total types of collision
    };

    public enum E_IGNORE_AXIS
    {
        IGNORE_X,
        IGNORE_Y,
        IGNORE_Z,
        NUM_IGNORE,
    };

    public enum E_Y_START
    {
        Y_BOTTOM,
        Y_MIDDLE,
        NUM_Y_START,
    };

    E_COLLIDER_TYPE m_type = E_COLLIDER_TYPE.CT_AABB; // Determine which collision method to use
    Vector3 m_minBound = new Vector3(), m_maxBound = new Vector3(); // AABB Collision data
    Vector3 m_diameter = new Vector3(), m_position = new Vector3(); // Distance-based collision data (Store as vector for irregular diameter like oval)
    boolean[] m_ignore = new boolean[E_IGNORE_AXIS.NUM_IGNORE.ordinal()];
    E_Y_START m_yStart = E_Y_START.Y_MIDDLE;
    boolean m_active = false;

    void SetType(E_COLLIDER_TYPE type, Transform transform)
    {
        this.m_type = type;
        calcAABB(transform);
        calcDist(transform);
    }

    E_COLLIDER_TYPE GetType()
    {
        return m_type;
    }

    void SetActive(boolean active)
    {
        this.m_active = active;
    }

    boolean GetActive()
    {
        return m_active;
    }

    Vector3 GetMinBound()
    {
        return m_minBound;
    }

    Vector3 GetMaxBound()
    {
        return m_maxBound;
    }

    Vector3 GetPosition()
    {
        return m_position;
    }

    Vector3 GetDiameter()
    {
        return m_diameter;
    }

    void SetIgnore(boolean x, boolean y, boolean z)
    {
        m_ignore[E_IGNORE_AXIS.IGNORE_X.ordinal()] = x;
        m_ignore[E_IGNORE_AXIS.IGNORE_Y.ordinal()] = y;
        m_ignore[E_IGNORE_AXIS.IGNORE_Z.ordinal()] = z;
    }

    boolean GetIgnore(E_IGNORE_AXIS index)
{
    return m_ignore[index.ordinal()];
}

    void Init(E_COLLIDER_TYPE type, Transform transform, E_Y_START yStart, boolean active)
    {
        this.m_type = type;
        calcAABB(transform);
        calcDist(transform);
        this.m_yStart = yStart;
        this.m_active = active;
    }

    void Update(Transform transform)
    {
        calcAABB(transform);
        calcDist(transform);
    }

    void Reset()
    {
        m_minBound = Vector3.ZERO_VECTOR;
        m_maxBound = Vector3.ZERO_VECTOR;
        m_position = Vector3.ZERO_VECTOR;
        m_diameter.Set(1.f, 1.f, 1.f);
        m_type = E_COLLIDER_TYPE.CT_AABB;
        m_yStart = E_Y_START.Y_BOTTOM;
        for (int i = 0; i < E_IGNORE_AXIS.NUM_IGNORE.ordinal(); ++i)
        {
            m_ignore[i] = false;
        }
        m_active = true;
    }

    boolean CollideWith(Collider other, final double dt)
    {
        if (!this.m_active || !other.GetActive()) // If one of the collider does not collide, no collision will occur, hence false
        {
            System.out.print("return");
            return false;
        }

        if (this.m_type == E_COLLIDER_TYPE.CT_AABB)
        {
            if (other.GetType() == E_COLLIDER_TYPE.CT_AABB)
            {
                // AABB - AABB collision
                return AABBCollision(other, dt);
            }
            else if (other.GetType() == E_COLLIDER_TYPE.CT_DIST)
            {
                // AABB - Dist collision
                return AABBCollision(other, dt); // Use AABB - AABB collision
            }
        }
        else if (this.m_type == E_COLLIDER_TYPE.CT_DIST)
        {
            if (other.GetType() == E_COLLIDER_TYPE.CT_AABB)
            {
                // Dist - AABB collision
                return AABBCollision(other, dt); // Use AABB - AABB collision
            }
            else if (other.GetType() == E_COLLIDER_TYPE.CT_DIST)
            {
                // Dist - Dist collision
                return distCollision(other, dt);
            }
        }

        return false;
    }

    void calcAABB(Transform transform)
    {
        switch (m_yStart)
        {
            case Y_BOTTOM:
            {
                m_minBound.Set(transform.m_translate.x - (transform.m_scale.x * 0.5f), transform.m_translate.y, transform.m_translate.z - (transform.m_scale.z * 0.5f));
                m_maxBound.Set(transform.m_translate.x + (transform.m_scale.x * 0.5f), transform.m_translate.y + transform.m_scale.y, transform.m_translate.z + (transform.m_scale.z * 0.5f));
            }
            break;
            case Y_MIDDLE:
            {
                m_minBound.Set(transform.m_translate.x - (transform.m_scale.x * 0.5f), transform.m_translate.y - (transform.m_scale.y * 0.5f), transform.m_translate.z - (transform.m_scale.z * 0.5f));
                m_maxBound.Set(transform.m_translate.x + (transform.m_scale.x * 0.5f), transform.m_translate.y + (transform.m_scale.y * 0.5f), transform.m_translate.z + (transform.m_scale.z * 0.5f));
            }
            break;
        }
    }

    void calcDist(Transform transform)
    {
        m_diameter = transform.m_scale;
        m_position = transform.m_translate;
    }

    boolean AABBCollision(Collider other, final double dt)
    {
        Vector3 oMin = other.GetMinBound();
        Vector3 oMax = other.GetMaxBound();
        if ((!(m_ignore[E_IGNORE_AXIS.IGNORE_X.ordinal()] || other.GetIgnore(E_IGNORE_AXIS.IGNORE_X)) && (m_maxBound.x < oMin.x || m_minBound.x > oMax.x)) ||
                (!(m_ignore[E_IGNORE_AXIS.IGNORE_Y.ordinal()] || other.GetIgnore(E_IGNORE_AXIS.IGNORE_Y)) && (m_maxBound.y < oMin.y || m_minBound.y > oMax.y)) ||
                (!(m_ignore[E_IGNORE_AXIS.IGNORE_Z.ordinal()] || other.GetIgnore(E_IGNORE_AXIS.IGNORE_Z)) && (m_maxBound.z < oMin.z || m_minBound.z > oMax.z))
                )
        {
            return false;
        }
        return true;
    }

    boolean distCollision(Collider other, final double dt)
    {
        // TODO: Make exceptions for oval

        // Temp dist collision (Only works with circles)
        float distSquared = (other.GetPosition().Subtract(m_position)).LengthSquared();
        float thisRadius = m_diameter.x * 0.5f, oRadius = other.GetDiameter().x * 0.5f;

        if (((thisRadius * thisRadius) + (oRadius * oRadius)) < distSquared)
        {
            return true;
        }
        return false;
    }

    boolean AABB_Dist_Collision(Collider other, final double dt)
    {
        // TODO: Do AABB - Dist collision check
        return false;
    }
}