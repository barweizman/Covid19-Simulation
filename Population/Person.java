// Guy Buky 208209817
// Bar Weizman 206492449

package Population;

import Country.Settlement;
import Location.Point;
import Virus.IVirus;

public abstract class Person {
    private final int age;
    private final Point location;
    private Settlement settlement;

    // default ctor
    public Person() {
        this.age = 0;
        this.location = new Point();
    }

    // param ctor
    public Person(int age, Point location, Settlement settlement) {
        this.age = age;
        this.location = location;
        this.settlement = settlement;
    }

    // copy ctor
    public Person(Person p) {
        this.age = p.age;
        this.location = p.location;
        this.settlement = p.settlement;
    }

    // getters
    public Point getLocation() {
        return location;
    }

    public int getAge() {
        return age;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public int getIndexInSettlement(){
        return this.settlement.getPeople().indexOf(this);
    }

    // setters
    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

    public abstract double contagionProbability(); // coefficient

    // returns a Sick object based on this object
    // must assign a virus
    public Sick contagion(long contagiousTime, IVirus virus) {
        return new Sick(this, contagiousTime, virus);
    }
}

