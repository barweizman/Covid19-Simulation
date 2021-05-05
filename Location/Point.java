// Guy Buky 208209817
// Bar Weizman 206492449

package Location;

public class Point {
    // each point has an x and y coordinates
    private final int x;
    private final int y;

    // default ctor
    public Point(){
        this.x = 0;
        this.y = 0;
    }

    // param ctor
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    // getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getDistance(Point p){
        return Math.sqrt(Math.pow((p.x - this.x), 2) + Math.pow((p.y - this.y), 2));
    }

    // equals overriding
    // p1 == p2 if has the same x and y coordinates
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Point)) return false;
        Point p = (Point)o;
        return p.x == this.x && p.y == this.y;
    }

    // toString
    @Override
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ")";
    }
}

