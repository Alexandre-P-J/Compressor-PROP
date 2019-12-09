package Persistence;

import Domain.DomainController;
import Domain.PPMTranslator;

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
        if (totalStats.executionTime > 0)
            return totalStats.executionTime;
        return 0;
    }

    public static long getTotalInputSizeStat() {
        if (totalStats.inputSize > 0)
            return totalStats.inputSize;
        return 0;
    }

    public static long getTotalOutputSizeStat() {
        if (totalStats.outputSize > 0)
            return totalStats.outputSize;
        return 0;
    }

    public static long getFileTimeStat(String path) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        Statistics stats = f.getStatistics();
        return stats.executionTime;
    }

    public static long getFileInputSizeStat(String path) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        Statistics stats = f.getStatistics();
        return stats.inputSize;
    }

    public static long getFileOutputSizeStat(String path) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        Statistics stats = f.getStatistics();
        return stats.outputSize;
    }

    public static boolean isFileImage(String path) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        return f.isImage();
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

    public static byte[] getImage(String Path) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Archive f = Folder.getFile(FileTree.getRoot(), Path);
        if (!f.isImage()) throw new Exception(Path+" Not an image!");
        PPMTranslator ppmt;
        if (isFileTreeCompressed()) {
            InputStream is = new BufferedInputStream(new FileInputStream(openedPath));
            is.skip(f.getHeaderIndex());
            DomainController.chainDecompress(is, baos, f.getCompressionType().toString());
            baos.flush();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ppmt = new PPMTranslator(bais);
        }
        else {
            InputStream is = f.getInputStream();
            ppmt = new PPMTranslator(is);
        }
        
        int w = ppmt.getWidth();
        byte[] wa = toArray(w);
        int h = ppmt.getHeight();
        byte[] ha = toArray(h);
        byte[] result = new byte[8 + (w*h*3)];
        System.arraycopy(wa, 0, result, 0, 4);
        System.arraycopy(ha, 0, result, 4, 4);
        for (int i = 8; i < result.length; ++i)
            result[i] = (byte)ppmt.getNextComponent(); // unsigned encoding
        return result;
    }


    public static void compressFiletree(String outputPath) throws Exception {
        Archive out = new Archive(outputPath);
        OutputStream os = new BufferedOutputStream(out.getOutputStream());
        OutputStreamWatcher osw = new OutputStreamWatcher(os);
        headerTranslator.reserveHeader(osw, FileTree.getRoot());
        totalStats.inputSize = 0;
        totalStats.outputSize = osw.getWrittenBytes(); // header size
        totalStats.executionTime = 0;
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
            DomainController.chainCompress(isw, osw, file.getCompressionType().toString());
            long timeEnd = System.currentTimeMillis();
            stats.inputSize = isw.getReadBytes();
            stats.outputSize = osw.getWrittenBytes();
            stats.executionTime = timeEnd - timeStart;
            file.setStatistics(stats);
            file.setHeaderIndex(totalStats.outputSize);
            totalStats.outputSize += stats.outputSize;
            totalStats.inputSize += stats.inputSize;
            totalStats.executionTime += stats.executionTime;
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
            stats.inputSize = isw.getReadBytes();
            stats.outputSize = osw.getWrittenBytes();
            stats.executionTime = timeEnd - timeStart;
            file.setStatistics(stats);
            totalStats.outputSize += stats.outputSize;
            totalStats.inputSize += stats.inputSize;
            totalStats.executionTime += stats.executionTime;
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

    private static byte[] toArray(int value) {
        byte[] result = new byte[4];
        result[0] = (byte)((value >> 24) & 0x000000FF);
        result[1] = (byte)((value >> 16) & 0x000000FF);
        result[2] = (byte)((value >> 8) & 0x000000FF);
        result[3] = (byte)(value & 0x000000FF);
        return result;
    }
}