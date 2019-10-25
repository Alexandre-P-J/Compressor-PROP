import java.io.*;
import java.util.Vector;

public class JPEG {
    class Color {
        int r,g,b;
        int Y,Cb,Cr;
        public void RGB(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            Y = (int)(float)((0.299 * r) + (0.587 * g) + (0.114 * b));
            Cb = (int)(float)(128.0 - (0.168736 * r) - (0.331264 * g) + (0.5 * b));
            Cr = (int)(float)(128.0 + (0.5 * r) - (0.418688 * g) - (0.081312 * b));
        }
        
        public void YCbCr(int Y, int Cb, int Cr){
            this.Y = Y;
            this.Cb = Cb;
            this.Cr = Cr;
            r = (int)(float)(Y + 1.402 * (Cr - 128.0));
            g = (int)(float)(Y - 0.344136 * (Cb - 128.0) - 0.714136 * (Cr - 128.0));
            b = (int)(float)(Y + 1.772 * (Cb - 128.0));
        }
    }

    private int width = -1;
    private int height = -1;

    private int read_next_number (InputStream is) throws IOException {
        int next;
        int num = 0;
        boolean numberRead = false;
        while ((next = is.read()) >= 0) {
            char c = (char)next;
            if ((c == '\n' || c == ' ') && numberRead) return num; // reached end of number
            if (c == '\n' || c == ' ') continue; // avoid excesive '\n'
            if (c == '#') { // avoid comments
                if (numberRead) return num; // number contains comment w/o space, return current num
                while ((next = is.read()) >= 0){
                    c = (char)next;
                    if (c == '\n') break; // comment finish
                }
                if (c != '\n') return -1; // no number, just EOF
                continue;
            }
            num = num * 10;
            num = num + Character.getNumericValue(c);
            numberRead = true;
        }
        if (numberRead) return num; // reached end of number at end of file
        return -1; // no number, just EOF
    }
    
    public void compress (InputStream is, OutputStream os) throws IOException {
        int next;
        char c;
        if ((next = is.read()) < 0) throw new IOException("Wrong ppm codification! - Empty file"); // 'P'
        if ((next = is.read()) < 0) throw new IOException("Wrong ppm codification! - File too short"); // '3' or '6'
        c = (char)next;
        if (c == '3') {} // nice! ascii ppm
        else if (c == '6') throw new IOException("Binary ppm currently not supported!");
        else throw new IOException("Wrong ppm codification! - Failed reading ascii/binary");

        if ((width = read_next_number(is)) < 0) throw new IOException("Wrong ppm codification! - Failed reading width");
        if ((height = read_next_number(is)) < 0) throw new IOException("Wrong ppm codification! - Failed reading height");
        int ppmMaxValue;
        if ((ppmMaxValue = read_next_number(is)) < 0) throw new IOException("Wrong ppm codification! - Failed reading max value");
        
        byte[] pixels = new byte[width*height];
        while ((next = read_next_number(is)) < 0){
            // wip
        }

        /*
        double matrix[][] = {{-122, 49, 66, 41, 41, 43, 40, 38},
                     {-121, 49, 31, 45, 35, 50, 41, 24},
                     {-122, 40, 45, 105, 31, -66, 18, 87},
                     {-4, 52, 42, 47, -122, -122, 8, 51},
                     {-119, -23, 53, 51, 45, 70, 61, 42},
                     {-64, -122, -25, -26, 33, 15, 6, 12},
                     {-76, -80, -64, -122, 53, 64, 38, -122},
                     {-78, -074, -84, -122, 57, 43, 41, -53}};
        double[][] dct = DCT(matrix);
        double[][] idct = IDCT(dct);
        
        for (int i = 0; i < 8; i++) { 
			for (int j = 0; j < 8; j++) 
				System.out.printf("%f\t", idct[i][j]);
			System.out.println(); 
        }*/

        //Color col = new Color();
        //col.RGB(148, 114, 72);
        //System.out.println(col.Y);
        //System.out.println(col.Cb);
        //System.out.println(col.Cr);
        //System.out.println(width);
        //System.out.println(height);
        
        
        /*
        Vector<double[][]> matrices = new Vector<double[][]>();
        int n = width / 8;
        if (width%8 != 0) n = n+1;
        int m = height / 8;
        if (height%8 != 0) m = m+1;
        for (int i = 0; i < n; ++i)

        */
        os.flush();
    }


    float lerp(float a, float b, float f) {
        return (float)(a * (1.0 - f)) + (b * f);
    }


    private double[][] initDCTCoefficients(double[][] c) {
        final int N = c.length;
        final double value = 1 / Math.sqrt(2.0);

        for (int i = 1; i < N; i++) {
            for (int j = 1; j < N; j++) {
                c[i][j] = 1;
            }
        }

        for (int i = 0; i < N; i++) {
            c[i][0] = value;
            c[0][i] = value;
        }
        c[0][0] = 0.5;
        return c;
    }

    
    private double[][] DCT(double[][] input) {
        final int N = input.length;
        final double mathPI = Math.PI;
        final int halfN = N / 2;
        final double doubN = 2.0 * N;

        double[][] c = new double[N][N];
        c = initDCTCoefficients(c);

        double[][] output = new double[N][N];

        for (int u = 0; u < N; u++) {
            double temp_u = u * mathPI;
            for (int v = 0; v < N; v++) {
                double temp_v = v * mathPI;
                double sum = 0.0;
                for (int x = 0; x < N; x++) {
                    int temp_x = 2 * x + 1;
                    for (int y = 0; y < N; y++) {
                        sum += input[x][y] * Math.cos((temp_x / doubN) * temp_u)
                                * Math.cos(((2 * y + 1) / doubN) * temp_v);
                    }
                }
                sum *= c[u][v] / halfN;
                output[u][v] = sum;
            }
        }
        return output;
    }

    
    private double[][] IDCT(double[][] input) {
        final int N = input.length;
        final double mathPI = Math.PI;
        final int halfN = N / 2;
        final double doubN = 2.0 * N;

        double[][] c = new double[N][N];
        c = initDCTCoefficients(c);

        double[][] output = new double[N][N];

        for (int x = 0; x < N; x++) {
            int temp_x = 2 * x + 1;
            for (int y = 0; y < N; y++) {
                int temp_y = 2 * y + 1;
                double sum = 0.0;
                for (int u = 0; u < N; u++) {
                    double temp_u = u * mathPI;
                    for (int v = 0; v < N; v++) {
                        sum += c[u][v] * input[u][v] * Math.cos((temp_x / doubN) * temp_u)
                                * Math.cos((temp_y / doubN) * v * mathPI);
                    }
                }
                sum /= halfN;
                output[x][y] = sum;
            }
        }
        return output;
    }
}