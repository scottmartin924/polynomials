import java.util.Random;

/**
 * Object for representing a polynomial
 */
public class Polynomial {
    private int[] coefficients;

    /**
     * construct a polynomail with given coefficient
     * for the degree term given
     * @param coefficient the value of the coefficient for this term
     * @param degree the degree of the term for which this is the coefficient
     */
    public Polynomial(int coefficient, int degree) {
        if(degree < 0) {
            throw new IllegalArgumentException("Degree of polynomial cannot be less than 0");
        }
        if(coefficient == 0) {
            throw new IllegalArgumentException("0 valued coefficient invalid");
        }
        this.coefficients = new int[degree+1];
        this.coefficients[degree] = coefficient;
    }

    // Private constructor to use within the class...takes in the coefficients array
    private Polynomial(final int[] coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * Remove leading 0 coefficients
     */
    private void simplify() {
        int leadingCoefficientPlace = -1;
        for(int i = this.coefficients.length-1; i > -1; i--) {
            if(this.coefficients[i] != 0) {
                leadingCoefficientPlace = i;
                break;
            }
        }
        // If no non-zero cofficients then should be empty array
        if(leadingCoefficientPlace == -1) {
            this.coefficients = new int[0];
        } else {
            int[] newCoefficients = new int[leadingCoefficientPlace + 1];
            for(int i = 0; i < newCoefficients.length; i++) {
                newCoefficients[i] = this.coefficients[i];
            }
            this.coefficients = newCoefficients;
        }
    }

    /**
     * Add two polynomials
     * @param that the other polynomial to add
     * @return sum of the two polynomials
     */
    public Polynomial add(final Polynomial that) {
        Polynomial minDegreePoly, maxDegreePoly;
        // find the min and max polynomials and create new poly arry
        if(Integer.compare(this.degree(), that.degree()) < 0) {
            minDegreePoly = this;
            maxDegreePoly = that;
        } else {
            minDegreePoly = that;
            maxDegreePoly = this;
        }
        int[] sumPolyCoefficients = new int[maxDegreePoly.degree() + 1];
        // Loop over polynomials to populate new values...worked hard to avoid O(nm)...but not sure it was worth it
        for(int i = 0; i < minDegreePoly.degree() + 1; i++) {
            sumPolyCoefficients[i] = this.coefficients[i] + that.coefficients[i];
        }
        for(int i = minDegreePoly.degree() + 1; i < sumPolyCoefficients.length; i++) {
            sumPolyCoefficients[i] = maxDegreePoly.coefficients[i];
        }
        final Polynomial sum = new Polynomial(sumPolyCoefficients);
        sum.simplify();
        return sum;
    }

    /**
     * Multiply two polynomials
     * @param that second factor
     * @return product of the two polynomials
     */
    public Polynomial multiply(final Polynomial that) {
        int[] newPolyCoefficients = new int[this.degree() + that.degree() + 1];
        Polynomial minDegreePoly, maxDegreePoly;
        // find the min and max polynomials and create new poly arry
        if(Integer.compare(this.degree(), that.degree()) < 0) {
            minDegreePoly = this;
            maxDegreePoly = that;
        } else {
            minDegreePoly = that;
            maxDegreePoly = this;
        }
        for(int i = maxDegreePoly.degree(); i>-1; i--) {
            for(int j = minDegreePoly.degree(); j>-1; j--) {
                newPolyCoefficients[i+j] += this.coefficients[i] * that.coefficients[j];
            }
        }
        Polynomial product = new Polynomial(newPolyCoefficients);
        product.simplify();
        return product;
    }

    /**
     * Subtract this polynomial from that param
     * @param that the other polynomial to subtract
     * @return difference of the polynomials
     */
    public Polynomial subtract(final Polynomial that) {
        return this.add(that.scalarMultiplication(-1));
    }

    /**
     * Multiply polynomial by a scalar
     * @param scalarMultiple the scalar to multiple the value by
     * @return polynomial create from scalar multiplication
     */
    public Polynomial scalarMultiplication(int scalarMultiple) {
        int[] newPolyCoefficients = new int[this.coefficients.length];
        for(int i = 0; i < this.coefficients.length; i++) {
            newPolyCoefficients[i] = scalarMultiple * this.coefficients[i];
        }
        final Polynomial product = new Polynomial(newPolyCoefficients);
        product.simplify();
        return product;
    }

    /**
     * Take derivative of polynomial
     * @return polynomial representing the derivative
     */
    public Polynomial differentiate() {
        if(this.degree() < 1) {
            return new Polynomial(new int[0]);
        }
        // degree is 1 less so size of array is the degree (size = degree + 1 - 1)
        int[] newPolyCoefficients = new int[this.degree()];
        for(int i = this.degree(); i>0; i--) {
            newPolyCoefficients[i-1] = this.coefficients[i] * i;
        }
        final Polynomial derivativePoly = new Polynomial(newPolyCoefficients);
        derivativePoly.simplify();
        return derivativePoly;
    }

    /**
     * get the degree of the polynomial
     * @return the degree of the polynomial. Returns -1 for 0 polynomial
     */
    public int degree() {
        if(this.coefficients.length == 0) {
            return -1;
        }
        return this.coefficients.length -1;
    }

    /**
     * Print the polynomial
     */
    public void print() {
        if(this.degree() == -1) {
            System.out.print("0 polynomial");
        } else {
            for(int i = this.degree(); i > -1; i--) {
                if(this.coefficients[i] != 0) {
                    System.out.print(this.termToString(this.coefficients[i], i));
                }
            }
        }
        System.out.println();
    }

    /**
     * convert individual polynomial term to string
     */
    private String termToString(int coefficient, int degree) {
        StringBuilder sb = new StringBuilder();
        if(degree < 0) {
            throw new IllegalArgumentException("Degree must be >0");
        }
        if(coefficient > 0) {
            sb.append("+");
        }
        switch(degree) {
            case 0:
                sb.append(String.valueOf(coefficient));
                break;
            case 1:
                sb.append(String.valueOf(coefficient) + "x");
                break;
            default:
                sb.append(String.valueOf(coefficient) + "x^" + String.valueOf(degree));
                break;
        }
        return sb.toString();
    }

    public int[] getCoefficients() {
        return this.coefficients;
    }

    /**
     * main method
     */
    public static void main(String[] args) {
        final Polynomial randPoly1 = generateRandomPoly(6);
        final Polynomial randPoly2 = generateRandomPoly(4);
        randPoly1.print();
        randPoly2.print();
        System.out.println("sum");
        randPoly1.add(randPoly2).print();
        System.out.println("difference");
        randPoly1.subtract(randPoly2).print();
        System.out.println("product");
        randPoly1.multiply(randPoly2).print();
        System.out.println("poly 1 derivative");
        randPoly1.differentiate().print();


        // Some corner case checks still need to happen
    }

    // testing function to generate random polynomials of given degree
    private static Polynomial generateRandomPoly(int degree) {
        Random rand = new Random(System.currentTimeMillis());
        Polynomial randPolynomial = new Polynomial(((rand.nextInt() % 20) + 1) * (rand.nextInt() % 2 == 0 ? -1 : 1), degree);
        for(int i = degree; i > -1; i--) {
            // Randomize a little bit with 0 coefficients
            if(rand.nextInt() % 3 != 0) {
                randPolynomial = randPolynomial.add(new Polynomial(((rand.nextInt() % 20) + 1) * (rand.nextInt() % 2 == 0 ? -1 : 1),i));
            }
        }

        return randPolynomial;
    }
}

