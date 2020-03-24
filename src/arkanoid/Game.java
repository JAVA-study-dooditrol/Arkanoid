package arkanoid;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;

public class Game implements Initializable{
   
    private static final double relativeBorderWidth = 0.02;
    private static final double relativePaddleWidth = 0.14;
    private static final double relativePaddlePosition = 0.93;
    private static final double relativeBallRadius = 0.01;
    private static final double relativeTextHeight = 0.07;
    private static final int maxNumberOfLives = 3;
    
    private double canvasWidth;
    private double canvasHeight;
    private double borderWidth;
    private double textHeight;
    
    private GameState gameState;
    private int score;
    private int numberOfLives;
    private boolean pressedLeft;
    private boolean pressedRight;
    
    private AnimationTimer timer;
    private GraphicsContext gc;
    private ArrayList<Border> borders;
    private Border lowerBorder;
    private ArrayList<Brick> bricks;
    private Paddle paddle;
    private Ball ball;
    
    @FXML private Canvas canvas;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        borderWidth = canvasWidth * relativeBorderWidth;
        textHeight = canvasWidth * relativeTextHeight;
        
        borders = new ArrayList<Border>();
        borders.add(new Border(0, 2 * textHeight + 2, canvasWidth, borderWidth));
        borders.add(new Border(0, 2 * textHeight + 2, borderWidth, canvasHeight - 2 * textHeight));
        borders.add(new Border(canvasWidth - borderWidth, 2 * textHeight + 2, 
                borderWidth, canvasHeight - 2 * textHeight));
        lowerBorder = new Border(0, canvasHeight, canvasWidth, 100);
        
        paddle = new Paddle(canvasWidth * (0.5 - relativePaddleWidth / 2),
                canvasHeight * relativePaddlePosition,
                canvasWidth * relativePaddleWidth);
        
        ball = new Ball(paddle.getX() + paddle.getWidth() / 2 + relativeBallRadius * canvasWidth,
                paddle.getY() - relativeBallRadius * canvasWidth - 1,
                relativeBallRadius * canvasWidth);
        
        bricks = new ArrayList<Brick>();
        createPlayingField();
        
        gameState = GameState.GAME_START;
        score = 0;
        numberOfLives = maxNumberOfLives;
        pressedLeft = false;
        pressedRight = false;
        
