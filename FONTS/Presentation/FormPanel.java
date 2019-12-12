package Presentation;

import java.awt.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class FormPanel extends JPanel implements ActionListener, NavigationClickObserver {

    // JLabels
    private JLabel titleFormat, titleInputSize, titleOutputSize, titleTime, titleRatio, titleBps;  
    private JLabel inputSize, outputSize, time, bytesPerSecond, compressionRatio;
    
    private JButton displayButton;
    private DefaultComboBoxModel comboModelAlg, comboModel;
    private final JComboBox algorithmCombo, comboBox;
    private String filePath;

    public FormPanel() {

        setVisible(false);

        // Dimensions
        final Dimension dim = getPreferredSize();
        dim.width = 250;
        setPreferredSize(dim);

        // Titles
        titleFormat = new JLabel("Format: ");
        titleInputSize = new JLabel("Input size: ");
        titleInputSize.setEnabled(false); 
        titleOutputSize = new JLabel("Otuput size: "); 
        titleOutputSize.setEnabled(false);
        titleRatio = new JLabel("Ratio: ");
        titleRatio.setEnabled(false); 
        titleTime = new JLabel("Elapsed time: "); 
        titleTime.setEnabled(false);
        titleBps = new JLabel("Bps: ");
        titleBps.setEnabled(false);

        // Algorithms inicialization
        algorithmCombo = new JComboBox();
        comboModelAlg = new DefaultComboBoxModel();
        comboModelAlg.addElement("-");
        algorithmCombo.setModel(comboModelAlg);

        // Especificacions inicialization
        comboBox = new JComboBox();
        comboModel = new DefaultComboBoxModel();
        comboModel.addElement("-");
        comboBox.setModel(comboModel);

        // Visualization image Button
        displayButton = new JButton("Display");

        // Statics inicializations
        inputSize = new JLabel("-");
        inputSize.setEnabled(false);
        outputSize = new JLabel("-");
        outputSize.setEnabled(false);
        compressionRatio = new JLabel("-");
        compressionRatio.setEnabled(false);
        time = new JLabel("-");
        time.setEnabled(false);
        bytesPerSecond = new JLabel("-");
        bytesPerSecond.setEnabled(false);

        // Layout GridBag
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Compressor properties"));
        layoutComponents();

        // Listeners
        displayButton.addActionListener(this);
        algorithmCombo.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == displayButton) {
            try {
                JFrame frame = new JFrame();
                if(PresentationController.isFileImage(filePath)) {
                    frame.setTitle("Image Viewer");
                    frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                    frame.add(new ShowImage(filePath, frame));
                }
                else {
                    frame.setTitle("Text Viewer");
                    frame.setSize(600, 850);
                    frame.add(new ShowText(PresentationController.getDocument(filePath)));
                }
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(this, "Unselected File", "Error", 0);
            }
        }
        if(e.getSource() == algorithmCombo) {
            if (algorithmCombo.getItemCount() > 0) {
                try {
                    // enable if it wasn't
                    comboBox.setEnabled(true);  
                    titleFormat.setEnabled(true); 

                    String algorithm = algorithmCombo.getSelectedItem().toString();
                    comboModel.removeAllElements();
                    // Image parameters
                    if (PresentationController.isFileImage(filePath)) {
                        setListComboBox(comboBox, comboModel, PresentationController.getValidCompressionParameters(algorithm));
                        comboBox.setSelectedItem(PresentationController.getCompressionParameter(filePath)); // Default
                    }
                    // Document parameters
                    else {
                        setListComboBox(comboBox, comboModel, getDictBytesToHumanLegible(PresentationController.getValidCompressionParameters(algorithm)));
                        comboBox.setSelectedItem(getDictBytesToHumanLegible(PresentationController.getCompressionParameter(filePath))); // Default
                    }
                    // Disable if it hasn't parameters
                    if (comboBox.getItemCount() == 0) { 
                        comboModel.addElement("-");
                        comboBox.setModel(comboModel);
                        comboBox.setEnabled(false);
                        titleFormat.setEnabled(false);  
                    }     

                } catch (Exception exc) {
                    System.out.println(exc.getMessage());
                }
            }
        } 
    }
    
    public void SingleClick_File(String context) {
        setVisible(true);
        filePath = context;
        // Compression mode
        if(!PresentationController.isCompressed()) {
            try {
                // enable if it wasn't
                comboBox.setEnabled(true);
                titleFormat.setEnabled(true); 

                comboModelAlg.removeAllElements();
                comboModel.removeAllElements();
                setListComboBox(algorithmCombo, comboModelAlg, PresentationController.getValidCompressionTypes(context));
                String type = PresentationController.getCompressionType(filePath);
                if (PresentationController.isFileImage(context)) {
                    setListComboBox(comboBox, comboModel, PresentationController.getValidCompressionParameters(type));
                    comboBox.setSelectedItem(PresentationController.getCompressionParameter(filePath)); // Default
                }
                else {
                    setListComboBox(comboBox, comboModel, getDictBytesToHumanLegible(PresentationController.getValidCompressionParameters(type)));
                    comboBox.setSelectedItem(getDictBytesToHumanLegible(PresentationController.getCompressionParameter(filePath))); // Default
                }      
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } // Decompression mode
        else {

        }
    }

    public void SingleClick_Folder(String context) {
        setVisible(false);
    }  

    private void setListComboBox(JComboBox comboBox,DefaultComboBoxModel model, String[] list) {
        for(String value : list) 
            model.addElement(value);
        comboBox.setModel(model);
    }

    private void stats(String file, boolean compression) {
        try {
            long auxInputSize = PresentationController.getFileInputSizeStat(file);
            long auxOutputSize = PresentationController.getFileOutputSizeStat(file);
            long auxTime = PresentationController.getFileTimeStat(file);

            inputSize.setText(bytesToHumanLegible(auxInputSize, true));
            outputSize.setText(bytesToHumanLegible(auxOutputSize, true));
            time.setText(timeToHumanLegible(auxTime));
            bytesPerSecond.setText(bytesToHumanLegible((long)((double)(auxInputSize-auxOutputSize)/(double)auxTime), true));
            if(compression) compressionRatio.setText(String.format("%.2f", (double)auxInputSize/(double)auxOutputSize));
            else compressionRatio.setText(String.format("%.2f", (double)auxOutputSize/(double)auxInputSize));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String[] getDictBytesToHumanLegible(String[] dictSize) {
        String[] bytes = new String[dictSize.length];
        for (int i = 0; i < dictSize.length; ++i) {
            bytes[i] = bytesToHumanLegible((long)Math.pow(2,Long.parseLong(dictSize[i])), false);
        }
        return bytes;
    }

    private String getDictBytesToHumanLegible(String dictSize) {
        return bytesToHumanLegible((long)Math.pow(2,Long.parseLong(dictSize)), false);
    }

    private static String bytesToHumanLegible(long bytes, boolean decimals) {
        String format = "";
        if (decimals) format = "%.2f";
        else format = "%.0f";

        if (bytes >= 1073741824) 
            return String.format(format, bytes/1073741824.0) + " GB";
        
        else if (bytes >= 1048576)
            return String.format(format, bytes/1048576.0) + " MB";
        
        else if (bytes >= 1024) 
            return String.format(format, bytes/1024.0) + " KB";

        else return String.valueOf(bytes) + " B";
    }

    private static String timeToHumanLegible(long millis) {
        long hours = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        millis -= TimeUnit.MINUTES.toMillis(seconds);

        StringBuilder sb = new StringBuilder(64);
        if (hours > 0) { sb.append(hours); sb.append(" h "); }
        if (minutes > 0) { sb.append(minutes); sb.append(" min "); }
        if (seconds > 0) { sb.append(seconds); sb.append(" sec "); }
        if (millis > 0) { sb.append(millis); sb.append(" ms"); }

        return(sb.toString());
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        final GridBagConstraints bag = new GridBagConstraints();

        // First row algorithm
        bag.weightx = 1;
        bag.weighty = 0.1;

        bag.gridx = 0;
        bag.gridy = 0;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(new JLabel("Algorithm: "), bag);

        bag.gridx = 1;
        bag.gridy = 0;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(algorithmCombo, bag);

        // Second row format
        bag.weightx = 1;
        bag.weighty = 0.1;

        bag.gridx = 0;
        bag.gridy = 1;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(titleFormat, bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(comboBox, bag);

        // Third row visualization
        bag.weightx = 1;
        bag.weighty = 0.1;

        bag.gridx = 0;
        bag.gridy = 2;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(new JLabel("Visualization: "), bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(displayButton, bag);

        // Fourth row statistic
        bag.weightx = 1;
        bag.weighty = 0.1;
        bag.gridx = 0;
        bag.gridy = 3;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(new JLabel("Statistics: "), bag);

        // Fifth row input size
        bag.weightx = 1;
        bag.weighty = 0.1;

        bag.gridx = 0;
        bag.gridy = 4;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(titleInputSize, bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(inputSize, bag);

        // Sixth row output size
        bag.weightx = 1;
        bag.weighty = 0.1;

        bag.gridx = 0;
        bag.gridy = 5;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(titleOutputSize, bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(outputSize, bag);

        // Seven row compression ratio
        bag.weightx = 1;
        bag.weighty = 0.1;

        bag.gridx = 0;
        bag.gridy = 6;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(titleRatio, bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(compressionRatio, bag);

        // Eight row elapsed time
        bag.weightx = 1;
        bag.weighty = 0.1;

        bag.gridx = 0;
        bag.gridy = 7;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(titleTime, bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(time, bag);

        // Nineth row Compression Per Second
        bag.weightx = 1;
        bag.weighty = 0.1;

        bag.gridx = 0;
        bag.gridy = 8;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(titleBps, bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(bytesPerSecond, bag);
    }


}