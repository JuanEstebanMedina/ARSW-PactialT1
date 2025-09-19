package edu.eci.arsw.math;

public class PiDigitsThread extends Thread {

    private static int DigitsPerSum = 8;
    private static final double Epsilon = 1e-17;
    public static byte[] digits;

    private volatile boolean isPaused = false;
    private int processedDigits = 0;
    private int start;
    private int count;

    public PiDigitsThread(int start, int count) {
        // System.err.println("Thread Start & Count: " + start + "  " + count);
        this.start = start;
        this.count = count;
    }

    @Override
    public void run() {
        try {
            getDigits();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a range of hexadecimal digits of pi.
     *
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public void getDigits() throws InterruptedException {

        double sum = 0;
        int end = (start + count);
        for (int i = start; i < end; i++) {
            synchronized (this) {
                while (isPaused) {
                    wait();
                }
            }
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);

                start += DigitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
            processedDigits++;
            // System.out.println("mod de: " + i + " resultado:" + (i % DigitsPerSum));

        }
    }

    public synchronized void setPaused(){
        isPaused = true;
    }

    public synchronized void setResume(){
        isPaused = false;
        notifyAll();
    }

    public int getProcessedDigits(){
        return processedDigits;
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
