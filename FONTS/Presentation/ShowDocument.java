package Presentation;

import java.awt.*;

import javax.swing.*;

public class ShowDocument extends JPanel {

    // font size for input file text
    private static final Font MONOSPACED_FONT = new Font("monospaced", Font.PLAIN, 11);

    private JTextArea inputFileArea;
    private String textFile;
    private JToolBar toolbar;

    public ShowDocument(String path) throws Exception {
        inputFileArea = new JTextArea();
        inputFileArea.setFont(MONOSPACED_FONT);
        inputFileArea.setEditable(false); 
        inputFileArea.setFocusable(false);
        inputFileArea.setLineWrap(false);
        // ToolBar
        toolbar = new JToolBar(JToolBar.VERTICAL);
        Dimension dim = getPreferredSize();
        dim.width = 25;
        toolbar.addSeparator(dim);
        try {
            textFile = PresentationController.getDocument(path);
            inputFileArea.setText(textFile);
        } catch (Exception e) {
            System.out.println("Unreachable Folder/File");
        }
        setLayout(new BorderLayout());
        add(new JScrollPane(inputFileArea), BorderLayout.CENTER);
        add(toolbar, BorderLayout.WEST);
    }
}
