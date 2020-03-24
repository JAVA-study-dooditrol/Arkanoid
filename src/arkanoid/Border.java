package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Border extends Block {
    
    Border(double x, double y, double width, double height) {

        super(x, y, width, height);
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        
        gc.setFill(Color.GREY);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}
