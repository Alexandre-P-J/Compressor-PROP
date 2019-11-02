package Compressor;

import java.io.*;
import java.util.PriorityQueue;
import IO.*;

public class Huffman {

    // alphabet size of extended ASCII
    private static final int R = 256;

    // Huffman trie node
    private class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }


    public void compress(InputStream is, OutputStream os) throws Exception {
        compress(is, os, 0x7FFFFFFF);
    }

    /**
     * Reads a sequence of 8-bit bytes from standard input; compresses them using
     * Huffman codes with an 8-bit alphabet; and writes the results to standard
     * output.  If maxSizeInBytes is specified then the same value must be specified
     * at decompression.
     */
    public void compress(InputStream is, OutputStream os, int maxSizeInBytes) throws Exception {
        int nBits = maxSizeInBytes == 0 ? 1 : 33 - Integer.numberOfLeadingZeros(maxSizeInBytes - 1);
        if ((nBits < 0) || (nBits > 32)) throw new IllegalArgumentException("maxSizeInBytes must be in [0, 2^31-1]");
        
        // read the input
        BitOutputStream bos = new BitOutputStream(os);
        ByteArrayOutputStream aux = new ByteArrayOutputStream();
        int[] freq = new int[R];
        int next;
        while ((next = is.read()) >= 0) {
            freq[next]++;
            aux.write(next);
        }
        aux.flush();
        byte[] input = aux.toByteArray();
        aux.close();

        // write number of bytes in original uncompressed message
        for (int i = 0; i < nBits; ++i) {
            int val = (input.length >>> i) & 0x01;
            bos.write1Bit(val);
        }
        
        if (input.length < 1) {
            bos.flush();
            return; // just store (0) for an empty stream
        }
        // build Huffman trie
        Node root = buildTrie(freq);
        
        // build code table
        String[] st = new String[R];
        buildCode(st, root, "");

        // write trie for decoder
        writeTrie(bos, root);

        // use Huffman code to encode input
        for (int i = 0; i < input.length; i++) {
            String code = st[input[i] & 0xFF];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    bos.write1Bit(false);
                } else if (code.charAt(j) == '1') {
                    bos.write1Bit(true);
                } else
                    throw new IllegalStateException("Illegal state");
            }
        }
        bos.flush();
    }

    // build the Huffman trie given frequencies
    private Node buildTrie(int[] freq) {

        // initialze priority queue with singleton trees
        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        // MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.add(new Node(i, freq[i], null, null));

        // special case in case there is only one character with a nonzero frequency
        if (pq.size() == 1) {
            if (freq['\0'] == 0)
                pq.add(new Node('\0', 0, null, null));
            else
                pq.add(new Node('\1', 0, null, null));
        }

        // merge two smallest trees
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.add(parent);
        }
        return pq.poll();
    }

    // write bitstring-encoded trie to standard output
    private void writeTrie(BitOutputStream bos, Node x) throws IOException {
        if (x.isLeaf()) {
            bos.write1Bit(true);
            byte out = (byte) (x.ch & 0xFF);
            bos.write8Bit(out);
            return;
        }
        bos.write1Bit(false);
        writeTrie(bos, x.left);
        writeTrie(bos, x.right);
    }

    // make a lookup table from symbols and their encodings
    private void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0');
            buildCode(st, x.right, s + '1');
        } else {
            st[x.ch & 0xFF] = s;
        }
    }


    public void decompress(InputStream is, OutputStream os) throws Exception {
        decompress(is, os, 0x7FFFFFFF);
    }

    /**
     * Reads a sequence of bits that represents a Huffman-compressed message from
     * standard input; expands them; and writes the results to standard output.
     * If maxSizeInBytes was specified at compression then the same value must be specified now.
     */
    public void decompress(InputStream is, OutputStream os, int maxSizeInBytes) throws Exception {
        int nBits = maxSizeInBytes == 0 ? 1 : 33 - Integer.numberOfLeadingZeros(maxSizeInBytes - 1);
        if ((nBits < 0) || (nBits > 32)) throw new IllegalArgumentException("maxSizeInBytes must be in [0, 2^31-1]");
        
        BitInputStream bis = new BitInputStream(is);

        int length = 0;
        for (int i = 0; i < nBits; ++i) {
            int next;
            if ((next = bis.read1Bit()) < 0) throw new IOException();
            length |= (next << i);
        }
        
        if (length < 1) return; // return if decompressed is an empty stream

        // read in Huffman trie from input stream
        Node root = readTrie(bis);

        // decode using the Huffman trie
        for (int i = 0; i < length; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                int next;
                if ((next = bis.read1Bit()) < 0)
                    throw new IOException();
                boolean bit = (next != 0);
                if (bit)
                    x = x.right;
                else
                    x = x.left;
            }
            int out = ((int) (x.ch) & 0xFF);
            os.write(out);
        }
        os.flush();
    }

    private Node readTrie(BitInputStream bis) throws IOException {
        int next;
        if ((next = bis.read1Bit()) < 0)
            throw new IOException();
        boolean isLeaf = (next != 0);
        if (isLeaf) {
            char tmp = (char) (bis.read8Bit()); // throws exception if fails
            return new Node(tmp, -1, null, null);
        } else {
            return new Node('\0', -1, readTrie(bis), readTrie(bis));
        }
    }
}
