package Presentation;

import java.awt.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormPanel extends JPanel implements ActionListener, NavigationClickObserver {

    // Dani
    /*
    private final JLabel TitTam, TitTamO, TitTemps, TitTamInTotal, TitTamOutTotal, TitTempsTotal;
    private JLabel TamField, TamOField, TempsField, TamInTotal, TamOutTotal, TempsTotal;
    private long MidaInput, MidaOutput, TempsFile, MidaInputTotal, MidaOutputTotal, TotalTime;
    private DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
    */
    private final JLabel titleDictSize, titleVisualization, titleStatistics;
    private JTextField statistics;
    private JButton displayButton;
    private JSpinner dictSize;
    private final JComboBox algorithmCombo;
    private String filePath;

    public FormPanel() {
        setVisible(false);
        // Inicializations Dani
        /*
        MidaInput = 0;
        TitTam = new JLabel("Tama\u00f1o ultimo archivo: ");
        TitTamO = new JLabel("Tama\u00f1o archivo output: ");
        TitTemps = new JLabel("Ultim temps: ");
        TitTamInTotal = new JLabel("Tama\u00f1o total Input: ");
        TitTamOutTotal = new JLabel("Tama\u00f1o total Output: ");
        TitTempsTotal = new JLabel("Temps total: ");
        TamField = new JLabel("");
        TamOField = new JLabel("");
        TempsField = new JLabel("");
        TamInTotal = new JLabel("");
        TamOutTotal = new JLabel("");
        TempsTotal = new JLabel("");
        String str = Long.toString(MidaInput); //????
        String[] algoritmes = PresentationController.getValidCompressionTypes();

        algorithmCombo = new JComboBox();
        
        for (int i = 0; i < algoritmes.length; ++i) {
            comboModel.addElement(algoritmes[i]);
        }
        algorithmCombo.setModel(comboModel);
        */

        // Dimensions
        final Dimension dim = getPreferredSize();
        dim.width = 250;
        setPreferredSize(dim);

        // Algorithm JComboBox
        algorithmCombo = new JComboBox();
        final DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        
        // ARREGLA ESTO, LA FIRMA DE LA FUNCION HA CAMBIADO!
        //String[] Ctypes = PresentationController.getValidCompressionTypes();
        //for (String alg : Ctypes) {
        //    comboModel.addElement(alg);
        //}
        algorithmCombo.setModel(comboModel);

        // Dictionary size JSpinner
        titleDictSize = new JLabel("Dictionary size: ");
        dictSize = new JSpinner(new SpinnerNumberModel(12, 0, 31, 1));
        dictSize.setPreferredSize(new Dimension(40, 20));

        // Visualization image Button
        titleVisualization = new JLabel("Visualization: ");
        displayButton = new JButton("Display");

        // Statistics compression values
        titleStatistics = new JLabel("Statistics: ");
        statistics = new JTextField("Soy las estadisticas");
        statistics.setEditable(false);
        statistics.setBorder(BorderFactory.createEmptyBorder());

        // Layout GridBag
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Compressor properties"));
        layoutComponents();

        displayButton.addActionListener(this);
    }

    public void layoutComponents() {
        setLayout(new GridBagLayout());
        final GridBagConstraints constr = new GridBagConstraints();

        // First Row
        constr.gridx = 0;
        constr.gridy = 0;
        constr.anchor = GridBagConstraints.LINE_END;
        constr.insets = new Insets(10, 0, 0, 5);
        add(new JLabel("Algorithm: "), constr);

        constr.gridx = 1;
        constr.gridy = 0;
        constr.anchor = GridBagConstraints.LINE_START;
        constr.insets = new Insets(10, 0, 0, 0);
        add(algorithmCombo, constr);

        // Second row
        constr.gridx = 0;
        constr.gridy = 1;
        constr.anchor = GridBagConstraints.LINE_END;
        constr.insets = new Insets(10, 0, 0, 5);
        add(titleDictSize, constr);

        constr.gridx = 1;
        constr.anchor = GridBagConstraints.LINE_START;
        constr.insets = new Insets(10, 0, 0, 0);
        add(dictSize, constr);

        // Third row
        constr.gridx = 0;
        constr.gridy = 2;
        constr.anchor = GridBagConstraints.LINE_END;
        constr.insets = new Insets(25, 5, 0, 0);
        add(titleVisualization, constr);

        // Fourth row
        constr.gridx = 1;
        constr.gridy = 3;
        constr.anchor = GridBagConstraints.LINE_START;
        constr.insets = new Insets(10, 0, 0, 0);
        add(displayButton, constr);

        // Fifth row
        constr.gridx = 0;
        constr.gridy = 4;
        constr.anchor = GridBagConstraints.LINE_START;
        constr.insets = new Insets(25, 5, 0, 0);
        ;
        add(titleStatistics, constr);

        // Sixth row
        constr.gridx = 1;
        constr.gridy = 5;
        constr.anchor = GridBagConstraints.LINE_END;
        constr.insets = new Insets(10, 0, 0, 0);
        add(statistics, constr);

        //////////////////////////////////////
        /*
            //TamañoUltimFileInput
            constr.gridx = 0;
            constr.gridy = 0;
            constr.fill = GridBagConstraints.NONE;
            constr.anchor = GridBagConstraints.LINE_END;
            constr.insets = new Insets(0,0,0,5);
            add(TitTam, constr);

            constr.gridx = 1;
            constr.gridy = 0;
            constr.anchor = GridBagConstraints.LINE_START;
            constr.insets = new Insets(0,0,0,0);
            add(TamField, constr);

            constr.gridy++;

            //TamañoUltimFileOutput
            constr.gridx = 0;

            constr.fill = GridBagConstraints.NONE;
            constr.anchor = GridBagConstraints.LINE_END;
            constr.insets = new Insets(0,0,0,5);
            add(TitTamO, constr);


            constr.gridx = 1;
            constr.anchor = GridBagConstraints.LINE_START;
            constr.insets = new Insets(0,0,0,0);
            add(TamOField, constr);

            constr.gridy++;

            //TamañoUltimTemps
            constr.gridx = 0;

            constr.fill = GridBagConstraints.NONE;
            constr.anchor = GridBagConstraints.LINE_END;
            constr.insets = new Insets(0,0,0,5);
            add(TitTemps, constr);

            constr.gridx = 1;
            constr.anchor = GridBagConstraints.LINE_START;
            constr.insets = new Insets(0,0,0,0);
            add(TempsField, constr);

            constr.gridy++;

             //TamañoInputTotal
            constr.gridx = 0;

            constr.fill = GridBagConstraints.NONE;
            constr.anchor = GridBagConstraints.LINE_END;
            constr.insets = new Insets(0,0,0,5);
            add(TitTamInTotal, constr);

            constr.gridx = 1;
            constr.anchor = GridBagConstraints.LINE_START;
            constr.insets = new Insets(0,0,0,0);
            add(TamInTotal, constr);

            constr.gridy++; 

            //TamañoOutputTotal
            constr.gridx = 0;

            constr.fill = GridBagConstraints.NONE;
            constr.anchor = GridBagConstraints.LINE_END;
            constr.insets = new Insets(0,0,0,5);
            add(TitTamOutTotal, constr);

            constr.gridx = 1;
            constr.anchor = GridBagConstraints.LINE_START;
            constr.insets = new Insets(0,0,0,0);
            add(TamOutTotal, constr);

            constr.gridy++;

            //TempsTotal
            constr.gridx = 0;

            constr.fill = GridBagConstraints.NONE;
            constr.anchor = GridBagConstraints.LINE_END;
            constr.insets = new Insets(0,0,0,5);
            add(TitTempsTotal, constr);

            constr.gridx = 1;
            constr.anchor = GridBagConstraints.LINE_START;
            constr.insets = new Insets(0,0,0,0);
            add(TempsTotal, constr);

            constr.gridy++; 
            */
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == displayButton) {
            try {
                ShowFrame v2 = new ShowFrame(filePath);
                v2.setVisible(true);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(this, "Unselected File", "Error", 0);
            }
        }
    }

    public void SingleClick_File(String context) {
        setVisible(true);
        filePath = context;
        /*
        try {
            if (PresentationController.isFileImage(context)) {
                comboModel.removeAllElements();
                comboModel.addElement("JPEG");
            }
            else {
                comboModel.removeElement("JPEG");
            }
            algorithmCombo.setModel(comboModel);
        }
        catch (Exception e) {
                System.out.println(e.getMessage());
        }
        stats(context);
        */
    }

    public void SingleClick_Folder(String context) {
        setVisible(false);
    }  

    /*
    public void stats(String file) {
        try {
            MidaInput = PresentationController.getFileInputSizeStat(file);
            MidaOutput = PresentationController.getFileOutputSizeStat(file);
            TempsFile = PresentationController.getFileTimeStat(file);
            MidaInputTotal = PresentationController.getTotalInputSizeStat();
            MidaOutputTotal = PresentationController.getTotalOutputSizeStat();
            TotalTime = PresentationController.getTotalTimeStat();
            TamField.setText(String.valueOf(MidaInput));
            TamOField.setText(String.valueOf(MidaOutput));
            TempsField.setText(String.valueOf(TempsFile));
            TamInTotal.setText(String.valueOf(MidaInputTotal));
            TamOutTotal.setText(String.valueOf(MidaOutputTotal));
            TempsTotal.setText(String.valueOf(TotalTime));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    */

}