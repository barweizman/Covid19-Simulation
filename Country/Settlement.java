// Guy Buky 208209817
// Bar Weizman 206492449

package Country;

import Location.*;
import Population.Person;
import Population.Sick;


import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

// class that represents a settlement
public abstract class Settlement {
    private final String name;
    private final Location location;
    private List<Person> people;
    private List<Person> healthyPeople;
    private List<Sick> sickPeople;
    private RamzorColor ramzorColor;
    private List<Settlement> attachedSettlements;
    private int populationSize;
    private int numOfVaccinations;

    // default ctor
    public Settlement() {
        this.name = "";
        this.location = new Location();
        this.people = new ArrayList<>();
        this.healthyPeople = new ArrayList<>();
        this.sickPeople = new ArrayList<>();
        this.numOfVaccinations = 0;
    }

    // param ctor
    public Settlement(List<Person> people, String name, Location location, RamzorColor color, int populationSize) {
        this.people = people;
        this.healthyPeople = new ArrayList<>();
        this.sickPeople = new ArrayList<>();
        this.name = name;
        this.location = location;
        this.ramzorColor = color;
        this.populationSize = populationSize;
        this.attachedSettlements = new ArrayList<>();
    }

    // getters
    public Location getLocation() {
        return location;
    }

    public RamzorColor getRamzorColor() {
        return ramzorColor;
    }

    public float getSickPercent(){
        return (float)this.sickPeople.size() / this.people.size();
    }

    public int getDeadPeopleAmount(){
        int count = 0;
        try {
            for (Sick sick : this.sickPeople) {
                if (sick.tryToDie()) count++;
            }
        }
        catch (ConcurrentModificationException ignored){

        }
        return count;
    }

    public int getNumOfVaccinations() {
        return numOfVaccinations;
    }

    public List<Sick> getSickPeople() {
        this.sickPeople = new ArrayList<>();
        for (var person : this.people) {
            if (person instanceof Sick)
                this.sickPeople.add((Sick) person);
        }
        return this.sickPeople;
    }

    public List<Person> getHealthyPeople() {
        this.healthyPeople = new ArrayList<>();
        for (var person : this.people) {
            if (!(person instanceof Sick))
                this.healthyPeople.add(person);
        }
        return this.healthyPeople;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public List<Settlement> getAttachedSettlements() {
        return attachedSettlements;
    }

    public String getName() {
        return name;
    }

    public double getRamzorColorValue() {
        return ramzorColor.getValue();
    }

    public List<Person> getPeople() {
        return this.people;
    }

    public void initPopulation() {
        for (var person : this.people) {
            if (!(person instanceof Sick))
                this.healthyPeople.add(person);
            else
                this.sickPeople.add((Sick) person);
        }
    }

    // setters
    public void setPersonInPopulation(int index, Person p) {
        this.people.set(index, p);
    }

    public void setNumOfVaccinations(int numOfVaccinations) {
        this.numOfVaccinations = numOfVaccinations;
    }

    public void setRamzorColor(RamzorColor ramzorColor) {
        this.ramzorColor = ramzorColor;
    }

    public void setPeopleInIndex(int index, Person p){
        this.people.set(index, p);
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    // calculates the color of the settlement
    // each settlement calculates the color based on different equations
    // City, Kibbutz, Moshav
    public RamzorColor calculateRamzorGrade() {
        return RamzorColor.GREEN;
    }

    // returns the number of sick people divided by the population size
    public double contagiousPercent() {
        int count = 0;
        for (var p : people) {
            if (p instanceof Sick) {
                count += 1;
            }
        }
        return (double) count / people.size();
    }

    // returns a random Point object
    public Point randomLocation() {
        Random rand = new Random();
        int x = rand.nextInt(this.location.getPositionX());
        int y = rand.nextInt(this.location.getPositionY());
        return new Point(x, y);
    }

    // adds a Person object to the population list
    public boolean addPerson(Person p) {
        this.people.add(p);
        return true;
    }

    // removes a Person from the population list
    public boolean removePerson(Person p) {
        this.people.remove(p);
        return true;
    }

    // adds a settlment to the attached settlements list
    public void addConnection(Settlement destination) {
        this.attachedSettlements.add(destination);
    }

    // transfers the person to given settlement (always true for this assignment)
    public boolean transferPerson(Person p, Settlement s) {
        int size = s.people.size();
        if (size == s.getPopulationSize()) return false;
        this.people.remove(p);
        s.people.add(p);
        p.setSettlement(s);
        return true;
    }

    //toString
    @Override
    public String toString() {
        return "Population: " + this.people.size() + "\nName: " + this.name + "\n" + this.location
                + "\nColor: " + this.ramzorColor.toString();
    }


}
