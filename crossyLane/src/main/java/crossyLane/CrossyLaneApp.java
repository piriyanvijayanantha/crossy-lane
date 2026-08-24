package crossyLane;

import processing.core.PApplet;

public class CrossyLaneApp extends PApplet {
    private Player player;
    private boolean leftPressed;
    private boolean rightPressed;

    @Override
    public void settings(){
        size(Constants.WIDTH, Constants.HEIGHT);
    }

    @Override
    public void setup(){
        imageMode(CENTER);
        player = new Player(this);
    }

    @Override
    public void draw(){
        background(200);
        drawLanes();

        if (leftPressed) player.moveLeft();
        if (rightPressed) player.moveRight();

        player.display();
    }

    private void drawLanes(){
        stroke(0);
        for (int i = 1; i < Constants.LANE_COUNT; i++) {
            float y = i * Constants.LANE_HEIGHT;
            line(0, y, Constants.WIDTH, y);
        }
    }

    @Override
    public void keyPressed(){
        if (key == CODED) {
            switch (keyCode) {
                case LEFT -> leftPressed = true;
                case RIGHT -> rightPressed = true;
                case UP -> player.jumpUp();
                case DOWN -> player.jumpDown();
            }
        }
    }

    @Override
    public void keyReleased(){
        if (key == CODED) {
            switch (keyCode) {
                case LEFT -> leftPressed = false;
                case RIGHT -> rightPressed = false;
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(CrossyLaneApp.class, args);
    }


}
