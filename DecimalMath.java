package com.nookco.util;

import java.math.RoundingMode;

public final class DecimalMath
{
    public static final Decimal.Constant DOUBLE_MAX_VALUE = new Decimal.Constant(Double.MAX_VALUE);
    public static final Decimal.Constant DOUBLE_MIN_VALUE = new Decimal.Constant(Double.MIN_VALUE);

    /**
     * Addition for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs Decimal representing the left-hand-side value of the addition operation
     * @param rhs Decimal representing the right-hand-side value of the addition operation
     * @return Sum result of adding the lhs value by rhs value
     */
    public static Decimal add(Decimal lhs, Decimal rhs)
    {
        return addTo(new Decimal(lhs), rhs);
    }

    /**
     * Addition for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs Decimal representing the left-hand-side value of the addition operation
     * @param rhs long primitive representing the right-hand-side value of the addition operation
     * @return Sum result of adding the lhs value by rhs value
     */
    public static Decimal add(Decimal lhs, long rhs)
    {
        return addTo(new Decimal(lhs), rhs);
    }

    /**
     * Addition for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs long primitive representing the right-hand-side value of the addition operation
     * @param rhs Decimal representing the left-hand-side value of the addition operation
     * @return Sum result of adding the lhs value by rhs value
     */
    public static Decimal add(long lhs, Decimal rhs)
    {
        return addTo(new Decimal(lhs, (short)0), rhs);
    }

    /**
     * Addition for Decimal objects.  The result will be written into the "lhs" Decimal object
     * passed into this method.
     * @param lhs Decimal representing the left-hand-side value of the addition operation
     * @param rhs Decimal representing the right-hand-side value of the addition operation
     * @return Sum result of adding the lhs value by rhs value
     */
    public static Decimal addTo(Decimal lhs, Decimal rhs)
    {
        return doAddSubtract(lhs, lhs.getSignificand(), lhs.getExponent(), rhs.getSignificand(), rhs.getExponent());
    }

    /**
     * Addition for Decimal objects.  The result will be written into the "lhs" Decimal object
     * passed into this method.
     * @param lhs Decimal representing the left-hand-side value of the addition operation
     * @param rhs long primitive representing the right-hand-side value of the addition operation
     * @return Sum result of adding the lhs value by rhs value
     */
    public static Decimal addTo(Decimal lhs, long rhs)
    {
        return doAddSubtract(lhs, lhs.getSignificand(), lhs.getExponent(), rhs, (short)0);
    }

    /**
     * Subtraction for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs Decimal representing the left-hand-side value of the subtraction operation
     * @param rhs Decimal representing the right-hand-side value of the subtraction operation
     * @return Difference result of subtracting the lhs value by rhs value
     */
    public static Decimal subtract(Decimal lhs, Decimal rhs)
    {
        return subtractFrom(new Decimal(lhs), rhs);
    }

    /**
     * Subtraction for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs Decimal representing the left-hand-side value of the subtraction operation
     * @param rhs long primitive representing the right-hand-side value of the subtraction operation
     * @return Difference result of subtracting the lhs value by rhs value
     */
    public static Decimal subtract(Decimal lhs, long rhs)
    {
        return subtractFrom(new Decimal(lhs), rhs);
    }

    /**
     * Subtraction for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs long primitive representing the right-hand-side value of the subtraction operation
     * @param rhs Decimal representing the left-hand-side value of the subtraction operation
     * @return Difference result of subtracting the lhs value by rhs value
     */
    public static Decimal subtract(long lhs, Decimal rhs)
    {
        return subtractFrom(new Decimal(lhs, (short)0), rhs);
    }

    /**
     * Subtraction for Decimal objects.  The result will be written into the "lhs" Decimal object
     * passed into this method.
     * @param lhs Decimal representing the left-hand-side value of the subtraction operation
     * @param rhs Decimal representing the right-hand-side value of the subtraction operation
     * @return Difference result of subtracting the lhs value by rhs value
     */
    public static Decimal subtractFrom(Decimal lhs, Decimal rhs)
    {
        return doAddSubtract(lhs, lhs.getSignificand(), lhs.getExponent(), -rhs.getSignificand(), rhs.getExponent());
    }

    /**
     * Subtraction for Decimal objects.  The result will be written into the "lhs" Decimal object
     * passed into this method.
     * @param lhs Decimal representing the left-hand-side value of the subtraction operation
     * @param rhs long primitive representing the right-hand-side value of the subtraction operation
     * @return Difference result of subtracting the lhs value by rhs value
     */
    public static Decimal subtractFrom(Decimal lhs, long rhs)
    {
        return doAddSubtract(lhs, lhs.getSignificand(), lhs.getExponent(), -rhs, (short)0);
    }

