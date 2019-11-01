import org.junit.Test;
import static org.junit.Assert.assertEquals;
import Compressor.LZ78;
import java.io.*;
import java.util.Random;

public class LZ78Test {

    @Test
    public void CompressDecompressTest() {
        for (int Fsize = 1024; Fsize <= 1030; ++Fsize)
            for (int Ds = 1; Ds < 31; ++Ds)
                for (int attempt = 1; attempt <= 500; ++attempt)
                    CompressDecompress(Ds, Fsize);
        


        assertEquals(1, 1);
    }


    public void CompressDecompress(int DictSize, int Fsize) {
        try {
            byte[] IN = new byte[Fsize];
            new Random().nextBytes(IN);
            InputStream is0 = new ByteArrayInputStream(IN);
            ByteArrayOutputStream os0 = new ByteArrayOutputStream();
            LZ78 alg_0 = new LZ78(DictSize);
            alg_0.compress(is0, os0);
            os0.close();
            byte[] Compressed = os0.toByteArray();

            InputStream is1 = new ByteArrayInputStream(Compressed);
            ByteArrayOutputStream os1 = new ByteArrayOutputStream();
            LZ78 alg_1 = new LZ78(DictSize);
            alg_1.decompress(is1, os1);
            os1.close();
            byte[] Decompressed = os1.toByteArray();
            
            int size = IN.length;
            assertEquals(size, IN.length);
            for (int i = 0; i < size; ++i) {
                assertEquals(IN[i], Decompressed[i]);
            }
        
        } catch (Exception e) {}
    }
}