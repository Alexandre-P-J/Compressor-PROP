package Container;

public class ByteArray {
    final byte[] array;

    // Constructora per defecte, array de tamany 0
    public ByteArray () {
        array = new byte[0];
    }

    // Constructora amb un sol byte
    public ByteArray (byte b) {
        array = new byte[] {b};
    }

    public ByteArray(int size) {
      array = new byte[size];
    }

    // Constructora de clonació
    public ByteArray (byte[] ab) {
        array = (byte[])ab.clone();
    }

    public int size () {
        return array.length;
	}

	// Necessari per a la taula hash
	public boolean equals (Object o) {
		ByteArray ba = (ByteArray)o;
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
    public byte getBytePos (int p) {
        return array[p];
    }

    // Concatena un altre array de bytes en aquesta, retorna un nou array concatenat
    public ByteArray concatenate (ByteArray ab) {
        int n = size() + ab.size();
        byte[] b = new byte[n];
        for (int i = 0; i < size(); ++i)
            b[i] = getBytePos(i);
        for (int i = 0; i < ab.size(); ++i)
            b[i+size()] = ab.getBytePos(i);
        return new ByteArray(b);
    }

    // Concatena un byte en aquest array de bytes
    public ByteArray concatenate (byte b) {
        return concatenate(new ByteArray(b));
    }

    public ByteArray substring (int beginIndex, int endIndex) {
      int n = endIndex - beginIndex + 1;
      byte[] b = new byte[n];
      int index = beginIndex;
      for (int i = 0; i < n; ++i) {
        b[i] = getBytePos(index);
        ++index;
      }
      return new ByteArray(b);
    }

    public int indexOf(ByteArray ba) {
      if (ba.size() == 0) return -1;
      out:
      for (int i = 0; i <= array.length - ba.size(); ++i) {
        for (int j = 0; j < ba.size(); ++j) {
            if (array[i + j] != ba.getBytePos(j)) {
              continue out;
            }
        }
        return i;
      }
      return -1;
    }

	  public byte[] getBytes() {
        return (byte[]) array.clone();
    }

    public byte getLastByte() {
        return array[size()-1];
    }

    public ByteArray dropLast() {
        byte[] arr = new byte[size()-1];
        for (int i = 0; i < arr.length; ++i)
            arr[i] = array[i];
        return new ByteArray(arr);
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