    /**
     * Multiplication for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs Decimal representing the left-hand-side value of the multiplication operation
     * @param rhs Decimal representing the right-hand-side value of the multiplication operation
     * @return Product result of multiplying the lhs value by rhs value
     */
    public static Decimal multiply(Decimal lhs, Decimal rhs)
    {
        return multiplyBy(new Decimal(lhs), rhs);
    }

    /**
     * Multiplication for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs Decimal representing the left-hand-side value of the multiplication operation
     * @param rhs long primitive representing the right-hand-side value of the multiplication operation
     * @return Product result of multiplying the lhs value by rhs value
     */
    public static Decimal multiply(Decimal lhs, long rhs)
    {
        return multiplyBy(new Decimal(lhs), rhs);
    }

    /**
     * Multiplication for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs long primitive representing the right-hand-side value of the multiplication operation
     * @param rhs Decimal representing the left-hand-side value of the multiplication operation
     * @return Product result of multiplying the lhs value by rhs value
     */
    public static Decimal multiply(long lhs, Decimal rhs)
    {
        return multiplyBy(new Decimal(lhs, (short)0), rhs);
    }

    /**
     * Multiplication for Decimal objects.  The result will be written into the "lhs" Decimal object
     * passed into this method.
     * @param lhs Decimal representing the left-hand-side value of the multiplication operation
     * @param rhs Decimal representing the right-hand-side value of the multiplication operation
     * @return Product result of multiplying the lhs value by rhs value
     */
    public static Decimal multiplyBy(Decimal lhs, Decimal rhs)
    {
        // TODO: When there is copious amounts of free time (hah), implement this without doubles
        lhs.setRawValue(lhs.toDouble() * rhs.toDouble());
        return lhs;
    }

    /**
     * Multiplication for Decimal objects.  The result will be written into the "lhs" Decimal object
     * passed into this method.
     * @param lhs Decimal representing the left-hand-side value of the multiplication operation
     * @param rhs long primitive representing the right-hand-side value of the multiplication operation
     * @return Product result of multiplying the lhs value by rhs value
     */
    public static Decimal multiplyBy(Decimal lhs, long rhs)
    {
        // TODO: When there is copious amounts of free time (hah), implement this without doubles
        lhs.setRawValue(lhs.toDouble() * (double)rhs);
        return lhs;
    }

    /**
     * Division for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs Decimal representing the left-hand-side value of the division operation
     * @param rhs Decimal representing the right-hand-side value of the division operation
     * @return Quotient result of dividing the lhs value by rhs value
     */
    public static Decimal divide(Decimal lhs, Decimal rhs)
    {
        return divideBy(new Decimal(lhs), rhs);
    }

    /**
     * Division for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs Decimal representing the left-hand-side value of the division operation
     * @param rhs long primitive representing the right-hand-side value of the division operation
     * @return Quotient result of dividing the lhs value by rhs value
     */
    public static Decimal divide(Decimal lhs, long rhs)
    {
        return divideBy(new Decimal(lhs), rhs);
    }

    /**
     * Division for Decimal objects.  The result will be stored in a new Decimal object
     * that is returned by this method.
     * @param lhs long primitive representing the right-hand-side value of the division operation
     * @param rhs Decimal representing the left-hand-side value of the division operation
     * @return Quotient result of dividing the lhs value by rhs value
     */
    public static Decimal divide(long lhs, Decimal rhs)
    {
        return divideBy(new Decimal(lhs, (short)0), rhs);
    }

    /**
     * Division for Decimal objects.  The result will be written into the "lhs" Decimal object
     * passed into this method.
     * @param lhs Decimal representing the left-hand-side value of the division operation
     *            The quotient result will be stored in this Decimal object
     * @param rhs Decimal representing the right-hand-side value of the division operation
     * @return Quotient result of dividing the lhs value by rhs value
     */
    public static Decimal divideBy(Decimal lhs, Decimal rhs)
    {
        // TODO: When there is copious amounts of free time (hah), implement this without doubles
        double rhsDouble = rhs.toDouble();
        if (rhsDouble == 0) { throw new ArithmeticException("Cannot divide by zero"); }
        
        lhs.setRawValue(lhs.toDouble() / rhsDouble);
        return lhs;
    }
   
