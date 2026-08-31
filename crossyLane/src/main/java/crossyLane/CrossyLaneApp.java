package crossyLane;

import processing.core.PApplet;

public class CrossyLaneApp extends PApplet {
    private Player player;
    private LaneManager laneManager;
    private boolean leftPressed;
    private boolean rightPressed;

    private GameState state;
    private int highScore;

    private Button playButton;
    private Button playAgainButton;
    private Button exitButton;

    @Override
    public void settings() {
        size(Constants.WIDTH, Constants.HEIGHT);
    }

    @Override
    public void setup() {
        imageMode(CENTER);
        createButtons();
        resetWorld();
        state = GameState.START;
    }

    private void createButtons() {
        float centerX = Constants.WIDTH / 2f;
        float centerY = Constants.HEIGHT / 2f;

        // Play und PlayAgain liegen an derselben Stelle, es ist immer nur einer sichtbar.
        playButton = new Button(this, "PLAY", centerX, centerY - 10);
        playAgainButton = new Button(this, "PLAY AGAIN", centerX, centerY - 10);
        exitButton = new Button(this, "EXIT", centerX, centerY + 70);
    }

    // Baut eine frische Welt. Laeuft auch vor dem Startscreen, damit dort
    // schon etwas im Hintergrund steht statt einer leeren Flaeche.
    private void resetWorld() {
        player = new Player(this);
        laneManager = new LaneManager(this);
        leftPressed = false;
        rightPressed = false;
    }

    private void startNewGame() {
        resetWorld();
        state = GameState.PLAYING;
    }

    private void endGame() {
        state = GameState.GAME_OVER;
        highScore = Math.max(highScore, player.getScore());
    }

    @Override
    public void draw() {
        // 60 frames pro sekunde -> Default
        if (state == GameState.PLAYING) {
            if (leftPressed) {
                player.moveLeft();
            }
            if (rightPressed) {
                player.moveRight();
            }
            player.update();
            updateWorld();
            checkCollisions();
        }
        //World
        drawLanes();
        //player komplett im Vordergrund immer
        player.display();

        //HUD immer zuoberst
        switch (state) {
            case START -> drawStartScreen();
            case PLAYING -> drawScore();
            case GAME_OVER -> drawGameOver();
        }
    }

    // Bewegt Autos und Logs
    private void updateWorld() {
        float cameraOffset = player.getCameraOffset();
        int firstLane = firstVisibleLane(cameraOffset);

        for (int laneIndex = firstLane; laneIndex <= firstLane + Constants.LANE_COUNT; laneIndex++) {
            for (Vehicle vehicle : laneManager.getVehicles(laneIndex)) {
                vehicle.update();
            }
            for (Log log : laneManager.getLogs(laneIndex)) {
                log.update();
            }
        }
    }

    private void checkCollisions() {
        int lane = player.getLaneIndex();

        for (Vehicle vehicle : laneManager.getVehicles(lane)) {
            if (overlapsPlayer(vehicle)) {
                endGame();
                return;
            }
        }

        for (Log log : laneManager.getLogs(lane)) {
            if (overlapsPlayer(log)) {
                player.carry(log.getSpeed(), log.getVisibleHeight());
                return;
            }
        }


        if (laneManager.getType(lane) == LaneType.RIVER) {
            endGame();
        }
    }

    private boolean overlapsPlayer(MovingSprite sprite) {
        return player.getHitRight() > sprite.getHitLeft()
                && player.getHitLeft() < sprite.getHitRight();
    }

    private void drawScore() {
        textAlign(LEFT, TOP);
        textSize(30);

        String label = "Score: " + player.getScore();
        // Dunkler Versatz dahinter, damit die Zahl auf jedem Lane-Typ lesbar bleibt.
        fill(0, 130);
        text(label, Constants.SCORE_MARGIN + 2, Constants.SCORE_MARGIN + 2);
        fill(255);
        text(label, Constants.SCORE_MARGIN, Constants.SCORE_MARGIN);
    }

    private void drawDimOverlay() {
        noStroke();
        fill(0, 150);
        rect(0, 0, Constants.WIDTH, Constants.HEIGHT);
    }

    private void drawStartScreen() {
        drawDimOverlay();

        float centerX = Constants.WIDTH / 2f;
        float centerY = Constants.HEIGHT / 2f;

        fill(255);
        textAlign(CENTER, CENTER);
        textSize(58);
        text("CROSSY LANE", centerX, centerY - 180);

        if (highScore > 0) {
            textSize(26);
            text("Best: " + highScore, centerX, centerY - 100);
        }

        playButton.display();
        exitButton.display();

        fill(220);
        textAlign(CENTER, CENTER);
        textSize(20);
        text("SPACE = starten     Pfeiltasten = bewegen", centerX, centerY + 160);
    }

    private void drawGameOver() {
        drawDimOverlay();

        float centerX = Constants.WIDTH / 2f;
        float centerY = Constants.HEIGHT / 2f;

        fill(255);
        textAlign(CENTER, CENTER);
        textSize(56);
        text("GAME OVER", centerX, centerY - 200);
        textSize(38);
        text("Score: " + player.getScore(), centerX, centerY - 125);
        textSize(26);
        text("Best: " + highScore, centerX, centerY - 75);

        playAgainButton.display();
        exitButton.display();

        fill(220);
        textAlign(CENTER, CENTER);
        textSize(20);
        text("SPACE = nochmal", centerX, centerY + 160);
    }

    private void drawLanes() {
        float cameraOffset = player.getCameraOffset();

        drawLaneBackgrounds(cameraOffset);
        drawLaneContents(cameraOffset);
    }

    // Bildschirm-Y der Lane-Mitte. cameraOffset ist gebrochen, dadurch scrollt die Welt weich.
    private float laneCenterY(int laneIndex, float cameraOffset) {
        return Constants.HEIGHT - (laneIndex - cameraOffset + 0.5f) * Constants.LANE_HEIGHT;
    }

    // Unterste sichtbare Lane. Eine Lane mehr zeichnen, weil bei gebrochenem Offset
    // oben und unten je eine Lane nur halb im Bild liegt.
    private int firstVisibleLane(float cameraOffset) {
        return Math.max(0, (int) Math.floor(cameraOffset));
    }

    private void drawLaneBackgrounds(float cameraOffset) {
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

    private void drawLaneContents(float cameraOffset) {
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

            for (Decoration decoration : laneManager.getDecorations(laneIndex)) {
                decoration.display(centerY);
            }
            for (Vehicle vehicle : laneManager.getVehicles(laneIndex)) {
                vehicle.display(centerY);
            }
            for (Log log : laneManager.getLogs(laneIndex)) {
                log.display(centerY);
            }
        }
    }

    private void drawRoadMarking(float y) {
        stroke(255);
        strokeWeight(Constants.ROAD_DASH_WEIGHT);

        float step = Constants.ROAD_DASH_LENGTH + Constants.ROAD_DASH_GAP;
        for (float x = 0; x < Constants.WIDTH; x += step) {
            line(x, y, Math.min(x + Constants.ROAD_DASH_LENGTH, Constants.WIDTH), y);
        }

        strokeWeight(1);
    }

    @Override
    public void mousePressed() {
        if (state == GameState.PLAYING) {
            return;
        }

        Button startButton = (state == GameState.START) ? playButton : playAgainButton;
        if (startButton.contains(mouseX, mouseY)) {
            startNewGame();
        } else if (exitButton.contains(mouseX, mouseY)) {
            exit();
        }
    }

    @Override
    public void keyPressed() {
        if (state != GameState.PLAYING) {
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
    public void keyReleased() {
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
