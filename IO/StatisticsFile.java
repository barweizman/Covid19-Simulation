package IO;

import Country.Settlement;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// class represents the statistics file actions such as
// export to csv
public class StatisticsFile {
    public static void exportFile(File f, List<Settlement> settlements) throws IOException {
    }

    // takes a settlement list and export it to a csv file chose by the user
    public static void exportToCSV(List<Settlement> settlementList) throws IOException {
        FileDialog fd = new FileDialog((Frame) null, "Please choose a file:", FileDialog.SAVE);
        fd.setFile("Stats.csv");
        fd.setVisible(true);

        if (fd.getFile() == null)
            return;
        File f = new File(fd.getDirectory(), fd.getFile());
        PrintWriter printWriter = new PrintWriter(f);
        printWriter.write("Settlement Name, Settlement Type, Ramzor Color, Sick %, Vaccines, Dead, Population\n");

        // write each settlement
        for (var s : settlementList){

            String value = s.getName() + ", " +
                    s.getClass().toString().substring(14) + ", " +
                    s.getRamzorColor().toString() + ", " +
                    s.getSickPercent() + ", " +
                    s.getNumOfVaccinations() + ", " +
                    s.getDeadPeopleAmount() + ", " +
                    s.getPeople().size() + "\n";
            printWriter.write(value);
        }
        printWriter.close();
    }
}
