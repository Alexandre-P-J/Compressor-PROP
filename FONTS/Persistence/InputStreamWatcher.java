package Persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public class InputStreamWatcher extends FilterInputStream { // Decorator
    long readBytes = 0;
    
    public InputStreamWatcher(InputStream is) {
        super(is);
    }

    @Override
    public int read() throws IOException {
        int result = super.read();
        if (result != -1)
            ++readBytes;
        return result;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int result = super.read(b);
        if (result != -1)
            readBytes += b.length;
        return result;
    }

    public long getReadBytes() {
        return readBytes;
    }
}