package Domain;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.io.IOException;

public class Archive { // No quiero que se confunda o aparezcan errores debido a que ya existe java.io.File, Archive suena mal, no se me ocurre nada mejor
    private InputStream is = null;
    private OutputStream os = null;
    private final String absPath;

    Archive(String absPath) {
        this.absPath = absPath;
    }

    public InputStream getInputStream() throws Exception {
        if (is == null) {
            if (os != null) throw new Exception("OutputStream already open, error trying to read");
            is = new BufferedInputStream(new FileInputStream(absPath));
        }
        return is;
    }

    public OutputStream getOutputStream() throws Exception {
        if (os == null) {
            if (is != null) throw new Exception("InputStream already open, error trying to write");
            os = new BufferedOutputStream(new FileOutputStream(absPath));
        }
        return os;
    }

    public String getFilename() {
        return Paths.get(absPath).getFileName().toString();
    }

    public String getSubPath() {
        int size = absPath.length() - getFilename().length();
        return absPath.substring(0, size);
    }

    public boolean isImage() {
        return absPath.substring(absPath.length() - 4) == ".ppm";
    }

    public void close() throws IOException {
        if (is != null) {
            is.close();
        }
        if (os != null) {
            os.close();
        }
    }
}