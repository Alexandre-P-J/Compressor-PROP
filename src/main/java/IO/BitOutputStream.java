package IO;

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
			if (count >= 7) {
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
			count = 0;
			int aux = buff;
			buff = 0;
			return aux|x; 
		}
	}

	ControlBit controlBit = new ControlBit();

	// Constructora que crea una nova instància de BitOutputStream
	public BitOutputStream (OutputStream os) { 
		super(os); 
	}

	// Escriu un bit al flux inclòs. Tot i que l'entrada és un sol bit,
	// es dona com a int. Si no és zero, es tracta com a 1.
	public void write1Bit (int i) throws IOException { 
		int x = controlBit.writeOne(i >= 1 ? 1:0);
		if (x >= 0) out.write(x); 
	}

	public void write1Bit (boolean i) throws IOException { 
		int x = controlBit.writeOne(i ? 1:0);
		if (x >= 0) out.write(x); 
	}

	public void write8Bit (int i) throws IOException {
		write1Bit((i >>> 7) & 0x00000001);
		write1Bit((i >>> 6) & 0x00000001);
		write1Bit((i >>> 5) & 0x00000001);
		write1Bit((i >>> 4) & 0x00000001);
		write1Bit((i >>> 3) & 0x00000001);
		write1Bit((i >>> 2) & 0x00000001);
		write1Bit((i >>> 1) & 0x00000001);
		write1Bit(i & 0x00000001);
	}

	public void write8Bit (byte i) throws IOException {
		write8Bit((int)i);
	}

	public void write(int i) throws Error {
		throw new Error("REPLACE THIS FUNCTION IN CODEBASE! USE write1Bit INSTEAD.");
	}
	
	public void flush() throws IOException {
		if (controlBit.count > 0) out.write(controlBit.writeLast());
		super.flush();
	}
}