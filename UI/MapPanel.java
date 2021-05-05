package UI;

import Country.Settlement;
import IO.SimulationFile;
import Location.Location;
import Location.Size;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// the class that represents the map
// here we create the rectangles for the settlements and its connections
// based on the settlement list from the population.txt file
public class MapPanel extends JPanel {
    private final List<JButton> settlementButtons;
    private final List<Settlement> settlementList;

    MapPanel() {
        this.settlementList = SimulationFile.getInstance().getPopulationList();
        this.settlementButtons = new ArrayList<>();
        initSettlementButtons();
    }

    @Override
    protected void paintComponent(Graphics gr) {
        // clears the last paint
        Graphics2D g = (Graphics2D)gr;
        super.paintComponent(g);

        // first we draw the connections, then we initialize the settlement buttons
        drawConnections(g);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void drawConnections(Graphics2D graphics2D) {
        for (var settlement : settlementList) {
            int x1 = settlement.getLocation().getPositionX();
            int y1 = settlement.getLocation().getPositionY();
            int w1 = settlement.getLocation().getSize().getWidth();
            int h1 = settlement.getLocation().getSize().getHeight();

            // initialize x1,x2,y1,y2 as middle settlements point
            // Middle position: x+1/2 width, y+1/2 height
            List<Settlement> attached = settlement.getAttachedSettlements();
            for (var a : attached) {
                int x2 = a.getLocation().getPositionX();
                int y2 = a.getLocation().getPositionY();
                int w2 = a.getLocation().getSize().getWidth();
                int h2 = a.getLocation().getSize().getHeight();

                // go on all settlements and paint them with its color and name -> settlements list
                // for each settlement we will create a rectangle, lets assume there are 10 settlements
                graphics2D.drawLine(
                        (int)(x1 + 0.5 * w1),
                        (int)(y1 + 0.5 * h1),
                        (int)(x2 + 0.5 * w2),
                        (int)(y2 + 0.5 * h2)
                );
            }
        }
    }

    public void initSettlementButtons(){
        for (var settlement : this.settlementList) {
            Location location = settlement.getLocation();
            Size size = location.getSize();
            Color color = settlement.getRamzorColor().getColor();
            String name = settlement.getName();
            int width = size.getWidth();
            int height = size.getHeight();
            int x = location.getPositionX();
            int y = location.getPositionY();

            // creating a new button based on the settlement's coordinates
            JButton settlementBtn = new JButton();
            settlementBtn.setBounds(new Rectangle(x, y, width, height));
            settlementBtn.setText(name);
            settlementBtn.setBackground(color);
            settlementBtn.setFocusPainted(false);

            settlementBtn.addActionListener(e -> {
                List<Settlement> lst = new ArrayList<>();
                lst.add(settlement);
                new StatisticsWindow();
            });

            // add button to the button list
            settlementButtons.add(settlementBtn);
            this.setLayout(null);
            this.add(settlementBtn);
        }
    }

    public List<Settlement> getSettlementList() {
        return settlementList;
    }

    // sets the preferred drawing size, when using pack() function
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

}
