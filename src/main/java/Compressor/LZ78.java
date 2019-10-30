package Compressor;

import Container.ByteArray;
import Container.Dictionary;
import IO.BitInputStream;
import IO.BitOutputStream;
import java.io.*;

public class LZ78 {
    class Code {
        int c;          // El próxim char
        int code;       // El codi
        Code (int code, int c) {
            this.c = c; this.code = code;
        }
    };

    final int nBits;
    Dictionary dict;

    byte[] buff;

    ByteArray emptyAB = new ByteArray();
    ByteArray ab = emptyAB;

    public LZ78 (int DictBitSize) {
        if (DictBitSize > 31 || DictBitSize < 0) throw new IllegalArgumentException("Dict size must be between 2^0 and 2^31 !");
        nBits = DictBitSize;
        buff = new byte[nBits];
        dict = new Dictionary(1<<nBits);

        dict.add(emptyAB);
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
            ab = emptyAB;
            return dict.getNumStr(aux);
        }
    }

    // Si queda algo en ab, retorna el seu codi
    Code codifyLast () {
        if (ab.size() == 0) return null;
        byte b = ab.getLastByte();
        ab = ab.dropLast();
        return new Code(dict.getNumStr(ab), (int)b);
    }

    void writeCode (BitOutputStream bos, Code co) throws IOException {
		writeCode(bos,co.c,8);
		writeCode(bos,co.code,nBits);
    }
    
    void writeCode (BitOutputStream bos, int n, int bits) throws IOException {
		for (int i = 0; i < bits; ++i) {
			bos.write1Bit(n&1);
			n = n / 2;
		}
    }
    
    Code readCode (BitInputStream bis) throws IOException { 
        int ch = readInt(bis,8);
        if (ch < 0) return null;
        int co = readInt(bis,nBits);
        if (co < 0) return null;
        return new Code(co,ch); 
    }

    int readInt (BitInputStream bis, int bits) throws IOException {
		int n = 0;
		for (int i=0;i < bits; ++i) {
			int next = bis.read1Bit();
			if (next < 0) return -1;
			n += next<<i;
		}
		return n;
    }
    
    public void compress (InputStream is, OutputStream os) throws IOException {
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

    ByteArray disarray (Code co) {
        ByteArray aux = dict.getStrNum(co.code);
        dict.add(aux.concatenate((byte)co.c));
        return aux;
    }

    public void decompress (InputStream is, OutputStream os) throws IOException {
        BitInputStream bis = new BitInputStream(is);
        ByteArray s;
        Code co;
        while ((co = readCode(bis)) != null) {
            s = disarray(co);
            os.write(s.getBytes());
            os.write(co.c);
        }
    }
}