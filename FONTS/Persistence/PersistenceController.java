package Persistence;

import Domain.DomainController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PersistenceController {
    // Singleton instance
    private static final PersistenceController instance = new PersistenceController();
    private static Folder FileTree; // must be the root of the filetree
    private static Statistics totalStats = new Statistics();
    private static HeaderTranslator headerTranslator;
    private static String openedPath;

    // Private to avoid external use of the constructor
    private PersistenceController() {}

    // Singleton getter
    public static PersistenceController getInstance() {
        return instance;
    }

    public static void readFileTree(String path) throws IOException {
        headerTranslator = new HeaderTranslator();
        totalStats = new Statistics();
        FileTree = headerTranslator.readFileTree(path);
        openedPath = path;
    }

    public static Boolean isFileTreeCompressed() {
        return headerTranslator.fileTreeIsCompressed();
    }

    public static String[] getFileNames(String pathToParentFolder) throws Exception {
        return Folder.getFolder(FileTree.getRoot(), pathToParentFolder).getFileNames();
    }

    public static String[] getFolderNames(String pathToParentFolder) throws Exception {
        return Folder.getFolder(FileTree.getRoot(), pathToParentFolder).getFolderNames();
    }

    public static void setCompressionType(String path, String Type) throws Exception {
        if (isFileTreeCompressed()) throw new Exception("Cannot change compression algorithm of a compressed file!");
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        CompressionType cType = CompressionType.valueOf(Type);
        f.setCompressionType(cType);
    }

    public static String getCompressionType(String path) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        return f.getCompressionType().toString();
    }

    public static long getTotalTimeStat() {
        return totalStats.getExecutionTime();
    }

    public static long getTotalInputSizeStat() {
        return totalStats.getInputSize();
    }

    public static long getTotalOutputSizeStat() {
        return totalStats.getOutputSize();
    }

    public static long getFileTimeStat(String path) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        Statistics stats = f.getStatistics();
        return stats.getExecutionTime();
    }

    public static long getFileInputSizeStat(String path) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        Statistics stats = f.getStatistics();
        return stats.getInputSize();
    }

    public static long getFileOutputSizeStat(String path) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        Statistics stats = f.getStatistics();
        return stats.getOutputSize();
    }

    public static boolean isFileImage(String path) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        return f.isImage();
    }

    public static final int[][] getLuminanceTable(String quality) {
        return JPEG_Quality.valueOf(quality).getLuminanceTable();
    }

    public static final int[][] getChrominanceTable(String quality) {
        return JPEG_Quality.valueOf(quality).getChrominanceTable();
    }

    public static String getCompressionParameter(String path) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        return f.getCompressionArgument();
    }

    public static void setCompressionParameter(String path, String arg) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        f.setCompressionArgument(arg);
    }

    public static String getDocument(String Path) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Archive f = Folder.getFile(FileTree.getRoot(), Path);
        if (isFileTreeCompressed()) {
            InputStream is = new BufferedInputStream(new FileInputStream(openedPath));
            System.out.println(headerTranslator.getReadHeaderSize());
            System.out.println(f.getHeaderIndex());
            is.skip(f.getHeaderIndex());
            DomainController.chainDecompress(is, baos, f.getCompressionType().toString());
        }
        else {
            InputStream is = f.getInputStream();
            int next;
            while ((next = is.read()) >= 0) {
                baos.write(next);
            }
        }
        baos.flush();
        return new String(baos.toByteArray(), "UTF-8"); // UTF-8 Encoding
    }

    public static InputStream getImage(String Path) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Archive f = Folder.getFile(FileTree.getRoot(), Path);
        if (!f.isImage()) throw new Exception(Path+" Not an image!");
        if (isFileTreeCompressed()) {
            InputStream is = new BufferedInputStream(new FileInputStream(openedPath));
            is.skip(f.getHeaderIndex());
            DomainController.chainDecompress(is, baos, f.getCompressionType().toString());
            baos.flush();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            return bais;
        }
        return f.getInputStream();
    }

    public static InputStream getImageAfterLossyCompression(String Path) throws Exception {    
        if (isFileTreeCompressed()) {
            return getImage(Path);
        }
        Archive f = Folder.getFile(FileTree.getRoot(), Path);
        if (!f.isImage()) throw new Exception(Path+" Not an image!");
        ByteArrayOutputStream baos0 = new ByteArrayOutputStream();
        DomainController.chainCompress(f.getInputStream(), baos0, f.getCompressionType().toString(), f.getCompressionArgument());
        ByteArrayInputStream bais1 = new ByteArrayInputStream(baos0.toByteArray());
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        DomainController.chainDecompress(bais1, baos1, f.getCompressionType().toString());
        ByteArrayInputStream bais2 = new ByteArrayInputStream(baos1.toByteArray());
        return bais2;
    }


    public static void compressFiletree(String outputPath) throws Exception {
        Archive out = new Archive(outputPath);
        OutputStream os = new BufferedOutputStream(out.getOutputStream());
        OutputStreamWatcher osw = new OutputStreamWatcher(os);
        headerTranslator.reserveHeader(osw, FileTree.getRoot());
        totalStats.setInputSize(0);
        totalStats.setOutputSize(osw.getWrittenBytes()); // header size
        totalStats.setExecutionTime(0);
        traverseCompress(os, FileTree.getRoot());
        os.flush();
        os.close();
        headerTranslator.setHeaderValues(outputPath, FileTree.getRoot());
    }

    public static void decompressFiletree(String outputPath) throws Exception {
        InputStream is = new BufferedInputStream(new FileInputStream(openedPath));
        is.skip(headerTranslator.getReadHeaderSize());
        traverseDecompress(is, FileTree.getRoot(), outputPath);
        is.close();
    }

    private static void traverseCompress(OutputStream os, Folder parentFolder) throws Exception {
        Archive[] files = parentFolder.getFiles();
        for (Archive file : files) {
            Statistics stats = new Statistics();
            InputStreamWatcher isw = new InputStreamWatcher(file.getInputStream());
            OutputStreamWatcher osw = new OutputStreamWatcher(os);
            long timeStart = System.currentTimeMillis();
            DomainController.chainCompress(isw, osw, file.getCompressionType().toString(), file.getCompressionArgument());
            long timeEnd = System.currentTimeMillis();
            stats.setInputSize(isw.getReadBytes());
            stats.setOutputSize(osw.getWrittenBytes());
            stats.setExecutionTime(timeEnd - timeStart);
            file.setStatistics(stats);
            file.setHeaderIndex(totalStats.getOutputSize());
            totalStats.setOutputSize(totalStats.getOutputSize() + stats.getOutputSize());
            totalStats.setInputSize(totalStats.getInputSize() + stats.getInputSize());
            totalStats.setExecutionTime(totalStats.getExecutionTime() + stats.getExecutionTime());
            isw.close();
        }
        Folder[] folders = parentFolder.getFolders();
        for (Folder folder : folders) {
            traverseCompress(os, folder);
        }
    }
    
    private static void traverseDecompress(InputStream is, Folder parentFolder, String path) throws Exception {
        Archive[] files = parentFolder.getFiles();
        for (Archive file : files) {
            Statistics stats = new Statistics();
            InputStreamWatcher isw = new InputStreamWatcher(is);
            OutputStreamWatcher osw = new OutputStreamWatcher(new BufferedOutputStream(new FileOutputStream(path+System.getProperty("file.separator")+file.getFilename())));
            long timeStart = System.currentTimeMillis();
            DomainController.chainDecompress(isw, osw, file.getCompressionType().toString());
            long timeEnd = System.currentTimeMillis();
            stats.setInputSize(isw.getReadBytes());
            stats.setOutputSize(osw.getWrittenBytes());
            stats.setExecutionTime(timeEnd - timeStart);
            file.setStatistics(stats);
            totalStats.setOutputSize(totalStats.getOutputSize() + stats.getOutputSize());
            totalStats.setInputSize(totalStats.getInputSize() + stats.getInputSize());
            totalStats.setExecutionTime(totalStats.getExecutionTime() + stats.getExecutionTime());
            osw.flush();
            osw.close();
        }
        Folder[] folders = parentFolder.getFolders();
        for (Folder folder : folders) {
            File f = new File(path+System.getProperty("file.separator")+folder.getName());
            f.mkdir();
            traverseDecompress(is, folder, f.getCanonicalPath());
        }
    }
}