        gc = canvas.getGraphicsContext2D();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }
    
    private void createPlayingField() {
        
        final int numberOfBrickInLine = 13;
        final int numberOfLines = 6;
        final int incrementScore = -30;
        int currentScoreForBrick = 230;
        Color[] colors= {Color.GREY, Color.RED, Color.YELLOW, Color.BLUE, Color.VIOLET,
                Color.GREEN};
        double brickWidth = (canvasWidth - 2 * borderWidth - 2) / 13;
        double x;
        double y = 4 * textHeight;
        
        bricks.clear();
        
        for (int i = 0; i < numberOfLines; ++i) {
            x = borderWidth + 1;
            for (int j = 0; j < numberOfBrickInLine; ++j) {
                bricks.add(new Brick(x, y, brickWidth, currentScoreForBrick, colors[i]));
                x += brickWidth;
            }
            y += brickWidth / 2;
            currentScoreForBrick += incrementScore;
        }
    }
    
    private void update() {
        
        switch (gameState) {
        
            case GAME_START:
                gameStart();
                break;
            case GAME_PROCESS:
                gameProcess();
                break;
            case GAME_OVER:
                gameOver();
                break;
            case GAME_END:
                gameEnd();
                break;
        }
    }
    
    private void gameStart() {
        
        if (pressedLeft) {
            paddle.moveLeft();
            if (paddle.getX() < borderWidth) {
                paddle.setX(borderWidth);
            }
            ball.setX(paddle.getX() + paddle.getWidth() / 2 + relativeBallRadius * canvasWidth);
        }
        
        if (pressedRight) {
            paddle.moveRight();
            if (paddle.getX() > canvasWidth - borderWidth - paddle.getWidth()) {
                paddle.setX(canvasWidth - borderWidth - paddle.getWidth());
            }
            ball.setX(paddle.getX() + paddle.getWidth() / 2 + relativeBallRadius * canvasWidth);
        }
        
        draw();
        
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.BASELINE);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("impact", 0.5 * textHeight));
        gc.fillText("press SPACE to begin", canvasWidth / 2, canvasHeight / 2);
    }
       
    private void gameProcess() {
       
        ball.update();
        
        if (pressedLeft) {
            paddle.moveLeft();
            if (paddle.getX() < borderWidth) {
                paddle.setX(borderWidth);
            }
        }
        
        if (pressedRight) {
            paddle.moveRight();
            if (paddle.getX() > canvasWidth - borderWidth - paddle.getWidth()) {
                paddle.setX(canvasWidth - borderWidth - paddle.getWidth());
            }
        }
        
        for (Border border : borders) {
            
            if (ball.isCollision(border)) {
                ball.collision(border);
            }
        }
        
        for (Brick brick : bricks) {
            
            if (ball.isCollision(brick)) {
                ball.collision(brick);
                score += brick.getScore();
                brick.setAlive(false);
                break;
            }
        }
        
        if (ball.isCollision(paddle)) {
            ball.collision(paddle);
        }
        
        if (ball.isCollision(lowerBorder)) {
            numberOfLives--;
            score = 0;
            if (numberOfLives == 0) {
                gameState = GameState.GAME_OVER;
            }
            else {
                gameState = GameState.GAME_START;
                paddle.setX(canvasWidth * (0.5 - relativePaddleWidth / 2));
                ball.setX(paddle.getX() + paddle.getWidth() / 2 + relativeBallRadius * canvasWidth);
                ball.setY(paddle.getY() - relativeBallRadius * canvasWidth - 1);
            }
        }
        
        bricks.removeIf(Brick::isDead);
        if (bricks.isEmpty()) {
            gameState = GameState.GAME_END;
        }
        
        draw();
    }
    
    private void gameOver() {
        
        draw();
        
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.BASELINE);
        gc.setFill(Color.RED);
        gc.setFont(new Font("impact", 2 * textHeight));
        gc.fillText("GAME OVER!", canvasWidth / 2, canvasHeight / 2);
        
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("impact", 0.5 * textHeight));
        gc.fillText("press SPACE to restart", canvasWidth / 2, canvasHeight / 2 + 0.5 * textHeight);
    }
    
    private void gameEnd() {
        
        draw();
        
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.BASELINE);
        gc.setFill(Color.RED);
        gc.setFont(new Font("impact", 3 * textHeight));
        gc.fillText("!WIN!", canvasWidth / 2, canvasHeight / 2 - 2 * textHeight);
        
        gc.setFill(Color.RED);
        gc.setFont(new Font("impact", 2 * textHeight));
        gc.fillText("YOUR SCORE", canvasWidth / 2, canvasHeight / 2);
        
        gc.setFont(new Font("impact", textHeight));
        gc.fillText(Integer.toString(score), canvasWidth / 2, canvasHeight / 2 + textHeight);
        
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("impact", 0.5 * textHeight));
        gc.fillText("press SPACE to restart", canvasWidth / 2, canvasHeight / 2 + 1.5 * textHeight);
    }
    
    private void draw() {
        
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvasWidth, 2 * textHeight + 2);
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 2 * textHeight + 2, canvasWidth, canvasHeight - 2 * textHeight - 2);
        
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.BASELINE);
        gc.setFill(Color.RED);
        gc.setFont(new Font("impact", textHeight));
        gc.fillText("SCORE", canvasWidth / 2, textHeight);
        
        gc.setFill(Color.WHITE);
        gc.fillText(Integer.toString(score), canvasWidth / 2, 2 * textHeight);
        
        gc.setFont(new Font("impact", textHeight * 3 / 4));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("lives", borderWidth, textHeight);
        
        gc.setFill(Color.RED);
        
        double coordXLive = 2.2 * textHeight;
        for (int i = 0; i < numberOfLives; ++i) {
            gc.beginPath();
            gc.arc(coordXLive, textHeight, textHeight * 0.2, textHeight * 0.2, 0, 360);
            gc.fill();
            gc.closePath();
            coordXLive += textHeight * 0.5;
        }
        
        gc.setFill(Color.YELLOW);
        gc.setFont(new Font("impact", textHeight * 0.4));
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText("restart - R", canvasWidth - textHeight * 0.4, 0);
  
        for (Border border : borders) {
            
            border.draw(gc);
        }
        
        for (Brick brick : bricks) {
            
            brick.draw(gc);
        }
        
        ball.draw(gc);
        paddle.draw(gc);
    }
    
    @FXML
    private void playerPressedKey(KeyEvent event) {
        
        if (event.getCode() == KeyCode.LEFT) {
            pressedLeft = true;
        }
        else if (event.getCode() == KeyCode.RIGHT) {
            pressedRight = true;
        }
    }
    
    @FXML
    private void playerReleasedKey(KeyEvent event) {
        
        if (event.getCode() == KeyCode.LEFT) {
            pressedLeft = false;
        }
        else if (event.getCode() == KeyCode.RIGHT) {
            pressedRight = false;
        }
        else if (event.getCode() == KeyCode.SPACE && gameState == GameState.GAME_START) {
            gameState = GameState.GAME_PROCESS;
            ball.start();
        }
        else if (event.getCode() == KeyCode.SPACE && 
                        gameState == GameState.GAME_OVER ||
                        gameState == GameState.GAME_END) {
            createPlayingField();
            score = 0;
            numberOfLives = maxNumberOfLives;
            gameState = GameState.GAME_START;
            paddle.setX(canvasWidth * (0.5 - relativePaddleWidth / 2));
            ball.setX(paddle.getX() + paddle.getWidth() / 2 + relativeBallRadius * canvasWidth);
            ball.setY(paddle.getY() - relativeBallRadius * canvasWidth - 1);
        }
        else if (event.getCode() == KeyCode.R && gameState == GameState.GAME_PROCESS) {
            createPlayingField();
            score = 0;
            numberOfLives = maxNumberOfLives;
            gameState = GameState.GAME_START;
            paddle.setX(canvasWidth * (0.5 - relativePaddleWidth / 2));
            ball.setX(paddle.getX() + paddle.getWidth() / 2 + relativeBallRadius * canvasWidth);
            ball.setY(paddle.getY() - relativeBallRadius * canvasWidth - 1);
        }
    }
}

enum GameState {
    
    GAME_START,
    GAME_PROCESS,
    GAME_OVER,
    GAME_END;
}
