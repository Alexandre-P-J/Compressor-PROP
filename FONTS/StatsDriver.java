import java.io.*;
import Compressor.LZW;

public class StatsDriver {
    public static void main(String[] args) {
        int DICT_BIT_SIZE = 10;

        if (args.length != 3) {
            printUsage();
            System.exit(1);
        }

        try {
            InputStream is = new BufferedInputStream(new FileInputStream(args[1]));
            OutputStream os = new BufferedOutputStream(new FileOutputStream(args[2]));
            LZW fileW = new LZW();
            long startTime = System.currentTimeMillis();
            long endTime = startTime;
            if (args[0].equals("compress")) {
                fileW.compress(is,os,DICT_BIT_SIZE);
                endTime = System.currentTimeMillis();
            }
            else if (args[0].equals("decompress")) {
                fileW.decompress(is,os);
                endTime = System.currentTimeMillis();
            }
            else {
                printUsage();
            }
            is.close();
            os.close();
            File in = new File(args[1]);
            File out = new File(args[2]);
            long inS = in.length();
            long outS = out.length();
            if (args[0].equals("compress")) {
                printCompressStats(inS, outS, (double)(endTime-startTime)/1000.0);
            }
            else if (args[0].equals("decompress")) {
                printDecompressStats(inS, outS, (double)(endTime-startTime)/1000.0);
            }

        } catch (FileNotFoundException fnfe) {
            System.out.println(args[1] + " Not Found");
			System.exit(1);
        } catch (IOException ioe) {
			System.out.println("IO Error: " + ioe.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void printUsage() {
        System.out.println("Arguments: compress/decompress FromFile ToFile");
    }

    public static void printCompressStats(long S0, long S1, double time) {
        System.out.printf("Compression Ratio: %s (Bigger is better, lower than 1 is bad)\n", String.format("%.2f",(double)(S0)/(double)(S1)));
        System.out.printf("Space Savings: %s (Bigger is better)\n", String.format("%.2f", 1-((double)(S1)/(double)(S0))));
        System.out.printf("Read: %s\n", bytesToHumanLegible(S0));
        System.out.printf("Written: %s\n", bytesToHumanLegible(S1));
        System.out.printf("Elapsed Time: %s Seconds\n", String.format("%.4f", time));
        System.out.printf("Compression per Second: %s\n", bytesToHumanLegible((long)((double)(S0-S1)/(double)(time))));
    }
    public static void printDecompressStats(long S0, long S1, double time) {
        System.out.printf("Decompression Ratio: %s (Inverse of compression ratio, Lower is better)\n", String.format("%.2f",(double)(S0)/(double)(S1)));
        System.out.printf("Read: %s\n", bytesToHumanLegible(S0));
        System.out.printf("Written: %s\n", bytesToHumanLegible(S1));
        System.out.printf("Elapsed Time: %s Seconds\n", String.format("%.4f", time));
        System.out.printf("Decompression per Second: %s\n", bytesToHumanLegible((long)((double)(S1-S0)/(double)(time))));
    }

    public static String bytesToHumanLegible(long bytes) {
        if (bytes >= 1073741824) {
            return String.format("%.2f", bytes/1073741824.0) + " GB";
        }
        else if (bytes >= 1048576)
            return String.format("%.2f", bytes/1048576.0) + " MB";
        else if (bytes >= 1024)
            return String.format("%.2f", bytes/1024.0) + " KB";
        else
            return String.valueOf(bytes) + " Bytes";
    }
}
