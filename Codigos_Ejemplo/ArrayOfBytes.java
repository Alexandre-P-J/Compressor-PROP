public class ArrayOfBytes {
    final byte[] array;

    // Constructora per defecte, array de tamany 0
    ArrayOfBytes () {
        array = new byte[0];
    }

    // Constructora amb un sol byte
    ArrayOfBytes (byte b) {
        array = new byte[] {b};
    }
    
    // Constructora de clonació
    ArrayOfBytes (byte[] ab) {
        array = (byte[])ab.clone(); 
    }

    public int size () {
        return array.length;
	}
	
	// Necessari per a la taula hash
	public boolean equals (Object o) { 
		ArrayOfBytes ba = (ArrayOfBytes)o;
		return java.util.Arrays.equals(array,ba.array); 
	}

	// Per una taula hash es necesita un codi hash
	public int hashCode() { 
		int code = 0;
		for (int i = 0; i < array.length; ++i)
		code = code*2 + array[i];
		return code;
	}

    // Retorna el byte que es troba en la posició p
    byte getBytePos (int p) {
        return array[p];
    }

    // Concatena un altre array de bytes en aquesta, retorna un nou array concatenat
    public ArrayOfBytes concatenate (ArrayOfBytes ab) {
        int n = size() + ab.size();
        byte[] b = new byte[n];
        for (int i = 0; i < size(); ++i)
            b[i] = getBytePos(i);
        for (int i = 0; i < ab.size(); ++i)
            b[i+size()] = ab.getBytePos(i);
        return new ArrayOfBytes(b);
    }

    // Concatena un byte en aquest array de bytes
    public ArrayOfBytes concatenate (byte b) {
        return concatenate(new ArrayOfBytes(b));
    }

	public byte[] getBytes() { 
        return (byte[]) array.clone(); 
    }

    public byte getLastByte() {
        return array[size()-1];
    }

    public ArrayOfBytes dropLast() {
        byte[] arr = new byte[size()-1];
        for (int i = 0; i < arr.length; ++i) 
            arr[i] = array[i];
        return new ArrayOfBytes(arr);
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}