package Container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {

    int maxSize;

    Map <ByteArray, Integer> m;
    List <ByteArray> l; 

    /**
     * 
     * @param maxSize
     */
    public Dictionary (int maxSize) {
        this.maxSize = maxSize;
        m = new HashMap<>();
        l = new ArrayList<>();
    }

    /**
     * Adds an element into the dictionary.
     * @param s ByteArray to add to the dictionary.
     */
    public void add (ByteArray s) {
        if (size() < maxSize) {
            m.put(s, Integer.valueOf(l.size()));
            l.add(s);
        }
    }

    /**
     * Gets the number for the given string.
     * @param s
     * @return 
     */
    public int getNumStr (ByteArray s) {
        return (m.containsKey(s) ? 
                ((Integer)m.get(s)).intValue() : -1);
    }

    /**
     * Gets the string for the given number.
     * @param n
     * @return the ByteArray for the given number, if the number does not exist, return null
     */
    public ByteArray getStrNum (int n) {
        return (n < l.size() ? 
                (ByteArray) l.get(n) : null);
    }

    /**
     * Returns the size of the byte array list.
     * @return the size of the byte array list.
     */
    public int size () {
        return l.size();
    }
}