package kahwei.com.dm2230_mgp.Object;

/**
 * Created by Koh Fang Shu on 5/12/2015.
 */
public class Vector2
{
    public static final Vector2 ZERO_VECTOR = new Vector2();
    public static final float EPSILON = 0.00001f;

    public float x, y;

    public boolean IsEqual(float a, float b) // Check equals
    {
        return a - b <= EPSILON && b - a <= EPSILON;
    }

    public Vector2() //Overloaded constructor
    {
        x = y = 0.0f;
    }

    public Vector2( float a, float b ) //Overloaded constructor
    {
        x = a;
        y = b;
    }

    public Vector2( final Vector2 rhs ) //copy constructor
    {
        x = rhs.x;
        y = rhs.y;
    }

    public void Set( float a, float b ) //Set all data
    {
        x = a;
        y = b;
    }

    public void SetZero()               // Set all data to zero
    {
        this.x = 0.f;
        this.y = 0.f;
    }

    public Vector2 Equal(final Vector2 rhs) //Assignment operator
    {
        x = rhs.x;
        y = rhs.y;
        return this;
    }

    public Vector2 Add( final Vector2 rhs )//Vector addition
    {
        Vector2 result = new Vector2();
        result.x = this.x + rhs.x;
        result.y = this.y + rhs.y;

        return result;
    }

    public Vector2 Subtract( final Vector2 rhs ) //Vector subtraction
    {
        Vector2 result = new Vector2();

        result.x = this.x - rhs.x;
        result.y = this.y - rhs.y;

        return result;
    }

    public Vector2 Negate() //Unary negation
    {
        Vector2 result = new Vector2();

        result.x = -this.x;
        result.y = -this.y;

        return result;
    }

    public Vector2 ScalarAdd(final float scalar) //Scalar addition
    {
        Vector2 result = new Vector2();

        result.x = this.x + scalar;
        result.y = this.y + scalar;

        return result;
    }

    public Vector2 ScalarMultiply(final float scalar) //Scalar multiplication
    {
        Vector2 result = new Vector2();

        result.x = this.x * scalar;
        result.y = this.y * scalar;

        return result;
    }

    public Vector2 AddTo(final Vector2 rhs) //Vector addition
    {
        this.x = this.x + rhs.x;
        this.y = this.y + rhs.y;

        return this;
    }

    public Vector2 SubtractTo(final Vector2 rhs) //Vector subtraction
    {
        this.x = this.x - rhs.x;
        this.y = this.y - rhs.y;

        return this;
    }

    public Vector2 ScalarAddTo(final float scalar) //Scalar addition
    {
        this.x = this.x + scalar;
        this.y = this.y + scalar;

        return this;
    }

    public Vector2 ScalarSubtractTo(final float scalar) //Scalar subtraction
    {
        this.x = this.x - scalar;
        this.y = this.y - scalar;

        return this;
    }

    public boolean isEqual(final Vector2 rhs) //Vector comparison
    {
        return IsEqual(x, rhs.x) && IsEqual(y, rhs.y);
    }

    public boolean notEqual(final Vector2 rhs) //Vector NOT comparison
    {
        return !IsEqual(x, rhs.x) || !IsEqual(y, rhs.y);
    }

    public float Length() //Get magnitude
    {
        return (float) Math.sqrt(LengthSquared());
    }

    public float LengthSquared() //Get magnitude
    {
        return x * x + y * y;
    }

    public float Dot(final Vector2 rhs) //Dot product
    {
        return (x * rhs.x) + (y * rhs.y);
    }

    public Vector2 Normalize() //Return a copy of this vector, normalized
    {
        float d = Length();
        if (d <= EPSILON && -d <= EPSILON)
        {
            //throw DivideByZero();
        }
        x /= d;
        y /= d;
        return this;
    }

    public Vector2 Normalized() //Return a copy of this vector, normalized
    {
        float d = Length();
        if (d <= EPSILON && -d <= EPSILON)
        {
            //throw DivideByZero();
        }
        Vector2 result = new Vector2();
        result.x = x / d;
        result.y = y / d;
        return result;
    }
}
