package Compressor;

import Container.ByteArray;
import Container.Dictionary;
import IO.BitInputStream;
import IO.BitOutputStream;
import java.io.*;

/**
 * @author Daniel Clemente
 */

public class LZ78 {
    class Code {
        int c;          // The next char
        int code;       // The code
        Code (int code, int c) {
            this.c = c; this.code = code;
        }
    };

    int nBits;
    Dictionary dict;

    byte[] buff;

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
            ab = emptyAB;
            return dict.getNumStr(aux);
        }
    }

    /**
     * If there something left in ab, encode it.
     * @return the code left.
     */
    Code codifyLast () {
        if (ab.size() == 0) return null;
        byte b = ab.getLastByte();
        ab = ab.dropLast();
        int next = b & 0xFF;
        return new Code(dict.getNumStr(ab), next);
    }

    /**
     * Call the write function with the necessary bits to write the code.
     * @param bos the BitOutputStream.
     * @param co the code to write.
     * @throws IOException If there is a problem.
     */
    void writeCode (BitOutputStream bos, Code co) throws IOException {
		writeCode(bos,co.c,8);
		writeCode(bos,co.code,nBits);
    }
    
    /**
     * Write the code in bits into output stream.
     * @param bos the BitOutputStream.
     * @param code the code to write.
     * @param bits number of bits from the code.
     * @throws IOException If there is a problem.
     */
    void writeCode (BitOutputStream bos, int n, int bits) throws IOException {
		for (int i = 0; i < bits; ++i) {
			bos.write1Bit(n&1);
			n = n / 2;
		}
    }
    
    /**
     * 
     * @param bis
     * @return
     * @throws IOException
     */
    Code readCode (BitInputStream bis) throws IOException { 
        int ch = readInt(bis,8);
        if (ch < 0) return null;
        int co = readInt(bis,nBits);
        if (co < 0) return null;
        return new Code(co,ch); 
    }

    /**
     * 
     * @param bis
     * @param bits
     * @return
     * @throws IOException
     */
    int readInt (BitInputStream bis, int bits) throws IOException {
		int n = 0;
		for (int i=0;i < bits; ++i) {
			int next = bis.read1Bit();
			if (next < 0) return -1;
			n += next<<i;
		}
		return n;
    }

    /**
     * Compresses the given input stream, writing to the given output stream.
     * @param is the InputStream.
     * @param os the OutputStream.
     * @param DictBitSize Dictionary size.
     * @throws Exception If cannot read/write files.
     */
    public void compress (InputStream is, OutputStream os, int DictBitSize) throws Exception {
        if (DictBitSize > 31 || DictBitSize < 0) throw new IllegalArgumentException("Dict size must be between 2^0 and 2^31 !");
        nBits = DictBitSize;
        buff = new byte[nBits];
        dict = new Dictionary(1<<nBits);
        dict.add(emptyAB);
        os.write(nBits); // Write DictBitSize to the compressed stream

        BitOutputStream bos = new BitOutputStream(os);

        int code;
        int next;
        while ((next = is.read()) >= 0){
            code = codifyChar(next);
            if (code >= 0) writeCode(bos, new Code(code,next));
        }
        Code co = codifyLast();
        if (co != null) writeCode(bos, co);
        bos.flush();
    }

    /**
     * Decodes the next code.
     * @param co the code to decode.
     * @return
     */
    ByteArray disarray (Code co) {
        ByteArray aux = dict.getStrNum(co.code);
        dict.add(aux.concatenate((byte)co.c));
        return aux;
    }

    /**
     * Decompresses the given input stream, writing to the given output stream.
     * @param is the InputStream.
     * @param os the OutputStream.
     * @throws Exception If cannot read/write files.
     */
    public void decompress (InputStream is, OutputStream os) throws Exception {
        nBits = is.read();
        if (nBits > 31 || nBits < 0) throw new IllegalArgumentException("Dict size must be between 2^0 and 2^31 !");
        buff = new byte[nBits];
        dict = new Dictionary(1<<nBits);
        dict.add(emptyAB);

        BitInputStream bis = new BitInputStream(is);
        ByteArray s;
        Code co;
        while ((co = readCode(bis)) != null) {
            s = disarray(co);
            os.write(s.getBytes());
            os.write(co.c);
        }
        os.flush();
    }
}