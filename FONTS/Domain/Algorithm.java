package Domain;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class Algorithm {
    public abstract void compress(InputStream is, OutputStream os) throws Exception;

    public abstract void decompress(InputStream is, OutputStream os) throws Exception;
}