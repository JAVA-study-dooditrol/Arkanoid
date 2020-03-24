package arkanoid;

public abstract class Block extends GameObject {

    private double width;
    private double height;
    
    Block(double x, double y, double width, double height) {
        
        super(x, y);
        this.width = width;
        this.height = height;
    }
    
    public double getWidth() {
        
        return width;
    }
    
    public double getHeight() {
        
        return height;
    }
    
    public boolean pointBelongs(double x, double y) {
        
        if (getX() <= x && x <= getX() + width 
                && getY() <= y && y <= getY() + height)
            return true;
        else
            return false;
    }
}
