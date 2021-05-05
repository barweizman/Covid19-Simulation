// Guy Buky 208209817
// Bar Weizman 206492449

package Country;

import Location.Location;
import Population.Person;

import java.util.List;

public class City extends Settlement{

    // param ctor
    public City(List<Person> people, String name, Location location, RamzorColor color,int populationSize){
        super(people, name, location, color, populationSize);
    }

    @Override
    public RamzorColor calculateRamzorGrade(){
        double p = this.contagiousPercent();
        double c = this.getRamzorColorValue();
        double percentage = (0.2 * Math.pow(4, 1.25 * p));

        RamzorColor color = RamzorColor.getRamzorColor(percentage);
        this.setRamzorColor(color);
        return color;
    }
}
