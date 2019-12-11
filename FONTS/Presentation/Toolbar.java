package Presentation;

import Presentation.PresentationController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Toolbar extends JPanel implements ActionListener {
    private final JButton fileButton;
    private final JButton compressButton;
    private final JButton StatsButton;
    private final JButton HelpButton;
    private final JFileChooser fileChooser;
    private final JFileChooser compressedSaveChooser;
    private final JFileChooser decompressedSaveChooser;
    private boolean compressed;

    public Toolbar() {
        setBorder(BorderFactory.createEtchedBorder());
        fileButton = new JButton("Open");
        compressButton = new JButton("Compress/Decompress");
        StatsButton = new JButton("Statistics");
        HelpButton = new JButton("Help");
        compressButton.setVisible(false);
        StatsButton.setVisible(false);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        compressedSaveChooser = new JFileChooser();
        compressedSaveChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        compressedSaveChooser.setDialogTitle("Save As");
        compressedSaveChooser.setApproveButtonText("Save");

        decompressedSaveChooser = new JFileChooser();
        decompressedSaveChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        decompressedSaveChooser.setDialogTitle("Save Into");
        decompressedSaveChooser.setApproveButtonText("Save");

        compressButton.addActionListener(this);
        fileButton.addActionListener(this);
        StatsButton.addActionListener(this);
        HelpButton.addActionListener(this);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(fileButton);
        add(compressButton);
        add(StatsButton);
        add(HelpButton);
    }

    public void actionPerformed(final ActionEvent e) {
        final JButton clicked = (JButton) e.getSource();

        if (clicked == fileButton) {
            if (fileChooser.showOpenDialog(Toolbar.this) == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                try {
                    compressed = PresentationController.readFileTree(f.getCanonicalPath());
                    if (compressed)
                        compressButton.setText("Decompress");
                    else {
                        compressButton.setText("Compress");
                    }
                    compressButton.setVisible(true);
                } catch (Exception exc) {
                    System.out.println(exc.getMessage());
                }
            }
        } else if (clicked == compressButton) {
            if (compressed) {
                if (decompressedSaveChooser.showOpenDialog(Toolbar.this) == JFileChooser.APPROVE_OPTION) {
                    File f = decompressedSaveChooser.getSelectedFile();
                    try {
                        PresentationController.decompressTo(f.getCanonicalPath());
                    } catch (Exception exc) {
                        System.out.println(exc.getMessage());
                    }
                }
            } else {
                if (compressedSaveChooser.showOpenDialog(Toolbar.this) == JFileChooser.APPROVE_OPTION) {
                    File f = compressedSaveChooser.getSelectedFile();
                    try {
                        PresentationController.compressTo(f.getCanonicalPath());
                    } catch (Exception exc) {
                        System.out.println(exc.getMessage());
                    }
                }
            }
        } else if (clicked == StatsButton) {

        } else if (clicked == HelpButton) {

        }
    }
}
