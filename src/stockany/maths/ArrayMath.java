/**
 * **************************************************************************
 * Copyright 2009, Colorado School of Mines and others.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************
 */
package stockany.maths;

import java.util.Random;

/**
 * Utilities for arrays plus math methods for floats and doubles.
 * <p>
 * The math methods mirror those in the standard {@link java.lang.Math}, but
 * include overloaded methods that return floats when passed float arguments.
 * This eliminates tedious and ugly casts when using floats.
 * <p>
 * This class also provides utility functions for working with arrays of
 * primitive types, including arrays of real numbers (floats and doubles) and
 * complex numbers (pairs of floats and doubles). Many of the array methods
 * (e.g., sqrt) overload scalar math methods.
 * <p>
 * Here is an example of using this class:
 * <pre><code>
 * import static edu.mines.jtk.util.ArrayMath.*;
 * ...
 * float[] x = randfloat(10); // an array of 10 random floats
 * System.out.println("x="); dump(x); // print x
 * float[] y = sqrt(x); // an array of square roots of those floats
 * System.out.println("y="); dump(y); // print y
 * float z = sqrt(x[0]); // no (float) cast required
 * System.out.println("z="); // print z
 * ...
 * </code></pre>
 * <p>
 * A real array rx is an array of numeric values, in which each value represents
 * one real number. A complex array is an array of float or double values, in
 * which each consecutive pair of values represents the real and imaginary parts
 * of one complex number. This means that a complex array cx contains
 * cx.length/2 complex numbers, and cx.length is an even number. For example,
 * the length of a 1-D complex array cx with dimension n1 is cx.length = 2*n1;
 * i.e., n1 is the number of complex elements in the array.
 * <p>
 * Methods are overloaded for 1-D arrays, 2-D arrays (arrays of arrays), and 3-D
 * arrays (arrays of arrays of arrays). Multi-dimensional arrays can be regular
 * or ragged. For example, the dimensions of a regular 3-D array
 * float[n3][n2][n1] are n1, n2, and n3, where n1 is the fastest dimension, and
 * n3 is the slowest dimension. In contrast, the lengths of arrays within a
 * ragged array of arrays (of arrays) may vary.
 * <p>
 * Some methods that create new arrays (e.g., zero, fill, ramp, and rand) have
 * no array arguments; these methods have arguments that specify regular array
 * dimensions n1, n2, and/or n3. All other methods, those with at least one
 * array argument, use the dimensions of the first array argument to determine
 * the number of array elements to process.
 * <p>
 * Some methods may have arguments that are arrays of real and/or complex
 * numbers. In such cases, arguments with names like rx, ry, and rz denote
 * arrays of real (non-complex) elements. Arguments with names like ra and rb
 * denote real values. Arguments with names like cx, cy, and cz denote arrays of
 * complex elements, and arguments with names like ca and cb denote complex
 * values.
 * <p>
 * Because complex numbers are packed into arrays of the same types (float or
 * double) as real arrays, method overloading cannot distinguish methods with
 * real array arguments from those with complex array arguments. Therefore, all
 * methods with at least one complex array argument are prefixed with the letter
 * 'c'. For example, methods mul that multiply two arrays of real numbers have
 * corresponding methods cmul that multiply two arrays of complex numbers.
 * <pre>
 * Creation and copy operations:
 * zero - fills an array with a constant value zero
 * fill - fills an array with a specified constant value
 * ramp - fills an array with a linear values ra + rb1*i1 (+ rb2*i2 + rb3*i3)
 * rand - fills an array with pseudo-random numbers
 * copy - copies an array, or a specified subset of an array
 * </pre><pre>
 * Binary operations:
 * add - adds one array (or value) to another array (or value)
 * sub - subtracts one array (or value) from another array (or value)
 * mul - multiplies one array (or value) by another array (or value)
 * div - divides one array (or value) by another array (or value)
 * </pre><pre>
 * Unary operations:
 * abs - absolute value
 * neg - negation
 * cos - cosine
 * sin - sine
 * sqrt - square-root
 * exp - exponential
 * log - natural logarithm
 * log10 - logarithm base 10
 * clip - clip values to be within specified min/max bounds
 * pow - raise to a specified power
 * sgn - sign (1 if positive, -1 if negative, 0 if zero)
 * </pre><pre>
 * Other operations:
 * equal - compares arrays for equality (to within an optional tolerance)
 * sum - returns the sum of array values
 * max - returns the maximum value in an array and (optionally) its indices
 * min - returns the minimum value in an array and (optionally) its indices
 * dump - prints an array to standard output
 * </pre> Many more utility methods are included as well, for sorting,
 * searching, etc.
 *
 * @see java.lang.Math
 * @author Dave Hale and Chris Engelsma, Colorado School of Mines
 * @version 2009.06.23
 */
public class ArrayMath {

    /**
     * The double value that is closer than any other to <i>e</i>, the base of
     * the natural logarithm.
     */
    public static final double E = Math.E;
    /**
     * The float value that is closer than any other to <i>e</i>, the base of
     * the natural logarithm.
     */
    public static final float FLT_E = (float) E;
    /**
     * The double value that is closer than any other to <i>e</i>, the base of
     * the natural logarithm.
     */
    public static final double DBL_E = E;
    /**
     * The double value that is closer than any other to <i>pi</i>, the ratio of
     * the circumference of a circle to its diameter.
     */
    public static final double PI = Math.PI;
    /**
     * The float value that is closer than any other to <i>pi</i>, the ratio of
     * the circumference of a circle to its diameter.
     */
    public static final float FLT_PI = (float) PI;
    /**
     * The double value that is closer than any other to <i>pi</i>, the ratio of
     * the circumference of a circle to its diameter.
     */
    public static final double DBL_PI = PI;
    /**
     * The maximum positive float value.
     */
    public static final float FLT_MAX = Float.MAX_VALUE;
    /**
     * The minimum positive float value.
     */
    public static final float FLT_MIN = Float.MIN_VALUE;
    /**
     * The smallest float value e such that (1+e) does not equal 1.
     */
    public static final float FLT_EPSILON = 1.19209290e-07f;
    /**
     * The maximum positive double value.
     */
    public static final double DBL_MAX = Double.MAX_VALUE;
    /**
     * The minimum positive double value.
     */
    public static final double DBL_MIN = Double.MIN_VALUE;
    /**
     * The smallest double value e such that (1+e) does not equal 1.
     */
    public static final double DBL_EPSILON = 2.2204460492503131e-16d;

    /**
     * Returns the trigonometric sine of an angle.
     *
     * @param x the angle, in radians.
     * @return the sine of the argument.
     */
    public static float sin(float x) {
        return (float) Math.sin(x);
    }

    /**
     * Returns the trigonometric sine of an angle.
     *
     * @param x the angle, in radians.
     * @return the sine of the argument.
     */
    public static double sin(double x) {
        return Math.sin(x);
    }

    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param x the angle, in radians.
     * @return the cosine of the argument.
     */
    public static float cos(float x) {
        return (float) Math.cos(x);
    }

    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param x the angle, in radians.
     * @return the cosine of the argument.
     */
    public static double cos(double x) {
        return Math.cos(x);
    }

    /**
     * Returns the trigonometric tangent of an angle.
     *
     * @param x the angle, in radians.
     * @return the tangent of the argument.
     */
    public static float tan(float x) {
        return (float) Math.tan(x);
    }

    /**
     * Returns the trigonometric tangent of an angle.
     *
     * @param x the angle, in radians.
     * @return the tangent of the argument.
     */
    public static double tan(double x) {
        return Math.tan(x);
    }

    /**
     * Returns the arc sine of the specified value, in the range -<i>pi</i>/2
     * through <i>pi</i>/2.
     *
     * @param x the value.
     * @return the arc sine.
     */
    public static float asin(float x) {
        return (float) Math.asin(x);
    }

    /**
     * Returns the arc sine of the specified value, in the range -<i>pi</i>/2
     * through <i>pi</i>/2.
     *
     * @param x the value.
     * @return the arc sine.
     */
    public static double asin(double x) {
        return Math.asin(x);
    }

    /**
     * Returns the arc cosine of the specified value, in the range 0.0 through
     * <i>pi</i>.
     *
     * @param x the value.
     * @return the arc cosine.
     */
    public static float acos(float x) {
        return (float) Math.acos(x);
    }

    /**
     * Returns the arc cosine of the specified value, in the range 0.0 through
     * <i>pi</i>.
     *
     * @param x the value.
     * @return the arc cosine.
     */
    public static double acos(double x) {
        return Math.acos(x);
    }

    /**
     * Returns the arc tangent of the specified value, in the range -<i>pi</i>/2
     * through <i>pi</i>/2.
     *
     * @param x the value.
     * @return the arc tangent.
     */
    public static float atan(float x) {
        return (float) Math.atan(x);
    }

    /**
     * Returns the arc tangent of the specified value, in the range -<i>pi</i>/2
     * through <i>pi</i>/2.
     *
     * @param x the value.
     * @return the arc tangent.
     */
    public static double atan(double x) {
        return Math.atan(x);
    }

    /**
     * Computes the arc tangent of the specified y/x, in the range -<i>pi</i> to
     * <i>pi</i>.
     *
     * @param y the ordinate coordinate y.
     * @param x the abscissa coordinate x.
     * @return the arc tangent.
     */
    public static float atan2(float y, float x) {
        return (float) Math.atan2(y, x);
    }

    /**
     * Computes the arc tangent of the specified y/x, in the range -<i>pi</i> to
     * <i>pi</i>.
     *
     * @param y the ordinate coordinate y.
     * @param x the abscissa coordinate x.
     * @return the arc tangent.
     */
    public static double atan2(double y, double x) {
        return Math.atan2(y, x);
    }

    /**
     * Converts an angle measured in degrees to radians.
     *
     * @param angdeg an angle, in degrees.
     * @return the angle in radians.
     */
    public static float toRadians(float angdeg) {
        return (float) Math.toRadians(angdeg);
    }

    /**
     * Converts an angle measured in degrees to radians.
     *
     * @param angdeg an angle, in degrees.
     * @return the angle in radians.
     */
    public static double toRadians(double angdeg) {
        return Math.toRadians(angdeg);
    }

    /**
     * Converts an angle measured in radians to degrees.
     *
     * @param angrad an angle, in radians.
     * @return the angle in degrees.
     */
    public static float toDegrees(float angrad) {
        return (float) Math.toDegrees(angrad);
    }

    /**
     * Converts an angle measured in radians to degrees.
     *
     * @param angrad an angle, in radians.
     * @return the angle in degrees.
     */
    public static double toDegrees(double angrad) {
        return Math.toDegrees(angrad);
    }

    /**
     * Returns the value of <i>e</i> raised to the specified power.
     *
     * @param x the exponent.
     * @return the value.
     */
    public static float exp(float x) {
        return (float) Math.exp(x);
    }

    /**
     * Returns the value of <i>e</i> raised to the specified power.
     *
     * @param x the exponent.
     * @return the value.
     */
    public static double exp(double x) {
        return Math.exp(x);
    }

    /**
     * Returns the natural logarithm (base <i>e</i>) of the specified value.
     *
     * @param x the value.
     * @return the natural logarithm.
     */
    public static float log(float x) {
        return (float) Math.log(x);
    }

