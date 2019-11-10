import org.junit.Test;
import static org.junit.Assert.*;

import Compressor.JPEG; // Class being tested
import Constants.JPEG_Quality; // STUB, needs to be in the same package as original to override signature
import java.io.IOException;

public class DriverJPEG {

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




    


    
    public void Constructor() {
    };

    public void initDCTMatrices() {
    };

    @Test
    public void compress() {
        try {
            InputStream is = new InputStream(); // Stub
            is.StubInitializeDiskData(Image1Pixel); // Config Stub to read this image instead of an stream
            OutputStream os = new OutputStream(); // Stub

            JPEG instance = new JPEG();
            instance.compress(is, os, JPEG_Quality.Q9);
            
            byte[] out = os.StubGetWrittenDiskData(); // Get the data written to the stub instead of an stream

            assertArrayEquals(Image1PixelCompressed, out);
        } catch (IOException ioe) {
            assertEquals("no exception", ioe.getMessage());
        }
    }

    @Test
    public void decompress() {
        try {
            InputStream is = new InputStream(); // Stub
            is.StubInitializeDiskData(Image1PixelCompressed); // Config Stub to read this image instead of an stream
            OutputStream os = new OutputStream(); // Stub

            JPEG instance = new JPEG();
            instance.decompress(is, os);
            
            byte[] out = os.StubGetWrittenDiskData(); // Get the data written to the stub instead of an stream

            assertArrayEquals(Image1PixelDecompressed, out);
        } catch (IOException ioe) {
            assertEquals("no exception", ioe.getMessage());
        }
    }

    public void Quantization() {
    };

    public void Dequantization() {
    };

    public void DCT() {
    };

    public void inverseDCT() {
    };

    public void ZigZag() {
    };

    public void inverseZigZag() {
    };

    public void LosslessEncode() {
    };

    public void LosslessDecode() {
    };
}