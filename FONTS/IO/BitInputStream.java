package IO;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

public class BitInputStream extends FilterInputStream {

	class ControlBit {
		private int[] buff = new int[8];
		private int count = -1 ;

		/**
		 * If we are at the end of the stream.
		 * @return true if we are at the end of the stream, otherwise false.
		 */
		private boolean atTheEnd () { 
			return ((buff[7] == 1) && (count < 0)); 
		}

		/**
		 * Set the flag for the end of stream.
		 */
		private void setTheEnd () { 
			buff[7] = 1;
			count = -1;
		}

		/**
		 * If we need to read the next byte.
		 * @return 
		 */
		private boolean noMoreBuffer () { 
			return count < 0; 
		}

		/**
		 * Set the buffer
		 * @param next
		 */
		private void setNext (int next) { 
			for (count = 0; count < 8; ++count) {
				buff[count] = next % 2;
				next /= 2;
			}

			if (buff[7] == 1) {
				for (count = 7;count >= 0; count--)
				if (buff[count] == 0) break;
				count--;
			} 
			else count = 6;
		}

		/**
		 * Get the next bit.
		 * @return
		 */		
		private int getNext() {
			return buff[count--]; 
		}
	};

	ControlBit bitControl = new ControlBit();

	byte[] tempBuf = null;
	int tempBufPtr = 0;
	int tempBufLen = 0;

	/**
	 * Constructor creates a new instance of BitOutputStream.
	 * @param is the input stream to read of.
	 */
	public BitInputStream (InputStream is) { 
		super(is); 
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private int readNextByte () throws IOException { 
		int val = -1;
		if (tempBufPtr==tempBufLen) val = super.read();
		else {
			byte b = tempBuf[tempBufPtr++];
			if ((b & 0x80) > 0) val = ((int)(b & 0x7F))|0x80;
				else val = b;
		}
		return val;
	}

	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	public int read1Bit() throws IOException {
		if (bitControl.atTheEnd()) return -1;
		if (bitControl.noMoreBuffer()) {
			int i = readNextByte();
			if (i < 0) bitControl.setTheEnd();
			else bitControl.setNext(i);
			return read1Bit(); // CHECK THIS 
		}
		return bitControl.getNext();
	}

	// losing data on incomplete input returning -1 is expected
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public int read8Bit() throws IOException {
		int next;
		int result = 0;
		for (int i = 7; i >= 0; --i) {
			if ((next = read1Bit()) < 0) return -1;
			result |= next << i; 
		}
		return result & 0xFF;
	}

	public int read () throws Error {
		throw new Error("REPLACE THIS FUNCTION IN CODEBASE! USE read1Bit() INSTEAD.");
	}
}