    /**
     * Returns the natural logarithm (base <i>e</i>) of the specified value.
     *
     * @param x the value.
     * @return the natural logarithm.
     */
    public static double log(double x) {
        return Math.log(x);
    }

    /**
     * Returns the logarithm base 10 of the specified value.
     *
     * @param x the value.
     * @return the logarithm base 10.
     */
    public static float log10(float x) {
        return (float) Math.log10(x);
    }

    /**
     * Returns the logarithm base 10 of the specified value.
     *
     * @param x the value.
     * @return the logarithm base 10.
     */
    public static double log10(double x) {
        return Math.log10(x);
    }

    /**
     * Returns the positive square root of a the specified value.
     *
     * @param x the value.
     * @return the positive square root.
     */
    public static float sqrt(float x) {
        return (float) Math.sqrt(x);
    }

    /**
     * Returns the positive square root of a the specified value.
     *
     * @param x the value.
     * @return the positive square root.
     */
    public static double sqrt(double x) {
        return Math.sqrt(x);
    }

    /**
     * Returns the value of x raised to the y'th power.
     *
     * @param x the base.
     * @param y the exponent.
     * @return the value.
     */
    public static float pow(float x, float y) {
        return (float) Math.pow(x, y);
    }

    /**
     * Returns the value of x raised to the y'th power.
     *
     * @param x the base.
     * @param y the exponent.
     * @return the value.
     */
    public static double pow(double x, double y) {
        return Math.pow(x, y);
    }

    /**
     * Returns the hyperbolic sine of the specified value.
     *
     * @param x the value.
     * @return the hyperbolic sine.
     */
    public static float sinh(float x) {
        return (float) Math.sinh(x);
    }

    /**
     * Returns the hyperbolic sine of the specified value.
     *
     * @param x the value.
     * @return the hyperbolic sine.
     */
    public static double sinh(double x) {
        return Math.sinh(x);
    }

    /**
     * Returns the hyperbolic cosine of the specified value.
     *
     * @param x the value.
     * @return the hyperbolic cosine.
     */
    public static float cosh(float x) {
        return (float) Math.cosh(x);
    }

    /**
     * Returns the hyperbolic cosine of the specified value.
     *
     * @param x the value.
     * @return the hyperbolic cosine.
     */
    public static double cosh(double x) {
        return Math.cosh(x);
    }

    /**
     * Returns the hyperbolic tangent of the specified value.
     *
     * @param x the value.
     * @return the hyperbolic tangent.
     */
    public static float tanh(float x) {
        return (float) Math.tanh(x);
    }

    /**
     * Returns the hyperbolic tangent of the specified value.
     *
     * @param x the value.
     * @return the hyperbolic tangent.
     */
    public static double tanh(double x) {
        return Math.tanh(x);
    }

    /**
     * Returns the smallest (closest to negative infinity) value that is greater
     * than or equal to the argument and is equal to a mathematical integer.
     *
     * @param x a value.
     * @return the smallest value.
     */
    public static float ceil(float x) {
        return (float) Math.ceil(x);
    }

    /**
     * Returns the smallest (closest to negative infinity) value that is greater
     * than or equal to the argument and is equal to a mathematical integer.
     *
     * @param x a value.
     * @return the smallest value.
     */
    public static double ceil(double x) {
        return Math.ceil(x);
    }

    /**
     * Returns the largest (closest to positive infinity) value that is less
     * than or equal to the argument and is equal to a mathematical integer.
     *
     * @param x a value.
     * @return the largest value.
     */
    public static float floor(float x) {
        return (float) Math.floor(x);
    }

    /**
     * Returns the largest (closest to positive infinity) value that is less
     * than or equal to the argument and is equal to a mathematical integer.
     *
     * @param x a value.
     * @return the largest value.
     */
    public static double floor(double x) {
        return Math.floor(x);
    }

    /**
     * Returns the value that is closest to the specified value and is equal to
     * a mathematical integer.
     *
     * @param x the value.
     * @return the closest value.
     */
    public static float rint(float x) {
        return (float) Math.rint(x);
    }

    /**
     * Returns the value that is closest to the specified value and is equal to
     * a mathematical integer.
     *
     * @param x the value.
     * @return the closest value.
     */
    public static double rint(double x) {
        return Math.rint(x);
    }

    /**
     * Returns the closest int to the specified value. The result is the value
     * of the expression <code>(int)Math.floor(a+0.5f)</code>.
     *
     * @param x the value.
     * @return the value rounded to the nearest int.
     */
    public static int round(float x) {
        return Math.round(x);
    }

    /**
     * Returns the closest long to the specified value. The result is the value
     * of the expression <code>(long)Math.floor(a+0.5)</code>.
     *
     * @param x the value.
     * @return the value rounded to the nearest long.
     */
    public static long round(double x) {
        return Math.round(x);
    }

    /**
     * Returns the signum of the specified value. The signum is zero if the
     * argument is zero, 1.0 if the argument is greater than zero, -1.0 if the
     * argument is less than zero.
     *
     * @param x the value.
     * @return the signum.
     */
    public static float signum(float x) {
        return (x > 0.0f) ? 1.0f : ((x < 0.0f) ? -1.0f : 0.0f);
    }

    /**
     * Returns the signum of the specified value. The signum is zero if the
     * argument is zero, 1.0 if the argument is greater than zero, -1.0 if the
     * argument is less than zero.
     *
     * @param x the value.
     * @return the signum.
     */
    public static double signum(double x) {
        return (x > 0.0) ? 1.0 : ((x < 0.0) ? -1.0 : 0.0);
    }

    /**
     * Returns the absolute value of the specified value.
     *
     * @param x the value.
     * @return the absolute value.
     */
    public static int abs(int x) {
        return (x >= 0) ? x : -x;
    }

    /**
     * Returns the absolute value of the specified value.
     *
     * @param x the value.
     * @return the absolute value.
     */
    public static long abs(long x) {
        return (x >= 0L) ? x : -x;
    }

    /**
     * Returns the absolute value of the specified value. Note that
     * {@code abs(-0.0f)} returns {@code -0.0f}; the sign bit is not cleared. If
     * this is a problem, use {@code Math.abs}.
     *
     * @param x the value.
     * @return the absolute value.
     */
    public static float abs(float x) {
        return (x >= 0.0f) ? x : -x;
    }

