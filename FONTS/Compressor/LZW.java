package Compressor;

import Container.ByteArray;
import Container.Dictionary;
import IO.BitInputStream;
import IO.BitOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Albert Ibars
 */

public class LZW {

    Dictionary dict;
    // The number of bits that should be written for each code
    int nBits;

    // The previous byte array that we should remember
    // in order to instert into the dictionary.
    ByteArray emptyBA = new ByteArray();
    ByteArray ba = emptyBA;

    /**
     * Encodes the next byte.
     * @param n the byte to encode.
     * @return the code generated, if not returns -1.
     */
    private int encodeByte (int n) {
        byte b = (byte)n;
        ByteArray aux = ba.concatenate(b);
        int code = dict.getNumStr(aux);
        // if it exists then we continue searching for a longer byte array
        if (code != -1) {
            ba = aux;
            return -1;
        }
        else {
            dict.add(aux);
            aux = ba;
            ba = new ByteArray(b);
            return dict.getNumStr(aux);
        }
    }

    /**
     * Encode de last byte of the sequence if there is something left. 
     * @return the code left.
     */
    private int encodeLastByte() {
        ByteArray aux = ba;
        ba = emptyBA;
        return dict.getNumStr(aux);
    }

    /**
     * Write the code in bits into output stream with the help of the BitOutputStream.
     * @param bos the BitOutputStream the write of.
     * @param code the code to write.
     * @throws IOException if writting to the output stream fails.
     */
    private void writeCode (BitOutputStream bos, int code) throws IOException {
        for (int i = 0; i < nBits; ++i) {
            bos.write1Bit(code&1);
            code /= 2;  
        }
    }

    /**
     * Creates a new dictionary with maximum the size of DictBitSize.
     * Compresses the given input stream, writing to the given output stream.
     * @param is the input stream to read data.
     * @param os the output stream to save data.
     * @param DictBitSize Dictionary size.
     * @throws Exception if reading or writting to a stream fails.
     */
    public void compress (InputStream is, OutputStream os, int DictBitSize) throws Exception {
        if (DictBitSize > 31 || DictBitSize < 8) throw new IllegalArgumentException("Dict size must be between 2^8 and 2^31 !");
        nBits = DictBitSize;
        dict = new Dictionary(1<<nBits);
        // Create a new dictionary with maximum of 2^bits entries
        for (int i = 0; i < 256; ++i)
            dict.add(new ByteArray((byte)i));
        os.write(nBits); // Write DictBitSize to the compressed stream

        BitOutputStream bos = new BitOutputStream(os);
        int code;   // next input byte
        int next;   // next code generated
        while ((next = is.read()) >= 0){
            code = encodeByte(next);
            if (code >= 0) writeCode(bos, code);
        }
        // If there something left in ba
        code = encodeLastByte();
        if (code >= 0) writeCode(bos, code);
        bos.flush();
    }

    /**
     * Read the code from the given bit input stream, and returns it as an int.
     * @param bis the BitInputStream to read of. 
     * @return an Integer with the code of nBits gereneted from the input stream.
     * @throws IOException if reading from the input stream fails.
     */
    private int readCode (BitInputStream bis) throws IOException {
        int n = 0;
        for (int i = 0; i < nBits; ++i) {
            int next = bis.read1Bit();
            if (next < 0) return -1;
            n += next << i;
        }
        return n;
    }

    /**
     * Decodes the next code.
     * @param code the code to decode.
     * @return a ByteArray with the code decoded.
     */
    private ByteArray disarray (int code) {
        ByteArray s = dict.getStrNum(code);
        if (s == null) {
            s = ba.concatenate(ba.getBytePos(0));
            dict.add(s);
        }
        else 
            if (!ba.isEmpty()) 
                dict.add(ba.concatenate(s.getBytePos(0)));
            ba = s;
        return ba;
    }

    /**
     * Creates a new dictionary of the sized received in the first input stream byte.
     * Decompresses the given input stream, writing to the given output stream.
     * @param is the input stream to read data.
     * @param os the output stream to write data.
     * @throws Exception if reading or writting to a stream fails.
     */
    public void decompress (InputStream is, OutputStream os) throws Exception {
        nBits = is.read();  // Dict size
        if (nBits > 31 || nBits < 8) throw new IllegalArgumentException("Dict size must be between 2^8 and 2^31 !");
        // Create a new dictionary with maximum of 2^bits entries
        dict = new Dictionary(1<<nBits);
        // Add all ascii characters to the dictionary
        for (int i = 0; i < 256; ++i)
            dict.add(new ByteArray((byte)i));

        BitInputStream bis = new BitInputStream(is);
        ByteArray s;    // Next entry
        int code;       // Next code to be read
        while ((code = readCode(bis)) >= 0) {
            s = disarray(code);
            os.write(s.getBytes());
        }
    }
}