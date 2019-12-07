package Persistence;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FilterOutputStream;

public class OutputStreamWatcher extends FilterOutputStream { // Decorator
    long writtenBytes = 0;
    
    public OutputStreamWatcher(OutputStream os) {
        super(os);
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        //writtenBytes += b.length; I dont know why
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        ++writtenBytes;
    }

    public long getWrittenBytes() {
        return writtenBytes;
    }
}