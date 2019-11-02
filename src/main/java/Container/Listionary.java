package Container;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;

public class Listionary {

    int maxSize;
    HashMap < ByteArray, LinkedList <Integer> > t;

    //Constructora del listionary de tamany limitat
    public Listionary (int maxSize) {
        this.maxSize = maxSize;
        t = new HashMap<>();
    }

    public LinkedList<Integer> getList (byte s) {
        return (t.containsKey(s) ? (LinkedList<Integer>)t.get(s) : null);
    }

    public void add (ByteArray ba, LinkedList<Integer> l) {
      if (t.size() < maxSize) t.put(ba, l);
    }
}
