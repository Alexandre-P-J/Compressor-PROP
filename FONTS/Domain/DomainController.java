package Domain;

import Presentation.PresentationController;
import Persistence.PersistenceController;
import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;

public class DomainController {
    // Singleton instance
    private static final DomainController instance = new DomainController();
    private static Folder FileTree; // must be the root of the filetree

    // Private to avoid external use of the constructor
    private DomainController() {}

    // Singleton getter
    public static DomainController getInstance() {
        return instance;
    }
    
    // lo llama presentation controller con el path obtenido despues de pulsar SELECT
    public static void readFileTree(String path) throws IOException {
        // check if compressed:
        //      WIP
        // compressed:
        //      WIP
        // Not compressed file/folder:
        FileTree = new Folder("root", null);
        readUncompressedFileTree(new File(path), FileTree);
    }

    private static void readUncompressedFileTree(File node, Folder parentFolder) throws IOException {
        if (node.isFile())
            parentFolder.addFile(new Archive(node.getCanonicalPath()));
        else {
            Folder folder = new Folder(node.getName(), parentFolder);
            File[] files = node.listFiles();
            for (File file : files)
                readUncompressedFileTree(file, folder);
        }
	}

    /**
     * Return filenames from the given relative path
     * @param pathToParentFolder either "/" or "foo/bar.." (replacing ".." with the rest of the path)
     * @return an array of filenames contained in the folder with path equal to path argument
     * @throws Exception if path is invalid or filetree not initialized
     */
    public static String[] getFileNames(String pathToParentFolder) throws Exception {
        return Folder.getFolder(FileTree.getRoot(), pathToParentFolder).getFileNames();
    }

    /**
     * Return folder names from the given relative path
     * @param pathToParentFolder either "/" or "foo/bar.." (replacing ".." with the rest of the path)
     * @return an array of folder names contained in the folder with path equal to path argument
     * @throws Exception if path is invalid or filetree not initialized
     */
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
    /*
    private static void reserveHeader(Archive out, Folder dir) throws Exception {
        String subpath = String.join("", dir.getPath());
        Archive[] files = dir.getFiles();
        for (Archive file : files) {
            out.getOutputStream().write((subpath+"/"+file.getFilename()).getBytes());
            byte[] reserve = new byte[8]; // long
            out.getOutputStream().write(reserve);
        }

        Folder[] folders = dir.getFolders();
        for (Folder folder : folders) {
            
        }
    }*/

    // escribe la cabecera (con la jerarquia de FileTree) y comprime todos los archivos
    public static void compress(String OutputFilePath) {

    }

    // descomprime todos los archivos
    public static void decompress(String OutputFolderPath) {}
    
}