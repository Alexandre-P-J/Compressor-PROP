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

    // lo llama presentation controller para abrir un archivo comprimido al pulsar SELECT
    // esta funcion inicializa FileTree (se lee la cabecera del comprimido)
    public static void openCompressed(String absPath) {}
    
    // lo llama presentation controller para abrir un archivo/carpeta no comprimido al pulsar SELECT
    // esta funcion inicializa FileTree
    public static void openNotCompressed(String path) throws IOException {
        String name = Paths.get(path).getFileName().toString();
        FileTree = new Folder(name, null);
        initFileTree(new File(path), FileTree);
    }

    private static void initFileTree(File dir, Folder node) throws IOException {
        if (dir.isFile()) { // special case
            node.addFile(new Archive(dir.getCanonicalPath()));
            return;
        }
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
                Folder f = new Folder(file.getName(), node);
				initFileTree(file, f);
			} else {
				node.addFile(new Archive(file.getCanonicalPath()));
			}
		}
	}

    // la usa presentation controller para
    public static String[] getFileNames(String relativePath) {
        Folder tmp = Folder.getFolder(FileTree.getRoot(), relativePath);
        assert(tmp != null);
        return tmp.getFileNames();
    }

    // la usa presentation controller para
    public static String[] getFolderNames(String relativePath) {
        Folder tmp = Folder.getFolder(FileTree.getRoot(), relativePath);
        assert(tmp != null);
        return tmp.getFolderNames();
    }

    public static void setCompressionType(String relativePath, String Type) {
        Archive f = Folder.getFile(FileTree.getRoot(), relativePath);
        CompressionType cType = CompressionType.valueOf(Type);
        f.setCompressionType(cType);
    }

    public static String getCompressionType(String relativePath) {
        Archive f = Folder.getFile(FileTree.getRoot(), relativePath);
        return f.getCompressionType().toString();
    }

    // escribe la cabecera (con la jerarquia de FileTree) y comprime todos los archivos
    public static void compress(String OutputFilePath) {}

    // descomprime todos los archivos
    public static void decompress(String OutputFolderPath) {}
    
}