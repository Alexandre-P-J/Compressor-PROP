package Compressor;
import org.junit.Test;
import static org.junit.Assert.*;

import UnitTest_JPEG.*;
import Compressor.JPEG;
import java.io.*;

// ESTO ES UN EJEMPLO DE DRIVER

public class UnitTest {

    // One pixel image in Binary ppm format
    static final byte[] Image1Pixel = { 0x50, 0x36, 0x0a, 0x31, 0x20, 0x31, 0x0a, 0x32, 0x35, 0x35, 0x0a, (byte) 0xff,
        (byte) 0xff, (byte) 0xff };

    static final byte[] Image1PixelDecompressed = {0x50, 0x36, 0x0a, 0x31, 0x20, 0x31, 0x0a, 0x32, 0x35, 0x35, 0x0a,
            (byte)0x90, (byte)0xff, 0x7b};

    // Compressed image its a lot bigger because jpeg isnt great with 1 pixel images, and its paying the cost of
    // the minimal 8x8 matrix + the cost of the huffman tree
    static final byte[] Image1PixelCompressed = { 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x09, 0x27, 0x02,
            0x05, 0x21, 0x27, 0x7f, 0x02, 0x20, 0x24, 0x02, 0x40, 0x62, 0x08, 0x45, 0x10, 0x3c, 0x1d, 0x00, 0x06, 0x38,
            0x5f, 0x3e, 0x15, 0x66, 0x14, 0x17, 0x6f, 0x42, 0x6b, 0x58, 0x57, 0x36, 0x4c, 0x3c, 0x10, 0x47, 0x4d, 0x26,
            0x6b, 0x2d, 0x1a, 0x73, 0x1b, 0x2d, 0x19, 0x75, 0x29, 0x4c, 0x6a, 0x5d, 0x26, 0x35, 0x7a, 0x55, 0x3e,
            (byte) 0xaa, 0x3d, 0x04, 0x06, 0x40, 0x48, 0x13, 0x7f, 0x40, 0x30, 0x01, 0x66, 0x44, 0x2c, 0x2f, 0x14, 0x5f,
            0x36, 0x7e, 0x6d, 0x5f, 0x5b, 0x36, 0x7e, 0x6d, 0x5b, 0x3f, 0x6d, 0x5f, 0x7d, (byte) 0xf5, 0x3d, 0x04, 0x06,
            0x40, 0x48, 0x13, 0x7f, 0x40, 0x30, 0x01, 0x66, 0x44, 0x2c, 0x2f, 0x14, 0x5f, 0x36, 0x7e, 0x6d, 0x5f, 0x5b,
            0x36, 0x7e, 0x6d, 0x5b, 0x3f, 0x6d, 0x5f, 0x7d, (byte) 0xf5 };
    
    static final int[][] Luminance09 = { { 4,  3,  4,  7,  9,  11, 14, 17 },
                                        { 3,  3,  4,  7,  9,  12, 12, 12 },
                                        { 4,  4,  5,  9,  12, 12, 12, 12 },
                                        { 7,  7,  9,  12, 12, 12, 12, 12 },
                                        { 9,  9,  12, 12, 12, 12, 12, 12 },
                                        { 11, 12, 12, 12, 12, 12, 12, 12 },
                                        { 14, 12, 12, 12, 12, 12, 12, 12 },
                                        { 17, 12, 12, 12, 12, 12, 12, 12 }
                                    };

    static final int[][] Chrominance09 = { { 4, 6, 12, 22, 20, 20, 17, 17 },
                                        { 6, 8, 12, 14, 14, 12, 12, 12 },
                                        { 12, 12, 14, 14, 12, 12, 12, 12 },
                                        { 22, 14, 14, 12, 12, 12, 12, 12 },
                                        { 20, 14, 12, 12, 12, 12, 12, 12 },
                                        { 20, 12, 12, 12, 12, 12, 12, 12 },
                                        { 17, 12, 12, 12, 12, 12, 12, 12 },
                                        { 17, 12, 12, 12, 12, 12, 12, 12 } };


    @Test
    public void sometestexample() {
        try {
            HuffmanStub huffmanStub = new HuffmanStub();
            InputStreamStub is =  new InputStreamStub();
            is.StubInitializeDiskData(Image1Pixel);
            OutputStreamStub os = new OutputStreamStub();

            JPEG jpeg = new JPEG(huffmanStub);
            jpeg.compress(is, os, Luminance09, Chrominance09);
            
            byte[] out = os.StubGetWrittenDiskData();
            assertArrayEquals(Image1PixelCompressed, out);
        }catch (Exception e) {
            assertEquals("no exception", e.toString()); // Muy importante esta linea, sino se hace el catch de la excepcion pero el test no falla!!
        }
    }
}