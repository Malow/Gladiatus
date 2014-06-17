#ifndef MALOWVECTOR
#define MALOWVECTOR

#include <math.h>

// Edit 2012-11-18 by Alexivan - Removed DX dependencies
// Edit 2012-11-23 by Alexivan - Added DX Conversions
// EDIT 2012-12-17 by Tillman  - Added GetD3DXVECTORX and Union & []-operator overloading.
// EDIT 2012-12-18 by Alexivan - Warning Ignore for nameless struct in union
// EDIT 2012-12-18 by Alexivan - GetLength function made constant
// EDIT 2012-12-19 by Alexivan - Added Less Than Comparison, Removed Destructors
// EDIT 2012-12-19 by Alexivan - Removed Destructors, Fixed GetRotated, Normalize with big N, Made some functions constant
// EDIT 2013-01-07 by Tillman  - Optimized Constructors.
// EDIT 2013-01-09 by Alexivan - GetXY, GetXZ, GetZY for Vector3
// EDIT 2013-01-11 by Tillman - Added the class Vector2UINT.
// EDIT 2013-01-22 by Alexivan - Vector2 Multiply.
// EDIT 2013-01-30 by Alexivan - Explicit Constructors, scalar addition and substraction
// EDIT 2013-01-30 by Crant	- Added Vector3 == Operator
// EDIT 2013-02-28 by Alexivan	- Added Vector3 != Operator

#pragma warning ( push ) 
#pragma warning ( disable : 4201 ) // nonstandard extension used : nameless struct/union
#pragma warning ( disable : 4290 ) // C++ exception specification ignored except to indicate a function is not __declspec(nothrow)

class Vector2UINT
{
public:
	union
	{
		//the variable "values" and x and y share the same memory
		float values[2];
		struct
		{
			unsigned int x; //values[0]
			unsigned int y; //values[1]
		};
	};
	

	explicit Vector2UINT(unsigned int x=0, unsigned int y=0) : x(x), y(y)
	{

	}

	inline bool operator<( const Vector2UINT& v ) const
	{
		if ( x < v.x ) return true;
		if ( v.x < x ) return false;
		if ( y < v.y ) return true;
		if ( v.y < y ) return false;

		return false;
	}

	inline bool operator==( const Vector2UINT &v ) const
	{
		return ( x == v.x && y == v.y );
	}

	inline Vector2UINT operator+( const Vector2UINT& v ) const
	{
		return Vector2UINT( x + v.x, y + v.y );
	}

	inline Vector2UINT operator+( const unsigned int& scalar ) const
	{
		return Vector2UINT( x + scalar, y + scalar );
	}

	inline Vector2UINT operator-( const Vector2UINT& v ) const
	{
		return Vector2UINT( x - v.x, y - v.y );
	}

	inline Vector2UINT operator-( const unsigned int& scalar ) const
	{
		return Vector2UINT( x - scalar, y - scalar );
	}

	inline Vector2UINT operator*( const Vector2UINT& v ) const
	{
		return Vector2UINT( x * v.x, y * v.y );
	}

	inline Vector2UINT operator*( unsigned int v ) const
	{
		return Vector2UINT( x * v, y * v );
	}

	inline float& operator[]( unsigned int i ) throw(...)
	{
		if(i > 1)
		{
			throw("index out of bounds");
		}
		return values[i];
	}

	inline const float& operator[]( unsigned int i ) const throw(...)
	{
		if(i > 1)
		{
			throw("index out of bounds");
		}
		return values[i];
	}

};

class Vector2
{
public:
	union
	{
		//the variable "values" and x and y share the same memory
		float values[2];
		struct
		{
			float x; //values[0]
			float y; //values[1]
		};
	};

	explicit Vector2(float _x = 0.0f, float _y = 0.0f) : x(_x), y(_y)
	{
		
	}

	inline float GetLength() const
	{
		return sqrtf(this->x * this->x + this->y * this->y);
	}

	inline Vector2& Normalize()
	{
		float length = this->GetLength();
		if(length > 0.0f)
		{
			this->x /= length;
			this->y /= length;
		}
		return *this;
	}

	inline bool operator<( const Vector2& v ) const
	{
		if ( x < v.x ) return true;
		if ( v.x < x ) return false;
		if ( y < v.y ) return true;
		if ( v.y < y ) return false;
		return false;
	}

	inline Vector2 operator-( const Vector2& v ) const
	{
		return Vector2( x - v.x, y - v.y );
	}

	inline Vector2 operator-( const float& scalar ) const
	{
		return Vector2( x - scalar, y - scalar );
	}

	inline Vector2 operator+( const Vector2& v ) const
	{
		return Vector2( x + v.x, y + v.y );
	}

	inline Vector2 operator+( const float& scalar ) const
	{
		return Vector2( x + scalar, y + scalar );
	}

	inline bool operator==( const Vector2& v) const
	{
		return ( x == v.x && y == v.y );
	}

	inline float& operator[]( unsigned int i ) throw(...)
	{
		if(i > 1)
		{
			throw("index out of bounds");
		}
		return values[i];
	}

