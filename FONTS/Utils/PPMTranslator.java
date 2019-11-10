package Utils;

import java.io.*;
import Utils.Color;

public class PPMTranslator {
    PPMType encoding;
    InputStream is = null;
    OutputStream os = null;
    int height, width, maxValue;

    public PPMTranslator(InputStream is) throws IOException {
        this.is = is;
        extractHeader();
    }

    public PPMTranslator(OutputStream os, int width, int height) throws IOException {
        this.os = os;
        this.width = width;
        this.height = height;
        maxValue = 255;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNextComponent() throws IOException {
        assert (is != null);
        int num = 0;
        int next;
        if (encoding == PPMType.ASCII) {
            char c;
            while ((next = is.read()) >= 0) {
                c = (char) next;
                if (c == '\n')
                    return num;
                num = num * 10 + Character.getNumericValue(c);
            }
        } else if ((next = is.read()) >= 0)
            return next;
        return -1;
    }

    public Color getNextColor() throws IOException {
        int r, g, b;
        if ((r = getNextComponent()) < 0)
            return null;
        if ((g = getNextComponent()) < 0)
            throw new IOException("Wrong ppm codification! - Incomplete RGB color");
        if ((b = getNextComponent()) < 0)
            throw new IOException("Wrong ppm codification! - Incomplete RGB color");
        float pixelDepthFactor = (float) (255.0 / (float) maxValue);
        r = (int)Math.min(r * pixelDepthFactor, 255);
        g = (int)Math.min(g * pixelDepthFactor, 255);
        b = (int)Math.min(b * pixelDepthFactor, 255);
        Color c = new Color();
        c.RGB(r, g, b);
        return c;
    }

    public void setNextComponent(int num) throws IOException {
        if (encoding == PPMType.ASCII) {
            String str = String.valueOf(num) + '\n';
            byte[] b = str.getBytes();
            os.write(b);
        } else
            os.write(num);
    }

    public void setNextColor(Color color) throws IOException {
        setNextComponent(color.r);
        setNextComponent(color.g);
        setNextComponent(color.b);
    }

    private void extractHeader() throws IOException {
        int next;
        if ((next = is.read()) < 0)
            throw new IOException("Wrong ppm codification! - Empty file"); // 'P'
        if ((next = is.read()) < 0)
            throw new IOException("Wrong ppm codification! - File too short"); // '3' or '6'
        char c = (char) next;
        if (c == '3')
            encoding = PPMType.ASCII;
        else if (c == '6')
            encoding = PPMType.Raw;
        else
            throw new IOException("Wrong ppm codification! - Failed reading ascii/binary flag");

        if ((width = readHeaderNum()) < 0)
            throw new IOException("Wrong ppm codification! - Failed reading width");
        if ((height = readHeaderNum()) < 0)
            throw new IOException("Wrong ppm codification! - Failed reading height");
        if ((maxValue = readHeaderNum()) < 0)
            throw new IOException("Wrong ppm codification! - Failed reading max value");
    }

    private int readHeaderNum() throws IOException {
        int next;
        int num = 0;
        boolean numberRead = false;
        while ((next = is.read()) >= 0) {
            char c = (char) next;
            if ((c == '\n' || c == ' ') && numberRead)
                return num; // reached end of number
            if (c == '\n' || c == ' ')
                continue; // avoid excesive '\n'
            if (c == '#') { // avoid comments
                if (numberRead)
                    return num; // number contains comment w/o space, return current num
                while ((next = is.read()) >= 0) {
                    c = (char) next;
                    if (c == '\n')
                        break; // comment finish
                }
                if (c != '\n')
                    return -1; // no number, just EOF
                continue;
            }
            num = num * 10;
            num = num + Character.getNumericValue(c);
            numberRead = true;
        }
        if (numberRead)
            return num; // reached end of number at end of file
        return -1; // no number, just EOF
    }

    public void writeHeader() throws IOException {
        this.encoding = PPMType.Raw;
        String type;
        if (encoding == PPMType.ASCII)
            type = "P3";
        else
            type = "P6";
        String str = type + '\n' + String.valueOf(width) + ' ' + String.valueOf(height) + '\n' + String.valueOf(255)
                + '\n';
        byte[] header = str.getBytes();
        os.write(header);
    }

}

enum PPMType {
    ASCII, Raw
}