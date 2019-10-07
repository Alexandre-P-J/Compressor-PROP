import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {

    int maxSize;

    Map <ArrayOfBytes, Integer> m;
    List <ArrayOfBytes> l; 

    // Constructora diccionari de tamany limitat
    Dictionary (int maxSize) {
        this.maxSize = maxSize;
        m = new HashMap<>();
        l = new ArrayList<>();
    }

    // Afegeix un element al diccionari
    public void add (ArrayOfBytes s) {
        if (size() < maxSize) {
            m.put(s, Integer.valueOf(l.size()));
            l.add(s);
        }
    }

    // Obté el nombre de la cadena de bytes str, si no existeix retorna -1
    public int getNumStr (ArrayOfBytes s) {
        return (m.containsKey(s) ? 
                ((Integer)m.get(s)).intValue() : -1);
    }

    // Obté la cadena de bytes del nombre n, si no existeix retorna -1
    public ArrayOfBytes getStrNum (int n) {
        return (n < l.size() ? 
                (ArrayOfBytes) l.get(n) : null);
    }

    public int size () {
        return l.size();
    }
}