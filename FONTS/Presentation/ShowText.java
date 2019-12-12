package Presentation;

import java.awt.*;

import javax.swing.*;

public class ShowText extends JPanel {

    public ShowText(String text) {
        JTextArea textArea = new JTextArea();
        textArea = new JTextArea();
        textArea.setFont(textArea.getFont().deriveFont(14.f));
        textArea.setEditable(false); 
        textArea.setFocusable(false);
        textArea.setLineWrap(false);
        textArea.setText(text);
        
        setLayout(new BorderLayout());
        JScrollPane sp = new JScrollPane(textArea);
        add(sp, BorderLayout.CENTER);
    }
}
