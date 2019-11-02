package Compressor;

import Container.ByteArray;
import Container.Dictionary;
import IO.BitInputStream;
import IO.BitOutputStream;
import java.io.*;

public class LZW {

    Dictionary dict;
    final int nBits;

    // L'anterior cadena de carácters que haurem de recordar
    // per tal d'inserir al diccionari 
    ByteArray emptyAB = new ByteArray();
    ByteArray ab = emptyAB;

    public LZW (int DictBitSize) {
        if (DictBitSize > 31 || DictBitSize < 8) throw new IllegalArgumentException("Dict size must be between 2^0 and 2^31 !");
        nBits = DictBitSize;
        dict = new Dictionary(1<<nBits);

        // Afegeix tots els codis ascii al diccionari
        for (int i = 0; i < 256; ++i)
            dict.add(new ByteArray((byte)i));   
    }

    // Codifica el pròxim caràcter, 
    // si hi ha generat un codi el retorna, sino retorna -1
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

    // Si queda algo en ab, retorna el seu codi
    int codifyLastChar() {
        ByteArray aux = ab;
        ab = emptyAB;
        return dict.getNumStr(aux);
    }

    void writeCode (BitOutputStream bos, int code) throws IOException {
        for (int i = 0; i < nBits; ++i) {
            bos.write1Bit(code&1);
            code /= 2;  
        }
    }

    // Funció principal de compressió
    public void compress (InputStream is, OutputStream os) throws IOException {
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

    int readCode (BitInputStream bis) throws IOException {
        int n = 0;
        for (int i = 0; i < nBits; ++i) {
            int next = bis.read1Bit();
            if (next < 0) return -1;
            n += next << i;
        }
        return n;
    }

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

    public void decompress (InputStream is, OutputStream os) throws IOException {
        BitInputStream bis = new BitInputStream(is);
        ByteArray s;
        int code;
        while ((code = readCode(bis)) >= 0) {
            s = disarray(code);
            os.write(s.getBytes());
        }
    }
}