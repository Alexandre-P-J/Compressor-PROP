import org.junit.Test;
import static org.junit.Assert.assertEquals;

import Compressor.Huffman;
import java.io.*;
import java.util.Random;

public class HuffmanTest {
    /*
    @Test
    public void CompressDecompressSmallSizeUnknown() {
        // Small size subset
        for (int Fsize = 0; Fsize <= 17; ++Fsize)
            for (int attempt = 1; attempt <= 256; ++attempt)
                    CompressDecompress(Fsize, false);
    }

    @Test
    public void CompressDecompressSmallSizeKnown() {
        // Small size subset
        for (int Fsize = 0; Fsize <= 17; ++Fsize)
            for (int attempt = 1; attempt <= 256; ++attempt)
                    CompressDecompress(Fsize, true);
    }

    @Test
    public void CompressDecompressMediumSizeUnknown() {
        // Medium size subset
        for (int Fsize = 1023; Fsize <= 1032; ++Fsize)
            for (int attempt = 1; attempt <= 256; ++attempt)
                CompressDecompress(Fsize, false);
    }

    @Test
    public void CompressDecompressMediumSizeKnown() {
        // Medium size subset
        for (int Fsize = 1023; Fsize <= 1032; ++Fsize)
            for (int attempt = 1; attempt <= 256; ++attempt)
                CompressDecompress(Fsize, true);
    }

    @Test
    public void CompressDecompressBigSizeUnknown() {
        // Small size subset
        for (int Fsize = 262143; Fsize <= 262145; ++Fsize)
            CompressDecompress(Fsize, false);
    }

    @Test
    public void CompressDecompressBigSizeKnown() {
        // Small size subset
        for (int Fsize = 262143; Fsize <= 262145; ++Fsize)
            CompressDecompress(Fsize, true);
    }

    public void CompressDecompress(int Fsize, boolean sizeSpecified) {
        try {
            byte[] IN = new byte[Fsize];
            new Random().nextBytes(IN);
            InputStream is0 = new ByteArrayInputStream(IN);
            ByteArrayOutputStream os0 = new ByteArrayOutputStream();
            Huffman alg_0 = new Huffman();
            if (sizeSpecified)
                alg_0.compress(is0, os0, Fsize);
            else
                alg_0.compress(is0, os0);
            os0.close();
            byte[] Compressed = os0.toByteArray();

            InputStream is1 = new ByteArrayInputStream(Compressed);
            ByteArrayOutputStream os1 = new ByteArrayOutputStream();
            Huffman alg_1 = new Huffman();
            if (sizeSpecified)
                alg_1.decompress(is1, os1, Fsize);
            else
                alg_1.decompress(is1, os1);
            os1.close();
            byte[] Decompressed = os1.toByteArray();
            
            int size = IN.length;
            String output = String.format("\n[!!!] ORIGINAL IS: %dbytes AND DECOMPRESSED LENGTH IS: %dbytes\n", size, Decompressed.length);
            assertEquals(output, size, Decompressed.length);
            for (int i = 0; i < size; ++i) {
                output = String.format("\n[!!!] DIFFERENCE IN BYTE NUMBER %d. ORIGINAL HAS %dbytes DECOMPRESSED HAS %dbytes\n", i, size, Decompressed.length);
                assertEquals(output, IN[i], Decompressed[i]);
            }
        
        } catch (Exception e) {
            String output = String.format("\n[!!!] EXCEPTION REACHED: %s\n", e.toString());
            e.printStackTrace();
            assertEquals(output, "no exception", "exception");
        }
    }
    */
}