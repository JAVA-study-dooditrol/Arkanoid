package arkanoid;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends GameObject {
    
    private static double velocityModule;
    
    private double radius;
    private Point2D velocity;
    
    {
        velocity = new Point2D(0 ,0);
    }
    
    static {
        velocityModule = 6;
    }
    
    Ball(double x, double y, double radius) {
        
        super(x, y);
        this.radius = radius;
    }
    
    public boolean pointBelongs(double x, double y) {
        
        if (Math.pow(x - getX(), 2) + Math.pow(y - getY(),2) <= Math.pow(radius, 2))
            return true;
        else
            return false;
    }
    
    public void start() {
        
        double velocityOnAxises = Math.sqrt(Math.pow(velocityModule, 2) / 2);
        this.velocity = new Point2D(velocityOnAxises, velocityOnAxises);
    }
    
    public void update() {
        
        setX(getX() + velocity.getX());
        setY(getY() + velocity.getY());
    }
    
    public boolean isCollision(Block block) {
        
        if (block.pointBelongs(getX() + radius, getY()) ||
                block.pointBelongs(getX() - radius, getY()) ||
                block.pointBelongs(getX(), getY() + radius) ||
                block.pointBelongs(getX(), getY() - radius)) 
            return true;
        else 
            return false;  
    }
    
    public boolean isCollision(Paddle block) {
        
        if (block.pointBelongs(getX() + radius, getY()) ||
                block.pointBelongs(getX() - radius, getY()) ||
                block.pointBelongs(getX(), getY() + radius) ||
                block.pointBelongs(getX(), getY() - radius) ||
                pointBelongs(block.getX(), block.getY()) ||
                pointBelongs(block.getX(), block.getY() + block.getHeight()) ||
                pointBelongs(block.getX() + block.getWidth(), block.getY() + block.getHeight()) ||
                pointBelongs(block.getX() + block.getWidth(), block.getY())) 
            return true;
        else 
            return false;  
    }
    
    public void collision(Block block) {
        
        Point2D normal;
        
        if (block.pointBelongs(getX() + radius, getY())) {
            normal = new Point2D(-1, 0);
        }
        else if (block.pointBelongs(getX() - radius, getY())) {
            normal = new Point2D(1, 0);
        }
        else if (block.pointBelongs(getX(), getY() - radius)) {
            normal = new Point2D(0, 1);
        }
        else {
            normal = new Point2D(0, -1);
        }
        
        double correctionVectorLength = -2 * normal.dotProduct(velocity);
        Point2D correctionVector = normal.multiply(correctionVectorLength);
        velocity = velocity.add(correctionVector);
    }
    
    public void collision(Paddle paddle) {
        
        if (paddle.pointBelongs(getX(), getY() + radius) ||
                pointBelongs(paddle.getX(), paddle.getY()) ||
                pointBelongs(paddle.getX() + paddle.getWidth(), paddle.getY())) {
            
            Point2D circleCenter = null;
            if (new Point2D(1, 0).angle(velocity) <= 90)
                circleCenter = new Point2D(paddle.getX() + paddle.getWidth() * 3 / 4,
                        paddle.getY() + paddle.getWidth() * 1.8);
            else
                circleCenter = new Point2D(paddle.getX() + paddle.getWidth() * 1 / 4,
                        paddle.getY() + paddle.getWidth() * 1.8);

            Point2D normal = new Point2D(getX(), getY()).subtract(circleCenter).normalize();
            double correctionVectorLength = -2 * normal.dotProduct(velocity);
            Point2D correctionVector = normal.multiply(correctionVectorLength);
            velocity = velocity.add(correctionVector);
        }
        else if (paddle.pointBelongs(getX() + radius, getY()) || 
                paddle.pointBelongs(getX() - radius, getY())) {
            collision((Block)paddle);
        }
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        
        gc.beginPath();
        gc.arc(getX(), getY(), radius, radius, 0, 360);
        gc.setFill(Color.LIGHTBLUE);
        gc.fill();
        gc.arc(getX(), getY(), radius, radius, 0, 360);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(radius * 0.3);
        gc.stroke();
        gc.closePath();
    }
    
    public static double getVelocityModule() {
        
        return velocityModule;
    }
    
    public static void setVelocityModule(double newVelocityModule) {
        
        velocityModule = newVelocityModule;
    }
}
