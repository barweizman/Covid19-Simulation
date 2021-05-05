// Guy Buky 208209817
// Bar Weizman 206492449

package Virus;

import Population.*;
import Simulation.Clock;

import java.util.Random;

public class ChineseVariant implements IVirus {
    public double contagionProbability(Person p) {
        double coefficient = p.contagionProbability();
        int age = p.getAge();

        if (age <= 18) return coefficient * 0.2;
        if (age <= 55) return coefficient * 0.5;
        return coefficient * 0.7;
    }

    public boolean tryToContagion(Person p1, Person p2) {
        if (!(p2 instanceof Sick)){
            Sick sick = (Sick)p1;
            // if the sick person's contagious time is less than 5 days, we return false
            if (Clock.now() - sick.getContagiousTime() / Clock.getTicksPerDay() < 5){
                return false;
            }
            double probability = this.contagionProbability(p2);
            double distance = p1.getLocation().getDistance(p2.getLocation());
            double formula = Math.min(1, 0.14 * Math.exp(2 - 0.25 * distance));
            double random = new Random().nextDouble();

            if (random <= probability * formula){
                // if the person was infected, we create a new sick object
                // we remove the healthy person and add the new sick person
                Sick s = p2.contagion(Clock.now(), this);
                p2.getSettlement().removePerson(p2);
                p2.getSettlement().addPerson(s);
                return true;
            }
        }
        return false;
    }

    public boolean tryToKill(Sick s) {
        int age = s.getAge();
        double probability;

        if (age <= 18) probability = 0.01;
        else if (age <= 55) probability = 0.05;
        else probability = 0.1;

        double formula = Math.max(0, probability - 0.01 * probability * (Math.pow(s.getContagiousTime(), 2)));
        return new Random().nextDouble() < formula;
    }
}
