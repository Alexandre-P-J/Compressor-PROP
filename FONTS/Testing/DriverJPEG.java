import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

import Compressor.JPEG; // Class being tested
import Constants.JPEG_Quality; // STUB, needs to be in the same package as original to override signature

public class DriverJPEG {
    
    public void Constructor() {};
    
    public void initDCTMatrices() {};

    @Test
    public void compress() {
        try {
            //String str = "P3 1 2 255\n0 0 0\n4 4 4";
            byte[] INPUT = {'P','3','\n',    '1',' ','1','\n',   '2','5','5','\n',   '1','\n','2','\n','3','\n'};//str.getBytes();
            InputStream is = new InputStream(); // STUB
            is.verbose = false;
            is.StubInitializeDiskData(INPUT); // InputStream stub will read this array stub instead of any stream
            OutputStream os = new OutputStream(); // STUB
            os.verbose = false;
            JPEG_Quality quality = JPEG_Quality.Q9; // STUB, no matter what quality setting it will use quality 9 (its not relevant for the test)
            
            JPEG instance01 = new JPEG();
            instance01.compress(is, os, quality);
            
            byte[] OUTPUT = os.StubGetWrittenDiskData();

            //assertArrayEquals(INPUT, OUTPUT);
            
            
            InputStream isD = new InputStream(); // STUB
            isD.StubInitializeDiskData(OUTPUT); // InputStream stub will read this array stub instead of any stream
            OutputStream osD = new OutputStream(); // STUB
            JPEG instance02 = new JPEG();
            instance02.decompress(isD, osD);
            byte[] DEC = osD.StubGetWrittenDiskData();
            assertArrayEquals(INPUT, DEC);
        }
        catch (IOException ioe) {
            assertEquals("no exception", ioe.getMessage());
        }
    }
    
    public void decompress() {};

    public void Quantization() {};
    
    public void Dequantization() {};
    
    public void DCT() {};
    
    public void inverseDCT() {};
    
    public void ZigZag() {};
    
    public void inverseZigZag() {};

    public void LosslessEncode() {};

    public void LosslessDecode() {};
}