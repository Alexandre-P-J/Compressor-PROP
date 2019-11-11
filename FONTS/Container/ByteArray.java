package Container;

public class ByteArray {
    final byte[] array;

    /**
     * Default constructor, 0 length array
     */
    public ByteArray() {
        array = new byte[0];
    }

    /**
     * Constructor with a single byte.
     * @param b the byte to be contained in the new byte array.
     */
    public ByteArray(byte b) {
        array = new byte[] { b };
    }

    /**
     * Cloning constructor
     * @param ba the byte array to be cloned.
     */
    public ByteArray(byte[] ba) {
        array = (byte[]) ba.clone();
    }

    /**
     * Returns the size of the byte array.
     * @return the size as a Integer.
     */
    public int size() {
        return array.length;
    }

    /**
     * For the hash-table we need this.
     * @return 
     */
    public boolean equals(Object o) {
        ByteArray ba = (ByteArray) o;
        return java.util.Arrays.equals(array, ba.array);
    }

    /**
     * For the hash-table we need to give a hash code.
     * @return 
     */
    public int hashCode() {
        int code = 0;
        for (int i = 0; i < array.length; ++i)
            code = code * 2 + array[i];
        return code;
    }

    /**
     * Returns the byte in a given position.
     * @param p position to get the byte of.
     * @return the byte in the position p.
     */
    public byte getBytePos(int p) {
        return array[p];
    }

    /**
     * Concatenates another byte array into this one.
     * @param ba the ByteArray to concatenate.
     * @return the concatenation in another newly created one.
     */
    public ByteArray concatenate(ByteArray ba) {
        int n = size() + ba.size();
        byte[] b = new byte[n];
        for (int i = 0; i < size(); ++i)
            b[i] = getBytePos(i);
        for (int i = 0; i < ba.size(); ++i)
            b[i + size()] = ba.getBytePos(i);
        return new ByteArray(b);
    }

    /**
     * Concatenates a byte in this array of bytes
     * @param b the byte to concatenate.
     * @return the concatenations in another newly created one.
     */
    public ByteArray concatenate(byte b) {
        return concatenate(new ByteArray(b));
    }

    // Retorna el ByteArray entre [beginIndex, endIndex)
    /**
     * 
     * @param beginIndex
     * @param endIndex
     * @return
     */
    public ByteArray subByteArray(int beginIndex, int endIndex) {
        assert (beginIndex >= 0);
        assert (endIndex >= beginIndex);
        assert (array.length >= endIndex);
        byte[] bar = new byte[endIndex - beginIndex];
        for (int i = 0; i < bar.length; ++i)
            bar[i] = array[beginIndex + i];
        return new ByteArray(bar);
    }

    // Retorna l'index de la primera ocurrencia del ByteArray ba dins d'aquest ByteArray
    /**
     * 
     * @param ba
     * @return
     */
    public int indexOf(ByteArray ba) {
        if (ba.size() == 0)
            return -1;
        int end = (array.length - ba.size()) + 1;
        for (int i = 0; i < end; ++i)
            for (int j = 0; j < ba.size(); ++j) {
                if (ba.getBytePos(j) != array[i + j])
                    break;
                else if (j == (ba.size() - 1))
                    return i;
            }
        return -1;
    }

    // Retorna un ByteArray que ha eliminat els valors compresos entre [beginIndex, endIndex)
    /**
     * 
     * @param beginIndex
     * @param endIndex
     * @return
     */
    public ByteArray delete(int beginIndex, int endIndex) {
        assert (beginIndex >= 0);
        assert (endIndex >= 0);
        endIndex = Math.min(endIndex, array.length);
        if (beginIndex == endIndex) return this;
        byte[] aux = new byte[array.length - (endIndex-beginIndex)];
        System.arraycopy(array, 0, aux, 0, beginIndex);
        System.arraycopy(array, endIndex, aux, beginIndex, array.length - endIndex);
        return new ByteArray(aux);
    }

    /**
     * Returns a byte array of the copy of the inner byte arrays.
     * @return a byte array.
     */
    public byte[] getBytes() {
        return (byte[]) array.clone();
    }

    /**
     * Drops the last byte.
     * @return the last byte of the ByteArray.
     */
    public byte getLastByte() {
        return array[size() - 1];
    }

    /**
     * 
     * @return
     */
    public ByteArray dropLast() {
        byte[] arr = new byte[size() - 1];
        for (int i = 0; i < arr.length; ++i)
            arr[i] = array[i];
        return new ByteArray(arr);
    }

    /**
     * Checks if it is zero length.
     * @return true if it is not empty, otherwise false.
     */
    public boolean isEmpty() {
        return size() == 0;
    }
}
