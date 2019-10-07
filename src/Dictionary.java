import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {

    int maxSize;

    Map <ByteArray, Integer> m;
    List <ByteArray> l; 

    // Constructora diccionari de tamany limitat
    Dictionary (int maxSize) {
        this.maxSize = maxSize;
        m = new HashMap<>();
        l = new ArrayList<>();
    }

    // Afegeix un element al diccionari
    public void add (ByteArray s) {
        if (size() < maxSize) {
            m.put(s, Integer.valueOf(l.size()));
            l.add(s);
        }
    }

    // Obté el nombre de la cadena de bytes str, si no existeix retorna -1
    public int getNumStr (ByteArray s) {
        return (m.containsKey(s) ? 
                ((Integer)m.get(s)).intValue() : -1);
    }

    // Obté la cadena de bytes del nombre n, si no existeix retorna -1
    public ByteArray getStrNum (int n) {
        return (n < l.size() ? 
                (ByteArray) l.get(n) : null);
    }

    public int size () {
        return l.size();
    }
}