// Guy Buky 208209817
// Bar Weizman 206492449

package Population;

public class Vaccinated extends Person{
    private final long vaccinationTime;

    // default ctor
    public Vaccinated(){
        vaccinationTime = 0;
    }

    // copy ctor
    public Vaccinated(Person p, long vaccinationTime){
        super(p);
        this.vaccinationTime = vaccinationTime;
    }

    // calculates the contagion probability of a vaccinated person
    // vaccinated person can be sick
    @Override
    public double contagionProbability() {
        if (vaccinationTime < 21){
            return Math.min(1, (0.56 + 0.15 * Math.sqrt(21 - vaccinationTime)));
        }
        return Math.max(0.05, 1.05 / vaccinationTime - 14);
    }
}
