package me.chrisvle.rechordly;

/**
 * Created by Chris on 12/4/15.
 */
public class PassFilters {

    public static double[] calculateFFT(byte[] signal, double level, String type)
    {
        final int mNumberOfFFTPoints =1024;
        double mMaxFFTSample;
        int mPeakPos;

        double temp;
        Complex[] y;
        Complex[] complexSignal = new Complex[mNumberOfFFTPoints];
        double[] absSignal = new double[mNumberOfFFTPoints/2];

        for(int i = 0; i < mNumberOfFFTPoints; i++){
            temp = (double)((signal[2*i] & 0xFF) | (signal[2*i+1] << 8)) / 32768.0F;
            complexSignal[i] = new Complex(temp,0.0);
        }

        y = fft(complexSignal);

        for(int i = 0; i < (mNumberOfFFTPoints/2); i++)
        {
            double val = Math.sqrt(Math.pow(y[i].re(), 2) + Math.pow(y[i].im(), 2));
            if (type.equals("low")) {
                if (val >= level){
                    absSignal[i] = val;
                }
                else {
                    absSignal[i] = 0;
                }

            }
            else {
                if (val <= level){
                    absSignal[i] = val;
                }
                else {
                    absSignal[i] = 0;
                }
            }
        }
        return absSignal;

    }

    public static Complex[] fft(Complex[] x) {
        int N = x.length;

        // base case
        if (N == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) { throw new RuntimeException("N is not a power of 2"); }

        // fft of even terms
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }
}
