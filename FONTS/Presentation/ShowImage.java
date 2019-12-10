package Presentation;

import java.awt.*;

import javax.swing.*;

public class ShowImage extends JPanel {

    private byte[] byte_array;
    protected int width, height;

    public ShowImage(String path) {
        try {
            byte_array = PresentationController.getImage(path);
        } catch (Exception e) {
            System.out.println("Unreachable Folder/File");
        }
        setLayout(new BorderLayout());
    }

    // Paint image
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
    
        width = byte_array[3] & 0xFF | (byte_array[2] & 0xFF) << 8 | (byte_array[1] & 0xFF) << 16 | (byte_array[0] & 0xFF) << 24;
        height = byte_array[7] & 0xFF | (byte_array[6] & 0xFF) << 8 | (byte_array[5] & 0xFF) << 16 | (byte_array[4] & 0xFF) << 24;

        int i = 0;
        int j = 0;

        for (int x = 8; x < byte_array.length; x+=3) {
            if(i >= width) { i = 0; ++j; }

            int red = (int)(byte_array[x] & 0x0FF);
            int green = (int)(byte_array[x+1] & 0x0FF);
            int blue = (int)(byte_array[x+2] & 0x0FF);

            Color pixelColor = new Color (red,green,blue);
            
            g.setColor(pixelColor);
            g.drawLine(i+10, j+10, i+10, j+10); // added 10 for margin
            ++i; 
        }   
    }
}
