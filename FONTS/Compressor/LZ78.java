package Compressor;

import Container.ByteArray;
import Container.Dictionary;
import IO.BitInputStream;
import IO.BitOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Daniel Clemente
 */

public class LZ78 {
    class Code {
        int c;          // The next byte/character
        int code;       // The code
        Code (int code, int c) {
            this.c = c; this.code = code;
        }
    };

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
    int encodeByte (int n) {
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
            ba = emptyBA;
            return dict.getNumStr(aux);
        }
    }

    /**
     * Encode de last byte of the sequence if there is something left.
     * @return the code left.
     */
    Code encodeLastByte () {
        if (ba.size() == 0) return null;
        byte b = ba.getLastByte();
        ba = ba.dropLast();
        int next = b & 0xFF;
        return new Code(dict.getNumStr(ba), next);
    }

    /**
     * Call the write function with the necessary bits to write the code.
     * @param bos the BitOutputStream to write of.
     * @param co the code to write.
     * @throws IOException If there is a problem.
     */
    void writeCode (BitOutputStream bos, Code co) throws IOException {
		writeCode(bos,co.c,8);
		writeCode(bos,co.code,nBits);
    }
    
    /**
     * Write the code in bits into output stream with the help of the BitOutputStream.
     * @param bos the BitOutputStream the write of.
     * @param n the code to write.
     * @param bits number of bits from the code.
     * @throws IOException If cannot write to the output stream.
     */
    void writeCode (BitOutputStream bos, int n, int bits) throws IOException {
		for (int i = 0; i < bits; ++i) {
			bos.write1Bit(n&1);
			n = n / 2;
		}
    }
    
    /**
     * Read the code from the given bit input stream, and returns it as a Code.
     * @param bis the BitInputStream to read of.
     * @return the code of nBits gereneted from the input stream.
     * @throws IOException If cannot read from the input stream.
     */
    Code readCode (BitInputStream bis) throws IOException { 
        int ch = readInt(bis,8);
        if (ch < 0) return null;
        int co = readInt(bis,nBits);
        if (co < 0) return null;
        return new Code(co,ch); 
    }

    /**
     * Read the code from the given bit input stream, and returns it as an int.
     * @param bis the BitInputStream to read of.
     * @param bits the number of bits of the code.
     * @return an Integer with the code generated from the input stream.
     * @throws IOException If cannot read from the input stream.
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
     * Creates a new dictionary with maximum the size of DictBitSize.
     * Compresses the given input stream, writing to the given output stream.
     * @param is the input stream to read data.
     * @param os the output stream to save data..
     * @param DictBitSize Dictionary size.
     * @throws Exception If cannot read/write files.
     */
    public void compress (InputStream is, OutputStream os, int DictBitSize) throws Exception {
        if (DictBitSize > 31 || DictBitSize < 0) throw new IllegalArgumentException("Dict size must be between 2^0 and 2^31 !");
        nBits = DictBitSize;
        dict = new Dictionary(1<<nBits);
        dict.add(emptyBA);
        os.write(nBits); // Write DictBitSize to the compressed stream

        BitOutputStream bos = new BitOutputStream(os);
        int code;   // next input byte
        int next;   // next code generated
        while ((next = is.read()) >= 0){
            code = encodeByte(next);
            if (code >= 0) writeCode(bos, new Code(code,next));
        }
        // If there something left in ba
        Code co = encodeLastByte();
        if (co != null) writeCode(bos, co);
        bos.flush();
    }

    /**
     * Decodes the next code.
     * @param co the code to decode.
     * @return a ByteArray with the code decoded.
     */
    ByteArray disarray (Code co) {
        ByteArray aux = dict.getStrNum(co.code);
        dict.add(aux.concatenate((byte)co.c));
        return aux;
    }

    /**
     * Creates a new dictionary of the sized received in the first input stream byte.
     * Decompresses the given input stream, writing to the given output stream.
     * @param is the input stream to read data.
     * @param os the output stream to write data
     * @throws Exception If cannot read/write files.
     */
    public void decompress (InputStream is, OutputStream os) throws Exception {
        nBits = is.read();
        if (nBits > 31 || nBits < 0) throw new IllegalArgumentException("Dict size must be between 2^0 and 2^31 !");
        // Create a new dictionary with maximum of 2^bits entrie
        dict = new Dictionary(1<<nBits);
        dict.add(emptyBA);

        BitInputStream bis = new BitInputStream(is);
        ByteArray s;    // Next entry
        Code co;        // Next code to be read
        while ((co = readCode(bis)) != null) {
            s = disarray(co);
            os.write(s.getBytes());
            os.write(co.c);
        }
        os.flush();
    }
}