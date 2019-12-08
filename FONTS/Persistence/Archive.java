package Persistence;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.io.IOException;

public class Archive { // No quiero que se confunda o aparezcan errores debido a que ya existe java.io.File, Archive suena mal, no se me ocurre nada mejor
    private final String Path;
    private CompressionType CType;
    private long index = -1; // offset from the start of the compressed file
    private Statistics stats;

    Archive(String Path) {
        this.Path = Path;
        if (isImage())
            CType = CompressionType.JPEG;
        else
            CType = CompressionType.LZW;
    }

    public InputStream getInputStream() throws Exception {
        return new BufferedInputStream(new FileInputStream(Path));
    }

    public OutputStream getOutputStream() throws Exception {
        return new BufferedOutputStream(new FileOutputStream(Path));
    }

    public String getFilename() {
        return Paths.get(Path).getFileName().toString();
    }

    public boolean isImage() {
        if (Path.length() > 4) {
            return Path.substring(Path.length() - 4).equals(".ppm");
        }
        return false;
    }

    public void setCompressionType(CompressionType Type) {
        CType = Type;
    }

    public CompressionType getCompressionType() {
        return CType;
    }

    public void setHeaderIndex(long position) {
        index = position;
    }

    public long getHeaderIndex() {
        return index;
    }

    public Statistics getStatistics() {
        return stats;
    }

    public void setStatistics(Statistics s) {
        stats = s;
    }
}