	inline const float& operator[]( unsigned int i ) const throw(...)
	{
		if(i > 1)
		{
			throw("index out of bounds");
		}
		return values[i];
	}

	inline Vector2 operator*( const float& t ) const
	{
		return Vector2( x * t, y * t );
	}

	inline Vector2& operator*=( const float& t )
	{
		x *= t;
		y *= t;
		return *this;
	}

	inline Vector2& operator+=( const float& t )
	{
		x += t;
		y += t;
		return *this;
	}

	inline Vector2& operator+=( const Vector2& t )
	{
		x += t.x;
		y += t.y;
		return *this;
	}
};


class Vector3
{
public:
	union
	{
		//the variable "values" and x,y and z share the same memory
		float values[3];
		struct
		{
			float x; //values[0]
			float y; //values[1]
			float z; //values[2]
		};
	};

	explicit Vector3(float _x = 0.0f, float _y = 0.0f, float _z = 0.0f) : x(_x), y(_y), z(_z)
	{

	}

	inline float GetLength() const
	{
		return sqrtf(this->x * this->x + this->y * this->y + this->z * this->z);
	}

	inline Vector3& Normalize()
	{
		float length = this->GetLength();
			
		if(length > 0.0f)
		{
			this->x /= length;
			this->y /= length;
			this->z /= length;
		}

		return *this;
	}

	inline float GetDotProduct(const Vector3& compObj) const
	{
		float dot = this->x * compObj.x;
		dot += this->y * compObj.y;
		dot += this->z * compObj.z;
		return dot;
	}

	inline Vector3 GetCrossProduct(const Vector3& vec) const
	{
		Vector3 retVec;
		retVec.x = this->y * vec.z - vec.y * this->z;
		retVec.y = -(this->x * vec.z - vec.x * this->z);
		retVec.z = this->x * vec.y - vec.x * this->y;

		return retVec;
	}

	inline float GetAngle(const Vector3& compObj)
	{
		return acos(this->GetDotProduct(compObj) / (this->GetLength() * compObj.GetLength()));
	}
	// new for physics
		
	inline Vector3 operator+(const Vector3& v) const
    {
        return Vector3(this->x+v.x, this->y+v.y, this->z+v.z);
    }

	inline Vector3 operator+(const float& scalar) const
	{
		return Vector3(this->x+scalar, this->y+scalar, this->z+scalar);
	}

	inline Vector3 operator-(const Vector3& v) const
	{
		return Vector3(this->x-v.x, this->y-v.y, this->z-v.z);
	}

	inline Vector3 operator-( const float& scalar ) const
	{
		return Vector3( x - scalar, y - scalar, z - scalar );
	}

	inline Vector3 operator*(const float& scalar) const
	{
		return Vector3(this->x*scalar, this->y*scalar, this->z*scalar);
	}

	inline Vector3 operator/(const float& scalar) const
	{
		return Vector3(this->x/scalar, this->y/scalar, this->z/scalar);
	}

	inline void operator+=(const Vector3& v)
    {
        x += v.x;
        y += v.y;
        z += v.z;
    }

	inline void operator-=(const Vector3& v)
    {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

	inline void operator*=(const float scalar)
    {
        x *= scalar;
        y *= scalar;
        z *= scalar;
    }

	inline void operator/=(const float scalar)
	{
		x /= scalar;
		y /= scalar;
		z /= scalar;
	}
	
	inline bool operator==(const Vector3& v)
	{
		return (x == v.x && y == v.y && z == v.z);
	}
	
	inline bool operator!=(const Vector3& v)
	{
		return (x != v.x || y != v.y || z != v.z);
	}

	inline float GetLengthSquared()
	{
		return this->GetDotProduct(*this);
	}

	inline Vector3 GetComponentMultiplication(const Vector3 & compVec)
	{
		return Vector3(this->x*compVec.x, this->y*compVec.y, this->z*compVec.z);
	}

	inline void RotateY(float angle)
	{
		Vector3 vec = *this;
		vec.x = cos(angle) * this->x + sin(angle) * this->z;
		vec.z = -sin(angle) * this->x + cos(angle) * this->z;
		*this = vec;
	}

	inline Vector3 GetRotated(float angle) const
	{
		Vector3 vec = *this;
		vec.RotateY(angle);
		return vec;
	}

	inline Vector3 GetInverseComponents() const
	{
		return Vector3(1.0f/this->x, 1.0f/this->y, 1.0f/this->z);
	}

	inline bool operator<( const Vector3& v ) const
	{
		if ( x < v.x ) return true;
		if ( v.x < x ) return false;
		if ( y < v.y ) return true;
		if ( v.y < y ) return false;
		if ( z < v.z ) return true;
		if ( v.z < z ) return false;
		return false;
	}

	inline float& operator[]( unsigned int i ) throw(...)
	{
		if(i > 2)
		{
			throw("index out of bounds");
		}
		return values[i];
	}

	inline const float& operator[]( unsigned int i ) const throw(...)
	{
		if(i > 2)
		{
			throw("index out of bounds");
		}
		return values[i];
	}

	inline Vector2 GetXY() const
	{
		return Vector2(x,y);
	}

	inline Vector2 GetXZ() const
	{
		return Vector2(x,z);
	}

	inline Vector2 GetYZ() const
	{
		return Vector2(y,z);
	}

	inline void RotateAroundAxis(const Vector3& axis, float angle)
	{
		float tr = 1-(float)cos((double)angle);
		float cosVal = (float)cos((double)angle);
		float sinVal = (float)sin((double)angle);

		this->x = ((tr * axis.x * axis.x) + cosVal) * this->x + ((tr * axis.x * axis.y) - (sinVal * axis.z)) * this->y + ((tr * axis.x * axis.z) + (sinVal * axis.y)) * this->z;
		this->y = ((tr * axis.x * axis.y) + (sinVal * axis.z)) * this->x + ((tr * axis.y * axis.y) + cosVal) * this->y + ((tr * axis.y * axis.z) - (sinVal * axis.x)) * this->z;
		this->z = ((tr * axis.x * axis.z) - (sinVal * axis.y)) * this->x + ((tr * axis.y * axis.z) + (sinVal * axis.x)) * this->y + ((tr * axis.z * axis.z) + cosVal) * this->z; 
	}

#ifdef D3DVECTOR_DEFINED
	operator D3DXVECTOR3 () const { return D3DXVECTOR3(x,y,z); }
#endif
};


class Vector4
{
public:
	union
	{
		//the variable "values" and x,y,z and w share the same memory
		float values[4];
		struct
		{
			float x; //values[0]
			float y; //values[1]
			float z; //values[2]
			float w; //values[3]
		};
	};

