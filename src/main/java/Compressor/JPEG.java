package Compressor;

import Constants.JPEG_Quality;
import Compressor.Huffman;
import java.io.*;
import Utils.Color;
import Utils.PPMTranslator;

public class JPEG {
    private final int[][] LuminanceQuantizationTable; // Table related to the amount of compression and quality on light
                                                      // intensity
    private final int[][] ChrominanceQuantizationTable; // Table related to the amount of compression and quality on
                                                        // color
    private int[][][][] M0; // 2D matrix of 8x8 Matrices for channel 0
    private int[][][][] M1; // 2D matrix of 8x8 Matrices for channel 1
    private int[][][][] M2; // 2D matrix of 8x8 Matrices for channel 2
    private int width = -1; // image width
    private int height = -1; // image height
    private int m_width = -1; // number of horizontal 8x8 matrices
    private int m_height = -1; // number of vertical 8x8 matrices
    // used to check if c and cT are initialized because matrix initialization order
    // must be enforced (c before cT)
    private static boolean initDCTMatrices = false;
    private static final double c[][] = new double[8][8]; // cosine matrix
    private static final double cT[][] = new double[8][8]; // transformed cosine matrix

    public JPEG(JPEG_Quality quality) {
        LuminanceQuantizationTable = quality.getLuminanceTable();
        ChrominanceQuantizationTable = quality.getChrominanceTable();
        if (!initDCTMatrices)
            initDCTMatrices();
        initDCTMatrices = true;
    }

    private static void initDCTMatrices() {
        int i;
        int j;
        for (j = 0; j < 8; j++) {
            double nn = (double) (8);
            c[0][j] = 1.0 / Math.sqrt(nn);
            cT[j][0] = c[0][j];
        }
        for (i = 1; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                double jj = (double) j;
                double ii = (double) i;
                c[i][j] = Math.sqrt(2.0 / 8.0) * Math.cos(((2.0 * jj + 1.0) * ii * Math.PI) / (2.0 * 8.0));
                cT[j][i] = c[i][j];
            }
        }
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

        // Fills the 3 YCbCr components (one component to each matrix) from the input
        // image
        M0 = new int[m_height][m_width][8][8];
        M1 = new int[m_height][m_width][8][8];
        M2 = new int[m_height][m_width][8][8];
        Color color = new Color();
        for (int i = 0; i < m_height; ++i) {
            int vMax = 8;
            if (i + 1 == m_height)
                vMax = 8 - (m_height * 8 - height);
            for (int j = 0; j < m_width; ++j) {
                int hMax = 8;
                if (j + 1 == m_width)
                    hMax = 8 - (m_width * 8 - width);
                for (int k = 0; k < vMax; ++k)
                    for (int l = 0; l < hMax; ++l) {
                        if ((color = ppmfile.getNextColor()) == null)
                            throw new IOException();
                        M0[i][j][k][l] = color.Y;
                        M1[i][j][k][l] = color.Cb;
                        M2[i][j][k][l] = color.Cr;
                    }
            }
        }
        // Lossy part of the algorithm, DCT and Quantization for each component matrix
        for (int i = 0; i < m_height; ++i)
            for (int j = 0; j < m_width; ++j) {
                DCT(M0[i][j]);
                Quantization(M0[i][j], M0[i][j], LuminanceQuantizationTable);
                DCT(M1[i][j]);
                Quantization(M1[i][j], M1[i][j], ChrominanceQuantizationTable);
                DCT(M2[i][j]);
                Quantization(M2[i][j], M2[i][j], ChrominanceQuantizationTable);
            }

        // WRITE 8x8 MATRIX TO OUTPUT COMPRESSING EACH WITH HUFFMAN

        LosslessEncode(os);