    /**
     * Division for Decimal objects.  The result will be written into the "lhs" Decimal object
     * passed into this method.
     * @param lhs Decimal representing the left-hand-side value of the division operation
     *            The quotient result will be stored in this Decimal object
     * @param rhs Decimal representing the right-hand-side value of the division operation
     * @return Quotient result of dividing the lhs value by rhs value
     */
    public static Decimal divideBy(Decimal lhs, long rhs)
    {
        lhs.setRawValue(lhs.toDouble() / (double)rhs);
        return lhs;
    }

    public static int compare(long lhs, Decimal rhs)
    {
        return compare(lhs, (short)0, rhs.getSignificand(), rhs.getExponent());
    }

    public static int compare(Decimal lhs, long rhs)
    {
        return compare(lhs.getSignificand(), lhs.getExponent(), rhs, (short)0);
    }

    /**
     * Compares Decimal objects.
     * @return -1 The 'lhs' value is less than the 'rhs' value
     *          0 The 'lhs' value is equal to the 'rhs' value
     *          1 The 'lhs' value is greater than the 'rhs' value
     */
    public static int compare(Decimal lhs, Decimal rhs)
    {
        return compare(lhs.getSignificand(), lhs.getExponent(), rhs.getSignificand(), rhs.getExponent());
    }

    public static int compare(long lhsSig, short lhsExp, long rhsSig, short rhsExp)
    {
        if (lhsSig == rhsSig && lhsExp == rhsExp)
        {
            return 0;
        }
        
        long value = rhsSig;

        boolean reverse = false;

        if (lhsSig == 0)
        {
            return (rhsSig > 0) ? -1 : (rhsSig < 0) ? 1 : 0;
        }
        
        if (rhsSig == 0)
        {
            return (lhsSig > 0) ? 1 : -1;
        }

        if (lhsSig < 0)
        {
            if (rhsSig > 0)
            {
                return -1;
            }
            else
            {
                reverse = !reverse;
                lhsSig = 0-lhsSig;
                rhsSig = 0-rhsSig;
            }
        }
        else
        {
            if (rhsSig < 0)
            {
                return 1;
            }
        }
        

        if (lhsExp < rhsExp)
        {
            reverse = !reverse;
            long tmpSig = lhsSig;
            lhsSig = rhsSig;
            rhsSig = tmpSig;

            short tmpExp = lhsExp;
            lhsExp = rhsExp;
            rhsExp = tmpExp;
        }

        boolean truncated = false;
        
        while (lhsExp > rhsExp)
        {
            int expChange = lhsExp - rhsExp;
            long divisor = 0;

            if (expChange >= Decimal.RADIX_POWERS.length)
            {
                rhsSig = 0;
                rhsExp = lhsExp;
            }
            else
            {
                divisor = Decimal.RADIX_POWERS[expChange];
            
                if ((rhsSig % divisor) != 0) { truncated = true; }
                rhsSig /= divisor;
                lhsExp -= expChange;
            }
        }

        int output;
        if (lhsSig > rhsSig)
        {
            output = 1;
        }
        else if (lhsSig == rhsSig)
        {
            // if we cut values off of rhs, then it is greater than lhs
            output = (truncated) ? -1 : 0;
        }
        else
        {
            output = -1;
        }

        return reverse ? -output : output;
    }

    /**
     * Addition and subtraction for Decimal objects.  The sigificands and exponents of the
     * left-hand and right-hand values are adjusted until the exponents match.  If the exponents
     * cannot be matched without causing an overflow of the significands, the numbers are
     * too large to be added or subtracted.
     * @param lhsSig long primitive representing the left-hand-side significand
     * @param lhsExp short primitive representing the left-hand-side exponent
     * @param rhsSig long primitive representing the right-hand-side significand
     * @param rhsExp short primitive representing the right-hand-side exponent
     * @param result Decimal object in which the sum or difference is stored
     * @return The Decimal object passed in as the result parameter
     */
    private static Decimal doAddSubtract(Decimal result, long lhsSig, short lhsExp, long rhsSig, short rhsExp)
    {
        if (lhsExp < rhsExp)
        {
            long value = rhsSig;

            while (rhsExp > lhsExp)
            {
                value *= 10;

                if ((value < 0 && value < Decimal.NEGATIVE_BOUNDARY_SIGNIFICAND) || (value >= 0 && value > Decimal.BOUNDARY_SIGNIFICAND))
                {
                    break;
                }

                rhsSig *= 10;
                rhsExp--;
            }

            value = lhsSig;
            
            while (rhsExp > lhsExp)
            {
                value /= 10;
                
                if (value == 0L)
                {
                    lhsSig = 0;
                    lhsExp = rhsExp;
                    break;
                }

                lhsSig /= 10;
                lhsExp++;
            }
        }
        else
        {
            long value = lhsSig;

            while (rhsExp < lhsExp)
            {
                value *= 10;

                if ((value < 0 && value < Decimal.NEGATIVE_BOUNDARY_SIGNIFICAND) || (value >= 0 && value > Decimal.BOUNDARY_SIGNIFICAND))
                {
                    break;
                }

                lhsSig *= 10;
                lhsExp--;
            }

            value = rhsSig;
            
            while (rhsExp < lhsExp)
            {
                value /= 10;
                
                if (value == 0L)
                {
                    rhsSig = 0;
                    rhsExp = lhsExp;
                    break;
                }

                rhsSig /= 10;
                rhsExp++;
            }
        }

        if (lhsExp != rhsExp)
        {
            throw new RuntimeException("Values are too large to add [" + lhsSig + "E" + lhsExp + ", " + rhsSig + "E" + rhsExp + "]");
        }

        result.setValue(lhsSig + rhsSig, lhsExp);
        return result;
    }

