package Presentation;

import java.awt.BorderLayout;

import javax.swing.*;

public class TextPanel extends JPanel{
    private JTextArea textArea;

    public TextPanel() {
        textArea = new JTextArea());
        setLayout(new BorderLayout());
        add(new JScrollPane(textArea), BorderLayout.CENTER);

    }

    public void appendText (String text) {
        textArea.append(text);
    }
    public void limpiarText () {
        textArea.setText("");
    }

}