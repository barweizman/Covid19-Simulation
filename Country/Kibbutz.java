// Guy Buky 208209817
// Bar Weizman 206492449

package Country;

import Location.Location;
import Population.Person;

import java.util.List;

public class Kibbutz extends Settlement{

    // param ctor
    public Kibbutz(List<Person> people, String name, Location location, RamzorColor color, int populationSize){
        super(people, name, location, color, populationSize);
    }

    @Override
    public RamzorColor calculateRamzorGrade(){
        double p = this.contagiousPercent();
        double c = this.getRamzorColorValue();

        double percentage = (0.45 + (Math.pow(Math.pow(1.5, c) * (p - 0.4),3)));
        RamzorColor color = RamzorColor.getRamzorColor(percentage);
        this.setRamzorColor(color);
        return color;
    }
}
