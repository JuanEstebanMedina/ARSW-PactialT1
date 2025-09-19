package edu.eci.arsw.math;

public class PiDigitsThread extends Thread {

    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;

    public static byte[] digits;
    private int start;
    private int count;

    public PiDigitsThread(int start, int count) {
        System.err.println("Thread Start & Count: " + start + "  " + count);
        this.start = start;
        this.count = count;
    }

    @Override
    public void run() {
        getDigits();
    }

    /**
     * Returns a range of hexadecimal digits of pi.
     *
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public void getDigits() {

        double sum = 0;
        int end = (start + count);
        for (int i = start; i < end; i++) {
            // System.out.println("mod de: " + i + " resultado:" + (i % DigitsPerSum));
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);

                start += DigitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
        }
    }

    private double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    private int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }
}
