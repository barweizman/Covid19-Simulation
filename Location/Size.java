// Guy Buky 208209817
// Bar Weizman 206492449

package Location;

public class Size {
    // a size is made of width and height values
    private final int width;
    private final int height;

    // default ctor
    public Size(){
        this.height = 0;
        this.width = 0;
    }

    // param ctor
    public Size(int w, int h){
        this.height = h;
        this.width = w;
    }

    // getters
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    // equals overriding
    // s1 == s2 if has the same width and height
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Size)) return false;
        Size s = (Size)o;
        return s.width == this.width && s.height == this.height;
    }

    // toString
    @Override
    public String toString()
    {
        return "Width: " + this.width + ", Height: " + this.height;
    }
}
