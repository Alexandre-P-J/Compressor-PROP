import java.io.*;
import java.util.Vector;

import javax.naming.Reference;

public class JPEG {

    private float CompressionFactor = 1; // 0 means minimal compression, 1 means maximum, >1 experimental
    private int[][][][] M0; // 2D matrix of 8x8 Matrices for channel 0
    private int[][][][] M1; // 2D matrix of 8x8 Matrices for channel 1
    private int[][][][] M2; // 2D matrix of 8x8 Matrices for channel 2
    private int width = -1;
    private int height = -1;
    private int m_width = -1;
    private int m_height = -1;
    private byte[] ppmMode;
    private int ppmMaxValue = 255;
    private final int[][] LuninanceQuantization = { { 16, 11, 10, 16, 24, 40, 51, 61    },
                                                    { 12, 12, 14, 19, 26, 58, 60, 55    },
                                                    { 14, 13, 16, 24, 40, 57, 69, 56    },
                                                    { 14, 17, 22, 29, 51, 87, 80, 62    },
                                                    { 18, 22, 37, 56, 68, 109, 103, 77  },
                                                    { 24, 35, 55, 64, 81, 104, 113, 92  },
                                                    { 49, 64, 78, 87, 103, 121, 120, 101},
                                                    { 72, 92, 95, 98, 112, 100, 103, 99 }
                                                  };


    private Color read_next_pixel (InputStream is) throws IOException {
        int r,g,b;
        if ((r = read_next_number(is)) < 0) return null;
        if ((g = read_next_number(is)) < 0) throw new IOException("Wrong ppm codification! - Incomplete RGB color");
        if ((b = read_next_number(is)) < 0) throw new IOException("Wrong ppm codification! - Incomplete RGB color");
        float pixelDepthFactor = (float)(255.0/(float)ppmMaxValue);
        Color c = new Color();
        float r_f,g_f,b_f;
        r_f = Math.min(r*pixelDepthFactor, 255);
        g_f = Math.min(g*pixelDepthFactor, 255);
        b_f = Math.min(b*pixelDepthFactor, 255);
        c.RGB(r_f, g_f, b_f);
        return c;
    }

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

    private void write_next_number (OutputStream os, int num) throws IOException {
        String str = String.valueOf(num) + '\n';
        byte[] b = str.getBytes();
        os.write(b);
    }
    

    private void Quantization(float[][] input, int[][] output, int[][] QuantizationMatrix, float factor) { // factor between 0 and +infinity
        for (int i = 0; i < 8; ++i)
            for  (int j = 0; j < 8; ++j) {
                output[i][j] = (int)(input[i][j] / Math.max(QuantizationMatrix[i][j]*factor, 1.0));
            }
    }

    private void Dequantization(int[][] input, float[][] output, int[][] QuantizationMatrix, float factor) { // factor between 0 and +infinity
        for (int i = 0; i < 8; ++i)
            for  (int j = 0; j < 8; ++j) {
                output[i][j] = (float)(input[i][j] * Math.max(QuantizationMatrix[i][j]*factor, 1.0));
            }
    }

    

    public void compress (InputStream is, OutputStream os) throws IOException {
        int next;
        ppmMode = new byte[2];
        if ((next = is.read()) < 0) throw new IOException("Wrong ppm codification! - Empty file"); // 'P'
        ppmMode[0] = (byte)next;
        if ((next = is.read()) < 0) throw new IOException("Wrong ppm codification! - File too short"); // '3' or '6'
        ppmMode[1] = (byte)next;
        char c = (char)next;
        if (c == '3') {} // nice! ascii ppm
        else if (c == '6') throw new IOException("Binary ppm currently not supported!");
        else throw new IOException("Wrong ppm codification! - Failed reading ascii/binary");

        if ((width = read_next_number(is)) < 0) throw new IOException("Wrong ppm codification! - Failed reading width");
        if ((height = read_next_number(is)) < 0) throw new IOException("Wrong ppm codification! - Failed reading height");
        if ((ppmMaxValue = read_next_number(is)) < 0) throw new IOException("Wrong ppm codification! - Failed reading max value");
        
        m_width = width/8;
        if (width%8 != 0) m_width = m_width + 1;
        m_height = height/8;
        if (height%8 != 0) m_height = m_height + 1;

        M0 = new int[m_height][m_width][8][8];
        M1 = new int[m_height][m_width][8][8];
        M2 = new int[m_height][m_width][8][8];
        
        Color color = new Color();
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        while ((color = read_next_pixel(is)) != null) {
            M0[i][j][k][l] = color.Y - 127;
            M1[i][j][k][l] = color.Cb - 127;
            M2[i][j][k][l] = color.Cr - 127;
            l = l+1;
            if (l >= 8) {
                l = 0;
                k = k+1;
                if (k >= 8) {
                    k = 0;
                    j = j+1;
                    if (j >= m_width) {
                        j = 0;
                        i = i + 1;
                        if (i >= m_height) break;
                    }
                }
            }
        }

        

        for (i = 0; i < m_height; ++i)
            for (j = 0; j < m_width; ++j) {
                float[][] dct0 = DCT(M0[i][j]);
                Quantization(dct0, M0[i][j], LuninanceQuantization, CompressionFactor);
                float[][] dct1 = DCT(M1[i][j]);
                Quantization(dct1, M1[i][j], LuninanceQuantization, CompressionFactor);
                float[][] dct2 = DCT(M2[i][j]);
                Quantization(dct2, M2[i][j], LuninanceQuantization, CompressionFactor);
            }

        /*for (i = 0; i < 8; i++) { 
			for (j = 0; j < 8; j++) 
				System.out.printf("%d\t", M0[4][5][i][j]);
			System.out.println();
        }*/

        
        /*int matrix[][] = {{-122, 49, 66, 41, 41, 43, 40, 38},
                     {-121, 49, 31, 45, 35, 50, 41, 24},
                     {-122, 40, 45, 105, 31, -66, 18, 87},
                     {-94, 52, 42, 47, -122, -122, 8, 51},
                     {-119, -23, 53, 51, 45, 70, 61, 42},
                     {-64, -122, -25, -26, 33, 15, 6, 12},
                     {-76, -80, -64, -122, 53, 64, 38, -122},
                     {-78, -074, -84, -122, 57, 43, 41, -53}};
        */
           
        
        /*float[][] dct = DCT(matrix);
        float[][] idct = IDCT(dct);
        
        for (int i = 0; i < 8; i++) { 
			for (int j = 0; j < 8; j++) 
				System.out.printf("%f\t", idct[i][j]);
			System.out.println(); 
        }*/

        

        decompress(is, os);
        os.flush();
    }

