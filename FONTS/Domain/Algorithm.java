package Domain;

import java.io.InputStream;
import java.io.OutputStream;

public interface Algorithm {
    public void compress(InputStream is, OutputStream os) throws Exception;

    public void decompress(InputStream is, OutputStream os) throws Exception;
}