package kahwei.com.dm2230_mgp.Object;

/**
 * Created by Koh Fang Shu on 5/12/2015.
 */
public class Vector3 extends Vector2
{
    public static final Vector3 ZERO_VECTOR = new Vector3();

    public float z;

    public Vector3()
    {
        super();
        z = 0.0f;
    }

    public Vector3(float x, float y, float z)
    {
        super(x, y);
        z = 0.0f;
    }

    public Vector3(Vector3 copy)
    {
        x = copy.x;
        y = copy.y;
        z = copy.z;
    }

    public void Set( float a, float b, float c) //Set all data
    {
        Set(a, b);
        this.z = c;
    }

    public void SetZero() //Set all data to zero
    {
        super.SetZero();
        this.z = 0.f;
    }

    public boolean IsZero() //Check if data is zero
    {
        return IsEqual(x, 0.f) && IsEqual(y, 0.f) && IsEqual(z, 0.f);
    }

    public Vector3 Add( final Vector3 rhs ) //Vector addition
    {
        Vector3 result = new Vector3();
        result.x = this.x + rhs.x;
        result.y = this.y + rhs.y;
        result.z = this.z + rhs.z;

        return result;
    }

    public Vector3 AddTo( final Vector3 rhs )
    {
        this.x += rhs.x;
        this.y += rhs.y;
        this.z += rhs.z;
        return this;
    }

    public Vector3 Subtract( final Vector3 rhs ) //Vector subtraction
    {
        Vector3 result = new Vector3();
        result.x = this.x - rhs.x;
        result.y = this.y - rhs.y;
        result.z = this.z - rhs.z;

        return result;
    }

    public Vector3 SubtractTo( final Vector3 rhs )
    {
        this.x -= rhs.x;
        this.y -= rhs.y;
        this.z -= rhs.z;
        return this;
    }

    public Vector3 Negate() //Unary negation
    {
        Vector3 result = new Vector3();
        result.x = -this.x;
        result.y = -this.y;
        result.z = -this.z;

        return result;
    }

    public Vector3 ScalarMultiply( float scalar ) //Scalar multiplication
    {
        Vector3 result = new Vector3();
        result.x = this.x * scalar;
        result.y = this.y * scalar;
        result.z = this.z * scalar;

        return result;
    }

    public Vector3 ScalarMultiplyTo( float scalar )
    {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;

        return this;
    }

    public boolean isEqual( final Vector3 rhs ) //Equality check
    {
        return IsEqual(x, rhs.x) && IsEqual(y, rhs.y) && IsEqual(z, rhs.z);
    }

    public boolean notEqual ( final Vector3 rhs ) //Inequality check
    {
        return !IsEqual(x, rhs.x) && IsEqual(y, rhs.y) && IsEqual(z, rhs.z);
    }

    public Vector3 Equal(final Vector3 rhs) //Assignment operator
    {
        this.x = rhs.x;
        this.y = rhs.y;
        this.z = rhs.z;

        return this;
    }

    public float Length() //Get magnitude
    {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float LengthSquared() //Get square of magnitude
    {
        return x * x + y * y + z * z;
    }

    public float Dot( final Vector3 rhs ) //Dot product
    {
        return x * rhs.x + y * rhs.y + z * rhs.z;
    }

    public Vector3 Cross( final Vector3 rhs ) //Cross product
    {
        Vector3 result = new Vector3();
        result.x = y * rhs.z - z * rhs.y;
        result.y = z * rhs.x - x * rhs.z;
        result.z = x * rhs.y - y * rhs.x;

        return result;
    }

    //Return a copy of this vector, normalized
    //Throw a divide by zero exception if normalizing a zero vector
    public Vector3 Normalized()
    {
        float d = Length();
        if(d <= EPSILON && -d <= EPSILON)
        {
            //throw DivideByZero();
        }
        Vector3 result = new Vector3();
        result.x = x / d;
        result.y = y / d;
        result.z = z / d;

        return result;
    }

    //Normalize this vector and return a reference to it
    //Throw a divide by zero exception if normalizing a zero vector
    public Vector3 Normalize()
    {
        float d = Length();
        if (d <= EPSILON && -d <= EPSILON)
        {
            //throw DivideByZero();
        }
        x /= d;
        y /= d;
        z /= d;

        return this;
    }
}
