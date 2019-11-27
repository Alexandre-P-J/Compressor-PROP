package Domain;

import java.io.File;
import java.io.IOException;

public class HeaderTranslator {

    public static Folder readFileTree(String path) throws IOException {
        Folder FileTree = new Folder("root", null);
        // check if compressed:
        //      WIP
        // compressed:
        //      WIP
        // Not compressed file/folder:
        readUncompressedFileTree(new File(path), FileTree);
        
        return FileTree;
    }

    public static void readUncompressedFileTree(File node, Folder parentFolder) throws IOException {
        if (node.isFile())
            parentFolder.addFile(new Archive(node.getCanonicalPath()));
        else {
            Folder folder = new Folder(node.getName(), parentFolder);
            File[] files = node.listFiles();
            for (File file : files)
                readUncompressedFileTree(file, folder);
        }
	}
    
    
    public static void reserveHeader(Archive out, Folder dir) throws Exception {
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
    }

    public static void setHeaderValues(Archive compressedFile, Folder FileTreeRoot) {

    }

}