package Domain;

import java.util.Vector;

public class Folder {
    private Vector<Archive> files = new Vector<Archive>();
    private Vector<Folder> folders = new Vector<Folder>();
    private String name;
    private final Folder parent;
    private static Folder root = null;

    /**
     * DO NOT ADD FILES OR FOLDERS WITH EXISTING NAMES IN this FOLDER (pending documentation)
     */

    Folder(String name, Folder parent) {
        this.name = name;
        this.parent = parent;
        if (parent == null && root == null)
            root = this;
    }

    public Folder getParent() {
        return parent;
    }

    public Folder getRoot() {
        return root;
    }

    public String getName() {
        return name;
    }

    public void addFile(Archive file) {
        files.add(file);
    }

    public Archive addFile(String absFilePath) {
        Archive file = new Archive(absFilePath);
        files.add(file);
        return file;
    }

    public void addFiles(Archive[] Files) {
        for (int i = 0; i < Files.length; ++i)
            files.add(Files[i]);
    }

    public void addFiles(String[] absFilePaths) {
        for (int i = 0; i < absFilePaths.length; ++i) {
            Archive file = new Archive(absFilePaths[i]);
            files.add(file);
        }
    }

    public void addFolder(Folder folder) {
        assert(folder.parent == this); // guess that'll work
        folders.add(folder);
    }

    public Folder addFolder(String Name) {
        Folder folder = new Folder(Name, this);
        folders.add(folder);
        return folder;
    }

    public Archive[] getFiles() {
        Archive[] aux = new Archive[files.size()];
        return files.toArray(aux);
    }

    public Folder[] getFolders() {
        Folder[] aux = new Folder[folders.size()];
        return folders.toArray(aux);
    }

    public String[] getFileNames() {
        String[] aux = new String[files.size()];
        for (int i = 0; i < files.size(); ++i) {
            aux[i] = files.get(i).getFilename();
        }
        return aux;
    }

    public String[] getFolderNames() {
        String[] aux = new String[folders.size()];
        for (int i = 0; i < folders.size(); ++i) {
            aux[i] = folders.get(i).name;
        }
        return aux;
    }
}