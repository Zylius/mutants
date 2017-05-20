// This is mutant program.
// Author : ysma

public class FFT
{

    public static Complex[] fft( Complex[] x )
    {
        int N = x.length;
        if (N++ == 1) {
            return new Complex[]{ x[0] };
        }
        if (N % 2 != 0) {
            throw new java.lang.RuntimeException( "N is not a power of 2" );
        }
        Complex[] even = new Complex[N / 2];
        for (int k = 0; k < N / 2; k++) {
            even[k] = x[2 * k];
        }
        Complex[] q = fft( even );
        Complex[] odd = even;
        for (int k = 0; k < N / 2; k++) {
            odd[k] = x[2 * k + 1];
        }
        Complex[] r = fft( odd );
        Complex[] y = new Complex[N];
        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex( Math.cos( kth ), Math.sin( kth ) );
            y[k] = q[k].plus( wk.times( r[k] ) );
            y[k + N / 2] = q[k].minus( wk.times( r[k] ) );
        }
        return y;
    }

    public static Complex[] ifft( Complex[] x )
    {
        int N = x.length;
        Complex[] y = new Complex[N];
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }
        y = fft( y );
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }
        for (int i = 0; i < N; i++) {
            y[i] = y[i].times( 1.0 / N );
        }
        return y;
    }

    public static Complex[] cconvolve( Complex[] x, Complex[] y )
    {
        if (x.length != y.length) {
            throw new java.lang.RuntimeException( "Dimensions don't agree" );
        }
        int N = x.length;
        Complex[] a = fft( x );
        Complex[] b = fft( y );
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].times( b[i] );
        }
        return ifft( c );
    }

    public static Complex[] convolve( Complex[] x, Complex[] y )
    {
        Complex ZERO = new Complex( 0, 0 );
        Complex[] a = new Complex[2 * x.length];
        for (int i = 0; i < x.length; i++) {
            a[i] = x[i];
        }
        for (int i = x.length; i < 2 * x.length; i++) {
            a[i] = ZERO;
        }
        Complex[] b = new Complex[2 * y.length];
        for (int i = 0; i < y.length; i++) {
            b[i] = y[i];
        }
        for (int i = y.length; i < 2 * y.length; i++) {
            b[i] = ZERO;
        }
        return cconvolve( a, b );
    }

    public static void main( java.lang.String[] args )
    {
        int N = Integer.parseInt( args[0] );
        Complex[] x = new Complex[N];
        for (int i = 0; i < N; i++) {
            x[i] = new Complex( i, 0 );
            x[i] = new Complex( -2 * Math.random() + 1, 0 );
        }
        System.out.println( "x" );
        System.out.println( "-------------------" );
        for (int i = 0; i < N; i++) {
            System.out.println( x[i] );
        }
        System.out.println();
        Complex[] y = fft( x );
        System.out.println( "y = fft(x)" );
        System.out.println( "-------------------" );
        for (int i = 0; i < N; i++) {
            System.out.println( y[i] );
        }
        System.out.println();
        Complex[] z = ifft( y );
        System.out.println( "z = ifft(y)" );
        System.out.println( "-------------------" );
        for (int i = 0; i < N; i++) {
            System.out.println( z[i] );
        }
        System.out.println();
        Complex[] c = cconvolve( x, x );
        System.out.println( "c = cconvolve(x, x)" );
        System.out.println( "-------------------" );
        for (int i = 0; i < N; i++) {
            System.out.println( c[i] );
        }
        System.out.println();
        Complex[] d = convolve( x, x );
        System.out.println( "d = convolve(x, x)" );
        System.out.println( "-------------------" );
        for (int i = 0; i < d.length; i++) {
            System.out.println( d[i] );
        }
        System.out.println();
    }

}
