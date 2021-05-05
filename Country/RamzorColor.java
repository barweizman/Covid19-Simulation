// Guy Buky 208209817
// Bar Weizman 206492449

package Country;

import java.awt.*;

// color enum representing the color of settlements
public enum RamzorColor {
    GREEN(0.4, 1.0, Color.GREEN),
    YELLOW(0.6, 0.8, Color.YELLOW),
    ORANGE(0.8, 0.6, Color.ORANGE),
    RED(1.0, 0.4, Color.RED);

    private final double coefficient;
    private final double transferCoefficient;
    private final Color color;
    RamzorColor(double coefficient, double transferCoefficient, Color color) {this.coefficient = coefficient; this.transferCoefficient = transferCoefficient ;this.color = color;}

    // getters
    public double getValue() {return this.coefficient;}
    public double getTransferCoefficient() {return this.transferCoefficient;}
    public Color getColor() {return this.color;}

    // methods
    public static RamzorColor getRamzorColor(double value){ // returns the color based on the coefficient value
        if (value <= 0.4) return RamzorColor.GREEN;
        if (value <= 0.6) return RamzorColor.YELLOW;
        if (value <= 0.8) return RamzorColor.ORANGE;
        return RamzorColor.RED;
    }
}


