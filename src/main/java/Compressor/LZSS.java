
package Compressor;

import Container.ByteArray;
import IO.BitInputStream;
import IO.BitOutputStream;
import java.io.*;


public class LZSS {

    // 12 bits to store maximum offset distance.
    public static final int MAX_WINDOW_SIZE = (1 << 12) - 1;
    // 4 bits to store length of the match.
    private static final int MAX_LENGTH = (1 << 4) - 1;
    private static final int MIN_LENGTH = 2;

    // sliding window size
    private int windowSize = LZSS.MAX_WINDOW_SIZE;
    //private int bufferSizeBytes = 4;
    private int maxLength = LZSS.MAX_LENGTH;
    private int minLength = LZSS.MIN_LENGTH;

    private int m = 12;
    private int n = 4;
    private int k = 2;


     void writeCode (BitOutputStream bos, int n, int bits) throws IOException {
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


    public void compress(InputStream is, OutputStream os, int DictBitSize) throws IOException {
        ByteArray buffer = new ByteArray(windowSize);
        BitOutputStream bos = new BitOutputStream(os);

        bos.write8Bit(m);
        bos.write8Bit(n);
        bos.write8Bit(k);

        ByteArray currentMatch = new ByteArray();
        int matchIndex = 0;
        int tempIndex = 0;
        int next;

        while ((next = is.read()) >= 0) {
            tempIndex = buffer.indexOf(currentMatch.concatenate((byte) next));
            if (tempIndex != -1 && currentMatch.size() < maxLength) {
                currentMatch.concatenate((byte) next);
                matchIndex = tempIndex;
            }
            else {
                if (currentMatch.size() >= minLength) {
                    bos.write1Bit(0);
                    writeCode(bos,matchIndex,m);
                    writeCode(bos,currentMatch.size(),n);
                    buffer.concatenate(currentMatch);
                    currentMatch = new ByteArray();
                    currentMatch.concatenate((byte) next);
                    matchIndex = 0;
                }
                else {
                    currentMatch.concatenate((byte) next);
                    matchIndex = -1;
                    while (currentMatch.size() > -1 && matchIndex == -1) {
                        bos.write1Bit(1);
                        bos.write8Bit((byte) currentMatch.getBytePos(0));
                        buffer.concatenate(currentMatch.getBytePos(0));
                        currentMatch = currentMatch.substring(1, currentMatch.size());
                        matchIndex = buffer.indexOf(currentMatch);
                    }
                }
                if (buffer.size() > windowSize) {
                    buffer = buffer.substring(buffer.size() - windowSize, buffer.size());
                }
            }
        }
        while (currentMatch.size() > 0) {
            if (currentMatch.size() >= minLength) {
                bos.write1Bit(0);
                writeCode(bos,matchIndex,m);
                writeCode(bos,currentMatch.size(),n);
                buffer.concatenate(currentMatch);
                currentMatch = new ByteArray();
                matchIndex = 0;
            }
            else {
                matchIndex = -1;
                while (currentMatch.size() > 0 && matchIndex == -1) {
                    bos.write1Bit(1);
                    bos.write8Bit((byte) currentMatch.getBytePos(0));
                    buffer.concatenate(currentMatch.getBytePos(0));
                    currentMatch = currentMatch.substring(1, currentMatch.size());
                    matchIndex = buffer.indexOf(currentMatch);
                }
            }
            if (buffer.size() > windowSize) {
                buffer = buffer.substring(buffer.size() - windowSize, buffer.size());
            }
        }
        bos.flush();
    }


    public void decompress(InputStream is, OutputStream os) throws IOException {
        BitInputStream bis = new BitInputStream(is);
        int m = bis.read8Bit();
        int windowSize = (1 << m) - 1;
        int n = bis.read8Bit();
        int minK = bis.read8Bit();
        ByteArray buffer = new ByteArray(windowSize);
        int flag;
        while ((flag = bis.read1Bit()) >= 0) {
            if (flag == 1) {
                int s = bis.read8Bit();
                buffer.concatenate((byte) s);
                os.write(s);
            }
            else {

                int offsetValue = readCode(bis,m);
                int lengthValue = readCode(bis,n);;

                if(offsetValue < 0 || lengthValue < 0) break;

                int start = offsetValue;
                int end = start + lengthValue;

                ByteArray temp = buffer.substring(start, end);
                for (int i = 0; i < temp.size(); ++i) {
                  byte b = temp.getBytePos(i);
                  os.write(b);
                }
                buffer.concatenate(temp);
            }

            if (buffer.size() > windowSize) {
                buffer = buffer.substring(buffer.size() - windowSize, buffer.size());
            }
        }
    }
}
