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

    ArrayOfBytes emptyAB = new ArrayOfBytes();
    ArrayOfBytes ab = emptyAB;

    public LZ78 (int DictBitSize) {
        if (DictBitSize > 31 && DictBitSize < 0) throw new IllegalArgumentException("Dict size must be between 2^0 and 2^31 !");
        nBits = DictBitSize;
        buff = new byte[nBits];
        dict = new Dictionary(1<<nBits);

        dict.add(emptyAB);
    }

    // Codifica el pròxim caràcter, 
    // si hi ha generat un codi el retorna, sino retorna -1
    int codifyChar (int n) {
        byte b = (byte)n;
        ArrayOfBytes aux = ab.concatenate(b);
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

    void writeCode (OutputStream os, Code co) throws IOException {
		writeCode(os,co.c,8);
		writeCode(os,co.code,nBits);
    }
    
    void writeCode (OutputStream os, int n, int bits) throws IOException {
		for (int i = 0; i < bits; ++i) {
			os.write(n&1);
			n = n / 2;
		}
    }
    
    Code readCode (InputStream is) throws IOException { 
        int ch = readInt(is,8);
        if (ch < 0) return null;
        int co = readInt(is,nBits);
        if (co < 0) return null;
        return new Code(co,ch); 
    }

    int readInt (InputStream is, int bits) throws IOException {
		int n = 0;
		for (int i=0;i < bits; ++i) {
			int next = is.read();
			if (next < 0) return -1;
			n += next<<i;
		}
		return n;
    }
    
    public void compress (InputStream is, OutputStream os) throws IOException {
        os = new BitOutputStream(os);
        int code;
        int next;
        while ((next = is.read()) >= 0){
            code = codifyChar(next);
            if (code >= 0) writeCode(os, new Code(code,next));
        }
        Code co = codifyLast();
        if (co != null) writeCode(os, co);
        os.flush();
    }

    ArrayOfBytes disarray (Code co) {
        ArrayOfBytes aux = dict.getStrNum(co.code);
        dict.add(aux.concatenate((byte)co.c));
        return aux;
    }

    public void decompress (InputStream is, OutputStream os) throws IOException {
        is = new BitInputStream(is);
        ArrayOfBytes s;
        Code co;
        while ((co = readCode(is)) != null) {
            s = disarray(co);
            os.write(s.getBytes());
            os.write(co.c);
        }
    }
}