package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick extends Block {

    private int score;
    private Color color;
    private boolean alive;
    
    {
        alive = true;
        color = Color.BLUE;
    }
    
    Brick(double x, double y, double width, int score) {
        
        super(x, y, width, width / 2);
        this.score = score;
    }
    
    Brick(double x, double y, double width, int score, Color color) {
        
        this(x, y, width, score);
        this.color = color;
    }
    
    public int getScore() {
        
        return score;
    }
    
    public boolean isAlive() {
        
        return alive;
    }
    
    public boolean isDead() {
        
        return !alive;
    }
    
    public void setAlive(boolean alive) {
        
        this.alive = alive;
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        
        gc.setFill(color);
        gc.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 
                getWidth() / 4, getWidth() / 4);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(getWidth() * 0.01);
        gc.strokeRoundRect(getX(), getY(), getWidth(), getHeight(), 
                getWidth() / 4, getWidth() / 4);
    }
}