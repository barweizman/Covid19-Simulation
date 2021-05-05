// Guy Buky 208209817
// Bar Weizman 206492449

package Population;

import Country.Settlement;
import Location.Point;
import Simulation.Clock;

public class Healthy extends Person{
    private static final int COEFFICIENT = 1;

    // default ctor
    public Healthy(){ }

    // param ctor
    public Healthy(int age, Point location, Settlement settlement){
        super(age, location, settlement);
    }

    // copy ctor
    public Healthy(Person p){
        super(p);
    }

    // returns a Vaccinated object based on this object
    public Vaccinated vaccinate(){
        return new Vaccinated(this, Clock.now());
    }

    @Override
    public double contagionProbability() {
        return COEFFICIENT;
    }
}
