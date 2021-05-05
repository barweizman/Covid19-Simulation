// Guy Buky 208209817
// Bar Weizman 206492449

package Virus;

import Population.*;

// each virus must contain these methods
public interface IVirus {
    // calculates the contagion probability of the person based on the virus
    // each virus has their own algorithm for calculating the contagion probability
    double contagionProbability(Person p);

    // sick person p1 trying to contagion person p2
    // returns true if succeeded, else false
    boolean tryToContagion(Person p1, Person p2);

    // returns true if sick person has died from the virus, else false
    boolean tryToKill(Sick s);
}
