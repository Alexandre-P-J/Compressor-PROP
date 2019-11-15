package UnitTest_JPEG;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import Compressor.Huffman;

// ESTO ES UN EJEMPLO DE STUB, EN ESTE CASO NO HACE NADA, SOLO UN PRINT.
// FIJATE QUE COMO SU PACKAGE ES 'Compressor' ESTA EN Testing/Compressor

public class HuffmanStub extends Huffman {
    @Override
    public void compress(InputStream is, OutputStream os, int maxSizeInBytes) throws IOException {
        System.out.printf("\n[Huffman::compress] Soy un stub que no hace na!");
        /**
         * PUEDES DESCOMENTAR LA SIGUIENTE LINEA PARA QUE NO TE FALLE TU TEST 
         * MIENTRAS AUN NO IMPLEMENTAS EL STUB O PARA SABER SI FALLA DEBIDO AL STUB O A OTRA COSA.
         */
        super.compress(is, os, maxSizeInBytes); // COMENTAME
    }

    @Override
    public void decompress(InputStream is, OutputStream os, int maxSizeInBytes) throws IOException {
        System.out.printf("\n[Huffman::decompress] Soy un stub que no hace na!");
        /**
         * PUEDES DESCOMENTAR LA SIGUIENTE LINEA PARA QUE NO TE FALLE TU TEST 
         * MIENTRAS AUN NO IMPLEMENTAS EL STUB O PARA SABER SI FALLA DEBIDO AL STUB O A OTRA COSA.
         */
        super.decompress(is, os, maxSizeInBytes); // COMENTAME
    }
}

