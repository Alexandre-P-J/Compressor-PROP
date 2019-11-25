package Domain;

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

    public static Folder getFolder(Folder start, String relativePath) {
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        String steps[] = Paths.get(relativePath).toString().split(pattern);
        
        if (steps.length == 1) {
            if (start.name.equals(steps[0]))
                return start;
            return null;
        }
        Folder aux = start;
        for (int i = 1; i < steps.length; ++i) {
            Folder[] tmp = aux.getFolders();
            boolean found = false;
            for (int j = 0; j < tmp.length; ++j) {
                if (tmp[j].getName().equals(steps[i])) {
                    aux = tmp[j];
                    found = true;
                    break;
                }
            }
            if (!found)
                return null;
        }
        return aux;
    }

    public String[] getPath() {
        Vector<String> v = new Vector<String>();
        Folder aux = this;
        while (aux != null) {
            v.add(0, aux.getName());
            aux = aux.parent;
        }
        String[] result = new String[v.size()];
        v.toArray(result);
        return result;
    }
}