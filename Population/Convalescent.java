// Guy Buky 208209817
// Bar Weizman 206492449

package Population;

import Virus.IVirus;

public class Convalescent extends Person{
    private static final double COEFFICIENT = 0.2;
    private IVirus virus;

    // param ctor
    public Convalescent(Person p, IVirus virus){
        super(p);
        this.virus = virus;
    }

    @Override
    public double contagionProbability() {
        return COEFFICIENT;
    }
}