    /**
     * Returns the absolute value of the specified value. Note that
     * {@code abs(-0.0d)} returns {@code -0.0d}; the sign bit is not cleared. If
     * this is a problem, use {@code Math.abs}.
     *
     * @param x the value.
     * @return the absolute value.
     */
    public static double abs(double x) {
        return (x >= 0.0d) ? x : -x;
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @return the maximum value.
     */
    public static int max(int a, int b) {
        return (a >= b) ? a : b;
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @return the maximum value.
     */
    public static int max(int a, int b, int c) {
        return max(a, max(b, c));
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @param d a value.
     * @return the maximum value.
     */
    public static int max(int a, int b, int c, int d) {
        return max(a, max(b, max(c, d)));
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @return the maximum value.
     */
    public static long max(long a, long b) {
        return (a >= b) ? a : b;
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @return the maximum value.
     */
    public static long max(long a, long b, long c) {
        return max(a, max(b, c));
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @param d a value.
     * @return the maximum value.
     */
    public static long max(long a, long b, long c, long d) {
        return max(a, max(b, max(c, d)));
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @return the maximum value.
     */
    public static float max(float a, float b) {
        return (a >= b) ? a : b;
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @return the maximum value.
     */
    public static float max(float a, float b, float c) {
        return max(a, max(b, c));
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @param d a value.
     * @return the maximum value.
     */
    public static float max(float a, float b, float c, float d) {
        return max(a, max(b, max(c, d)));
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @return the maximum value.
     */
    public static double max(double a, double b) {
        return (a >= b) ? a : b;
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @return the maximum value.
     */
    public static double max(double a, double b, double c) {
        return max(a, max(b, c));
    }

    /**
     * Returns the maximum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @param d a value.
     * @return the maximum value.
     */
    public static double max(double a, double b, double c, double d) {
        return max(a, max(b, max(c, d)));
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @return the minimum value.
     */
    public static int min(int a, int b) {
        return (a <= b) ? a : b;
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @return the minimum value.
     */
    public static int min(int a, int b, int c) {
        return min(a, min(b, c));
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @param d a value.
     * @return the minimum value.
     */
    public static int min(int a, int b, int c, int d) {
        return min(a, min(b, min(c, d)));
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @return the minimum value.
     */
    public static long min(long a, long b) {
        return (a <= b) ? a : b;
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @return the minimum value.
     */
    public static long min(long a, long b, long c) {
        return min(a, min(b, c));
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @param d a value.
     * @return the minimum value.
     */
    public static long min(long a, long b, long c, long d) {
        return min(a, min(b, min(c, d)));
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @return the minimum value.
     */
    public static float min(float a, float b) {
        return (a <= b) ? a : b;
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @return the minimum value.
     */
    public static float min(float a, float b, float c) {
        return min(a, min(b, c));
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @param d a value.
     * @return the minimum value.
     */
    public static float min(float a, float b, float c, float d) {
        return min(a, min(b, min(c, d)));
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @return the minimum value.
     */
    public static double min(double a, double b) {
        return (a <= b) ? a : b;
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @return the minimum value.
     */
    public static double min(double a, double b, double c) {
        return min(a, min(b, c));
    }

    /**
     * Returns the minimum of the specified values.
     *
     * @param a a value.
     * @param b a value.
     * @param c a value.
     * @param d a value.
     * @return the minimum value.
     */
    public static double min(double a, double b, double c, double d) {
        return min(a, min(b, min(c, d)));
    }

// Newly extracted function from java.lang.Math
    /**
     * Returns the cube root of the specified value.
     *
     * @param a a value.
     * @return the cube root
     */
    public static double cbrt(double a) {
        return Math.cbrt(a);
    }

    /**
     * Returns the cube root of the specified value.
     *
     * @param a a value.
     * @return the cube root
     */
    public static float cbrt(float a) {
        return (float) Math.cbrt(a);
    }

    /**
     * Computes the remainder operation on two arguments as prescribed by the
     * IEEE 754 standard.
     *
     * @param f1 the dividend.
     * @param f2 the divisor.
     * @return the remainder when f1 is divided by f2
     */
    public static double IEEEremainder(double f1, double f2) {
        return Math.IEEEremainder(f1, f2);
    }

    /**
     * Computes the remainder operation on two arguments as prescribed by the
     * IEEE 754 standard.
     *
     * @param f1 the dividend.
     * @param f2 the divisor.
     * @return the remainder when f1 is divided by f2
     */
    public static float IEEEremainder(float f1, float f2) {
        return (float) Math.IEEEremainder(f1, f2);
    }

    /**
     * Returns a positive number that is greater than or equal to 0.0, and less
     * than 1.0.
     *
     * @return a pseudorandom number.
     */
    public static double random() {
        return Math.random();
    }

    /**
     * Returns a positive number that is greater than or equal to 0.0, and less
     * than 1.0.
     *
     * @return a pseudorandom number.
     */
    public static double randomDouble() {
        return Math.random();
    }

    /**
     * Returns a positive number that is greater than or equal to 0.0, and less
     * than 1.0.
     *
     * @return a pseudorandom number.
     */
    public static float randomFloat() {
        return (float) Math.random();
    }

    /**
     * Returns the size of an ulp of the argument.
     *
     * @param d the value whose ulp is to be returned.
     * @return the size of an ulp of the argument.
     */
    public static double ulp(double d) {
        return Math.ulp(d);
    }

    /**
     * Returns the size of an ulp of the argument.
     *
     * @param d the value whose ulp is to be returned.
     * @return the size of an ulp of the argument.
     */
    public static float ulp(float d) {
        return Math.ulp(d);
    }

    /**
     * Returns the hypotenuse for two given values. The hypotenuse is calculated
     * using the Pythagorean theorem.
     *
     * @param x a value.
     * @param y a value.
     * @return the hypotenuse.
     */
    public static double hypot(double x, double y) {
        return Math.hypot(x, y);
    }

    /**
     * Returns the hypotenuse for two given values. The hypotenuse is calculated
     * using the Pythagorean theorem.
     *
     * @param x a value.
     * @param y a value.
     * @return the hypotenuse.
     */
    public static float hypot(float x, float y) {
        return (float) Math.hypot(x, y);
    }

    /**
     * Returns <i>e</i><sup>x</sup>-1.
     *
     * @param x the exponent.
     * @return the value <i>e</i><sup>x</sup>-1.
     */
    public static double expm1(double x) {
        return Math.expm1(x);
    }

    /**
     * Returns <i>e</i><sup>x</sup>-1.
     *
     * @param x the exponent.
     * @return the value <i>e</i><sup>x</sup>-1.
     */
    public static float expm1(float x) {
        return (float) Math.expm1(x);
    }

    /**
     * Returns the natural logarithm of the sum of the argument and 1.
     *
     * @param x a value.
     * @return the value ln(x+1).
     */
    public static double log1p(double x) {
        return Math.log1p(x);
    }

    /**
     * Returns the natural logarithm of the sum of the argument and 1.
     *
     * @param x a value.
     * @return the value ln(x+1).
     */
    public static float log1p(float x) {
        return (float) Math.log1p(x);
    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    // ArrayMath methods
    ///////////////////////////////////////////////////////////////////////////
    // zero
    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     */
    public static byte[] zerobyte(int n1) {
        return new byte[n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @return 
     */
    public static byte[][] zerobyte(int n1, int n2) {
        return new byte[n2][n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     * @return 
     */
    public static byte[][][] zerobyte(int n1, int n2, int n3) {
        return new byte[n3][n2][n1];
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(byte[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = 0;
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(byte[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            zero(rx[i2]);
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(byte[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            zero(rx[i3]);
        }
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     */
    public static short[] zeroshort(int n1) {
        return new short[n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static short[][] zeroshort(int n1, int n2) {
        return new short[n2][n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static short[][][] zeroshort(int n1, int n2, int n3) {
        return new short[n3][n2][n1];
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(short[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = 0;
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(short[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            zero(rx[i2]);
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(short[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            zero(rx[i3]);
        }
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     */
    public static int[] zeroint(int n1) {
        return new int[n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static int[][] zeroint(int n1, int n2) {
        return new int[n2][n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static int[][][] zeroint(int n1, int n2, int n3) {
        return new int[n3][n2][n1];
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(int[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = 0;
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(int[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            zero(rx[i2]);
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(int[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            zero(rx[i3]);
        }
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     */
    public static long[] zerolong(int n1) {
        return new long[n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static long[][] zerolong(int n1, int n2) {
        return new long[n2][n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static long[][][] zerolong(int n1, int n2, int n3) {
        return new long[n3][n2][n1];
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(long[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = 0L;
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(long[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            zero(rx[i2]);
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(long[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            zero(rx[i3]);
        }
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     */
    public static float[] zerofloat(int n1) {
        return new float[n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static float[][] zerofloat(int n1, int n2) {
        return new float[n2][n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static float[][][] zerofloat(int n1, int n2, int n3) {
        return new float[n3][n2][n1];
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(float[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = 0.0f;
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(float[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            zero(rx[i2]);
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(float[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            zero(rx[i3]);
        }
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     */
    public static float[] czerofloat(int n1) {
        return new float[2 * n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static float[][] czerofloat(int n1, int n2) {
        return new float[n2][2 * n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static float[][][] czerofloat(int n1, int n2, int n3) {
        return new float[n3][n2][2 * n1];
    }

    /**
     * Zeros the the specified array.
     *
     * @param cx the array.
     */
    public static void czero(float[] cx) {
        zero(cx);
    }

    /**
     * Zeros the the specified array.
     *
     * @param cx the array.
     */
    public static void czero(float[][] cx) {
        zero(cx);
    }

    /**
     * Zeros the the specified array.
     *
     * @param cx the array.
     */
    public static void czero(float[][][] cx) {
        zero(cx);
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     */
    public static double[] zerodouble(int n1) {
        return new double[n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static double[][] zerodouble(int n1, int n2) {
        return new double[n2][n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static double[][][] zerodouble(int n1, int n2, int n3) {
        return new double[n3][n2][n1];
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(double[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = 0.0;
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(double[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            zero(rx[i2]);
        }
    }

    /**
     * Zeros the the specified array.
     *
     * @param rx the array.
     */
    public static void zero(double[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            zero(rx[i3]);
        }
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     */
    public static double[] czerodouble(int n1) {
        return new double[2 * n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static double[][] czerodouble(int n1, int n2) {
        return new double[n2][2 * n1];
    }

    /**
     * Returns a new array of zeros.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static double[][][] czerodouble(int n1, int n2, int n3) {
        return new double[n3][n2][2 * n1];
    }

    /**
     * Zeros the the specified array.
     *
     * @param cx the array.
     */
    public static void czero(double[] cx) {
        zero(cx);
    }

    /**
     * Zeros the the specified array.
     *
     * @param cx the array.
     */
    public static void czero(double[][] cx) {
        zero(cx);
    }

    /**
     * Zeros the the specified array.
     *
     * @param cx the array.
     */
    public static void czero(double[][][] cx) {
        zero(cx);
    }

    ///////////////////////////////////////////////////////////////////////////
    // rand
    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     */
    public static int[] randint(int n1) {
        return randint(_random, n1);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static int[][] randint(int n1, int n2) {
        return randint(_random, n1, n2);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static int[][][] randint(int n1, int n2, int n3) {
        return randint(_random, n1, n2, n3);
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     */
    public static int[] randint(Random random, int n1) {
        int[] rx = new int[n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static int[][] randint(Random random, int n1, int n2) {
        int[][] rx = new int[n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static int[][][] randint(Random random, int n1, int n2, int n3) {
        int[][][] rx = new int[n3][n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(int[] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(int[][] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(int[][][] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, int[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = random.nextInt();
        }
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, int[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            rand(random, rx[i2]);
        }
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, int[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            rand(random, rx[i3]);
        }
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     */
    public static long[] randlong(int n1) {
        return randlong(_random, n1);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static long[][] randlong(int n1, int n2) {
        return randlong(_random, n1, n2);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static long[][][] randlong(int n1, int n2, int n3) {
        return randlong(_random, n1, n2, n3);
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     */
    public static long[] randlong(Random random, int n1) {
        long[] rx = new long[n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static long[][] randlong(Random random, int n1, int n2) {
        long[][] rx = new long[n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static long[][][] randlong(Random random, int n1, int n2, int n3) {
        long[][][] rx = new long[n3][n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(long[] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(long[][] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(long[][][] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, long[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = random.nextLong();
        }
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, long[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            rand(random, rx[i2]);
        }
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, long[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            rand(random, rx[i3]);
        }
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     */
    public static float[] randfloat(int n1) {
        return randfloat(_random, n1);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static float[][] randfloat(int n1, int n2) {
        return randfloat(_random, n1, n2);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static float[][][] randfloat(int n1, int n2, int n3) {
        return randfloat(_random, n1, n2, n3);
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     */
    public static float[] randfloat(Random random, int n1) {
        float[] rx = new float[n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static float[][] randfloat(Random random, int n1, int n2) {
        float[][] rx = new float[n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static float[][][] randfloat(Random random, int n1, int n2, int n3) {
        float[][][] rx = new float[n3][n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(float[] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(float[][] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(float[][][] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, float[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = random.nextFloat();
        }
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, float[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            rand(random, rx[i2]);
        }
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, float[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            rand(random, rx[i3]);
        }
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     */
    public static float[] crandfloat(int n1) {
        return crandfloat(_random, n1);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static float[][] crandfloat(int n1, int n2) {
        return crandfloat(_random, n1, n2);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static float[][][] crandfloat(int n1, int n2, int n3) {
        return crandfloat(_random, n1, n2, n3);
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     */
    public static float[] crandfloat(Random random, int n1) {
        float[] cx = new float[2 * n1];
        crand(random, cx);
        return cx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static float[][] crandfloat(Random random, int n1, int n2) {
        float[][] cx = new float[n2][2 * n1];
        crand(random, cx);
        return cx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static float[][][] crandfloat(Random random, int n1, int n2, int n3) {
        float[][][] cx = new float[n3][n2][2 * n1];
        crand(random, cx);
        return cx;
    }

    /**
     * Fills the specified array with random values.
     *
     * @param cx the array.
     */
    public static void crand(float[] cx) {
        crand(_random, cx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param cx the array.
     */
    public static void crand(float[][] cx) {
        crand(_random, cx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param cx the array.
     */
    public static void crand(float[][][] cx) {
        crand(_random, cx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param cx the array.
     */
    public static void crand(Random random, float[] cx) {
        rand(random, cx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param cx the array.
     */
    public static void crand(Random random, float[][] cx) {
        rand(random, cx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param cx the array.
     */
    public static void crand(Random random, float[][][] cx) {
        rand(random, cx);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     */
    public static double[] randdouble(int n1) {
        return randdouble(_random, n1);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static double[][] randdouble(int n1, int n2) {
        return randdouble(_random, n1, n2);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static double[][][] randdouble(int n1, int n2, int n3) {
        return randdouble(_random, n1, n2, n3);
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     */
    public static double[] randdouble(Random random, int n1) {
        double[] rx = new double[n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static double[][] randdouble(Random random, int n1, int n2) {
        double[][] rx = new double[n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static double[][][] randdouble(Random random, int n1, int n2, int n3) {
        double[][][] rx = new double[n3][n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(double[] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(double[][] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param rx the array.
     */
    public static void rand(double[][][] rx) {
        rand(_random, rx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, double[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = random.nextDouble();
        }
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, double[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            rand(random, rx[i2]);
        }
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param rx the array.
     */
    public static void rand(Random random, double[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            rand(random, rx[i3]);
        }
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     */
    public static double[] cranddouble(int n1) {
        return cranddouble(_random, n1);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static double[][] cranddouble(int n1, int n2) {
        return cranddouble(_random, n1, n2);
    }

    /**
     * Returns a new array of random values.
     *
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static double[][][] cranddouble(int n1, int n2, int n3) {
        return cranddouble(_random, n1, n2, n3);
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     */
    public static double[] cranddouble(Random random, int n1) {
        double[] cx = new double[2 * n1];
        crand(random, cx);
        return cx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static double[][] cranddouble(Random random, int n1, int n2) {
        double[][] cx = new double[n2][2 * n1];
        crand(random, cx);
        return cx;
    }

    /**
     * Returns a new array of random values.
     *
     * @param random random number generator.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static double[][][] cranddouble(Random random, int n1, int n2, int n3) {
        double[][][] cx = new double[n3][n2][2 * n1];
        crand(random, cx);
        return cx;
    }

    /**
     * Fills the specified array with random values.
     *
     * @param cx the array.
     */
    public static void crand(double[] cx) {
        crand(_random, cx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param cx the array.
     */
    public static void crand(double[][] cx) {
        crand(_random, cx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param cx the array.
     */
    public static void crand(double[][][] cx) {
        crand(_random, cx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param cx the array.
     */
    public static void crand(Random random, double[] cx) {
        rand(random, cx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param cx the array.
     */
    public static void crand(Random random, double[][] cx) {
        rand(random, cx);
    }

    /**
     * Fills the specified array with random values.
     *
     * @param random random number generator.
     * @param cx the array.
     */
    public static void crand(Random random, double[][][] cx) {
        rand(random, cx);
    }
    private static Random _random = new Random();

    ///////////////////////////////////////////////////////////////////////////
    // fill
    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     */
    public static byte[] fillbyte(byte ra, int n1) {
        byte[] rx = new byte[n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static byte[][] fillbyte(byte ra, int n1, int n2) {
        byte[][] rx = new byte[n2][n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static byte[][][] fillbyte(byte ra, int n1, int n2, int n3) {
        byte[][][] rx = new byte[n3][n2][n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(byte ra, byte[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = ra;
        }
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(byte ra, byte[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            fill(ra, rx[i2]);
        }
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(byte ra, byte[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            fill(ra, rx[i3]);
        }
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     */
    public static short[] fillshort(short ra, int n1) {
        short[] rx = new short[n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static short[][] fillshort(short ra, int n1, int n2) {
        short[][] rx = new short[n2][n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static short[][][] fillshort(short ra, int n1, int n2, int n3) {
        short[][][] rx = new short[n3][n2][n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(short ra, short[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = ra;
        }
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(short ra, short[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            fill(ra, rx[i2]);
        }
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(short ra, short[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            fill(ra, rx[i3]);
        }
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     */
    public static int[] fillint(int ra, int n1) {
        int[] rx = new int[n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static int[][] fillint(int ra, int n1, int n2) {
        int[][] rx = new int[n2][n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static int[][][] fillint(int ra, int n1, int n2, int n3) {
        int[][][] rx = new int[n3][n2][n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(int ra, int[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = ra;
        }
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(int ra, int[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            fill(ra, rx[i2]);
        }
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(int ra, int[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            fill(ra, rx[i3]);
        }
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     */
    public static long[] filllong(long ra, int n1) {
        long[] rx = new long[n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static long[][] filllong(long ra, int n1, int n2) {
        long[][] rx = new long[n2][n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static long[][][] filllong(long ra, int n1, int n2, int n3) {
        long[][][] rx = new long[n3][n2][n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(long ra, long[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = ra;
        }
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(long ra, long[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            fill(ra, rx[i2]);
        }
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(long ra, long[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            fill(ra, rx[i3]);
        }
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     */
    public static float[] fillfloat(float ra, int n1) {
        float[] rx = new float[n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     */
    public static float[][] fillfloat(float ra, int n1, int n2) {
        float[][] rx = new float[n2][n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Returns an array initialized to a specified value.
     *
     * @param ra the value.
     * @param n1 1st array dimension.
     * @param n2 2nd array dimension.
     * @param n3 3rd array dimension.
     */
    public static float[][][] fillfloat(float ra, int n1, int n2, int n3) {
        float[][][] rx = new float[n3][n2][n1];
        fill(ra, rx);
        return rx;
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(float ra, float[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = ra;
        }
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(float ra, float[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            fill(ra, rx[i2]);
        }
    }

    /**
     * Fills the specified array with a specified value.
     *
     * @param ra the value.
     * @param rx the array.
     */
    public static void fill(float ra, float[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            fill(ra, rx[i3]);
        }
    }
    ///////////////////////////////////////////////////////////////////////////
    // transpose

    /**
     * Transpose the specified 2-D array.
     *
     * @param rx the array; must be regular.
     * @return the transposed array.
     */
    public static byte[][] transpose(byte[][] rx) {
        int n2 = rx.length;
        int n1 = rx[0].length;
        byte[][] ry = new byte[n1][n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            for (int i1 = 0; i1 < n1; ++i1) {
                ry[i1][i2] = rx[i2][i1];
            }
        }
        return ry;
    }

    /**
     * Transpose the specified 2-D array.
     *
     * @param rx the array; must be regular.
     * @return the transposed array.
     */
    public static short[][] transpose(short[][] rx) {
        int n2 = rx.length;
        int n1 = rx[0].length;
        short[][] ry = new short[n1][n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            for (int i1 = 0; i1 < n1; ++i1) {
                ry[i1][i2] = rx[i2][i1];
            }
        }
        return ry;
    }

    /**
     * Transpose the specified 2-D array.
     *
     * @param rx the array; must be regular.
     * @return the transposed array.
     */
    public static int[][] transpose(int[][] rx) {
        int n2 = rx.length;
        int n1 = rx[0].length;
        int[][] ry = new int[n1][n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            for (int i1 = 0; i1 < n1; ++i1) {
                ry[i1][i2] = rx[i2][i1];
            }
        }
        return ry;
    }

    /**
     * Transpose the specified 2-D array.
     *
     * @param rx the array; must be regular.
     * @return the transposed array.
     */
    public static long[][] transpose(long[][] rx) {
        int n2 = rx.length;
        int n1 = rx[0].length;
        long[][] ry = new long[n1][n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            for (int i1 = 0; i1 < n1; ++i1) {
                ry[i1][i2] = rx[i2][i1];
            }
        }
        return ry;
    }

    /**
     * Transpose the specified 2-D array.
     *
     * @param rx the array; must be regular.
     * @return the transposed array.
     */
    public static float[][] transpose(float[][] rx) {
        int n2 = rx.length;
        int n1 = rx[0].length;
        float[][] ry = new float[n1][n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            for (int i1 = 0; i1 < n1; ++i1) {
                ry[i1][i2] = rx[i2][i1];
            }
        }
        return ry;
    }

    /**
     * Transpose the specified 2-D array.
     *
     * @param cx the array; must be regular.
     * @return the transposed array.
     */
    public static float[][] ctranspose(float[][] cx) {
        int n2 = cx.length;
        int n1 = cx[0].length / 2;
        float[][] cy = new float[n1][2 * n2];
        for (int i2 = 0, iy = 0; i2 < n2; ++i2, iy += 2) {
            for (int i1 = 0, ix = 0; i1 < n1; ++i1, ix += 2) {
                cy[i1][iy] = cx[i2][ix];
                cy[i1][iy + 1] = cx[i2][ix + 1];
            }
        }
        return cy;
    }

    /**
     * Transpose the specified 2-D array.
     *
     * @param rx the array; must be regular.
     * @return the transposed array.
     */
    public static double[][] transpose(double[][] rx) {
        int n2 = rx.length;
        int n1 = rx[0].length;
        double[][] ry = new double[n1][n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            for (int i1 = 0; i1 < n1; ++i1) {
                ry[i1][i2] = rx[i2][i1];
            }
        }
        return ry;
    }
    ///////////////////////////////////////////////////////////////////////////
    // sorting

    /**
     * Sorts the elements of the specified array in ascending order. After
     * sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
     *
     * @param a the array to be sorted.
     */
    public static void quickSort(byte[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
     * Sorts indices of the elements of the specified array in ascending order.
     * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
     *
     * @param a the array.
     * @param i the indices to be sorted.
     */
    public static void quickIndexSort(byte[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
     * Partially sorts the elements of the specified array in ascending order.
     * After partial sorting, the element a[k] with specified index k has the
     * value it would have if the array were completely sorted. That is,
     * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
     *
     * @param k the index.
     * @param a the array to be partially sorted.
     */
    public static void quickPartialSort(int k, byte[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
     * Partially sorts indices of the elements of the specified array. After
     * partial sorting, the element i[k] with specified index k has the value it
     * would have if the indices were completely sorted. That is, a[i[0:k-1]]
     * &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
     *
     * @param k the index.
     * @param a the array.
     * @param i the indices to be partially sorted.
     */
    public static void quickPartialIndexSort(int k, byte[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }

    /**
     * Sorts the elements of the specified array in ascending order. After
     * sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
     *
     * @param a the array to be sorted.
     */
    public static void quickSort(short[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
     * Sorts indices of the elements of the specified array in ascending order.
     * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
     *
     * @param a the array.
     * @param i the indices to be sorted.
     */
    public static void quickIndexSort(short[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
     * Partially sorts the elements of the specified array in ascending order.
     * After partial sorting, the element a[k] with specified index k has the
     * value it would have if the array were completely sorted. That is,
     * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
     *
     * @param k the index.
     * @param a the array to be partially sorted.
     */
    public static void quickPartialSort(int k, short[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
     * Partially sorts indices of the elements of the specified array. After
     * partial sorting, the element i[k] with specified index k has the value it
     * would have if the indices were completely sorted. That is, a[i[0:k-1]]
     * &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
     *
     * @param k the index.
     * @param a the array.
     * @param i the indices to be partially sorted.
     */
    public static void quickPartialIndexSort(int k, short[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }

    /**
     * Sorts the elements of the specified array in ascending order. After
     * sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
     *
     * @param a the array to be sorted.
     */
    public static void quickSort(int[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
     * Sorts indices of the elements of the specified array in ascending order.
     * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
     *
     * @param a the array.
     * @param i the indices to be sorted.
     */
    public static void quickIndexSort(int[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
     * Partially sorts the elements of the specified array in ascending order.
     * After partial sorting, the element a[k] with specified index k has the
     * value it would have if the array were completely sorted. That is,
     * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
     *
     * @param k the index.
     * @param a the array to be partially sorted.
     */
    public static void quickPartialSort(int k, int[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
     * Partially sorts indices of the elements of the specified array. After
     * partial sorting, the element i[k] with specified index k has the value it
     * would have if the indices were completely sorted. That is, a[i[0:k-1]]
     * &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
     *
     * @param k the index.
     * @param a the array.
     * @param i the indices to be partially sorted.
     */
    public static void quickPartialIndexSort(int k, int[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }

    /**
     * Sorts the elements of the specified array in ascending order. After
     * sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
     *
     * @param a the array to be sorted.
     */
    public static void quickSort(long[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
     * Sorts indices of the elements of the specified array in ascending order.
     * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
     *
     * @param a the array.
     * @param i the indices to be sorted.
     */
    public static void quickIndexSort(long[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
     * Partially sorts the elements of the specified array in ascending order.
     * After partial sorting, the element a[k] with specified index k has the
     * value it would have if the array were completely sorted. That is,
     * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
     *
     * @param k the index.
     * @param a the array to be partially sorted.
     */
    public static void quickPartialSort(int k, long[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
     * Partially sorts indices of the elements of the specified array. After
     * partial sorting, the element i[k] with specified index k has the value it
     * would have if the indices were completely sorted. That is, a[i[0:k-1]]
     * &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
     *
     * @param k the index.
     * @param a the array.
     * @param i the indices to be partially sorted.
     */
    public static void quickPartialIndexSort(int k, long[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }

    /**
     * Sorts the elements of the specified array in ascending order. After
     * sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
     *
     * @param a the array to be sorted.
     */
    public static void quickSort(float[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
     * Sorts indices of the elements of the specified array in ascending order.
     * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
     *
     * @param a the array.
     * @param i the indices to be sorted.
     */
    public static void quickIndexSort(float[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
     * Partially sorts the elements of the specified array in ascending order.
     * After partial sorting, the element a[k] with specified index k has the
     * value it would have if the array were completely sorted. That is,
     * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
     *
     * @param k the index.
     * @param a the array to be partially sorted.
     */
    public static void quickPartialSort(int k, float[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
     * Partially sorts indices of the elements of the specified array. After
     * partial sorting, the element i[k] with specified index k has the value it
     * would have if the indices were completely sorted. That is, a[i[0:k-1]]
     * &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
     *
     * @param k the index.
     * @param a the array.
     * @param i the indices to be partially sorted.
     */
    public static void quickPartialIndexSort(int k, float[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }

    /**
     * Sorts the elements of the specified array in ascending order. After
     * sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
     *
     * @param a the array to be sorted.
     */
    public static void quickSort(double[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
     * Sorts indices of the elements of the specified array in ascending order.
     * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
     *
     * @param a the array.
     * @param i the indices to be sorted.
     */
    public static void quickIndexSort(double[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
     * Partially sorts the elements of the specified array in ascending order.
     * After partial sorting, the element a[k] with specified index k has the
     * value it would have if the array were completely sorted. That is,
     * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
     *
     * @param k the index.
     * @param a the array to be partially sorted.
     */
    public static void quickPartialSort(int k, double[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
     * Partially sorts indices of the elements of the specified array. After
     * partial sorting, the element i[k] with specified index k has the value it
     * would have if the indices were completely sorted. That is, a[i[0:k-1]]
     * &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
     *
     * @param k the index.
     * @param a the array.
     * @param i the indices to be partially sorted.
     */
    public static void quickPartialIndexSort(int k, double[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }
// Adapted from Bentley, J.L., and McIlroy, M.D., 1993, Engineering a sort
  // function, Software -- Practice and Experience, v. 23(11), p. 1249-1265.
  private static final int NSMALL_SORT =  7;
  private static final int NLARGE_SORT = 40;
  private static int med3(byte[] a, int i, int j, int k) {
    return a[i]<a[j] ? 
           (a[j]<a[k] ? j : a[i]<a[k] ? k : i) :
           (a[j]>a[k] ? j : a[i]>a[k] ? k : i);
  }
  private static int med3(byte[] a, int[] i, int j, int k, int l) {
    return a[i[j]]<a[i[k]] ? 
           (a[i[k]]<a[i[l]] ? k : a[i[j]]<a[i[l]] ? l : j) :
           (a[i[k]]>a[i[l]] ? k : a[i[j]]>a[i[l]] ? l : j);
  }
  private static void swap(byte[] a, int i, int j) {
    byte ai = a[i];
    a[i] = a[j];
    a[j] = ai;
  }
  private static void swap(byte[] a, int i, int j, int n) {
    while (n>0) {
      byte ai = a[i];
      a[i++] = a[j];
      a[j++] = ai;
      --n;
    }
  }
  private static void insertionSort(byte[] a, int p, int q) {
    for (int i=p; i<=q; ++i)
      for (int j=i; j>p && a[j-1]>a[j]; --j)
        swap(a,j,j-1);
  }
  private static void insertionSort(byte[] a, int[] i, int p, int q) {
    for (int j=p; j<=q; ++j)
      for (int k=j; k>p && a[i[k-1]]>a[i[k]]; --k)
        swap(i,k,k-1);
  }
  private static void quickSort(byte[] a, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,p,r-1,m);
      if (q>s+1)
        quickSort(a,s+1,q,m);
    }
  }
  private static void quickSort(byte[] a, int[] i, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,i,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,i,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,i,p,r-1,m);
      if (q>s+1)
        quickSort(a,i,s+1,q,m);
    }
  }
  private static void quickPartition(byte[] x, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,j,j+s,j+2*s);
        k = med3(x,k-s,k,k+s);
        l = med3(x,l-2*s,l-s,l);
      }
      k = med3(x,j,k,l);
    }
    byte y = x[k];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[b]<=y) {
        if (x[b]==y) 
          swap(x,a++,b);
        ++b;
      }
      while (c>=b && x[c]>=y) {
        if (x[c]==y)
          swap(x,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(x,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(x,p,b-r,r);
    swap(x,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  private static void quickPartition(byte[] x, int[] i, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,i,j,j+s,j+2*s);
        k = med3(x,i,k-s,k,k+s);
        l = med3(x,i,l-2*s,l-s,l);
      }
      k = med3(x,i,j,k,l);
    }
    byte y = x[i[k]];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[i[b]]<=y) {
        if (x[i[b]]==y) 
          swap(i,a++,b);
        ++b;
      }
      while (c>=b && x[i[c]]>=y) {
        if (x[i[c]]==y)
          swap(i,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(i,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(i,p,b-r,r);
    swap(i,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  private static int med3(short[] a, int i, int j, int k) {
    return a[i]<a[j] ? 
           (a[j]<a[k] ? j : a[i]<a[k] ? k : i) :
           (a[j]>a[k] ? j : a[i]>a[k] ? k : i);
  }
  private static int med3(short[] a, int[] i, int j, int k, int l) {
    return a[i[j]]<a[i[k]] ? 
           (a[i[k]]<a[i[l]] ? k : a[i[j]]<a[i[l]] ? l : j) :
           (a[i[k]]>a[i[l]] ? k : a[i[j]]>a[i[l]] ? l : j);
  }
  private static void swap(short[] a, int i, int j) {
    short ai = a[i];
    a[i] = a[j];
    a[j] = ai;
  }
  private static void swap(short[] a, int i, int j, int n) {
    while (n>0) {
      short ai = a[i];
      a[i++] = a[j];
      a[j++] = ai;
      --n;
    }
  }
  private static void insertionSort(short[] a, int p, int q) {
    for (int i=p; i<=q; ++i)
      for (int j=i; j>p && a[j-1]>a[j]; --j)
        swap(a,j,j-1);
  }
  private static void insertionSort(short[] a, int[] i, int p, int q) {
    for (int j=p; j<=q; ++j)
      for (int k=j; k>p && a[i[k-1]]>a[i[k]]; --k)
        swap(i,k,k-1);
  }
  private static void quickSort(short[] a, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,p,r-1,m);
      if (q>s+1)
        quickSort(a,s+1,q,m);
    }
  }
  private static void quickSort(short[] a, int[] i, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,i,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,i,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,i,p,r-1,m);
      if (q>s+1)
        quickSort(a,i,s+1,q,m);
    }
  }
  private static void quickPartition(short[] x, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,j,j+s,j+2*s);
        k = med3(x,k-s,k,k+s);
        l = med3(x,l-2*s,l-s,l);
      }
      k = med3(x,j,k,l);
    }
    short y = x[k];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[b]<=y) {
        if (x[b]==y) 
          swap(x,a++,b);
        ++b;
      }
      while (c>=b && x[c]>=y) {
        if (x[c]==y)
          swap(x,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(x,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(x,p,b-r,r);
    swap(x,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  private static void quickPartition(short[] x, int[] i, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,i,j,j+s,j+2*s);
        k = med3(x,i,k-s,k,k+s);
        l = med3(x,i,l-2*s,l-s,l);
      }
      k = med3(x,i,j,k,l);
    }
    short y = x[i[k]];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[i[b]]<=y) {
        if (x[i[b]]==y) 
          swap(i,a++,b);
        ++b;
      }
      while (c>=b && x[i[c]]>=y) {
        if (x[i[c]]==y)
          swap(i,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(i,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(i,p,b-r,r);
    swap(i,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  private static int med3(int[] a, int i, int j, int k) {
    return a[i]<a[j] ? 
           (a[j]<a[k] ? j : a[i]<a[k] ? k : i) :
           (a[j]>a[k] ? j : a[i]>a[k] ? k : i);
  }
  private static int med3(int[] a, int[] i, int j, int k, int l) {
    return a[i[j]]<a[i[k]] ? 
           (a[i[k]]<a[i[l]] ? k : a[i[j]]<a[i[l]] ? l : j) :
           (a[i[k]]>a[i[l]] ? k : a[i[j]]>a[i[l]] ? l : j);
  }
  private static void swap(int[] a, int i, int j) {
    int ai = a[i];
    a[i] = a[j];
    a[j] = ai;
  }
  private static void swap(int[] a, int i, int j, int n) {
    while (n>0) {
      int ai = a[i];
      a[i++] = a[j];
      a[j++] = ai;
      --n;
    }
  }
  private static void insertionSort(int[] a, int p, int q) {
    for (int i=p; i<=q; ++i)
      for (int j=i; j>p && a[j-1]>a[j]; --j)
        swap(a,j,j-1);
  }
  private static void insertionSort(int[] a, int[] i, int p, int q) {
    for (int j=p; j<=q; ++j)
      for (int k=j; k>p && a[i[k-1]]>a[i[k]]; --k)
        swap(i,k,k-1);
  }
  private static void quickSort(int[] a, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,p,r-1,m);
      if (q>s+1)
        quickSort(a,s+1,q,m);
    }
  }
  private static void quickSort(int[] a, int[] i, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,i,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,i,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,i,p,r-1,m);
      if (q>s+1)
        quickSort(a,i,s+1,q,m);
    }
  }
  private static void quickPartition(int[] x, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,j,j+s,j+2*s);
        k = med3(x,k-s,k,k+s);
        l = med3(x,l-2*s,l-s,l);
      }
      k = med3(x,j,k,l);
    }
    int y = x[k];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[b]<=y) {
        if (x[b]==y) 
          swap(x,a++,b);
        ++b;
      }
      while (c>=b && x[c]>=y) {
        if (x[c]==y)
          swap(x,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(x,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(x,p,b-r,r);
    swap(x,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  private static void quickPartition(int[] x, int[] i, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,i,j,j+s,j+2*s);
        k = med3(x,i,k-s,k,k+s);
        l = med3(x,i,l-2*s,l-s,l);
      }
      k = med3(x,i,j,k,l);
    }
    int y = x[i[k]];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[i[b]]<=y) {
        if (x[i[b]]==y) 
          swap(i,a++,b);
        ++b;
      }
      while (c>=b && x[i[c]]>=y) {
        if (x[i[c]]==y)
          swap(i,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(i,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(i,p,b-r,r);
    swap(i,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  private static int med3(long[] a, int i, int j, int k) {
    return a[i]<a[j] ? 
           (a[j]<a[k] ? j : a[i]<a[k] ? k : i) :
           (a[j]>a[k] ? j : a[i]>a[k] ? k : i);
  }
  private static int med3(long[] a, int[] i, int j, int k, int l) {
    return a[i[j]]<a[i[k]] ? 
           (a[i[k]]<a[i[l]] ? k : a[i[j]]<a[i[l]] ? l : j) :
           (a[i[k]]>a[i[l]] ? k : a[i[j]]>a[i[l]] ? l : j);
  }
  private static void swap(long[] a, int i, int j) {
    long ai = a[i];
    a[i] = a[j];
    a[j] = ai;
  }
  private static void swap(long[] a, int i, int j, int n) {
    while (n>0) {
      long ai = a[i];
      a[i++] = a[j];
      a[j++] = ai;
      --n;
    }
  }
  private static void insertionSort(long[] a, int p, int q) {
    for (int i=p; i<=q; ++i)
      for (int j=i; j>p && a[j-1]>a[j]; --j)
        swap(a,j,j-1);
  }
  private static void insertionSort(long[] a, int[] i, int p, int q) {
    for (int j=p; j<=q; ++j)
      for (int k=j; k>p && a[i[k-1]]>a[i[k]]; --k)
        swap(i,k,k-1);
  }
  private static void quickSort(long[] a, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,p,r-1,m);
      if (q>s+1)
        quickSort(a,s+1,q,m);
    }
  }
  private static void quickSort(long[] a, int[] i, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,i,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,i,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,i,p,r-1,m);
      if (q>s+1)
        quickSort(a,i,s+1,q,m);
    }
  }
  private static void quickPartition(long[] x, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,j,j+s,j+2*s);
        k = med3(x,k-s,k,k+s);
        l = med3(x,l-2*s,l-s,l);
      }
      k = med3(x,j,k,l);
    }
    long y = x[k];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[b]<=y) {
        if (x[b]==y) 
          swap(x,a++,b);
        ++b;
      }
      while (c>=b && x[c]>=y) {
        if (x[c]==y)
          swap(x,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(x,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(x,p,b-r,r);
    swap(x,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  private static void quickPartition(long[] x, int[] i, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,i,j,j+s,j+2*s);
        k = med3(x,i,k-s,k,k+s);
        l = med3(x,i,l-2*s,l-s,l);
      }
      k = med3(x,i,j,k,l);
    }
    long y = x[i[k]];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[i[b]]<=y) {
        if (x[i[b]]==y) 
          swap(i,a++,b);
        ++b;
      }
      while (c>=b && x[i[c]]>=y) {
        if (x[i[c]]==y)
          swap(i,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(i,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(i,p,b-r,r);
    swap(i,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  private static int med3(float[] a, int i, int j, int k) {
    return a[i]<a[j] ? 
           (a[j]<a[k] ? j : a[i]<a[k] ? k : i) :
           (a[j]>a[k] ? j : a[i]>a[k] ? k : i);
  }
  private static int med3(float[] a, int[] i, int j, int k, int l) {
    return a[i[j]]<a[i[k]] ? 
           (a[i[k]]<a[i[l]] ? k : a[i[j]]<a[i[l]] ? l : j) :
           (a[i[k]]>a[i[l]] ? k : a[i[j]]>a[i[l]] ? l : j);
  }
  private static void swap(float[] a, int i, int j) {
    float ai = a[i];
    a[i] = a[j];
    a[j] = ai;
  }
  private static void swap(float[] a, int i, int j, int n) {
    while (n>0) {
      float ai = a[i];
      a[i++] = a[j];
      a[j++] = ai;
      --n;
    }
  }
  private static void insertionSort(float[] a, int p, int q) {
    for (int i=p; i<=q; ++i)
      for (int j=i; j>p && a[j-1]>a[j]; --j)
        swap(a,j,j-1);
  }
  private static void insertionSort(float[] a, int[] i, int p, int q) {
    for (int j=p; j<=q; ++j)
      for (int k=j; k>p && a[i[k-1]]>a[i[k]]; --k)
        swap(i,k,k-1);
  }
  private static void quickSort(float[] a, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,p,r-1,m);
      if (q>s+1)
        quickSort(a,s+1,q,m);
    }
  }
  private static void quickSort(float[] a, int[] i, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,i,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,i,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,i,p,r-1,m);
      if (q>s+1)
        quickSort(a,i,s+1,q,m);
    }
  }
  private static void quickPartition(float[] x, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,j,j+s,j+2*s);
        k = med3(x,k-s,k,k+s);
        l = med3(x,l-2*s,l-s,l);
      }
      k = med3(x,j,k,l);
    }
    float y = x[k];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[b]<=y) {
        if (x[b]==y) 
          swap(x,a++,b);
        ++b;
      }
      while (c>=b && x[c]>=y) {
        if (x[c]==y)
          swap(x,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(x,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(x,p,b-r,r);
    swap(x,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  private static void quickPartition(float[] x, int[] i, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,i,j,j+s,j+2*s);
        k = med3(x,i,k-s,k,k+s);
        l = med3(x,i,l-2*s,l-s,l);
      }
      k = med3(x,i,j,k,l);
    }
    float y = x[i[k]];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[i[b]]<=y) {
        if (x[i[b]]==y) 
          swap(i,a++,b);
        ++b;
      }
      while (c>=b && x[i[c]]>=y) {
        if (x[i[c]]==y)
          swap(i,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(i,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(i,p,b-r,r);
    swap(i,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  private static int med3(double[] a, int i, int j, int k) {
    return a[i]<a[j] ? 
           (a[j]<a[k] ? j : a[i]<a[k] ? k : i) :
           (a[j]>a[k] ? j : a[i]>a[k] ? k : i);
  }
  private static int med3(double[] a, int[] i, int j, int k, int l) {
    return a[i[j]]<a[i[k]] ? 
           (a[i[k]]<a[i[l]] ? k : a[i[j]]<a[i[l]] ? l : j) :
           (a[i[k]]>a[i[l]] ? k : a[i[j]]>a[i[l]] ? l : j);
  }
  private static void swap(double[] a, int i, int j) {
    double ai = a[i];
    a[i] = a[j];
    a[j] = ai;
  }
  private static void swap(double[] a, int i, int j, int n) {
    while (n>0) {
      double ai = a[i];
      a[i++] = a[j];
      a[j++] = ai;
      --n;
    }
  }
  private static void insertionSort(double[] a, int p, int q) {
    for (int i=p; i<=q; ++i)
      for (int j=i; j>p && a[j-1]>a[j]; --j)
        swap(a,j,j-1);
  }
  private static void insertionSort(double[] a, int[] i, int p, int q) {
    for (int j=p; j<=q; ++j)
      for (int k=j; k>p && a[i[k-1]]>a[i[k]]; --k)
        swap(i,k,k-1);
  }
  private static void quickSort(double[] a, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,p,r-1,m);
      if (q>s+1)
        quickSort(a,s+1,q,m);
    }
  }
  private static void quickSort(double[] a, int[] i, int p, int q, int[] m) {
    if (q-p<=NSMALL_SORT) {
      insertionSort(a,i,p,q);
    } else {
      m[0] = p;
      m[1] = q;
      quickPartition(a,i,m);
      int r = m[0];
      int s = m[1];
      if (p<r-1)
        quickSort(a,i,p,r-1,m);
      if (q>s+1)
        quickSort(a,i,s+1,q,m);
    }
  }
  private static void quickPartition(double[] x, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,j,j+s,j+2*s);
        k = med3(x,k-s,k,k+s);
        l = med3(x,l-2*s,l-s,l);
      }
      k = med3(x,j,k,l);
    }
    double y = x[k];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[b]<=y) {
        if (x[b]==y) 
          swap(x,a++,b);
        ++b;
      }
      while (c>=b && x[c]>=y) {
        if (x[c]==y)
          swap(x,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(x,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(x,p,b-r,r);
    swap(x,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  private static void quickPartition(double[] x, int[] i, int[] m) {
    int p = m[0];
    int q = m[1];
    int n = q-p+1;
    int k = (p+q)/2;
    if (n>NSMALL_SORT) {
      int j = p;
      int l = q;
      if (n>NLARGE_SORT) {
        int s = n/8;
        j = med3(x,i,j,j+s,j+2*s);
        k = med3(x,i,k-s,k,k+s);
        l = med3(x,i,l-2*s,l-s,l);
      }
      k = med3(x,i,j,k,l);
    }
    double y = x[i[k]];
    int a=p,b=p;
    int c=q,d=q;
    while (true) {
      while (b<=c && x[i[b]]<=y) {
        if (x[i[b]]==y) 
          swap(i,a++,b);
        ++b;
      }
      while (c>=b && x[i[c]]>=y) {
        if (x[i[c]]==y)
          swap(i,c,d--);
        --c;
      }
      if (b>c)
        break;
      swap(i,b,c);
      ++b;
      --c;
    }
    int r = Math.min(a-p,b-a); 
    int s = Math.min(d-c,q-d); 
    int t = q+1;
    swap(i,p,b-r,r);
    swap(i,b,t-s,s);
    m[0] = p+(b-a); // p --- m[0]-1 | m[0] --- m[1] | m[1]+1 --- q
    m[1] = q-(d-c); //   x<y               x=y               x>y
  }
  ///////////////////////////////////////////////////////////////////////////
  // binary search

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(byte[] a, byte x) {
    return binarySearch(a,x,a.length);
  }

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * This method is most efficient when called repeatedly for slightly 
   * changing search values; in such cases, the index returned from one 
   * call should be passed in the next.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @param i the index at which to begin the search. If negative, this 
   *  method interprets this index as if returned from a previous call.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(byte[] a, byte x, int i) {
    int n = a.length;
    int nm1 = n-1;
    int low = 0;
    int high = nm1;
    boolean increasing = n<2 || a[0]<a[1];
    if (i<n) {
      high = (0<=i)?i:-(i+1);
      low = high-1;
      int step = 1;
      if (increasing) {
        for (; 0<low && x<a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]<x; high+=step,step+=step)
          low = high;
      } else {
        for (; 0<low && x>a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]>x; high+=step,step+=step)
          low = high;
      }
      if (low<0) low = 0;
      if (high>nm1) high = nm1;
    }
    if (increasing) {
      while (low<=high) {
        int mid = (low+high)>>1;
        byte amid = a[mid];
        if (amid<x)
          low = mid+1;
        else if (amid>x)
          high = mid-1;
        else
          return mid;
      }
    } else {
      while (low<=high) {
        int mid = (low+high)>>1;
        byte amid = a[mid];
        if (amid>x)
          low = mid+1;
        else if (amid<x)
          high = mid-1;
        else
          return mid;
      }
    }
    return -(low+1);
  }

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(short[] a, short x) {
    return binarySearch(a,x,a.length);
  }

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * This method is most efficient when called repeatedly for slightly 
   * changing search values; in such cases, the index returned from one 
   * call should be passed in the next.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @param i the index at which to begin the search. If negative, this 
   *  method interprets this index as if returned from a previous call.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(short[] a, short x, int i) {
    int n = a.length;
    int nm1 = n-1;
    int low = 0;
    int high = nm1;
    boolean increasing = n<2 || a[0]<a[1];
    if (i<n) {
      high = (0<=i)?i:-(i+1);
      low = high-1;
      int step = 1;
      if (increasing) {
        for (; 0<low && x<a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]<x; high+=step,step+=step)
          low = high;
      } else {
        for (; 0<low && x>a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]>x; high+=step,step+=step)
          low = high;
      }
      if (low<0) low = 0;
      if (high>nm1) high = nm1;
    }
    if (increasing) {
      while (low<=high) {
        int mid = (low+high)>>1;
        short amid = a[mid];
        if (amid<x)
          low = mid+1;
        else if (amid>x)
          high = mid-1;
        else
          return mid;
      }
    } else {
      while (low<=high) {
        int mid = (low+high)>>1;
        short amid = a[mid];
        if (amid>x)
          low = mid+1;
        else if (amid<x)
          high = mid-1;
        else
          return mid;
      }
    }
    return -(low+1);
  }

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(int[] a, int x) {
    return binarySearch(a,x,a.length);
  }

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * This method is most efficient when called repeatedly for slightly 
   * changing search values; in such cases, the index returned from one 
   * call should be passed in the next.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @param i the index at which to begin the search. If negative, this 
   *  method interprets this index as if returned from a previous call.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(int[] a, int x, int i) {
    int n = a.length;
    int nm1 = n-1;
    int low = 0;
    int high = nm1;
    boolean increasing = n<2 || a[0]<a[1];
    if (i<n) {
      high = (0<=i)?i:-(i+1);
      low = high-1;
      int step = 1;
      if (increasing) {
        for (; 0<low && x<a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]<x; high+=step,step+=step)
          low = high;
      } else {
        for (; 0<low && x>a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]>x; high+=step,step+=step)
          low = high;
      }
      if (low<0) low = 0;
      if (high>nm1) high = nm1;
    }
    if (increasing) {
      while (low<=high) {
        int mid = (low+high)>>1;
        int amid = a[mid];
        if (amid<x)
          low = mid+1;
        else if (amid>x)
          high = mid-1;
        else
          return mid;
      }
    } else {
      while (low<=high) {
        int mid = (low+high)>>1;
        int amid = a[mid];
        if (amid>x)
          low = mid+1;
        else if (amid<x)
          high = mid-1;
        else
          return mid;
      }
    }
    return -(low+1);
  }

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(long[] a, long x) {
    return binarySearch(a,x,a.length);
  }

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * This method is most efficient when called repeatedly for slightly 
   * changing search values; in such cases, the index returned from one 
   * call should be passed in the next.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @param i the index at which to begin the search. If negative, this 
   *  method interprets this index as if returned from a previous call.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(long[] a, long x, int i) {
    int n = a.length;
    int nm1 = n-1;
    int low = 0;
    int high = nm1;
    boolean increasing = n<2 || a[0]<a[1];
    if (i<n) {
      high = (0<=i)?i:-(i+1);
      low = high-1;
      int step = 1;
      if (increasing) {
        for (; 0<low && x<a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]<x; high+=step,step+=step)
          low = high;
      } else {
        for (; 0<low && x>a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]>x; high+=step,step+=step)
          low = high;
      }
      if (low<0) low = 0;
      if (high>nm1) high = nm1;
    }
    if (increasing) {
      while (low<=high) {
        int mid = (low+high)>>1;
        long amid = a[mid];
        if (amid<x)
          low = mid+1;
        else if (amid>x)
          high = mid-1;
        else
          return mid;
      }
    } else {
      while (low<=high) {
        int mid = (low+high)>>1;
        long amid = a[mid];
        if (amid>x)
          low = mid+1;
        else if (amid<x)
          high = mid-1;
        else
          return mid;
      }
    }
    return -(low+1);
  }

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(float[] a, float x) {
    return binarySearch(a,x,a.length);
  }

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * This method is most efficient when called repeatedly for slightly 
   * changing search values; in such cases, the index returned from one 
   * call should be passed in the next.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @param i the index at which to begin the search. If negative, this 
   *  method interprets this index as if returned from a previous call.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(float[] a, float x, int i) {
    int n = a.length;
    int nm1 = n-1;
    int low = 0;
    int high = nm1;
    boolean increasing = n<2 || a[0]<a[1];
    if (i<n) {
      high = (0<=i)?i:-(i+1);
      low = high-1;
      int step = 1;
      if (increasing) {
        for (; 0<low && x<a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]<x; high+=step,step+=step)
          low = high;
      } else {
        for (; 0<low && x>a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]>x; high+=step,step+=step)
          low = high;
      }
      if (low<0) low = 0;
      if (high>nm1) high = nm1;
    }
    if (increasing) {
      while (low<=high) {
        int mid = (low+high)>>1;
        float amid = a[mid];
        if (amid<x)
          low = mid+1;
        else if (amid>x)
          high = mid-1;
        else
          return mid;
      }
    } else {
      while (low<=high) {
        int mid = (low+high)>>1;
        float amid = a[mid];
        if (amid>x)
          low = mid+1;
        else if (amid<x)
          high = mid-1;
        else
          return mid;
      }
    }
    return -(low+1);
  }

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(double[] a, double x) {
    return binarySearch(a,x,a.length);
  }

  /**
   * Performs a binary search in a monotonic array of values. Values are
   * assumed to increase or decrease monotonically, with no equal values.
   * This method is most efficient when called repeatedly for slightly 
   * changing search values; in such cases, the index returned from one 
   * call should be passed in the next.
   * <p>
   * Warning: this method does not ensure that the specified array is
   * monotonic; that check would be more costly than this search.
   * @param a the array of values, assumed to be monotonic.
   * @param x the value for which to search.
   * @param i the index at which to begin the search. If negative, this 
   *  method interprets this index as if returned from a previous call.
   * @return the index at which the specified value is found, or, if not
   *  found, -(i+1), where i equals the index at which the specified value 
   *  would be located if it was inserted into the monotonic array.
   */
  public static int binarySearch(double[] a, double x, int i) {
    int n = a.length;
    int nm1 = n-1;
    int low = 0;
    int high = nm1;
    boolean increasing = n<2 || a[0]<a[1];
    if (i<n) {
      high = (0<=i)?i:-(i+1);
      low = high-1;
      int step = 1;
      if (increasing) {
        for (; 0<low && x<a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]<x; high+=step,step+=step)
          low = high;
      } else {
        for (; 0<low && x>a[low]; low-=step,step+=step)
          high = low;
        for (; high<nm1 && a[high]>x; high+=step,step+=step)
          low = high;
      }
      if (low<0) low = 0;
      if (high>nm1) high = nm1;
    }
    if (increasing) {
      while (low<=high) {
        int mid = (low+high)>>1;
        double amid = a[mid];
        if (amid<x)
          low = mid+1;
        else if (amid>x)
          high = mid-1;
        else
          return mid;
      }
    } else {
      while (low<=high) {
        int mid = (low+high)>>1;
        double amid = a[mid];
        if (amid>x)
          low = mid+1;
        else if (amid<x)
          high = mid-1;
        else
          return mid;
      }
    }
    return -(low+1);
  }
   ///////////////////////////////////////////////////////////////////////////
  // sum

  public static byte sum(byte[] rx) {
    int n1 = rx.length;
    byte sum = 0;
    for (int i1=0; i1<n1; ++i1)
      sum += rx[i1];
    return sum;
  }
  public static byte sum(byte[][] rx) {
    int n2 = rx.length;
    byte sum = 0;
    for (int i2=0; i2<n2; ++i2)
      sum += sum(rx[i2]);
    return sum;
  }
  public static byte sum(byte[][][] rx) {
    int n3 = rx.length;
    byte sum = 0;
    for (int i3=0; i3<n3; ++i3)
      sum += sum(rx[i3]);
    return sum;
  }
  public static short sum(short[] rx) {
    int n1 = rx.length;
    short sum = 0;
    for (int i1=0; i1<n1; ++i1)
      sum += rx[i1];
    return sum;
  }
  public static short sum(short[][] rx) {
    int n2 = rx.length;
    short sum = 0;
    for (int i2=0; i2<n2; ++i2)
      sum += sum(rx[i2]);
    return sum;
  }
  public static short sum(short[][][] rx) {
    int n3 = rx.length;
    short sum = 0;
    for (int i3=0; i3<n3; ++i3)
      sum += sum(rx[i3]);
    return sum;
  }
  public static int sum(int[] rx) {
    int n1 = rx.length;
    int sum = 0;
    for (int i1=0; i1<n1; ++i1)
      sum += rx[i1];
    return sum;
  }
  public static int sum(int[][] rx) {
    int n2 = rx.length;
    int sum = 0;
    for (int i2=0; i2<n2; ++i2)
      sum += sum(rx[i2]);
    return sum;
  }
  public static int sum(int[][][] rx) {
    int n3 = rx.length;
    int sum = 0;
    for (int i3=0; i3<n3; ++i3)
      sum += sum(rx[i3]);
    return sum;
  }
  public static long sum(long[] rx) {
    int n1 = rx.length;
    long sum = 0;
    for (int i1=0; i1<n1; ++i1)
      sum += rx[i1];
    return sum;
  }
  public static long sum(long[][] rx) {
    int n2 = rx.length;
    long sum = 0;
    for (int i2=0; i2<n2; ++i2)
      sum += sum(rx[i2]);
    return sum;
  }
  public static long sum(long[][][] rx) {
    int n3 = rx.length;
    long sum = 0;
    for (int i3=0; i3<n3; ++i3)
      sum += sum(rx[i3]);
    return sum;
  }
  public static float sum(float[] rx) {
    int n1 = rx.length;
    float sum = 0.0f;
    for (int i1=0; i1<n1; ++i1)
      sum += rx[i1];
    return sum;
  }
  public static float sum(float[][] rx) {
    int n2 = rx.length;
    float sum = 0.0f;
    for (int i2=0; i2<n2; ++i2)
      sum += sum(rx[i2]);
    return sum;
  }
  public static float sum(float[][][] rx) {
    int n3 = rx.length;
    float sum = 0.0f;
    for (int i3=0; i3<n3; ++i3)
      sum += sum(rx[i3]);
    return sum;
  }
  public static double sum(double[] rx) {
    int n1 = rx.length;
    double sum = 0.0;
    for (int i1=0; i1<n1; ++i1)
      sum += rx[i1];
    return sum;
  }
  public static double sum(double[][] rx) {
    int n2 = rx.length;
    double sum = 0.0;
    for (int i2=0; i2<n2; ++i2)
      sum += sum(rx[i2]);
    return sum;
  }
  public static double sum(double[][][] rx) {
    int n3 = rx.length;
    double sum = 0.0;
    for (int i3=0; i3<n3; ++i3)
      sum += sum(rx[i3]);
    return sum;
  }

  ///////////////////////////////////////////////////////////////////////////
  // max, min

  public static byte max(byte[] rx) {
    return max(rx,null);
  }
  public static byte max(byte[][] rx) {
    return max(rx,null);
  }
  public static byte max(byte[][][] rx) {
    return max(rx,null);
  }
  public static byte max(byte[] rx, int[] index) {
    int i1max = 0;
    byte max = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]>max) {
        max = rx[i1];
        i1max = i1;
      }
    }
    if (index!=null)
      index[0] = i1max;
    return max;
  }
  public static byte max(byte[][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    byte max = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      byte[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]>max) {
          max = rxi2[i1];
          i2max = i2;
          i1max = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
    }
    return max;
  }
  public static byte max(byte[][][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    int i3max = 0;
    byte max = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      byte[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        byte[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]>max) {
            max = rxi3i2[i1];
            i1max = i1;
            i2max = i2;
            i3max = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
      index[2] = i3max;
    }
    return max;
  }
  public static byte min(byte[] rx) {
    return min(rx,null);
  }
  public static byte min(byte[][] rx) {
    return min(rx,null);
  }
  public static byte min(byte[][][] rx) {
    return min(rx,null);
  }
  public static byte min(byte[] rx, int[] index) {
    int i1min = 0;
    byte min = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]<min) {
        min = rx[i1];
        i1min = i1;
      }
    }
    if (index!=null)
      index[0] = i1min;
    return min;
  }
  public static byte min(byte[][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    byte min = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      byte[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]<min) {
          min = rxi2[i1];
          i2min = i2;
          i1min = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
    }
    return min;
  }
  public static byte min(byte[][][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    int i3min = 0;
    byte min = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      byte[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        byte[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]<min) {
            min = rxi3i2[i1];
            i1min = i1;
            i2min = i2;
            i3min = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
      index[2] = i3min;
    }
    return min;
  }
  public static short max(short[] rx) {
    return max(rx,null);
  }
  public static short max(short[][] rx) {
    return max(rx,null);
  }
  public static short max(short[][][] rx) {
    return max(rx,null);
  }
  public static short max(short[] rx, int[] index) {
    int i1max = 0;
    short max = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]>max) {
        max = rx[i1];
        i1max = i1;
      }
    }
    if (index!=null)
      index[0] = i1max;
    return max;
  }
  public static short max(short[][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    short max = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      short[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]>max) {
          max = rxi2[i1];
          i2max = i2;
          i1max = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
    }
    return max;
  }
  public static short max(short[][][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    int i3max = 0;
    short max = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      short[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        short[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]>max) {
            max = rxi3i2[i1];
            i1max = i1;
            i2max = i2;
            i3max = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
      index[2] = i3max;
    }
    return max;
  }
  public static short min(short[] rx) {
    return min(rx,null);
  }
  public static short min(short[][] rx) {
    return min(rx,null);
  }
  public static short min(short[][][] rx) {
    return min(rx,null);
  }
  public static short min(short[] rx, int[] index) {
    int i1min = 0;
    short min = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]<min) {
        min = rx[i1];
        i1min = i1;
      }
    }
    if (index!=null)
      index[0] = i1min;
    return min;
  }
  public static short min(short[][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    short min = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      short[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]<min) {
          min = rxi2[i1];
          i2min = i2;
          i1min = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
    }
    return min;
  }
  public static short min(short[][][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    int i3min = 0;
    short min = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      short[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        short[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]<min) {
            min = rxi3i2[i1];
            i1min = i1;
            i2min = i2;
            i3min = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
      index[2] = i3min;
    }
    return min;
  }
  public static int max(int[] rx) {
    return max(rx,null);
  }
  public static int max(int[][] rx) {
    return max(rx,null);
  }
  public static int max(int[][][] rx) {
    return max(rx,null);
  }
  public static int max(int[] rx, int[] index) {
    int i1max = 0;
    int max = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]>max) {
        max = rx[i1];
        i1max = i1;
      }
    }
    if (index!=null)
      index[0] = i1max;
    return max;
  }
  public static int max(int[][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    int max = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      int[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]>max) {
          max = rxi2[i1];
          i2max = i2;
          i1max = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
    }
    return max;
  }
  public static int max(int[][][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    int i3max = 0;
    int max = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      int[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        int[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]>max) {
            max = rxi3i2[i1];
            i1max = i1;
            i2max = i2;
            i3max = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
      index[2] = i3max;
    }
    return max;
  }
  public static int min(int[] rx) {
    return min(rx,null);
  }
  public static int min(int[][] rx) {
    return min(rx,null);
  }
  public static int min(int[][][] rx) {
    return min(rx,null);
  }
  public static int min(int[] rx, int[] index) {
    int i1min = 0;
    int min = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]<min) {
        min = rx[i1];
        i1min = i1;
      }
    }
    if (index!=null)
      index[0] = i1min;
    return min;
  }
  public static int min(int[][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    int min = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      int[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]<min) {
          min = rxi2[i1];
          i2min = i2;
          i1min = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
    }
    return min;
  }
  public static int min(int[][][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    int i3min = 0;
    int min = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      int[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        int[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]<min) {
            min = rxi3i2[i1];
            i1min = i1;
            i2min = i2;
            i3min = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
      index[2] = i3min;
    }
    return min;
  }
  public static long max(long[] rx) {
    return max(rx,null);
  }
  public static long max(long[][] rx) {
    return max(rx,null);
  }
  public static long max(long[][][] rx) {
    return max(rx,null);
  }
  public static long max(long[] rx, int[] index) {
    int i1max = 0;
    long max = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]>max) {
        max = rx[i1];
        i1max = i1;
      }
    }
    if (index!=null)
      index[0] = i1max;
    return max;
  }
  public static long max(long[][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    long max = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      long[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]>max) {
          max = rxi2[i1];
          i2max = i2;
          i1max = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
    }
    return max;
  }
  public static long max(long[][][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    int i3max = 0;
    long max = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      long[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        long[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]>max) {
            max = rxi3i2[i1];
            i1max = i1;
            i2max = i2;
            i3max = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
      index[2] = i3max;
    }
    return max;
  }
  public static long min(long[] rx) {
    return min(rx,null);
  }
  public static long min(long[][] rx) {
    return min(rx,null);
  }
  public static long min(long[][][] rx) {
    return min(rx,null);
  }
  public static long min(long[] rx, int[] index) {
    int i1min = 0;
    long min = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]<min) {
        min = rx[i1];
        i1min = i1;
      }
    }
    if (index!=null)
      index[0] = i1min;
    return min;
  }
  public static long min(long[][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    long min = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      long[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]<min) {
          min = rxi2[i1];
          i2min = i2;
          i1min = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
    }
    return min;
  }
  public static long min(long[][][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    int i3min = 0;
    long min = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      long[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        long[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]<min) {
            min = rxi3i2[i1];
            i1min = i1;
            i2min = i2;
            i3min = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
      index[2] = i3min;
    }
    return min;
  }
  public static float max(float[] rx) {
    return max(rx,null);
  }
  public static float max(float[][] rx) {
    return max(rx,null);
  }
  public static float max(float[][][] rx) {
    return max(rx,null);
  }
  public static float max(float[] rx, int[] index) {
    int i1max = 0;
    float max = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]>max) {
        max = rx[i1];
        i1max = i1;
      }
    }
    if (index!=null)
      index[0] = i1max;
    return max;
  }
  public static float max(float[][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    float max = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      float[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]>max) {
          max = rxi2[i1];
          i2max = i2;
          i1max = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
    }
    return max;
  }
  public static float max(float[][][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    int i3max = 0;
    float max = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      float[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        float[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]>max) {
            max = rxi3i2[i1];
            i1max = i1;
            i2max = i2;
            i3max = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
      index[2] = i3max;
    }
    return max;
  }
  public static float min(float[] rx) {
    return min(rx,null);
  }
  public static float min(float[][] rx) {
    return min(rx,null);
  }
  public static float min(float[][][] rx) {
    return min(rx,null);
  }
  public static float min(float[] rx, int[] index) {
    int i1min = 0;
    float min = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]<min) {
        min = rx[i1];
        i1min = i1;
      }
    }
    if (index!=null)
      index[0] = i1min;
    return min;
  }
  public static float min(float[][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    float min = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      float[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]<min) {
          min = rxi2[i1];
          i2min = i2;
          i1min = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
    }
    return min;
  }
  public static float min(float[][][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    int i3min = 0;
    float min = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      float[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        float[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]<min) {
            min = rxi3i2[i1];
            i1min = i1;
            i2min = i2;
            i3min = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
      index[2] = i3min;
    }
    return min;
  }
  public static double max(double[] rx) {
    return max(rx,null);
  }
  public static double max(double[][] rx) {
    return max(rx,null);
  }
  public static double max(double[][][] rx) {
    return max(rx,null);
  }
  public static double max(double[] rx, int[] index) {
    int i1max = 0;
    double max = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]>max) {
        max = rx[i1];
        i1max = i1;
      }
    }
    if (index!=null)
      index[0] = i1max;
    return max;
  }
  public static double max(double[][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    double max = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      double[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]>max) {
          max = rxi2[i1];
          i2max = i2;
          i1max = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
    }
    return max;
  }
  public static double max(double[][][] rx, int[] index) {
    int i1max = 0;
    int i2max = 0;
    int i3max = 0;
    double max = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      double[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        double[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]>max) {
            max = rxi3i2[i1];
            i1max = i1;
            i2max = i2;
            i3max = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1max;
      index[1] = i2max;
      index[2] = i3max;
    }
    return max;
  }
  public static double min(double[] rx) {
    return min(rx,null);
  }
  public static double min(double[][] rx) {
    return min(rx,null);
  }
  public static double min(double[][][] rx) {
    return min(rx,null);
  }
  public static double min(double[] rx, int[] index) {
    int i1min = 0;
    double min = rx[0];
    int n1 = rx.length;
    for (int i1=1; i1<n1; ++i1) {
      if (rx[i1]<min) {
        min = rx[i1];
        i1min = i1;
      }
    }
    if (index!=null)
      index[0] = i1min;
    return min;
  }
  public static double min(double[][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    double min = rx[0][0];
    int n2 = rx.length;
    for (int i2=0; i2<n2; ++i2) {
      double[] rxi2 = rx[i2];
      int n1 = rxi2.length;
      for (int i1=0; i1<n1; ++i1) {
        if (rxi2[i1]<min) {
          min = rxi2[i1];
          i2min = i2;
          i1min = i1;
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
    }
    return min;
  }
  public static double min(double[][][] rx, int[] index) {
    int i1min = 0;
    int i2min = 0;
    int i3min = 0;
    double min = rx[0][0][0];
    int n3 = rx.length;
    for (int i3=0; i3<n3; ++i3) {
      double[][] rxi3 = rx[i3];
      int n2 = rxi3.length;
      for (int i2=0; i2<n2; ++i2) {
        double[] rxi3i2 = rxi3[i2];
        int n1 = rxi3i2.length;
        for (int i1=0; i1<n1; ++i1) {
          if (rxi3i2[i1]<min) {
            min = rxi3i2[i1];
            i1min = i1;
            i2min = i2;
            i3min = i3;
          }
        }
      }
    }
    if (index!=null) {
      index[0] = i1min;
      index[1] = i2min;
      index[2] = i3min;
    }
    return min;
  }
  public static double meanVal(double array[]){
      return sum(array)/array.length;
  }
  
   public static float meanVal(float array[]){
      return sum(array)/array.length;
  } 
    private ArrayMath() {
    }

}
