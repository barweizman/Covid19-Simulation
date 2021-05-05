// Guy Buky 208209817
// Bar Weizman 206492449

package Country;

// the Map class
public class Map {
    // the settlements list
    private Settlement[] settlements;

    // default ctor
    public Map(){}

    // param ctor
    public Map(Settlement[] settlements){
        this.settlements = settlements;
    }

    // getters
    public Settlement[] getSettlements() {
        return settlements;
    }

    // setters
    public void setSettlements(Settlement[] settlements) {
        this.settlements = settlements;
    }
}
