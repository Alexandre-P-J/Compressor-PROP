import java.io.*;

public class LZW {

    Dictionary dict;
    int nBits;

    // L'anterior cadena de carácters que haurem de recordar
    // per tal d'inserir al diccionari 
    ArrayOfBytes emptyAB = new ArrayOfBytes();
    ArrayOfBytes ab = emptyAB;

    public LZW () {
        nBits = 12;
        dict = new Dictionary(1<<nBits);

        // Afegeix tots els codis ascii al diccionari
        for (int i = 0; i < 256; ++i)
            dict.add(new ArrayOfBytes((byte)i));   
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
            ab = new ArrayOfBytes(b);
            return dict.getNumStr(aux);
        }
    }

    // Si queda algo en ab, retorna el seu codi
    int codifyLastChar() {
        ArrayOfBytes aux = ab;
        ab = emptyAB;
        return dict.getNumStr(aux);
    }

    void writeCode (OutputStream os, int code) throws IOException {
        for (int i = 0; i < nBits; ++i) {
            os.write(code&1);
            code /= 2;  
        }
    }

    // Funció principal de compressió
    public void compress (InputStream is, OutputStream os) throws IOException {
        os = new BitOutputStream(os);
        int code;
        int next;
        while ((next = is.read()) >= 0){
            code = codifyChar(next);
            if (code >= 0) writeCode(os, code);
        }
        code = codifyLastChar();
        if (code >= 0) writeCode(os, code);
        os.flush();
    }

    int readCode (InputStream is) throws IOException {
        int n = 0;
        for (int i = 0; i < nBits; ++i) {
            int next = is.read();
            if (next < 0) return -1;
            n += next << i;
        }
        return n;
    }

    ArrayOfBytes disarray (int code) {
        ArrayOfBytes s = dict.getStrNum(code);
        if (s == null) {
            s = ab.concatenate(ab.getBytePos(0));
            dict.add(s);
        }
        else {
            if (!ab.isEmpty()) 
                dict.add(ab.concatenate(s.getBytePos(0)));
            ab = s;
        }
        return ab;
    }

    public void decompress (InputStream is, OutputStream os) throws IOException {
        is = new BitInputStream(is);
        ArrayOfBytes s;
        int code;
        while ((code = readCode(is)) >= 0) {
            s = disarray(code);
            os.write(s.getBytes());
        }
    }
}