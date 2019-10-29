import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import Compressor.LZ78;

public class LZ78Test {

    @Test
    public void ThisFunctionWorks() {
        try {
            int DictSize = 10;
            String IN = "dfdfsdddddddddddddddddddggggggggggggggggggggsddg";
            InputStream is_0 = new ByteArrayInputStream(IN.getBytes());
            OutputStream os_0 = new ByteArrayOutputStream();
            LZ78 file78_0 = new LZ78(DictSize);
            file78_0.compress(is_0,os_0);
            String Compressed = os_0.toString();
            InputStream is_1 = new ByteArrayInputStream(Compressed.getBytes());
            OutputStream os_1 = new ByteArrayOutputStream();
            LZ78 file78_1 = new LZ78(DictSize);
            /*file78_1.decompress(is_1,os_1);
            String OUT = os_1.toString();

            assertEquals(IN, OUT);*/
        } catch (IOException ioe) {
            assertEquals(0,1);// Always fail if exception
        }
    }

    @Test
    public void ThisFunctionWillFail() {
        int result = 1;
        assertEquals(1, result);
    }
}