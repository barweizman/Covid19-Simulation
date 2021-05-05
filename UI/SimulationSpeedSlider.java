package UI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

// the simulation speed slider
// based on the slider we set the speed for each iteration of the simulation
public class SimulationSpeedSlider extends JPanel {
    SimulationSpeedSlider(){
        JLabel lbl = new JLabel("Simulation speed");
        this.add(lbl);
        this.add(new SpeedSlider());
    }

    private class SpeedSlider extends JSlider implements ChangeListener {
        static final int FPS_MIN = 0;
        static final int FPS_MAX = 100;
        static final int FPS_INIT = 15;    //initial frames per second

        SpeedSlider() {
            super(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, FPS_INIT);
            this.setPreferredSize(new Dimension(600, 50));
            //Turn on labels at major tick marks.
            this.setMajorTickSpacing(10);
            this.setMinorTickSpacing(1);
            this.setPaintTicks(true);
            this.setPaintLabels(true);
            this.addChangeListener(this);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            int value = this.getValue();
        }

    }

}
