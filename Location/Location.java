// Guy Buky 208209817
// Bar Weizman 206492449

package Location;

public class Location {
    // each location has a point and a size
    private final Point position;
    private final Size size;

    // default ctor
    public Location(){
        this.position = new Point();
        this.size = new Size();
    }

    // param ctor
    public Location(Point p, Size s){
        this.position = p;
        this.size = s;
    }

    // getters
    public Size getSize() {
        return size;
    }

    public Point getPosition() {
        return position;
    }

    public int getPositionX() {
        return position.getX();
    }

    public int getPositionY(){
        return position.getY();
    }

    // equals overriding
    // location l1 == l2 if l1 and l2 has the same point and size
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Location)) return false;
        Location l = (Location)o;
        return l.position == this.position && l.size == this.size;
    }

    // toString
    @Override
    public String toString()
    {
        return "Location: " + this.position + "\n" + this.size;
    }
}
