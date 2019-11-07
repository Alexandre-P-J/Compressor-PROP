package Compressor;

import Container.ByteArray;
import Container.Dictionary;
import IO.BitInputStream;
import IO.BitOutputStream;
import java.io.*;

/**
 * @author Albert Ibars
 */

public class LZW {

    Dictionary dict;
    int nBits;

    // L'anterior cadena de carácters que haurem de recordar
    // per tal d'inserir al diccionari 
    ByteArray emptyAB = new ByteArray();
    ByteArray ab = emptyAB;

    /**
     * Encodes the next character.
     * @param n the character to encode.
     * @return the code generated, if not returns -1.
     */
    int codifyChar (int n) {
        byte b = (byte)n;
        ByteArray aux = ab.concatenate(b);
        int code = dict.getNumStr(aux);
        if (code != -1) {
            ab = aux;
            return -1;
        }
        else {
            dict.add(aux);
            aux = ab;
            ab = new ByteArray(b);
            return dict.getNumStr(aux);
        }
    }

    /**
     * If there something left in ab, encode it.
     * @return the code left.
     */
    int codifyLastChar() {
        ByteArray aux = ab;
        ab = emptyAB;
        return dict.getNumStr(aux);
    }

    /**
     * Write the code in bits into output stream.
     * @param bos the BitOutputStream.
     * @param code the code to write.
     * @throws IOException If there is a problem.
     */
    void writeCode (BitOutputStream bos, int code) throws IOException {
        for (int i = 0; i < nBits; ++i) {
            bos.write1Bit(code&1);
            code /= 2;  
        }
    }

    /**
     * Compresses the given input stream, writing to the given output stream.
     * @param is the InputStream.
     * @param os the OutputStream.
     * @param DictBitSize Dictionary size.
     * @throws Exception If cannot read/write files.
     */
    public void compress (InputStream is, OutputStream os, int DictBitSize) throws Exception {
        if (DictBitSize > 31 || DictBitSize < 8) throw new IllegalArgumentException("Dict size must be between 2^8 and 2^31 !");
        nBits = DictBitSize;
        dict = new Dictionary(1<<nBits);
        // Afegeix tots els codis ascii al diccionari
        for (int i = 0; i < 256; ++i)
            dict.add(new ByteArray((byte)i));
        os.write(nBits); // Write DictBitSize to the compressed stream

        BitOutputStream bos = new BitOutputStream(os);
        int code;
        int next;
        while ((next = is.read()) >= 0){
            code = codifyChar(next);
            if (code >= 0) writeCode(bos, code);
        }
        code = codifyLastChar();
        if (code >= 0) writeCode(bos, code);
        bos.flush();
    }

    /**
     * 
     * @param bis
     * @return
     * @throws IOException
     */
    int readCode (BitInputStream bis) throws IOException {
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
     * @return
     */
    ByteArray disarray (int code) {
        ByteArray s = dict.getStrNum(code);
        if (s == null) {
            s = ab.concatenate(ab.getBytePos(0));
            dict.add(s);
        }
        else 
            if (!ab.isEmpty()) 
                dict.add(ab.concatenate(s.getBytePos(0)));
            ab = s;
        return ab;
    }

    /**
     * Decompresses the given input stream, writing to the given output stream.
     * @param is the InputStream.
     * @param os the OutputStream.
     * @throws Exception If cannot read/write files.
     */
    public void decompress (InputStream is, OutputStream os) throws Exception {
        nBits = is.read();
        if (nBits > 31 || nBits < 8) throw new IllegalArgumentException("Dict size must be between 2^8 and 2^31 !");
        // Create a new dictionary with maximum of 2^bits entries
        dict = new Dictionary(1<<nBits);
        // Add all ascii characters to the dictionary
        for (int i = 0; i < 256; ++i)
            dict.add(new ByteArray((byte)i));

        BitInputStream bis = new BitInputStream(is);
        ByteArray s;    //Next entry
        int code;       // Next code to be read
        while ((code = readCode(bis)) >= 0) {
            s = disarray(code);
            os.write(s.getBytes());
        }
    }
}