package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.List;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    private static final List<PiDigitsThread> THREADS_LIST = new ArrayList<>();
    private static int DigitsPerSum = 8;

    /**
     * Returns a range of hexadecimal digits of pi.
     *
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int nThreads, int start, int count) throws InterruptedException {

        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        // Tamaño de cada intervalo de digitos. Debe ser multiplo de DigitsPerSum
        int size = (count - start) / nThreads;
        // System.out.println("size: " + size);
        while (size % DigitsPerSum != 0) {
            size += 1;
        }
        // System.out.println("size (8): " + size);

        byte[] digits = new byte[count];
        PiDigitsThread.digits = digits;
        for (int i = 0; i < nThreads; i++) {
            // Ejemplo de n = 100;
            // Para 2 hilos 0 - 49 / 50 - 99
            // 0 - 31 / 32 - 63 / 64 - 95 / 96 - 99
            PiDigitsThread thread;
            if (i == (nThreads - 1)) {
                thread = new PiDigitsThread(i * size, count - (i * size));
            } else {
                thread = new PiDigitsThread(i * size, size);
            }
            THREADS_LIST.add(thread);
            thread.start();
        }

        for (PiDigitsThread thread : THREADS_LIST) {
            thread.join();
        }

        return digits;
    }
}