	explicit Vector4(float _x=0.0f, float _y=0.0f, float _z=0.0f, float _w=0.0f) : x(_x), y(_y), z(_z), w(_w)
	{

	}

	inline float GetLength() const
	{
		return sqrtf(this->x * this->x + this->y * this->y + this->z * this->z + this->w * this->w);
	}

	inline Vector4& Normalize()
	{
		float length = this->GetLength();

		if(length > 0.0f)
		{
			this->x /= length;
			this->y /= length;
			this->z /= length;
			this->w /= length;
		}

		return *this;
	}

	inline bool operator<( const Vector4& v ) const
	{
		if ( x < v.x ) return true;
		if ( v.x < x ) return false;
		if ( y < v.y ) return true;
		if ( v.y < y ) return false;
		if ( z < v.z ) return true;
		if ( v.z < z ) return false;
		if ( w < v.w ) return true;
		if ( v.w < w ) return false;
		return false;
	}

	inline float& operator[]( unsigned int i ) throw(...)
	{
		if(i > 3)
		{
			throw("index out of bounds");
		}
		return values[i];
	}

	inline const float& operator[]( unsigned int i ) const throw(...)
	{
		if(i > 3)
		{
			throw("index out of bounds");
		}
		return values[i];
	}

	inline Vector4 operator+(const Vector4& v) const
	{
		return Vector4(this->x+v.x, this->y+v.y, this->z+v.z, this->w+v.w);
	}

	inline Vector4 operator+( const float& scalar ) const
	{
		return Vector4( x + scalar, y + scalar, z + scalar, w + scalar );
	}

	inline Vector4 operator-(const Vector4& v) const
	{
		return Vector4(this->x-v.x, this->y-v.y, this->z-v.z, w-v.w);
	}

	inline Vector4 operator-( const float& scalar ) const
	{
		return Vector4( x - scalar, y - scalar, z - scalar, w - scalar );
	}

	inline Vector4 operator*(const float& scalar) const
	{
		return Vector4(this->x*scalar, this->y*scalar, this->z*scalar, w*scalar);
	}

	inline Vector4 operator*(const Vector4& v) const
	{
		return Vector4(this->x*v.x, this->y*v.y, this->z*v.z, w*v.w);
	}

	inline Vector4 operator/(const float& scalar) const
	{
		return Vector4(this->x/scalar, this->y/scalar, this->z/scalar, w/scalar);
	}

	inline void operator+=(const Vector4& v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
		w += v.w;
	}

	inline void operator-=(const Vector4& v)
	{
		x -= v.x;
		y -= v.y;
		z -= v.z;
		w -= v.w;
	}

	inline void operator*=(const float scalar)
	{
		x *= scalar;
		y *= scalar;
		z *= scalar;
		w *= scalar;
	}

	inline Vector3 ToAngles() const
	{
		float sqw = w * w;
		float sqx = x * x;
		float sqy = y * y;
		float sqz = z * z;

		Vector3 angles;
		angles.x = atan2f(2.0f * ( y * z + x * w ) , ( -sqx - sqy + sqz + sqw ));
		angles.y = asinf(-2.0f * ( x * z - y * w ));
		angles.z = atan2f(2.0f * ( x * y + z * w ) , (  sqx - sqy - sqz + sqw ));

		return angles;
	}

#ifdef D3DVECTOR_DEFINED
	operator D3DXVECTOR4 () const { return D3DXVECTOR4(x,y,z,w); }
#endif
};

#pragma warning (pop)

#endif