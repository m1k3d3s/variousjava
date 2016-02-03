import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Decimal implements Comparable<Decimal>
{
    public static final short PREFERRED_EXPONENT = -7;

    public static final long RADIX = 10;
    public static final long HALF_RADIX = 5;
    public static final long MAX_SIGNIFICAND = 1000000000000000000l;
    public static final long BOUNDARY_SIGNIFICAND = MAX_SIGNIFICAND / RADIX;

    public static final long BINARY_BOUNDARY_SIGNIFICAND = MAX_SIGNIFICAND / 2;
    public static final long FIVARY_BOUNDARY_SIGNIFICAND = MAX_SIGNIFICAND / 5;

    public static final long MIN_SIGNIFICAND = -1000000000000000000l;
    public static final long NEGATIVE_BOUNDARY_SIGNIFICAND = MIN_SIGNIFICAND / RADIX;
    public static final long[] RADIX_POWERS = new long[19];
    public static final long[] MAX_LONGS = new long[19];

    public static final short MAX_EXPONENT = Short.MAX_VALUE;
    public static final short MIN_EXPONENT = Short.MIN_VALUE;

    static {
        long p = 1;
        for (int i = 0; i < RADIX_POWERS.length; ++i)
        {
            RADIX_POWERS[i] = p;
            MAX_LONGS[i] = Long.MAX_VALUE / p;
            p *= RADIX;
        }
    }

    public static final Decimal ZERO = new Constant(0L, PREFERRED_EXPONENT);

    private static final DecimalFormat formatter = new DecimalFormat("");

    protected long significand = 0L;
    protected short exponent = PREFERRED_EXPONENT;

    public Decimal()
    {
        // Do nothing
    }

    public Decimal(Decimal decimal)
    {
        this(decimal.getSignificand(), decimal.getExponent());
    }

    public Decimal(long significand, short exponent)
    {
        setValue(significand, exponent);
    }

    public Decimal(BigInteger value)
    {
        setValue(value);
    }

    public Decimal(BigDecimal value)
    {
        setValue(value);
    }

    public static Decimal valueOf(String x)
    {
        return new Decimal(x);
    }

    public Decimal(String x)
    {
        setValue(x);
    }

    public Decimal(String x, RoundingMode rounding)
    {
        setValue(x,rounding);
    }

    public Decimal(double d)
    {
        setValue(d);
    }

    public Decimal(double d, boolean raw)
    {
        if (raw) { setRawValue(d); } else { setValue(d); }
    }

    public Decimal(float f)
    {
        setValue(f);
    }

    public Decimal(float f, boolean raw)
    {
        if (raw) { setRawValue(f); } else { setValue(f); }
    }

    /**
     * Immutable Decimal
     */
    private static ThreadLocal<Decimal> constantInitializer = new ThreadLocal<Decimal>() {
        public Decimal initialValue() { return new Decimal(); }
    };

    public static class Constant extends Decimal
    {
        public Constant()
        {
            super();
        }

        public Constant(double d)
        {
            this(constantInitializer.get().setValue(d));
        }

        public Constant(double d, boolean raw)
        {
            this(raw ? constantInitializer.get().setRawValue(d) : constantInitializer.get().setValue(d) );
        }


        public Constant(float f)
        {
            this(constantInitializer.get().setValue(f));
        }

        public Constant(float f, boolean raw)
        {
            this(raw ? constantInitializer.get().setRawValue(f) : constantInitializer.get().setValue(f) );
        }

        public Constant(String x)
        {
            this(constantInitializer.get().setValue(x));
        }

        public Constant(String x, RoundingMode rounding)
        {
            this(constantInitializer.get().setValue(x,rounding));
        }

        public Constant(BigDecimal bd)
        {
            this(constantInitializer.get().setValue(bd));
        }

        public Constant(BigInteger bi)
        {
            this(constantInitializer.get().setValue(bi));
        }


        public Constant(Decimal d)
        {
            super();
            this.significand = d.getSignificand();
            this.exponent = d.getExponent();
        }

        public Constant(long significand, short exponent)
        {
            super();
            this.significand = significand;
            this.exponent = exponent;
            normalize(PREFERRED_EXPONENT);
        }

        @Override public Decimal setValue(long significand, short exponent)
        {
            throw new RuntimeException("Constant Decimal cannot be altered.");
        }
    }

    @Override public String toString()
    {
        return formatter.format(this);
    }

    public Decimal setValue(String x)
    {
        return setValue(x,RoundingMode.UNNECESSARY);
    }

    public Decimal setValue(String x, RoundingMode rounding)
    {
        if (x == null) { throw new IllegalArgumentException("String [" + x + "] cannot be parsed into a Decimal"); }
        long significand = 0;

        // for digits beyond the abiliity for a long to store them, to be used to drive rounding later.
        int leadingRemainderDigit = -1;
        boolean remainderNonZero = false;

        short offsetExponent = 0;
        short exponent = 0;
        boolean negative = false;
        boolean negativeExponent = false;

        int index = 0;
        int len = x.length();

        if (index == len) { throw new IllegalArgumentException("String [" + x + "] cannot be parsed into a Decimal"); }

        boolean afterDecimal = false;
        boolean afterExponent = false;
        boolean first = true;
        for (; index < len; ++index)
        {
            char c = x.charAt(index);

            switch (c)
            {
            case Base64.ZERO:
            case Base64.ONE:
            case Base64.TWO:
            case Base64.THREE:
            case Base64.FOUR:
            case Base64.FIVE:
            case Base64.SIX:
            case Base64.SEVEN:
            case Base64.EIGHT:
            case Base64.NINE:
                {
                    if (afterExponent)
                    {
                        if (exponent >= Short.MAX_VALUE/10) { throw new IllegalArgumentException("String [" + x + "] cannot be parsed into a Decimal"); }
                        exponent = (short)((exponent * 10) + (c-Base64.ZERO));
                    }
                    else
                    {
                        if (significand > BOUNDARY_SIGNIFICAND)
                        {
                            if (c == Base64.ZERO)
                            {
                                if (!afterDecimal)
                                {
                                    offsetExponent++;
                                }
                            }
                            else
                            {
                                if (leadingRemainderDigit < 0)
                                {
                                    leadingRemainderDigit = (c-Base64.ZERO);
                                }
                                else if (c != Base64.ZERO)
                                {
                                    remainderNonZero = true;
                                }
                            }
                        }
                        else
                        {
                            significand = (significand * 10) + (c-Base64.ZERO);
                            if (afterDecimal)
                            {
                                offsetExponent--;
                            }
                        }
                    }
                }
                break;
            case '.':
                {
                    if (afterDecimal || afterExponent)
                    {
                        throw new IllegalArgumentException("String [" + x + "] cannot be parsed into a Decimal");
                    }
                    afterDecimal = true;
                }
                break;
            case '-':
                {
                    if (first)
                    {
                        if (afterExponent)
                        {
                            negativeExponent = true;
                        }
                        else
                        {
                            negative = true;
                        }
                        if (index+1 == len)
                        {
                            throw new IllegalArgumentException("String [" + x + "] cannot be parsed into a Decimal");
                        }
                    }
                    else
                    {
                        throw new IllegalArgumentException("String [" + x + "] cannot be parsed into a Decimal");
                    }
                }
                break;
            case '+':
            {
                if (first)
                {
                    if (index+1 == len)
                    {
                        throw new IllegalArgumentException("String [" + x + "] cannot be parsed into a Decimal");
                    }
                }
                else
                {
                    throw new IllegalArgumentException("String [" + x + "] cannot be parsed into a Decimal");
                }
            }
            break;
            case 'e':
            case 'E':
                {
                    afterExponent = true;
                    first = true;
                    continue;
                }
                //unreachable: break;
            default:
                {
                    throw new IllegalArgumentException("String [" + x + "] cannot be parsed into a Decimal");
                }
                //unreachable: break;
            }

            first = false;
        }

        if (afterExponent && first)
        {
            throw new IllegalArgumentException("String [" + x + "] cannot be parsed into a Decimal");
        }

        if (leadingRemainderDigit >= 0)
        {
            switch (rounding)
            {
            case UNNECESSARY:
                throw new IllegalArgumentException("String [" + x + "] has too many significant digits to parse into a decimal with rounding mode of UNNECESSARY");
            case CEILING:
                if (!negative)
                {
                    significand++;
                }
                break;
            case DOWN:
                // nothing;
                break;
            case FLOOR:
                if (negative)
                {
                    significand++;
                }
                break;
            case HALF_DOWN:
                if (leadingRemainderDigit > HALF_RADIX
                    || leadingRemainderDigit == HALF_RADIX && remainderNonZero)
                {
                    significand++;
                }
                break;
            case HALF_EVEN:
                if (leadingRemainderDigit > HALF_RADIX
                    || leadingRemainderDigit == HALF_RADIX && remainderNonZero)
                {
                    significand++;
                }
                else if (leadingRemainderDigit == HALF_RADIX && ((significand % 2) == 1))
                {
                    significand++;
                }
                break;
            case HALF_UP:
                if (leadingRemainderDigit >= HALF_RADIX)
                {
                    significand++;
                }
                break;
            case UP:
                significand++;
                break;
            }
        }

        if (negative) { significand = 0-significand; }
        if (negativeExponent) { exponent = (short)(0-exponent); }
        exponent += offsetExponent;

        return setValue(significand,exponent);
    }

    public Decimal setValue(long significand, short exponent)
    {
        this.significand = significand;
        this.exponent = exponent;

        normalize(PREFERRED_EXPONENT);

        return this;
    }

    public Decimal setValue(Number n)
    {
        if (n instanceof Float)
        {
            return setValue(n.floatValue());
        }

        if (n instanceof Double)
        {
            return setValue(n.doubleValue());
        }

        if (n instanceof BigDecimal)
        {
            return setValue((BigDecimal)n);
        }

        if (n instanceof BigInteger)
        {
            return setValue((BigInteger)n);
        }

        return setValue(n.longValue(), (short)0);
    }

    public Decimal setValue(float f)
    {
        return setRoundedValue(f);
    }

    public Decimal setValue(double d)
    {
        return setRoundedValue(d);
    }

    public Decimal setValue(BigInteger value)
    {
        int bitLength = value.bitLength();
        if (bitLength < Long.SIZE)
        {
            setValue(value.longValue(),(short)0);
        }
        else
        {
            setRawValue(value.doubleValue());
        }
        return this;
    }

    public Decimal setValue(BigDecimal value)
    {
        BigInteger unscaled = value.unscaledValue();
        int scale = value.scale();
        if (scale == 0)
        {
            return setValue(unscaled);
        }

        int bitLength = unscaled.bitLength();
        if (bitLength < Long.SIZE)
        {
            return setValue(unscaled.longValue(),(short)(-1*scale));
        }
        
        return setRawValue(value.doubleValue());
    }

    public Decimal setRoundedValue(float f)
    {
        return setValue(Float.toString(f));
    }

    public Decimal setRoundedValue(double d)
    {
        return setValue(Double.toString(d));
    }

    public Decimal setRawValue(float f)
    {
        if (Float.isInfinite(f) || Float.isNaN(f)) { throw new IllegalArgumentException("Infinite or NaN"); }

        int fbits = Float.floatToRawIntBits(f);
        int sign = ((fbits & 0x80000000)==0) ? 0 : -1;
        int ieeExponent = ((fbits & 0x7f800000) >> 23);
        long ieeSignificand = (fbits & 0x007fffff);
        if (ieeExponent == 0) { ieeSignificand <<= 1; } else { ieeSignificand |= (1L<<23); }

        ieeExponent -= 150;

        return setBinaryValue(sign,ieeExponent,ieeSignificand);
    }

    /**
     *    m * 2^b = m/5^b * 2^b * 10^b = m/5^b * 10^b
     */
    public Decimal setRawValue(double d)
    {
        if (Double.isInfinite(d) || Double.isNaN(d)) { throw new IllegalArgumentException("Infinite or NaN"); }

        long dbits = Double.doubleToRawLongBits(d);
        int sign = ((dbits & 0x8000000000000000L)==0) ? 0 : -1;
        int ieeExponent = (int)((dbits & 0x7ff0000000000000L) >> 52);
        long ieeSignificand = (dbits & 0x000fffffffffffffL);
        if (ieeExponent == 0) { ieeSignificand <<= 1; } else { ieeSignificand |= (1L<<52); }

        ieeExponent -= 1075;

        return setBinaryValue(sign,ieeExponent,ieeSignificand);
    }

    public Decimal setBinaryValue(int sign, int ieeExponent, long ieeSignificand)
    {
        if (ieeSignificand == 0)
        {
            return setValue(0,(short)0);
        }

        while ((ieeSignificand & 0xffff) == 0)
        {
            ieeSignificand >>= 16;
            ieeExponent += 16;
        }

        while ((ieeSignificand & 0xff) == 0)
        {
            ieeSignificand >>= 8;
            ieeExponent += 8;
        }

        while ((ieeSignificand & 0xf) == 0)
        {
            ieeSignificand >>= 4;
            ieeExponent += 4;
        }

        while ((ieeSignificand & 1) == 0)
        {
            ieeSignificand >>= 1;
            ieeExponent++;
        }

        int dExponent = 0;
        while (ieeExponent > 0)
        {
            if (ieeSignificand <= BINARY_BOUNDARY_SIGNIFICAND)
            {
                ieeSignificand <<= 1;
            }
            else
            {
                // x / 5 * 10 == x * 2
                ieeSignificand /= 5;
                dExponent++;
            }
            ieeExponent--;
        }
        while (ieeExponent < 0)
        {
            if (ieeSignificand <= FIVARY_BOUNDARY_SIGNIFICAND)
            {
                ieeSignificand *= 5;
                dExponent--;
            }
            else
            {
                ieeSignificand >>= 1;
            }
            ieeExponent++;
        }

        if (sign != 0)
        {
            ieeSignificand = 0 - ieeSignificand;
        }

        return setValue(ieeSignificand,(short)dExponent);
    }

    public Decimal setValue(Decimal d)
    {
        return setValue(d.significand, d.exponent);
    }

    protected void normalize(short desiredExponent)
    {
        //a * 10^b = 10*a * 10^(b-1)
        //a * 10^b = a/10 * 10^(b+1)

        if (significand > 0)
        {
            while (((this.exponent < desiredExponent) && (this.significand % RADIX == 0)) || (this.significand > MAX_SIGNIFICAND))
            {
                this.exponent++;
                this.significand /= RADIX;
            }

            while ((this.exponent > desiredExponent) && (this.significand < BOUNDARY_SIGNIFICAND))
            {
                this.exponent--;
                this.significand *= RADIX;
            }

        }
        else if (significand < 0)
        {
            while (((this.exponent < desiredExponent) && (this.significand % RADIX == 0)) || (this.significand < MIN_SIGNIFICAND))
            {
                this.exponent++;
                this.significand /= RADIX;
            }

            while ((this.exponent > desiredExponent) && (this.significand > NEGATIVE_BOUNDARY_SIGNIFICAND))
            {
                this.exponent--;
                this.significand *= RADIX;
            }
        }
        else
        {
            exponent = desiredExponent;
        }
    }

    public long getSignificand()
    {
        return significand;
    }
    public short getExponent()
    {
        return exponent;
    }

    public boolean isZero()
    {
        return significand == 0L;
    }

    public boolean isNegative()
    {
        return significand < 0L;
    }

    public long toLong()
    {
        long value = significand;

        if (value == 0) { return value; }

        if (exponent < 0)
        {
            short e = (short)(-1 * exponent);
            if (e >= RADIX_POWERS.length)
            {
                value = 0;
            }
            else
            {
                value /= RADIX_POWERS[e];
            }
            return value;
        }
        else if (exponent > 0)
        {
            if (exponent >= RADIX_POWERS.length || value > MAX_LONGS[exponent])
            {
                return Long.MAX_VALUE;
            }
            else
            {
                return value * RADIX_POWERS[exponent];
            }
        }
        else
        {
            return value;
        }
    }

    public boolean isLong()
    {
        if (significand == 0) { return true; }

        if (exponent < 0)
        {
            short e = (short)(-1 * exponent);
            if (e >= RADIX_POWERS.length)
            {
                return false;
            }
            else
            {
                return (significand % RADIX_POWERS[e]) == 0;
            }
        }
        else if (exponent > 0)
        {
            if (exponent >= RADIX_POWERS.length || significand > MAX_LONGS[exponent])
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return true;
        }
    }

    private static final int DOUBLE_PREFERRED_ZEROS = 11;
    private static final long DOUBLE_SIGNIFICAND_MASK = 0x000fffffffffffffL;

    public double toPowDouble()
    {
        double val = (double)significand;
        if (exponent < 0)
        {
            val = (val / Math.pow((double)10, (double)(-exponent)));
        }
        else if (exponent > 0)
        {
            val = (val * Math.pow((double)10, (double)(exponent)));
        }
        return val;
    }

    public double toDouble()
    {
        return toRawDouble();
        /*
        if (Math.abs(exponent) < 8)
        {
            return toPowDouble();
        }
        else
        {
            return toRawDouble();
        }
        */
    }

    public double toRawDouble()
    {
        if (significand == 0) { return (double)0; }

        long sign = 0;
        long significand = this.significand;
        long binaryExponent = 0;
        if (significand < 0)
        {
            sign = 1l << 63;
            significand = 0-significand;
        }

        int decimalExponent = this.exponent;

        while (decimalExponent > 0)
        {
            significand *= 5;
            binaryExponent++;
            decimalExponent--;

            int zeros = Long.numberOfLeadingZeros(significand);
            if (zeros < 4)
            {
                int offset = (4-zeros);
                significand >>= offset;
                binaryExponent += offset;
            }
        }
        while (decimalExponent < 0)
        {
            int zeros = Long.numberOfLeadingZeros(significand);
            if (zeros > 1)
            {
                int offset = (zeros-1);
                significand <<= offset;
                binaryExponent -= offset;
            }

            significand /= 5;
            significand <<= 2;
            binaryExponent -= 3;
            decimalExponent++;
        }

        int currentZeros = Long.numberOfLeadingZeros(significand);
        if (currentZeros != DOUBLE_PREFERRED_ZEROS)
        {
            int offset = DOUBLE_PREFERRED_ZEROS - currentZeros;
            significand >>= offset;
            binaryExponent += offset;
        }

        binaryExponent += 1075;

        if (binaryExponent <= (-64 + DOUBLE_PREFERRED_ZEROS))
        {
            throw new ArithmeticException("Cannot convert this to double (too small):" + this);
        }

        if (binaryExponent < 0)
        {
            significand >>= (-1 * binaryExponent);
            binaryExponent = 0;
        }

        if (binaryExponent > 0x7ff || binaryExponent < 0 || significand == 0)
        {
            throw new ArithmeticException("Cannot convert this to double:" + this);
        }

        return Double.longBitsToDouble(sign | (significand & DOUBLE_SIGNIFICAND_MASK) | (binaryExponent << 52));
    }

    @Override public boolean equals(Object rhs)
    {
        return (rhs instanceof Decimal) && equals((Decimal)rhs);
    }

    public boolean equals(Decimal rhs)
    {
        return (rhs != null)
            && (rhs.significand == significand)
            && (rhs.exponent == exponent);
    }

    @Override public int hashCode()
    {
        return (int)( significand ^ (significand >> 32) ^ exponent);
    }

    public int compareTo(Decimal d)
    {
        return DecimalMath.compare(significand,exponent,d.significand,d.exponent);
    }

    public static boolean isValidSignificand(long significand)
    {
        if (significand < 0)
        {
            return significand >= MIN_SIGNIFICAND;
        }
        else if (significand > 0)
        {
            return significand <= MAX_SIGNIFICAND;
        }
        else
        {
            return true;
        }
    }
}
