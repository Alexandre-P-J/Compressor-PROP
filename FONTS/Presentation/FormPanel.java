package Presentation;

import java.awt.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class FormPanel extends JPanel implements ActionListener, NavigationClickObserver {

    private JLabel titleParameters;
    private JLabel in, out, time, ratio, bps;

    private DefaultComboBoxModel algorithmModel, parameterModel;
    private final JComboBox algorithmSelection, parameterSelection;
    private JButton displayButton;
    private String filePath;

    public FormPanel() {

        setVisible(false);
                
        // Dimensions
        final Dimension dim = getPreferredSize();
        dim.width = 250;
        setPreferredSize(dim);

        // Titles
        titleParameters = new JLabel("Parameters: ");
        in = new JLabel("-");
        out = new JLabel("-");
        time = new JLabel("-");
        ratio = new JLabel("-");
        bps = new JLabel("-");

        // Algorithm selection inicialization
        algorithmSelection = new JComboBox();
        algorithmModel = new DefaultComboBoxModel();
        algorithmSelection.setModel(algorithmModel);

        // Parameters selection inicialization
        parameterSelection = new JComboBox();
        parameterModel = new DefaultComboBoxModel();
        parameterSelection.setModel(parameterModel);

        // Visualization Button
        displayButton = new JButton("Dispaly");

        // Listeners
        algorithmSelection.addActionListener(this);
        parameterSelection.addActionListener(this);
        displayButton.addActionListener(this);

        // Layout GridBag
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Compressor properties"));
        layoutComponents();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == displayButton) {
            try {
                JFrame frame = new JFrame();
                if(PresentationController.isFileImage(filePath)) {
                    frame.setTitle("Image Viewer");
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
        if (e.getSource() == algorithmSelection) {
            if (algorithmSelection.getItemCount() > 0) {
                String algorithm = algorithmSelection.getSelectedItem().toString();
                try {
                    PresentationController.setCompressionType(filePath, algorithm);
                    refreshParameterSelection(filePath);
                } catch (Exception exc) {
                    System.out.println(exc.getMessage());
                }
            }                     
        }
        if (e.getSource() == parameterSelection) {
            if (parameterSelection.getItemCount() > 0) {
                String algorithm = algorithmSelection.getSelectedItem().toString();
                int indexParameter = parameterSelection.getSelectedIndex();
                try {
                    String[] parameterList = PresentationController.getValidCompressionParameters(algorithm);
                    PresentationController.setCompressionParameter(filePath, parameterList[indexParameter]);
                    refreshParameterSelection(filePath);
                } catch (Exception exc) {
                    System.out.println(exc.getMessage());
                }
            }
        }
    }

    @Override
    public void SingleClick_File(String path) {
        setVisible(true);

        filePath = path;

        refreshAlgortihmSelection(path);
        refreshParameterSelection(path);

        if (PresentationController.isCompressed()) {
            stats(path, true);
            // View mode
            algorithmSelection.setEditable(false);
            parameterSelection.setEditable(false);
        }
        else stats(path, false);
    }

    @Override
    public void SingleClick_Folder(String path) {
        setVisible(false);

    }

    private void refreshAlgortihmSelection(String path) {
        algorithmModel.removeAllElements();
        try {
            setSelection(algorithmSelection, algorithmModel, PresentationController.getValidCompressionTypes(path));
            algorithmSelection.setSelectedItem(PresentationController.getCompressionType(path));
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }

    private void refreshParameterSelection(String path) {
        parameterModel.removeAllElements();
        try {
            String algorithm = PresentationController.getCompressionType(path);
            // Image
            if (PresentationController.isFileImage(path)) {
                setSelection(parameterSelection, parameterModel, PresentationController.getValidCompressionParameters(algorithm));
                parameterSelection.setSelectedItem(PresentationController.getCompressionParameter(path));
            }
            // TextFile
            else {
                setSelection(parameterSelection, parameterModel, getDictBytesToHumanLegible(PresentationController.getValidCompressionParameters(algorithm)));
                parameterSelection.setSelectedItem(getDictBytesToHumanLegible(PresentationController.getCompressionParameter(path)));
            }
            // Disable if it hasn't parameters
            if (parameterSelection.getItemCount() == 0) { 
                parameterModel.addElement("-");
                parameterSelection.setModel(parameterModel);
                parameterSelection.setEnabled(false);
                titleParameters.setEnabled(false);  
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }

    private void setSelection(JComboBox comboBox,DefaultComboBoxModel model, String[] list) {
        for(String value : list) 
            model.addElement(value);
        comboBox.setModel(model);
    }

    private String[] getDictBytesToHumanLegible(String[] exponents) {
        String[] bytes = new String[exponents.length];
        for (int i = 0; i < exponents.length; ++i) 
            bytes[i] = bytesToHumanLegible((long)Math.pow(2,Long.parseLong(exponents[i])), false);
        return bytes;
    }

    private String getDictBytesToHumanLegible(String exponent) {
        return bytesToHumanLegible((long)Math.pow(2,Long.parseLong(exponent)), false);
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

    private void stats(String file, boolean isCompressed) {
        try {
            long auxIn = PresentationController.getFileInputSizeStat(file);
            long auxOut = PresentationController.getFileOutputSizeStat(file);
            long auxTime = PresentationController.getFileTimeStat(file);

            // Hasn't started
            if(auxIn == 0 && auxOut == 0 && auxTime == 0) {
                in.setText("-");
                out.setText("-");
                time.setText("-");
                bps.setText("-");
                ratio.setText("-");
            }
            else {
                in.setText(bytesToHumanLegible(auxIn, true));
                out.setText(bytesToHumanLegible(auxOut, true));
                time.setText(milisToHumanLegible(auxTime));
    
                if(!isCompressed) {
                    bps.setText(bytesToHumanLegible((long)((double)(auxIn-auxOut)/((double)(auxTime)/1000.0)), true));
                    ratio.setText(String.format("%.2f", (double)auxIn/(double)auxOut));
                }
                else {
                    bps.setText(bytesToHumanLegible((long)((double)(auxOut-auxIn)/((double)(auxTime)/1000.0)), true));
                    ratio.setText(String.format("%.2f", (double)auxOut/(double)auxIn));
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String milisToHumanLegible(long ms) {
        if (ms >= 3600000) {
            return String.format("%.2f", ms/3600000.0) + " h";
        }
        else if (ms >= 60000)
            return String.format("%.2f", ms/60000.0) + " min";
        else if (ms >= 1000)
            return String.format("%.2f", ms/1000.0) + " sec";
        else
            return String.valueOf(ms) + " ms";
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
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(algorithmSelection, bag);

        // Second row parameter
        bag.gridx = 0;
        bag.gridy = 1;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(titleParameters, bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(parameterSelection, bag);

        // Third row visualization
        bag.gridx = 0;
        bag.gridy = 2;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(new JLabel("Visualization: "), bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(displayButton, bag);

        // Fourth row individual statistic
        bag.gridx = 0;
        bag.gridy = 3;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(new JLabel("Statistics: "), bag);

        // Fifth row input size
        bag.gridx = 0;
        bag.gridy = 4;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(new JLabel("Read: "), bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(in, bag);

        // Sixth row output size
        bag.gridx = 0;
        bag.gridy = 5;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(new JLabel("Written: "), bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(out, bag);

        // Seven row ratio
        bag.gridx = 0;
        bag.gridy = 6;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(new JLabel("Ratio: "), bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(ratio, bag);

        // Eight row elapsed time
        bag.gridx = 0;
        bag.gridy = 7;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(new JLabel("Elapsed Time: "), bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(time, bag);

        // Nineth row Compression Per Second
        bag.gridx = 0;
        bag.gridy = 8;
        bag.anchor = GridBagConstraints.LINE_END;
        bag.insets = new Insets(0, 0, 0, 5);
        add(new JLabel("Bps: "), bag);

        bag.gridx = 1;
        bag.anchor = GridBagConstraints.LINE_START;
        bag.insets = new Insets(0, 0, 0, 0);
        add(bps, bag);
    } 
}