    public void decompress (InputStream is, OutputStream os) throws IOException {
        // write ppm header
        os.write(ppmMode);
        String str = '\n' + String.valueOf(width) + ' ' + String.valueOf(height) + '\n' + String.valueOf(255) + '\n';
        byte[] b = str.getBytes();
        os.write(b);

        for (int i = 0; i < m_height; ++i)
            for (int j = 0; j < m_width; ++j) {
                float[][] m0 = new float[8][8];
                Dequantization(M0[i][j], m0, LuninanceQuantization, CompressionFactor);
                M0[i][j] = IDCT(m0);
                float[][] m1 = new float[8][8];
                Dequantization(M1[i][j], m1, LuninanceQuantization, CompressionFactor);
                M1[i][j] = IDCT(m1);
                float[][] m2 = new float[8][8];
                Dequantization(M2[i][j], m2, LuninanceQuantization, CompressionFactor);
                M2[i][j] = IDCT(m2);
            }
        int m_x = 8;
        int m_y = 8;
        Color color = new Color();
        for (int i = 0; i < m_height; ++i) {
            m_x = 8;
            if ((i+1)*8 > height) m_y = m_height*8 - height;
            for (int j = 0; j < m_width; ++j) {
                if ((j+1)*8 > width) m_x = m_width*8 - width;
                for (int k = 0; k < m_y; ++k) {
                    for (int l = 0; l < m_x; ++l) {
                        int Y = M0[i][j][k][l] + 127;
                        int Cb = M1[i][j][k][l] + 127;
                        int Cr = M2[i][j][k][l] + 127;
                        color.YCbCr(Y, Cb, Cr);
                        write_next_number(os, Math.max(Math.min(255, color.r), 0));
                        write_next_number(os, Math.max(Math.min(255, color.g), 0));
                        write_next_number(os, Math.max(Math.min(255, color.b), 0));
                    }
                }
            }
        }
    

    }


    private float[][] initDCTCoefficients(float[][] c) {
        final int N = c.length;
        final float value = (float)(1 / Math.sqrt(2.0));

        for (int i = 1; i < N; i++) {
            for (int j = 1; j < N; j++) {
                c[i][j] = 1;
            }
        }

        for (int i = 0; i < N; i++) {
            c[i][0] = value;
            c[0][i] = value;
        }
        c[0][0] = 0.5f;
        return c;
    }

    
    private float[][] DCT(int[][] input) {
        final int N = input.length;
        final float mathPI = (float)(Math.PI);
        final int halfN = N / 2;
        final float doubN = 2.f * N;

        float[][] c = new float[N][N];
        c = initDCTCoefficients(c);

        float[][] output = new float[N][N];

        for (int u = 0; u < N; u++) {
            float temp_u = u * mathPI;
            for (int v = 0; v < N; v++) {
                float temp_v = v * mathPI;
                float sum = 0.f;
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

    
    private int[][] IDCT(float[][] input) {
        final int N = input.length;
        final float mathPI = (float)(Math.PI);
        final int halfN = N / 2;
        final float doubN = 2.f * N;

        float[][] c = new float[N][N];
        c = initDCTCoefficients(c);

        int[][] output = new int[N][N];

        for (int x = 0; x < N; x++) {
            int temp_x = 2 * x + 1;
            for (int y = 0; y < N; y++) {
                int temp_y = 2 * y + 1;
                float sum = 0.f;
                for (int u = 0; u < N; u++) {
                    float temp_u = u * mathPI;
                    for (int v = 0; v < N; v++) {
                        sum += c[u][v] * input[u][v] * Math.cos((temp_x / doubN) * temp_u)
                                * Math.cos((temp_y / doubN) * v * mathPI);
                    }
                }
                sum /= halfN;
                output[x][y] = (int)sum;
            }
        }
        return output;
    }
}

class Color {
    int r,g,b;
    int Y,Cb,Cr;
    public void RGB(float r, float g, float b) { // uses float instead of int to reduce aproximation imprecision
        this.r = (int)r;
        this.g = (int)g;
        this.b = (int)b;
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