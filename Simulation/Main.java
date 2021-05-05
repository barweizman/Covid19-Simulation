// Guy Buky 208209817
// Bar Weizman 206492449

package Simulation;

import Country.Map;
import Country.Settlement;
import IO.SimulationFile;
import Population.Healthy;
import Population.Person;
import Population.Sick;
import Population.Vaccinated;
import UI.MainWindow;
import UI.StatisticsWindow;
import Virus.BritishVariant;
import Virus.ChineseVariant;
import Virus.IVirus;
import Virus.SouthAfricanVariant;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    // the simulation process
    public static void Simulation() {
        Map map = new Map(SimulationFile.getInstance().getPopulationListArray());
        int amountOfPeopleToContagion = 3;
        Settlement[] settlementArr = map.getSettlements();

        for (var settlement : settlementArr) {
            var random = new Random();
            int count = 0; // counting how many people were contaminated
            List<Person> population = settlement.getPeople(); // the settlement's population

            // we contaminate 1% of the population
            contaminate(settlement, (int) (population.size() * 0.01));

            // creating a list with 20% of the sick people population
            // iterating over every sick person and try to contagion random people based on amountOfPeopleToContagion
            List<Sick> sickList = settlement.getSickPeople();
            List<Sick> randomSickPeople = new ArrayList<>();
            for (int i = 0; i < sickList.size() * 0.2; i++) {
                randomSickPeople.add(sickList.get(new Random().nextInt(sickList.size())));
            }
            List<Person> healthyPeople = settlement.getHealthyPeople();

            for (var sick : sickList) {
                for (int i = 0; i < amountOfPeopleToContagion; i++) {
                    Person person = healthyPeople.get(new Random().nextInt(healthyPeople.size()));
                    if (sick.getVirus().tryToContagion(sick, person)) { // if the person was infected
                        count++;
                    }
                }
            }
            // System.out.println(count + " people were contaminated in " + settlement.getName());

            // now we iterate over every sick person and check if it has been 25 days from his contagious time
            // if true -> we recover the sick person and add the healthy version of him to the list
            for (var sick : sickList) {
                if (Clock.now() - sick.getContagiousTime() / Clock.getTicksPerDay() >= 25) {
                    Healthy healthy = (Healthy) sick.recover();
                    settlement.getPeople().remove(sick);
                    settlement.getPeople().add(healthy);
                }
            }
        }

        // transfering 3% of the population to a random settlement
        for (int i = 0; i < settlementArr.length - 1; i++) {
            List<Person> people = settlementArr[i].getPeople();
            int size = (int) (people.size() * 0.03);

            for (int j = 0; j < size; j++) {
                boolean isSuccess = settlementArr[i].transferPerson(
                        people.get(j),
                        settlementArr[new Random().nextInt(settlementArr.length - 1)]);
            }
        }

        // vaccinating the healthy population in the settlements
        for (var settlement : settlementArr) {
            int i = 0;
            List<Person> healthyPeople = settlement.getHealthyPeople();
            while (settlement.getNumOfVaccinations() > 0) {
                if (i == healthyPeople.size()) break;
                Person person = healthyPeople.get(i);

                if (person instanceof Healthy) {
                    Vaccinated vaccinated = ((Healthy) person).vaccinate();
                    settlement.getPeople().remove(person);
                    settlement.getPeople().add(vaccinated);
                    settlement.setNumOfVaccinations(settlement.getNumOfVaccinations() - 1);
                    i++;
                }
            }
        }
    }

    public static void contaminate(Settlement s, int size) {
        List<Person> people = s.getPeople();

        for (int i = 0; i < size; i++) {
            // each person will be infected with a virus (virus chosen randomly)
            IVirus virus = switch (new Random().nextInt(3)) {
                case 0 -> new ChineseVariant();
                case 1 -> new SouthAfricanVariant();
                default -> new BritishVariant();
            };

            // after choosing a virus, we infect the person
            Sick sick = people.get(i).contagion(Clock.now(), virus);
            people.set(i, sick);
        }
        s.setPeople(people);
    }

    public static class RunningSimulation {
        private static AtomicBoolean isThreadRunning = new AtomicBoolean(true);

        public static void setIsThreadRunning(boolean flag) {
            isThreadRunning.set(flag);
        }

        public static boolean getIsThreadRunning() {
            return isThreadRunning.get();
        }
    }
}
