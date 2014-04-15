package org.codemine.holdabletorches.Utils;

/**
 * Name: RomanNumeral.java Created: 30 March 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class RomanNumeral {

    enum Numeral {
        I(1), IV(4), V(5), IX(9), X(10), XL(40), L(50), XC(90), C(100), CD(400), D(500), CM(900), M(1000);
        int weight;

        Numeral(int weight)
        {

            this.weight = weight;
        }
    }

    ;

    public static String convert(long n)
    {

        if(n <= 0)
        {
            throw new IllegalArgumentException();
        }

        StringBuilder buf = new StringBuilder();

        final Numeral[] values = Numeral.values();
        for(int i = values.length - 1 ; i >= 0 ; i--)
        {
            while(n >= values[i].weight)
            {
                buf.append(values[i]);
                n -= values[i].weight;
            }
        }
        return buf.toString();
    }
}
