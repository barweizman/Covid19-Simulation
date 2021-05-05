// Guy Buky 208209817
// Bar Weizman 206492449

package Population;

import Country.Settlement;
import Location.Point;
import Virus.IVirus;

public class Sick extends Person{
    private long contagiousTime;
    private IVirus virus;

    // default ctor
    public Sick(){
        this.contagiousTime = 0;
    }

    // param ctor
    public Sick(int age, Point location, Settlement settlement, long contagiousTime, IVirus virus){
        super(age, location, settlement);
        this.contagiousTime = contagiousTime;
        this.virus = virus;
    }

    // copy ctor
    public Sick(Person p, long contagiousTime, IVirus virus){
        super(p);
        this.contagiousTime = contagiousTime;
        this.virus = virus;
    }

    // getters
    public IVirus getVirus() {
        return virus;
    }

    public long getContagiousTime() {
        return contagiousTime;
    }

    // setters
    public void setVirus(IVirus virus) {
        this.virus = virus;
    }

    // activates the tryToKill method of the virus
    // the method calculates if the person has died from the virus and returns true, else false
    public boolean tryToDie(){
        return this.virus.tryToKill(this);
    }

    // returns a health person by creating a new Healthy object based on this
    public Person recover(){
        return new Healthy(this);
    }

    // a sick person is always contaminated
    @Override
    public double contagionProbability() {
        return 1;
    }
}
