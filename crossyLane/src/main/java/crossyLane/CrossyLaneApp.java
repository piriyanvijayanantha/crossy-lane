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
        laneManager = new LaneManager(this);
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

        drawLaneBackgrounds(cameraOffset);
        drawLaneContents(cameraOffset);
    }

    private void drawLaneBackgrounds(int cameraOffset){
        noStroke();

        for (int row = 0; row < Constants.LANE_COUNT; row++) {
            LaneType type = laneManager.getType(cameraOffset + row);

            if (type == LaneType.RIVER) {
                fill(Constants.RIVER_R, Constants.RIVER_G, Constants.RIVER_B);
            } else if (type == LaneType.ROAD) {
                fill(Constants.ROAD_R, Constants.ROAD_G, Constants.ROAD_B);
            } else {
                continue;
            }

            float topY = Constants.HEIGHT - (row + 1) * Constants.LANE_HEIGHT;
            rect(0, topY, Constants.WIDTH, Constants.LANE_HEIGHT);
        }
    }

    private void drawLaneContents(int cameraOffset){
        textAlign(CENTER, CENTER);
        textSize(20);

        for (int row = 0; row < Constants.LANE_COUNT; row++) {
            int laneIndex = cameraOffset + row;
            LaneType type = laneManager.getType(laneIndex);
            float centerY = Constants.HEIGHT - (row + 0.5f) * Constants.LANE_HEIGHT;

            float topY = centerY - Constants.LANE_HEIGHT / 2f;
            // Zwischen den beiden Auto-Spuren eine gestrichelte Mittellinie statt der schwarzen Trennlinie
            if (type == LaneType.ROAD && laneIndex % 2 == 0) {
                drawRoadMarking(topY);
            } else {
                stroke(0);
                line(0, topY, Constants.WIDTH, topY);
            }

            // Label nur einmal pro Block (auf der unteren der beiden Spuren)
            if (type != LaneType.START && laneIndex % 2 == 0) {
                fill(0);
                text(type.getLabel(), Constants.WIDTH / 2f, centerY);
            }

            for (Car car : laneManager.getCars(laneIndex)) {
                car.update();
                car.display(centerY);
            }
            for (Log log : laneManager.getLogs(laneIndex)) {
                log.update();
                log.display(centerY);
            }
        }
    }

    private void drawRoadMarking(float y){
        stroke(255);
        strokeWeight(Constants.ROAD_DASH_WEIGHT);

        float step = Constants.ROAD_DASH_LENGTH + Constants.ROAD_DASH_GAP;
        for (float x = 0; x < Constants.WIDTH; x += step) {
            line(x, y, Math.min(x + Constants.ROAD_DASH_LENGTH, Constants.WIDTH), y);
        }

        strokeWeight(1);
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
