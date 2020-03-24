package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle extends Block{
    
    private static final double velocity = 5;

    
    Paddle(double x, double y, double width) {
        
        super(x, y, width, width / 4);
    }
    
    public void moveLeft() {
        
        setX(getX() - velocity);
    }
    
    public void moveRight() {
        
        setX(getX() + velocity);
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        
        gc.setFill(Color.BLACK);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}
