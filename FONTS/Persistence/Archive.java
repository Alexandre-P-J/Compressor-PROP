package Persistence;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;

public class Archive { // No quiero que se confunda o aparezcan errores debido a que ya existe java.io.File, Archive suena mal, no se me ocurre nada mejor
    private final String Path;
    private String CType;
    private String compressionArg;
    private long index = -1; // offset from the start of the compressed file
    private Statistics stats;

    Archive(String Path) {
        this.Path = Path;
        stats = new Statistics();
        CType = PersistenceController.getDefaultCompressionType(isImage());
        try {
            compressionArg = PersistenceController.getDefaultCompressionParameter(CType);
        } catch (Exception e) {
            compressionArg = null;
        }
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

    public void setCompressionType(String Type) throws Exception {
        if (isImage() && (!Type.equals("JPEG"))) 
            throw new Exception("Compression algorithm not compatible with images!");
        if (!isImage() && Type.equals("JPEG"))
            throw new Exception("JPEG algorithm not compatible with documents!");
        CType = Type;
        compressionArg = PersistenceController.getDefaultCompressionParameter(CType);
    }

    public String getCompressionType() {
        return CType;
    }

    public void setCompressionArgument(String arg) throws Exception {
        if (PersistenceController.isCompressionParameterValid(arg, CType)) {
            compressionArg = arg;
        }
        else {
            throw new Exception("Invalid compression argument!");
        }
    }

    public String getCompressionArgument() {
        return compressionArg;
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