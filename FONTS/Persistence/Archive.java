package Persistence;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;

public class Archive {
    private final String Path;
    private String CType;
    private String compressionArg;
    private long index = -1; // offset from the start of the compressed file
    private Statistics stats;

    /**
     * Constructor
     * @param Path path or name of the file
     */
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

    /**
     * Input stream getter for the file
     * @return InputStream for the file represented
     * @throws Exception if io error
     */
    public InputStream getInputStream() throws Exception {
        return new BufferedInputStream(new FileInputStream(Path));
    }

    /**
     * Output stream setter for the file
     * @return OutputStream for the file represented
     * @throws Exception if io error
     */
    public OutputStream getOutputStream() throws Exception {
        return new BufferedOutputStream(new FileOutputStream(Path));
    }

    /**
     * Filename getter
     * @return String containing the filename
     */
    public String getFilename() {
        return Paths.get(Path).getFileName().toString();
    }

    /**
     * Consultor returning if the file is a ppm image
     * @return true if it represents a ppm image, false otherwise
     */
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

    /**
     * CompressionArgument getter
     * @return String representing a valid or invalid compression argument
     */
    public String getCompressionArgument() {
        return compressionArg;
    }

    /**
     * Sets the header index
     */
    public void setHeaderIndex(long position) {
        index = position;
    }

    /**
     * header index getter
     * @return a long >= 0
     */
    public long getHeaderIndex() {
        return index;
    }

    /**
     * Statistics getter
     * @return Statistics object of this file
     */
    public Statistics getStatistics() {
        return stats;
    }

    /**
     * Statistics setter
     * @param s Statistics object
     */
    public void setStatistics(Statistics s) {
        stats = s;
    }
}