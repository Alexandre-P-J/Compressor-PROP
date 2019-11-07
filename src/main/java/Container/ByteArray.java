package Container;

public class ByteArray {
    final byte[] array;

    /**
     * Dafault constructor, 0 length array
     */
    public ByteArray () {
        array = new byte[0];
    }

    /**
     * Constructor with a single byte.
     * @param b
     */
    public ByteArray (byte b) {
        array = new byte[] {b};
    }
    
    /**
     * Cloning constructor
     * @param ab
     */
    public ByteArray (byte[] ab) {
        array = (byte[])ab.clone(); 
    }

    /**
     * Returns the size of the byte array.
     * @return the size.
     */
    public int size () {
        return array.length;
	}
	
    /**
     * For the hash-table we need this.
     * @return 
     */
	public boolean equals (Object o) { 
		ByteArray ba = (ByteArray)o;
		return java.util.Arrays.equals(array,ba.array); 
	}

    /**
     * For the hash-table we need to give a hash code.
     * @return 
     */
	public int hashCode() { 
		int code = 0;
		for (int i = 0; i < array.length; ++i)
		code = code*2 + array[i];
		return code;
	}

    /**
     * Returns the byte in a given position.
     * @param p position 
     * @return the byte in the position p.
     */
    public byte getBytePos (int p) {
        return array[p];
    }

    /**
     * Concatenates another byte array into this one.
     * @param ab the ByteArray to concatenate.
     * @return the concatenation in another newly created one.
     */
    public ByteArray concatenate (ByteArray ab) {
        int n = size() + ab.size();
        byte[] b = new byte[n];
        for (int i = 0; i < size(); ++i)
            b[i] = getBytePos(i);
        for (int i = 0; i < ab.size(); ++i)
            b[i+size()] = ab.getBytePos(i);
        return new ByteArray(b);
    }

    /**
     * Concatenates a byte in this array of bytes
     * @param b the byte to concatenate.
     * @return the concatenations in another newly created one.
     */
    public ByteArray concatenate (byte b) {
        return concatenate(new ByteArray(b));
    }

    /**
     * Returns a byte array of the copy of the inner byte arrays.
     * @return a byte array.
     */
	public byte[] getBytes() { 
        return (byte[]) array.clone(); 
    }

    /**
     * Drops the last character.
     * @return the last byte of the ByteArray.
     */
    public byte getLastByte() {
        return array[size()-1];
    }

    /**
     * 
     * @return
     */
    public ByteArray dropLast() {
        byte[] arr = new byte[size()-1];
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