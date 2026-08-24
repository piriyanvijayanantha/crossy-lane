package crossyLane;

import processing.core.PApplet;

public class CrossyLaneApp extends PApplet {
    private Player player;
    private LaneManager laneManager;
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
        laneManager = new LaneManager();
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
        int cameraOffset = player.getCameraOffset();
        textAlign(CENTER, CENTER);
        textSize(24);

        for (int row = 0; row < Constants.LANE_COUNT; row++) {
            int laneIndex = cameraOffset + row;
            LaneType type = laneManager.getType(laneIndex);
            float centerY = Constants.HEIGHT - (row + 0.5f) * Constants.LANE_HEIGHT;

            fill(0);
            if (type != LaneType.START) {
                text(type.getLabel(), Constants.WIDTH / 2f, centerY);
            }

            stroke(0);
            float lineY = Constants.HEIGHT - row * Constants.LANE_HEIGHT;
            line(0, lineY, Constants.WIDTH, lineY);
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
