import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;

public class Listionary {

    int maxSize;
    HashMap <ByteArray, LinkedList <Integer> > t;

    //Constructora del listionary de tamany limitat
    public Listionary (int maxSize) {
        this.maxSize = maxSize;
        t = new HashMap<>();
    }

    public List<int> getList (ByteArray s) {
        return (t.containsKey(s) ? (LinkedList<int>)m.get(s) : null);
    }

}