package Compressor;

import Constants.JPEG_Quality;
import java.io.*;
import Utils.Color;
import Utils.PPMTranslator;

public class JPEG {
    private final int[][] LuminanceQuantizationTable; // Table related to the amount of compression and quality on light intensity
    private final int[][] ChrominanceQuantizationTable; // Table related to the amount of compression and quality on color
    private int[][][][] M0; // 2D matrix of 8x8 Matrices for channel 0
    private int[][][][] M1; // 2D matrix of 8x8 Matrices for channel 1
    private int[][][][] M2; // 2D matrix of 8x8 Matrices for channel 2
    private int width = -1; // image width
    private int height = -1; // image height
    private int m_width = -1; // number of horizontal 8x8 matrices
    private int m_height = -1; // number of vertical 8x8 matrices

    public JPEG(JPEG_Quality quality) {
        LuminanceQuantizationTable = quality.getLuminanceTable();
        ChrominanceQuantizationTable = quality.getChrominanceTable();
    }

    public void compress(InputStream is, OutputStream os) throws IOException {
        PPMTranslator ppmfile = new PPMTranslator(is);
        width = ppmfile.getWidth();
        height = ppmfile.getHeight();

        // 8x8 Matrix subdivision (uses margin at the image right and down borders if
        // height or width arent multiple of 8)
        m_width = width / 8;
        if (width % 8 != 0)
            m_width = m_width + 1;
        m_height = height / 8;
        if (height % 8 != 0)
            m_height = m_height + 1;

        M0 = new int[m_height][m_width][8][8];
        M1 = new int[m_height][m_width][8][8];
        M2 = new int[m_height][m_width][8][8];

        Color color = new Color();
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        while ((color = ppmfile.getNextColor()) != null) {
            M0[i][j][k][l] = color.Y - 128;
            M1[i][j][k][l] = color.Cb - 128;
            M2[i][j][k][l] = color.Cr - 128;
            l = l + 1;
            if (l >= 8) {
                l = 0;
                k = k + 1;
                if (k >= 8) {
                    k = 0;
                    j = j + 1;
                    if (j >= m_width) {
                        j = 0;
                        i = i + 1;
                        if (i >= m_height)
                            break;
                    }
                }
            }
        }

        for (i = 0; i < m_height; ++i)
            for (j = 0; j < m_width; ++j) {
                float[][] dct0 = DCT(M0[i][j]);
                Quantization(dct0, M0[i][j], LuminanceQuantizationTable);
                float[][] dct1 = DCT(M1[i][j]);
                Quantization(dct1, M1[i][j], ChrominanceQuantizationTable);
                float[][] dct2 = DCT(M2[i][j]);
                Quantization(dct2, M2[i][j], ChrominanceQuantizationTable);
            }

        // WRITE 8x8 MATRIX TO OUTPUT COMPRESSING EACH WITH HUFFMAN
        // WIP

        os.flush();
    }

    public void decompress(InputStream is, OutputStream os) throws IOException {
        // READ:
        // It should read from is, decompress each 8x8 matrix into the internal state
        // using huffman
        // WIP
        // Create decompressed image using the internal state:
        PPMTranslator ppmFile = new PPMTranslator(os, width, height);
        ppmFile.writeHeader();

        for (int i = 0; i < m_height; ++i)
            for (int j = 0; j < m_width; ++j) {
                float[][] m0 = new float[8][8];
                Dequantization(M0[i][j], m0, LuminanceQuantizationTable);
                M0[i][j] = IDCT(m0);
                float[][] m1 = new float[8][8];
                Dequantization(M1[i][j], m1, ChrominanceQuantizationTable);
                M1[i][j] = IDCT(m1);
                float[][] m2 = new float[8][8];
                Dequantization(M2[i][j], m2, ChrominanceQuantizationTable);
                M2[i][j] = IDCT(m2);
            }
        int m_x = 8;
        int m_y = 8;
        Color color = new Color();
        for (int i = 0; i < m_height; ++i) {
            m_x = 8;
            if ((i + 1) * 8 > height)
                m_y = m_height * 8 - height;
            for (int j = 0; j < m_width; ++j) {
                if ((j + 1) * 8 > width)
                    m_x = m_width * 8 - width;
                for (int k = 0; k < m_y; ++k) {
                    for (int l = 0; l < m_x; ++l) {
                        int Y = M0[i][j][k][l] + 128;
                        int Cb = M1[i][j][k][l] + 128;
                        int Cr = M2[i][j][k][l] + 128;
                        color.YCbCr(Y, Cb, Cr);
                        ppmFile.setNextComponent(Math.max(Math.min(255, color.r), 0));
                        ppmFile.setNextComponent(Math.max(Math.min(255, color.g), 0));
                        ppmFile.setNextComponent(Math.max(Math.min(255, color.b), 0));
                    }
                }
            }
        }
        
        os.flush();
    }

    private void Quantization(float[][] input, int[][] output, int[][] QuantizationTable) { // factor between 0 and +infinity
        for (int i = 0; i < 8; ++i)
            for (int j = 0; j < 8; ++j) {
                output[i][j] = (int) (input[i][j] / QuantizationTable[i][j]);
            }
    }

    private void Dequantization(int[][] input, float[][] output, int[][] QuantizationTable) { // factor between 0 and +infinity
        for (int i = 0; i < 8; ++i)
            for (int j = 0; j < 8; ++j) {
                output[i][j] = (float) (input[i][j] * QuantizationTable[i][j]);
            }
    }

    private float[][] initDCTCoefficients(float[][] c) {
        final int N = c.length;
        final float value = (float) (1 / Math.sqrt(2.0));

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
        final float mathPI = (float) (Math.PI);
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
        final float mathPI = (float) (Math.PI);
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
                output[x][y] = (int) sum;
            }
        }
        return output;
    }

}
