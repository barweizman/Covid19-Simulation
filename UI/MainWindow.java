package UI;

import IO.SimulationFile;
import Simulation.Clock;
import Simulation.Main;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;


// the main window class represents the map and main menu, simulation speed slider and all features
public class MainWindow extends JFrame {
    private final JMenuBar menuBar;
    public static StatisticsWindow statisticsWindow;
    private static MapPanel mapPanel;
    private final SimulationSpeedSlider speedSlider;
    private final JMenu mf, ms, mh; // mf - sub menu file, ms - sub menu simulation, mh - sub menu help

    MainWindow() {
        super("Main Window");
        this.setLayout(new BorderLayout());
        // Menu Bar
        this.menuBar = new JMenuBar();
        mf = new SubMenuFile(this);
        ms = new SubMenuSimulation();
        mh = new SubMenuHelp();
        this.menuBar.add(ms);
        this.menuBar.add(mf);
        this.menuBar.add(mh);
        this.setJMenuBar(this.menuBar);
        // Speed Slider
        speedSlider = new SimulationSpeedSlider();
        this.add(speedSlider, BorderLayout.SOUTH);
        // packs everything together
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // This is a sub-menu: "File" - includes load,statistics,edit mutations and exit options
    private static class SubMenuFile extends JMenu {
        // load - for loading the file, opens a window for the user to choose -> SimulationFile class
        // statistics - opens the statistics window, only if there was a simulation charge (Modal)
        // editMutations - opens the edit mutations window (Modal)
        // exit - closes everything and stops the simulation
        private final JMenuItem load, statistics, editMutations, exit;
        private IsRunning isRunning = IsRunning.getInstance();

        // ctor
        SubMenuFile(JFrame frame) {
            super("File");
            load = new JMenuItem("Load");

            statistics = new JMenuItem("Statistics");
            // statistics.setEnabled(false);

            editMutations = new JMenuItem("Edit Mutations");

            exit = new JMenuItem("Exit");

            editMutations.addActionListener(e -> {
                editMutationsWindow();
            });

            exit.addActionListener(e -> {
                frame.dispose();
                System.exit(1);
            });

            load.addActionListener(e -> {
                // we can load the simulation only if the simulation is stopped/didnt start
                if (!isRunning.getIsRunningStatus()) {
                    isRunning.setIsRunningStatus(true);
                    SimulationFile.getInstance();

                    // we add the map panel only after we load the file
                    mapPanel = new MapPanel();
                    frame.add(mapPanel, BorderLayout.CENTER);
                    frame.pack();
                    frame.setVisible(true);
                    load.setEnabled(false);
                }
            });

            statistics.addActionListener(e -> {
                if (MainWindow.statisticsWindow == null)
                    MainWindow.statisticsWindow = new StatisticsWindow();
                else
                    MainWindow.statisticsWindow.setVisible(true);
            });

            this.add(load);
            this.add(statistics);
            this.add(editMutations);
            this.add(exit);
        }

        // creates a JDialog for the edit mutations window
        public void editMutationsWindow() {
            JFrame f = new JFrame();
            String[] column = getViruses();
            JPanel panel = new JPanel();
            Object[][] data = {{false, false, false}, {false, false, false}, {false, false, false}};
            MutationsTable model = new MutationsTable(data, column);
            JTable table = new JTable(model);
            table.setBounds(f.getBounds());
            table.setPreferredScrollableViewportSize(table.getPreferredSize());
            table.setFillsViewportHeight(true);
            panel.add(new RowedTableScroll(table, column));
            JDialog d = new JDialog(f, "Mutations Window", true);
            d.setBounds(panel.getBounds());
            model.addTableModelListener(e -> System.out.println("checked"));

            JButton submitBtn = new JButton("Submit");
            submitBtn.setBounds(80, 150, 80, 30);
            submitBtn.addActionListener(e1 -> {
                d.dispose();
            });
            d.add(panel);
            d.add(submitBtn, BorderLayout.SOUTH);
            d.pack();
            f.pack();
            d.setVisible(true);
        }

        // gets the list of viruses from the Virus package dynamically
        // so we can get a list of viruses every time we add or remove a virus
        public String[] getViruses() {
            java.util.List<String> viruses = new ArrayList<>();
            File file = new File(System.getProperty("user.dir") + "\\src\\Virus");
            File[] listOfFiles = file.listFiles();
            assert listOfFiles != null;
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if (!listOfFile.getName().equals("IVirus.java")) {
                        viruses.add(listOfFile.getName().substring(0, listOfFile.getName().length() - 5));
                    }
                }
            }
            return viruses.toArray(new String[0]);
        }
    }

    private static class SubMenuSimulation extends JMenu implements Runnable {
        // play - starts the simulation when simulation is paused
        // pause - pause the simulation when simulation is running
        // stop - stops completely the simulation unti the next simulation charge, can be done only after charging the file and as long we didnt stop
        // setTicksPerDay - opens a Dialog (Modal) , that gets an integer number from the user(using JSpinner) , and puts it in the new static variable in -> Clock class
        private final JMenuItem play, pause, stop, setTicksPerDay;
        private IsRunning isRunning = IsRunning.getInstance();
        private Thread thread = new Thread();

        // ctor
        SubMenuSimulation() {
            super("Simulation");
            play = new JMenuItem("Play");
            pause = new JMenuItem("Pause");
            stop = new JMenuItem("Stop");
            setTicksPerDay = new JMenuItem("Set Ticks Per Day");


            setTicksPerDay.addActionListener(e -> {
                JDialog setTicks = new JDialog((Dialog) null, "Set Ticks Per Day", true);
                final JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setSize(250, 100);
                SpinnerModel value = new SpinnerNumberModel(5, 0, 10000, 1);
                JSpinner spinner = new JSpinner(value);
                spinner.setBounds(100, 100, 50, 30);
                setTicks.add(spinner);
                setTicks.add(label);
                JButton submit = new JButton("Submit");
                setTicks.add(submit);
                submit.setBounds(80, 150, 80, 30);
                submit.addActionListener(e1 -> {
                    Clock.setTicksPerDay((int) spinner.getValue());
                    setTicks.dispose();
                });

                setTicks.setSize(300, 300);
                setTicks.setLayout(null);
                setTicks.setVisible(true);
                // spinner.addChangeListener(e1 -> label.setText("Value : " + ((JSpinner) e1.getSource()).getValue()));

            });

            play.addActionListener(e -> {
                if (!this.thread.isAlive()) {
                    this.thread = new Thread(this);
                    this.thread.start();
                } else {
                    Main.RunningSimulation.setIsThreadRunning(true);
                }
            });

            pause.addActionListener(e -> Main.RunningSimulation.setIsThreadRunning(false));

            stop.addActionListener(e -> this.thread.interrupt());


            this.add(play);
            this.add(pause);
            this.add(stop);
            this.add(setTicksPerDay);
        }

        // the function that runs in a different thread
        // while the thread is running, we constantly update the statistics window
        // and repaint the map
        @Override
        public void run() {
            try {
                while (true) {
                    if (!Main.RunningSimulation.getIsThreadRunning()) continue;
                    Main.Simulation();
                    TimeUnit.SECONDS.sleep(Clock.getTicksPerDay());
                    Clock.nextTick();
                    if (statisticsWindow != null){
                        statisticsWindow.setSettlementList(SimulationFile.getInstance().getPopulationList());
                        statisticsWindow.updateStatisticsWindow();
                    }
                    if (mapPanel != null){
                        mapPanel.repaint();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Thread Stopped.");
            }
            catch (ConcurrentModificationException ignored){
            }
        }

    }

    private static class SubMenuHelp extends JMenu {
        // help - opens a Dialog(NOT Modal), that provides guide for the user
        // about - opens a Dialog(NOT Modal), that includes our names&day of writing the assignment
        private final JMenuItem help, about;

        SubMenuHelp() {
            super("Help");
            help = new JMenuItem("Help");
            help.addActionListener(e -> {
                JDialog helpNotModal = new JDialog((Dialog) null, "Help", false);
                helpNotModal.setSize(700, 500);
                JTextArea txt = new JTextArea();
                txt.setEditable(false);
                helpNotModal.setLayout(new BorderLayout());
                txt.setText("""
                        The Covid-19 simulation is here to help you get information about the pandemic.
                        in the Menu Bar, you can choose the File -> so you can load/see statistics/edit mutations/exit the simulation
                        or choose Simulation -> in order to play/stop/resume/set ticks per day for the simulatio
                        or choose Help -> in order to get the creators name and the date of creation.
                        Below the Menu Bar, you will have the Map Panel : that`s where you get the information of different settlements:
                         how many sick people and healthy people, ramzor color, see the connections between the settlements and more.
                        The statistics window includes a few options:
                         add sick people, vaccinate people, filter the population and get it in an excel table and save the changes.
                        Remember that in any phase you can stop the simulation.
                        Enjoy!""");
                helpNotModal.add(txt);
                helpNotModal.setVisible(true);
            });
            about = new JMenuItem("About");
            about.setSize(400, 400);
            about.addActionListener(e -> {
                JDialog aboutNotModal = new JDialog((Dialog) null, "About", false);
                aboutNotModal.setSize(700, 500);
                JTextArea txt = new JTextArea();
                txt.setEditable(false);
                aboutNotModal.setLayout(new BorderLayout());
                txt.setText("Creators:\nGuy Buky\nBar Weizman\nDate of creation:\n29.04.2021");
                aboutNotModal.add(txt);
                aboutNotModal.setVisible(true);
            });

            this.add(help);
            this.add(about);
        }
    }

    private static class IsRunning {
        private static IsRunning obj;
        private static boolean IsRunningStatus = false;

        // getter
        public boolean getIsRunningStatus() {
            return IsRunningStatus;
        }

        // setter
        public void setIsRunningStatus(boolean value) {
            IsRunningStatus = value;
        }

        // singleton
        public static IsRunning getInstance() {
            if (obj == null) {
                obj = new IsRunning();
            }
            return obj;
        }
    }
}

