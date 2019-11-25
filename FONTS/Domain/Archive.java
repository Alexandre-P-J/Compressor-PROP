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
    private final String Path;
    private CompressionType CType;

    Archive(String Path) {
        this.Path = Path;
        if (isImage())
            CType = CompressionType.JPEG;
        else
            CType = CompressionType.LZW;
    }

    public InputStream getInputStream() throws Exception {
        if (is == null) {
            if (os != null) throw new Exception("OutputStream already open, error trying to read");
            is = new BufferedInputStream(new FileInputStream(Path));
        }
        return is;
    }

    public OutputStream getOutputStream() throws Exception {
        if (os == null) {
            if (is != null) throw new Exception("InputStream already open, error trying to write");
            os = new BufferedOutputStream(new FileOutputStream(Path));
        }
        return os;
    }

    public String getFilename() {
        return Paths.get(Path).getFileName().toString();
    }

    public String getSubPath() {
        int size = Path.length() - getFilename().length();
        return Path.substring(0, size);
    }

    public boolean isImage() {
        return Path.substring(Path.length() - 4) == ".ppm";
    }

    public void setCompressionType(CompressionType Type) {
        CType = Type;
    }

    public CompressionType getCompressionType() {
        return CType;
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