package UI;

import Country.RamzorColor;
import Country.Settlement;
import IO.SimulationFile;
import IO.StatisticsFile;
import Population.Person;
import Population.Sick;
import Simulation.Clock;
import Virus.BritishVariant;
import Virus.ChineseVariant;
import Virus.IVirus;
import Virus.SouthAfricanVariant;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


// the class that represents the statistics window
// here we create the table that is constantly updated based on the new data from the simulation
public class StatisticsWindow extends JFrame {
    private String currSettlementName;
    private static StatisticsWindow obj;
    private final JPanel filterPanel = new JPanel();
    private static List<Settlement> settlementList = SimulationFile.getInstance().getPopulationList();
    private final JTable statsTable = new JTable();
    private final JPanel buttonPanel = new JPanel();
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    StatisticsWindow() {
        super("Statistics Windows");
        this.currSettlementName = "";
        this.model = new DefaultTableModel();

        // we initialize each component. the filter panel, statistics table, and button panel
        initFilterPanel();
        initStatsTable(settlementList);
        initButtonPanel();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(filterPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(this.statsTable), BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);

        // after we create the statistics window we constantly updating it
        updateStatisticsWindow();
    }

    public void setSettlementList(List<Settlement> s) {
        settlementList = s;
    }

    public void initFilterPanel(){
        String[] filterList = new String[] {"Select Filter..", "Settlement Name", "Settlement Type", "Ramzor Color"};
        JComboBox<String> colSelectCombo = new JComboBox<>(filterList);
        JTextField filterRegex = new JTextField();
        filterRegex.setToolTipText("Enter regex");
        filterRegex.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                int selectedCol = colSelectCombo.getSelectedIndex();
                try{
                    if(selectedCol != 0){
                        sorter.setRowFilter(RowFilter.regexFilter(filterRegex.getText(), selectedCol-1));
                    }
                }
                catch(java.util.regex.PatternSyntaxException ex){
                    // if the user`s entered a regex expression that doesn`t parse, don`t update
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                int selectedCol = colSelectCombo.getSelectedIndex();
                try{
                    if(selectedCol != 0){
                        sorter.setRowFilter(RowFilter.regexFilter(filterRegex.getText(), selectedCol));
                    }
                }
                catch(java.util.regex.PatternSyntaxException ex){
                    // if the user`s entered a regex expression that doesn`t parse, don`t update
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                int selectedCol = colSelectCombo.getSelectedIndex();
                try{
                    if(selectedCol != 0){
                        sorter.setRowFilter(RowFilter.regexFilter(filterRegex.getText(), selectedCol));
                    }
                }
                catch(java.util.regex.PatternSyntaxException ex){
                    // if the user`s entered a regex expression that doesn`t parse, don`t update
                }
            }
        });

        this.filterPanel.setLayout(new GridLayout(0, 2));
        this.filterPanel.add(colSelectCombo);
        this.filterPanel.add(filterRegex);
    }

    public void initStatsTable(List<Settlement> lst) {
        String[] columns = new String[] {"Settlement Type", "Ramzor Color", "Sick %", "Vaccines", "Dead", "Population"};
        this.model = new DefaultTableModel();
        this.model.addColumn("Settlement Name");
        for (String column: columns){
            this.model.addColumn(column);
        }

        for (var settlement: lst){
            this.model.addRow(new String[] {
                    settlement.getName(),
                    settlement.getClass().toString().substring(14),
                    settlement.getRamzorColor().toString(),
                    String.valueOf(settlement.getSickPercent()),
                    String.valueOf(settlement.getNumOfVaccinations()),
                    String.valueOf(settlement.getDeadPeopleAmount()),
                    String.valueOf(settlement.getPeople().size())
            });
        }

        this.statsTable.getSelectionModel().addListSelectionListener(e -> {
            int row = this.statsTable.getSelectedRow();
            this.currSettlementName = this.statsTable.getValueAt(row != -1 ? row : 0, 0).toString();
        });

        this.statsTable.setFillsViewportHeight(true);
        //this.statsTable.setAutoCreateRowSorter(true); // meyotar?
        this.statsTable.setRowSorter(sorter = new TableRowSorter<DefaultTableModel>(model));
        this.statsTable.setModel(this.model);
    }

    public void initButtonPanel() {
        JButton save = new JButton("Save");
        JButton addSick = new JButton("Add Sick");
        JButton vaccinate = new JButton("Vaccinate");
        save.setFocusPainted(false);
        addSick.setFocusPainted(false);
        vaccinate.setFocusPainted(false);

        save.addActionListener(e -> saveEvent());
        addSick.addActionListener(e -> addSickEvent());
        vaccinate.addActionListener(e -> vaccinateEvent());

        this.buttonPanel.setLayout(new GridLayout(0, 3));
        this.buttonPanel.add(save);
        this.buttonPanel.add(addSick);
        this.buttonPanel.add(vaccinate);
    }

    // returns the settlement that was selected from the table
    public Settlement getSettlementByCurrentSelection(){
        Settlement settlement = null;

        for (var s : settlementList){
            if (s.getName().equals(this.currSettlementName))
                settlement = s;
        }
        return settlement;
    }

    // events
    public void addSickEvent(){
        Settlement settlement = getSettlementByCurrentSelection();
        if (settlement == null) return;
        List<Person> healthyPeople = settlement.getHealthyPeople();
        for (int i = 0; i < healthyPeople.size() * 0.001; i++){
            IVirus virus = switch (new Random().nextInt(2)) {
                case 0 -> new BritishVariant();
                case 1 -> new ChineseVariant();
                case 2 -> new SouthAfricanVariant();
                default -> new BritishVariant();
            };
            Sick sick = healthyPeople.get(i).contagion(Clock.now(), virus);
            settlement.getPeople().remove(healthyPeople.get(i));
            settlement.getPeople().add(sick);
            System.out.println(settlement.getSickPeople().size());
        }
    }

    public void vaccinateEvent(){
        JDialog vaccinateDialog = new JDialog((Dialog) null, "Vaccinate Population", true);
        SpinnerModel value = new SpinnerNumberModel(5, 0, 10000, 1);
        JSpinner spinner = new JSpinner(value);
        spinner.setBounds(100, 100, 50, 30);
        vaccinateDialog.add(spinner);
        JButton submit = new JButton("Submit");
        vaccinateDialog.add(submit);
        submit.setBounds(80, 150, 80, 30);

        submit.addActionListener(e -> {
            Settlement s = getSettlementByCurrentSelection();
            s.setNumOfVaccinations((int)value.getValue());
            vaccinateDialog.dispose();
        });

        vaccinateDialog.setSize(300, 300);
        vaccinateDialog.setLayout(null);
        vaccinateDialog.setVisible(true);
    }

    public void saveEvent(){
        try {
            StatisticsFile.exportToCSV(this.settlementList);
        }
        catch (IOException e) {
            System.out.println("ERROR SAVING FILE!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void updateStatisticsWindow(){
        SwingUtilities.invokeLater(() -> initStatsTable(settlementList));
    }
}
