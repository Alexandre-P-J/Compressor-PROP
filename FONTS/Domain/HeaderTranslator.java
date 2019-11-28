package Domain;

import java.io.File;
import java.io.IOException;

public class HeaderTranslator {

    public static Folder readFileTree(String path) throws IOException {
        Folder FileTree = new Folder("root", null);
        // check if compressed:
        //      WIP
        // compressed:
        //      WIP
        // Not compressed file/folder:
        readUncompressedFileTree(new File(path), FileTree);
        
        return FileTree;
    }

    public static void readUncompressedFileTree(File node, Folder parentFolder) throws IOException {
        if (node.isFile())
            parentFolder.addFile(new Archive(node.getCanonicalPath()));
        else {
            Folder folder = new Folder(node.getName(), parentFolder);
            File[] files = node.listFiles();
            for (File file : files)
                readUncompressedFileTree(file, folder);
        }
	}
    
    private static void reserveFileHeader(Archive[] files, BitOutputStream bos) throws Exception {
        for (Archive file : files) {
            byte type = 0;
            switch (file.getCompressionType()) {
                case LZW:
                    type = 0x01;
                    break;
                case LZ78:
                    type = 0x02;
                    break;
                case LZSS:
                    type = 0x03;
                    break;
                case JPEG:
                    type = 0x04;
                    break;
                default:
                    throw new Exception("Invalid compression type");
            }
            bos.write8Bit(type);
            for (int i = 0; i < 8; ++i) {
                bos.write8Bit(0);
            }
            byte[] name = file.getFilename().getBytes("UTF-8");
            for (byte b : name) {
                bos.write8Bit(b);
            }
            bos.flush();
        }
    }
    
    public static void reserveHeader(BitOutputStream bos, Folder dir) throws Exception {
        Archive[] files = dir.getFiles();
        reserveFileHeader(files, bos);
        Folder[] folders = dir.getFolders();
        for (Folder folder : folders) {
            bos.write8Bit(0x00); // Folder type
            byte[] name = folder.getName().getBytes("UTF-8");
            for (byte b : name) {
                bos.write8Bit(b);
            }
            bos.flush();
            reserveHeader(bos, folder);
        }

    }

    public static void setHeaderValues(Archive compressedFile, Folder FileTreeRoot) {

    }

    public static void readCompressedFileTree(BitInputStream bis, Folder parentFolder) throws Exception {
        int type;
        while ((type = bis.read8Bit()) >= 0) {
            if ((type > 0) && (type <= 4)) { // File
                byte[] localPointer = new byte[8];
                for (int i = 0; i < 8; ++i) {
                    localPointer[i] = (byte)bis.read8Bit();
                }
                long index = toLong(localPointer);
                int next;
                ByteArray ba = new ByteArray();
                while ((next = bis.read8Bit()) >= 0) {
                    ba.concatenate((byte)next);
                }
                String name = new String(ba.getBytes(), "UTF-8");
                Archive file = new Archive(parentFolder.getPath() + "/" + name);
                file.setHeaderIndex(index);
                parentFolder.addFile(file);
            }
            else if (type == 0) { // Folder
                int next;
                ByteArray ba = new ByteArray();
                while ((next = bis.read8Bit()) >= 0) {
                    ba.concatenate((byte)next);
                }
                String name = new String(ba.getBytes(), "UTF-8");
                Folder folder = new Folder(name, parentFolder);
                readCompressedFileTree(bis, folder);
            }
            else throw new Exception("Invalid file type");
        }
    }

    private static long toLong(byte[] bytes) {
        long result = 0;
        result |= (long)(bytes[0]) << 56;
        result |= ((long)(bytes[1]) << 48) & 0x00FF000000000000L;
        result |= ((long)(bytes[2]) << 40) & 0x0000FF0000000000L;
        result |= ((long)(bytes[3]) << 32) & 0x000000FF00000000L;
        result |= ((long)(bytes[4]) << 24) & 0x00000000FF000000L;
        result |= ((long)(bytes[5]) << 16) & 0x0000000000FF0000L;
        result |= ((long)(bytes[6]) << 8) & 0x000000000000FF00L;
        result |= (long)(bytes[7]) & 0x00000000000000FFL;
        return result;
    }

    private static byte[] toArray(long value) {
        byte[] result = new byte[8];
        result[0] = (byte)((value >> 56) & 0x00000000000000FFL);
        result[1] = (byte)((value >> 48) & 0x00000000000000FFL);
        result[2] = (byte)((value >> 40) & 0x00000000000000FFL);
        result[3] = (byte)((value >> 32) & 0x00000000000000FFL);
        result[4] = (byte)((value >> 24) & 0x00000000000000FFL);
        result[5] = (byte)((value >> 16) & 0x00000000000000FFL);
        result[6] = (byte)((value >> 8) & 0x00000000000000FFL);
        result[7] = (byte)(value & 0x00000000000000FFL);
        return result;
    }

}