package Persistence;

import java.util.Vector;
import java.util.regex.Pattern;
import java.nio.file.Paths;

public class Folder {
    private Vector<Archive> files = new Vector<Archive>();
    private Vector<Folder> folders = new Vector<Folder>();
    private String name;
    private final Folder parent;
    private Folder root = null;

    /**
     * DO NOT ADD FILES OR FOLDERS WITH EXISTING NAMES IN this FOLDER (pending documentation)
     */

    Folder(String name, Folder parent) {
        this.name = name;
        this.parent = parent;
        if (parent == null)
            root = this;
        else {
            root = parent.root;
            parent.folders.add(this);
        }
    }

    public Folder getParent() {
        return parent;
    }

    public Folder getRoot() {
        return root;
    }

    /*// Returns first valid folder, null if hyerarchy is empty or represented by an unique file at root
    public Folder getFirstFolder() {
        if (!root.folders.isEmpty()) // if isn't empty then only has 1 folder (invariant)
            return root.folders.get(0);
        return null;
    }*/

    public String getName() {
        return name;
    }

    public void addFile(Archive file) {
        files.add(file);
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

    // start is not included in the path, hence the path must be relative to start
    public static Folder getFolder(Folder start, String pathToFolder) throws Exception {
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        String steps[] = Paths.get(pathToFolder).toString().split(pattern);
        
        if (pathToFolder.equals("/"))
            return start.root;
        else if (steps.length == 0) throw new Exception(pathToFolder + " folder path not valid");
        
        Folder aux = start;
        for (String step : steps) {
            Folder[] folders = aux.getFolders();
            boolean found = false;
            for (Folder folder : folders) {
                if (folder.getName().equals(step)) {
                    aux = folder;
                    found = true;
                    break;
                }
            }
            if (!found)
                throw new Exception("error traversing " + pathToFolder + " , " + step + " not found");
        }
        return aux;
    }

    // start is not included in the path, hence the path must be relative to start
    public static Archive getFile(Folder start, String pathToFile) throws Exception {
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        String steps[] = Paths.get(pathToFile).toString().split(pattern);
        
        if (steps.length == 0) throw new Exception(pathToFile + " filepath not valid");
        
        Folder aux = start;
        int end = steps.length - 1;
        for (int i = 0; i < end; ++i) {
            Folder[] folders = aux.getFolders();
            boolean found = false;
            for (Folder folder : folders) {
                if (folder.getName().equals(steps[i])) {
                    aux = folder;
                    found = true;
                    break;
                }
            }
            if (!found)
                throw new Exception("error traversing " + pathToFile + " , " + steps[i] + " not found");
        }
        Archive[] files = aux.getFiles();
        for (Archive file : files)
            if (file.getFilename().equals(steps[end]))
                return file;
        throw new Exception(steps[end] + " does not exist at " + pathToFile);
    }

    public String[] getPath() {
        Vector<String> v = new Vector<String>();
        Folder aux = this;
        while (aux != root) {
            v.add(0, aux.getName());
            aux = aux.parent;
        }
        String[] result = new String[v.size()];
        v.toArray(result);
        return result;
    }
}