    public static Decimal round(Decimal d, int roundExponent, RoundingMode mode)
    {
        Decimal output = new Decimal(d);
        roundTo(output,roundExponent,mode,false);
        return output;
    }

    public static Decimal roundTo(Decimal d, int roundExponent, RoundingMode mode)
    {
        return roundTo(d,roundExponent,mode,false);
    }

    public static Decimal roundTo(Decimal d, int roundExponent, RoundingMode mode, boolean reproduceBugs)
    {
        long significand = d.getSignificand();
        short exponent = d.getExponent();

        if (exponent >= roundExponent || significand == 0)
        {
            return d;
        }

        while ((significand % Decimal.RADIX) == 0)
        {
            significand /= Decimal.RADIX;
            exponent++;
        }
        
        if (exponent >= roundExponent)
        {
            return d;
        }

        boolean negative;
        if (significand < 0)
        {
            negative = true;
            significand = -significand;
        }
        else
        {
            negative = false;
        }

        // both of htese are relative to the desired roundExponent
        long wholePart;
        int leadingFractionDigit;
        long fractionRemainder;
        
        int diff = roundExponent - exponent;

        if (diff > Decimal.RADIX_POWERS.length)
        {
            wholePart = 0;
            leadingFractionDigit = 0;
            fractionRemainder = significand;
        }
        else if (diff == Decimal.RADIX_POWERS.length)
        {
            wholePart = 0;
            leadingFractionDigit = (int)(significand / Decimal.RADIX_POWERS[diff-1]);
            fractionRemainder = significand % Decimal.RADIX_POWERS[diff-1];
        }
        else
        {
            wholePart = significand / Decimal.RADIX_POWERS[diff];
            leadingFractionDigit = (int)((significand / Decimal.RADIX_POWERS[diff-1]) % Decimal.RADIX);
            fractionRemainder = significand % Decimal.RADIX_POWERS[diff-1];
        }

        if (reproduceBugs && wholePart == 0 && leadingFractionDigit == 0)
        {
        }
        else
        {
            switch (mode)
            {
            case UNNECESSARY:
                throw new ArithmeticException("Rounding mode must be set to round this value");
            case CEILING:
                if (!negative)
                {
                    wholePart++;
                }
                break;
            case DOWN:
                // nothing
                break;
            case FLOOR:
                if (negative)
                {
                    wholePart++;
                }
                break;
            case HALF_DOWN:
                if (leadingFractionDigit > Decimal.HALF_RADIX
                    || leadingFractionDigit == Decimal.HALF_RADIX && fractionRemainder != 0)
                {
                    wholePart++;
                }
                break;
            case HALF_EVEN:
                if (leadingFractionDigit > Decimal.HALF_RADIX
                    || leadingFractionDigit == Decimal.HALF_RADIX && fractionRemainder != 0)
                {
                    wholePart++;
                }
                else if (leadingFractionDigit == Decimal.HALF_RADIX && ((wholePart % 2) == 1))
                {
                    wholePart++;
                }
                break;
            case HALF_UP:
                if (leadingFractionDigit >= Decimal.HALF_RADIX)
                {
                    wholePart++;
                }
                break;
            case UP:
                wholePart++;
                break;
            }
        }

        significand = negative ? -wholePart : wholePart;
        exponent = (short)roundExponent;

        d.setValue(significand,exponent);
        return d;
    }
}