        os.flush();
    }

    public void decompress(InputStream is, OutputStream os) throws IOException {
        // read internal state from input stream and decompress each matrix using
        // huffman
        m_height = 116;
        m_width = 175;
        height = 921;
        width = 1400;

        M0 = new int[m_height][m_width][8][8];
        M1 = new int[m_height][m_width][8][8];
        M2 = new int[m_height][m_width][8][8];

        LosslessDecode(is);

        // Dequantizes and applies the DCT inverse for each component matrix
        for (int i = 0; i < m_height; ++i)
            for (int j = 0; j < m_width; ++j) {
                Dequantization(M0[i][j], M0[i][j], LuminanceQuantizationTable);
                inverseDCT(M0[i][j]);
                Dequantization(M1[i][j], M1[i][j], ChrominanceQuantizationTable);
                inverseDCT(M1[i][j]);
                Dequantization(M2[i][j], M2[i][j], ChrominanceQuantizationTable);
                inverseDCT(M2[i][j]);
            }

        // writes the ppm file converting the component matrices into RGB output
        PPMTranslator ppmFile = new PPMTranslator(os, width, height);
        ppmFile.writeHeader();
        Color color = new Color();
        for (int i = 0; i < m_height; ++i) {
            int vMax = 8;
            if (i + 1 == m_height)
                vMax = 8 - (m_height * 8 - height);
            for (int j = 0; j < m_width; ++j) {
                int hMax = 8;
                if (j + 1 == m_width)
                    hMax = 8 - (m_width * 8 - width);
                for (int k = 0; k < vMax; ++k)
                    for (int l = 0; l < hMax; ++l) {
                        int Y = M0[i][j][k][l];
                        int Cb = M1[i][j][k][l];
                        int Cr = M2[i][j][k][l];
                        color.YCbCr(Y, Cb, Cr);
                        ppmFile.setNextComponent(Math.max(Math.min(255, color.r), 0));
                        ppmFile.setNextComponent(Math.max(Math.min(255, color.g), 0));
                        ppmFile.setNextComponent(Math.max(Math.min(255, color.b), 0));
                    }
            }
        }

        os.flush();
    }

    private void Quantization(int[][] input, int[][] output, int[][] QuantizationTable) {
        for (int i = 0; i < 8; ++i)
            for (int j = 0; j < 8; ++j) {
                output[i][j] = input[i][j] / QuantizationTable[i][j];
            }
    }

    private void Dequantization(int[][] input, int[][] output, int[][] QuantizationTable) {
        for (int i = 0; i < 8; ++i)
            for (int j = 0; j < 8; ++j) {
                output[i][j] = input[i][j] * QuantizationTable[i][j];
            }
    }

    // Applies the DCT-II transformation to M matrix where M = input[y][x] - 128
    // (centers values around 0)
    private void DCT(int input[][]) {
        double temp[][] = new double[8][8];
        double temp1;
        int i;
        int j;
        int k;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                temp[i][j] = 0.0;
                for (k = 0; k < 8; k++) {
                    temp[i][j] += ((input[i][k] - 128) * cT[k][j]);
                }
            }
        }
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                temp1 = 0.0;
                for (k = 0; k < 8; k++) {
                    temp1 += (c[i][k] * temp[k][j]);
                }
                input[i][j] = (int) Math.round(temp1);
            }
        }
    }

    // Applies the DCT-III transformation to input matrix and adds 128 to each
    // element
    // to undo the 128 that was substracted in DCT-II
    private void inverseDCT(int input[][]) {
        double temp[][] = new double[8][8];
        double temp1;
        int i;
        int j;
        int k;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                temp[i][j] = 0.0;
                for (k = 0; k < 8; k++) {
                    temp[i][j] += input[i][k] * c[k][j];
                }
            }
        }
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                temp1 = 0.0;
                for (k = 0; k < 8; k++) {
                    temp1 += cT[i][k] * temp[k][j];
                }
                temp1 += 128.0;
                if (temp1 < 0) {
                    input[i][j] = 0;
                } else if (temp1 > 255) {
                    input[i][j] = 255;
                } else {
                    input[i][j] = (int) Math.round(temp1);
                }
            }
        }
    }

    private byte[] ZigZag(int[][] in) {
        byte[] data = null;
        int data_index = 0;
        int i = 8;
        int j = 8;
        int count = 64;
        for (int element = 0; element < 64; element++) {
            if (data == null) {
                if (in[i - 1][j - 1] != 0) {
                    data = new byte[count * 2];
                    data_index = 2 * count - 1;
                    data[data_index] = (byte) (in[i - 1][j - 1] & 0x000000FF);
                    --data_index;
                    data[data_index] = (byte) ((in[i - 1][j - 1] >>> 8) & 0x000000FF);
                    --data_index;
                }
            } else {
                data[data_index] = (byte) (in[i - 1][j - 1] & 0x000000FF);
                --data_index;
                data[data_index] = (byte) ((in[i - 1][j - 1] >>> 8) & 0x000000FF);
                --data_index;
            }
            --count;
            if ((i + j) % 2 == 0) {
                // Even stripes
                if (j > 1)
                    j--;
                else
                    i -= 2;
                if (i < 8)
                    i++;
            } else {
                // Odd stripes
                if (i > 1)
                    i--;
                else
                    j -= 2;
                if (j < 8)
                    j++;
            }
        }
        if (data == null)
            data = new byte[0];
        return data;
    }

    private int[][] inverseZigZag(byte[] in) {
        int[][] data = new int[8][8];
        int i = 1;
        int j = 1;
        int count = 0;
        for (int element = 0; element < 64; element++) {
            if (count < in.length) {
                int value = 0;
                value |= ((((int) in[count]) << 8) & 0xFFFFFF00);
                ++count;
                value |= (((int) in[count]) & 0x000000FF);
                ++count;
                data[i - 1][j - 1] = value;
            } else
                data[i - 1][j - 1] = 0;
            if ((i + j) % 2 == 0) {
                // Even stripes
                if (j < 8)
                    j++;
                else
                    i += 2;
                if (i > 1)
                    i--;
            } else {
                // Odd stripes
                if (i < 8)
                    i++;
                else
                    j += 2;
                if (j > 1)
                    j--;
            }
        }
        return data;
    }

    private void LosslessEncode(OutputStream os) throws IOException {
        for (int i = 0; i < m_height; ++i)
            for (int j = 0; j < m_width; ++j) {
                byte[] zz0 = ZigZag(M0[i][j]);
                ByteArrayInputStream bai0 = new ByteArrayInputStream(zz0);
                Huffman huff0 = new Huffman();
                huff0.compress(bai0, os, 128);

                byte[] zz1 = ZigZag(M1[i][j]);
                ByteArrayInputStream bai1 = new ByteArrayInputStream(zz1);
                Huffman huff1 = new Huffman();
                huff1.compress(bai1, os, 128);

                byte[] zz2 = ZigZag(M2[i][j]);
                ByteArrayInputStream bai2 = new ByteArrayInputStream(zz2);
                Huffman huff2 = new Huffman();
                huff2.compress(bai2, os, 128);
            }
    }

    private void LosslessDecode(InputStream is) throws IOException {
        for (int i = 0; i < m_height; ++i)
            for (int j = 0; j < m_width; ++j) {
                ByteArrayOutputStream bao0 = new ByteArrayOutputStream();
                Huffman huff0 = new Huffman();
                huff0.decompress(is, bao0, 128);
                bao0.flush();
                byte[] zz0 = bao0.toByteArray();
                bao0.close();
                M0[i][j] = inverseZigZag(zz0);

                ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
                Huffman huff1 = new Huffman();
                huff1.decompress(is, bao1, 128);
                bao1.flush();
                byte[] zz1 = bao1.toByteArray();
                bao1.close();
                M1[i][j] = inverseZigZag(zz1);

                ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
                Huffman huff2 = new Huffman();
                huff2.decompress(is, bao2, 128);
                bao2.flush();
                byte[] zz2 = bao2.toByteArray();
                if (zz2.length % 2 != 0)
                    System.out.printf("%d, %d", i, j);
                bao2.close();
                M2[i][j] = inverseZigZag(zz2);
                if (zz2.length % 2 != 0)
                    System.out.printf("%d, %d", i, j);
            }
    }

}
