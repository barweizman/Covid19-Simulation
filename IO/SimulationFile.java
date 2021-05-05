// Guy Buky 208209817
// Bar Weizman 206492449

package IO;

import Country.*;
import Location.*;
import Location.Point;
import Population.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class SimulationFile {
    // the population list we read from the file
    private static SimulationFile obj;
    private List<Settlement> settlementList;
    private final double multi = 1.3;

    private SimulationFile() {
        this.settlementList = new ArrayList<>();
        try {
            // loading the file
            File file = loadFileFunc();
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {

                // splitting each line in the file
                // 0 - type, 1 - name, 2 - x, 3 - y, 4 - width, 5 - height, 6 - population
                var line = myReader.nextLine();

                if (line.startsWith("#")) {
                    continue;
                }

                String[] dataList = line.split(";");

                // based on the type of settlement, we create the appropriate object
                // each settlement is initialized with a green color
                switch (dataList[0]) { // the type of settlement
                    case "City" -> {
                        City c = new City(
                                null,
                                dataList[1],
                                new Location(
                                        new Point(Integer.parseInt(dataList[2]), Integer.parseInt(dataList[3])),
                                        new Size(Integer.parseInt(dataList[4]), Integer.parseInt(dataList[5]))
                                ),
                                RamzorColor.GREEN,
                                (int) (Integer.parseInt(dataList[6]) * multi)
                        );
                        // we create the population and setting it to the city
                        c.setPeople(CreatePopulation(c, Integer.parseInt(dataList[6])));
                        c.initPopulation();
                        this.settlementList.add(c);
                    }
                    case "Kibbutz" -> {
                        Kibbutz k = new Kibbutz(
                                null,
                                dataList[1],
                                new Location(
                                        new Point(Integer.parseInt(dataList[2]), Integer.parseInt(dataList[3])),
                                        new Size(Integer.parseInt(dataList[4]), Integer.parseInt(dataList[5]))
                                ),
                                RamzorColor.GREEN,
                                (int) (Integer.parseInt(dataList[6]) * multi)
                        );
                        // we create the population and setting it to the kibbutz
                        k.setPeople(CreatePopulation(k, Integer.parseInt(dataList[6])));
                        k.initPopulation();
                        this.settlementList.add(k);
                    }
                    case "Moshav" -> {
                        Moshav m = new Moshav(
                                null,
                                dataList[1],
                                new Location(
                                        new Point(Integer.parseInt(dataList[2]), Integer.parseInt(dataList[3])),
                                        new Size(Integer.parseInt(dataList[4]), Integer.parseInt(dataList[5]))
                                ),
                                RamzorColor.GREEN,
                                (int) (Integer.parseInt(dataList[6]) * multi)
                        );
                        // we create the population and setting it to the moshav
                        m.setPeople(CreatePopulation(m, Integer.parseInt(dataList[6])));
                        m.initPopulation();
                        this.settlementList.add(m);
                    }
                }
            }
            setConnections(file.getPath());

        } catch (FileNotFoundException ex) {
            System.out.println("FILE NOT FOUND!");
            System.exit(1);
        }
    }

    public static SimulationFile getInstance(){
        if (obj == null){
            obj = new SimulationFile();
        }
        return obj;
    }

    public static void dispose(){
        obj = null;
    }

    // getters
    public List<Settlement> getPopulationList() {
        return this.settlementList;
    }

    public Settlement[] getPopulationListArray() {
        return settlementList.toArray(new Settlement[0]);
    }

    // returns a population based on the size
    // we assign each person to the settlement s
    public List<Person> CreatePopulation(Settlement s, int size) {
        List<Person> personList = new ArrayList<>();
        Random random = new Random();
        Point point = s.getLocation().getPosition();
        int pointX = point.getX();
        int pointY = point.getY();
        Size sizeOfSettlement = s.getLocation().getSize();

        for (int i = 0; i < size; i++) {
            int x = (int) random.nextGaussian() * 6 + 9;
            int y = random.nextInt(4);
            int age = 5 * x + y; // the age formula

            // we initialize the population as healthy people
            personList.add(new Healthy(
                    age,
                    new Point(
                            random.nextInt(pointX + sizeOfSettlement.getWidth() - pointX) + pointX,
                            random.nextInt(pointY + sizeOfSettlement.getHeight() - pointY) + pointY
                    ),
                    s // assigning the person to the settlement
            ));
        }
        return personList;
    }

    private static File loadFileFunc() {
        // Instead of "(Frame) null" use a real frame, when GUI is learned
        FileDialog fd = new FileDialog((Frame) null, "Please choose a file:", FileDialog.LOAD);
        fd.setVisible(true);

        if (fd.getFile() == null)
            return null;
        File f = new File(fd.getDirectory(), fd.getFile());
        return f;
    }

    public static void copy(File fileSrc, File fileDst) {
        try (FileInputStream in = new FileInputStream(fileSrc)) {
            try (FileOutputStream out = new FileOutputStream(fileDst)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.printf("File %s not found : %s%n", fileSrc.getName(), e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setConnections(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            if (line.startsWith("#")) {
                line = line.substring(1);
                String[] arr = line.split(";");
                Settlement source = null, destination = null;
                for (var settlement : this.settlementList) {
                    if (settlement.getName().equals(arr[0])) {
                        source = settlement;
                    }
                    if (settlement.getName().equals(arr[1])) {
                        destination = settlement;
                    }
                }
                source.addConnection(destination);
            }
        }
    }
}
