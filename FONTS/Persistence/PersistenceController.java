package Persistence;

import Domain.DomainController;
import java.io.IOException;
import java.io.OutputStream;

public class PersistenceController {
    // Singleton instance
    private static final PersistenceController instance = new PersistenceController();
    private static Folder FileTree; // must be the root of the filetree

    // Private to avoid external use of the constructor
    private PersistenceController() {}

    // Singleton getter
    public static PersistenceController getInstance() {
        return instance;
    }

    public static void readFileTree(String path) throws IOException {
        FileTree = HeaderTranslator.readFileTree(path);
    }

    public static String[] getFileNames(String pathToParentFolder) throws Exception {
        return Folder.getFolder(FileTree.getRoot(), pathToParentFolder).getFileNames();
    }

    public static String[] getFolderNames(String pathToParentFolder) throws Exception {
        return Folder.getFolder(FileTree.getRoot(), pathToParentFolder).getFolderNames();
    }

    public static void setCompressionType(String path, String Type) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        CompressionType cType = CompressionType.valueOf(Type);
        f.setCompressionType(cType);
    }

    public static String getCompressionType(String path) throws Exception {
        Archive f = Folder.getFile(FileTree.getRoot(), path);
        return f.getCompressionType().toString();
    }

    public static OutputStream reserveHeader(String outputPath) throws Exception {
        Archive out = new Archive(outputPath);
        OutputStream os = out.getOutputStream();
        HeaderTranslator.reserveHeader(os, FileTree.getRoot());
        return os;
    }
}