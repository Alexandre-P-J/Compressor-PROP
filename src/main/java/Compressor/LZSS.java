package Compressor;

import Container.ByteArray;
import Container.Listionary;
import IO.BitInputStream;
import IO.BitOutputStream;
import java.io.*;

public class LZSS {

    int windowSize = 4096;

    Listionary t;
    final int nBits;

    public LZSS (int ListionaryBitSize) {
        if (ListionaryBitSize > 31 || ListionaryBitSize < 0) throw new IllegalArgumentException("Listionary size must be between 2^0 and 2^31 !");
        nBits = ListionaryBitSize;
        t = new Listionary(1<<nBits);
    }

    void writeCode (OutputStream os, int n, int bits) throws IOException {
        for (int i = 0; i < bits; ++i) {
            bos.write1Bit(n&1);
            n = n / 2;
        }
    }

    int readCode (BitInputStream bis, int bits) throws IOException {
        int n = 0;
        for (int i=0;i < bits; ++i) {
            int next = bis.read1Bit();
            if (next < 0) return -1;
            n += next<<i;
        }
        return n;
    }

    public void compress(InputStream is, OutputStream os) throws IOException {
        BitOutputStream bos = new BitOutputStream(os);
        int next;
        ByteArray buffer = new ByteArray();
        int bytesInBuffer = 0;
        while ((next = is.readNextByte()) >= 0) {
            buffer = buffer.concatenate((byte)next);
            ++bytesInBuffer;
        }
        for(int i = 0; i < bytesInBuffer; i++) {
            boolean found = false;
            int start = 0;
            int matchLen = 0;
            byte target = buffer.getBytePos(i*8);
            LinkedList<Integer> l = t.getList(target);
            if(l != null) {
                Iterator<Integer> it = l.iterator();
                while(it.hasNext()) {
                    int s = it.next();
                    if((i - s) > windowSize){
                        it.remove();
                        continue;
                    }
                    int len = getMatchedLen(buffer, s + 1, i + 1, bytesInBuffer) + 1;
                    if(len > matchLen) {
                        start = i - s;
                        matchLen = len;
                    }
                    found = true;
                }
                l.add(i);
                int jn = Math.min(i + matchLen, bytesInBuffer);
                for(int j = i + 1; j < jn; j++){
                    LinkedList<Integer> p = t.getList(buffer.getBytePos(j*8));
                    if(p == null){
                        p = new LinkedList<Integer>();
                        t.put(buffer.getBytePos(j*8), p);
                    }
                    p.add(j);
                }
            } else{
                l = new LinkedList<Integer>();
                l.add(i);
                t.put(target, l);
            }
            if(found && matchLen > 1){
                bos.write1Bit(1); //Añadimos un 1 al output para indicar que se trata de una codificación
                writeCode(bos,start,12);
                writeCode(bos,matchLen,4);
                i += matchLen - 1;
            } else{
                bos.write1Bit(0); //Añadimos un 0 al output para indicar que se trata de un literal
                writeCode(bos,target,8);
            }
        }
        bos.flush();
    }

    private static int getMatchedLen(ByteArray buffer, int i1, int i2, int end) {
        int n = Math.min(i2 - i1, end - i2);
        for(int i = 0; i < n; i++){
            if(buffer.getBytePos((i1++)*8) != buffer.getBytePos((i2++)*8)) return i;
        }
        return 0;
    }

    public void decompress (InputStream is, OutputStream os) throws IOException {
        int start, matchLen;
        byte next;
        BitInputStream bis = new BitInputStream(is);
        ByteArray decodeBuffer = new ByteArray();
        while ((next = is.read1Bit()) >= 0) {
            if (next == 1) {
                // Se tratará de una dupla de posición de la coincidencia (12bits) y tamaño de esta (4bits)
                start = readCode(bis,12);
                matchLen = readCode(bis,4);
                int s = decodeBuffer.size() - start;
                int e = s + matchLen;
                for (; s < e; s++) {
                    next = bis.readNextByte();
                    decodeBuffer.concatenate(next);
                    os.write(next);
                }
            }
            else {
                next = bis.readNextByte();
                decodeBuffer.concatenate(next);
                os.write(next);
            }
        }
    }
}