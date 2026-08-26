package crossyLane;

import processing.core.PApplet;

public class CrossyLaneApp extends PApplet {
    private Player player;
    private LaneManager laneManager;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean gameOver;

    @Override
    public void settings(){
        size(Constants.WIDTH, Constants.HEIGHT);
    }

    @Override
    public void setup(){
        imageMode(CENTER);
        startNewGame();
    }

    private void startNewGame(){
        player = new Player(this);
        laneManager = new LaneManager(this);
        leftPressed = false;
        rightPressed = false;
        gameOver = false;
    }

    @Override
    public void draw(){
        //calculate, bei Game Over eingefroren
        if (!gameOver) {
            if (leftPressed) player.moveLeft();
            if (rightPressed) player.moveRight();
            player.updateCamera();
            updateWorld();
            checkCollisions();
        }
        //World
        drawLanes();
        //player komplett im Vordergrund immer
        player.display();

        if (gameOver) {
            drawGameOver();
        }
    }

    // Bewegt Autos und Logs
    private void updateWorld(){
        float cameraOffset = player.getCameraOffset();
        int firstLane = firstVisibleLane(cameraOffset);

        for (int laneIndex = firstLane; laneIndex <= firstLane + Constants.LANE_COUNT; laneIndex++) {
            for (Car car : laneManager.getCars(laneIndex)) {
                car.update();
            }
            for (Log log : laneManager.getLogs(laneIndex)) {
                log.update();
            }
        }
    }

    private void checkCollisions(){
        int lane = player.getLaneIndex();

        for (Car car : laneManager.getCars(lane)) {
            if (overlapsPlayer(car)) {
                gameOver = true;
                return;
            }
        }

        for (Log log : laneManager.getLogs(lane)) {
            if (overlapsPlayer(log)) {
                player.carry(log.getSpeed());
                return;
            }
        }
    }

    private boolean overlapsPlayer(MovingSprite sprite){
        return player.getHitRight() > sprite.getHitLeft()
                && player.getHitLeft() < sprite.getHitRight();
    }

    private void drawGameOver(){
        noStroke();
        fill(0, 150);
        rect(0, 0, Constants.WIDTH, Constants.HEIGHT);

        fill(255);
        textAlign(CENTER, CENTER);
        textSize(56);
        text("GAME OVER", Constants.WIDTH / 2f, Constants.HEIGHT / 2f - 30);
        textSize(22);
        text("SPACE = neu starten", Constants.WIDTH / 2f, Constants.HEIGHT / 2f + 40);
    }

    private void drawLanes(){
        float cameraOffset = player.getCameraOffset();

        drawLaneBackgrounds(cameraOffset);
        drawLaneContents(cameraOffset);
    }

    // Bildschirm-Y der Lane-Mitte. cameraOffset ist gebrochen, dadurch scrollt die Welt weich.
    private float laneCenterY(int laneIndex, float cameraOffset){
        return Constants.HEIGHT - (laneIndex - cameraOffset + 0.5f) * Constants.LANE_HEIGHT;
    }

    // Unterste sichtbare Lane. Eine Lane mehr zeichnen, weil bei gebrochenem Offset
    // oben und unten je eine Lane nur halb im Bild liegt.
    private int firstVisibleLane(float cameraOffset){
        return Math.max(0, (int) Math.floor(cameraOffset));
    }

    private void drawLaneBackgrounds(float cameraOffset){
        noStroke();

        int firstLane = firstVisibleLane(cameraOffset);
        for (int laneIndex = firstLane; laneIndex <= firstLane + Constants.LANE_COUNT; laneIndex++) {
            LaneType type = laneManager.getType(laneIndex);

            if (type == LaneType.RIVER) {
                fill(Constants.RIVER_R, Constants.RIVER_G, Constants.RIVER_B);
            } else if (type == LaneType.ROAD) {
                fill(Constants.ROAD_R, Constants.ROAD_G, Constants.ROAD_B);
            } else {
                fill(Constants.LAWN_R, Constants.LAWN_G, Constants.LAWN_B);
            }

            float topY = laneCenterY(laneIndex, cameraOffset) - Constants.LANE_HEIGHT / 2f;
            rect(0, topY, Constants.WIDTH, Constants.LANE_HEIGHT);
        }
    }

    private void drawLaneContents(float cameraOffset){
        textAlign(CENTER, CENTER);
        textSize(20);

        int firstLane = firstVisibleLane(cameraOffset);
        for (int laneIndex = firstLane; laneIndex <= firstLane + Constants.LANE_COUNT; laneIndex++) {
            LaneType type = laneManager.getType(laneIndex);
            float centerY = laneCenterY(laneIndex, cameraOffset);

            float topY = centerY - Constants.LANE_HEIGHT / 2f;
            if (type == LaneType.ROAD && laneIndex % 2 == 0) {
                drawRoadMarking(topY);
            }

            for (Car car : laneManager.getCars(laneIndex)) {
                car.display(centerY);
            }
            for (Log log : laneManager.getLogs(laneIndex)) {
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
        if (gameOver) {
            if (key == ' ') {
                startNewGame();
            }
            return;
        }

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
