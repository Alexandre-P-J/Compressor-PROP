import java.io.* ;

public class BitOutputStream extends FilterOutputStream {
	
	class ControlBit {
		int  buff = 0;
		int  count = 0;
		// Retorna -1 si encara no hi ha res ecrit 
		int  writeOne (int next) {
			int res = -1;
			buff = buff*2 + next;
			count++;
			if (count == 7) {
				res = buff;
				count = 0;
				buff = 0;
			} 
			else res = -1;
			return res; 
		}

		int  writeLast () { 
			int x = 0;
			for (int i = 0; i < 7-count; ++i)
				x = x*2 + 1;
			for (int i = 7-count; i < 8; ++i)
				x = x*2;
			return buff|x; 
		}
	}

	ControlBit controlBit = new ControlBit();

	// Constructora que crea una nova instància de BitOutputStream
	public BitOutputStream (OutputStream os) { 
		super(os); 
	}

	// Escriu un bit al flux inclòs. Tot i que l'entrada és un sol bit,
	// es dona com a int. Si no és zero, es tracta com a 1.
	public void write (int i) throws IOException { 
		int x = controlBit.writeOne(i >= 1 ? 1:0);
		if (x >= 0) out.write(x); 
	}
	
	public void flush() throws IOException { 
		out.write(controlBit.writeLast());
		  super.flush(); 
	}
}