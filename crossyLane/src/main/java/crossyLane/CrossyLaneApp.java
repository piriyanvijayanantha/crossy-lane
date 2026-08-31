package crossyLane;

import processing.core.PApplet;

public class CrossyLaneApp extends PApplet {
    private Player player;
    private LaneManager laneManager;
    private boolean leftPressed;
    private boolean rightPressed;

    private GameState state;
    private HighscoreStore highscores;
    private String playerName = "";

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
        highscores = new HighscoreStore(this);
        resetWorld();
        state = GameState.START;
    }

    private void createButtons() {
        float centerX = Constants.WIDTH / 2f;
        float centerY = Constants.HEIGHT / 2f;

        // Play und PlayAgain liegen an derselben Stelle, es ist immer nur einer sichtbar.
        playButton = new Button(this, "PLAY", centerX, centerY + 80);
        playAgainButton = new Button(this, "PLAY AGAIN", centerX, centerY + 80);
        exitButton = new Button(this, "EXIT", centerX, centerY + 160);
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
        highscores.add(playerName, player.getScore());
    }

    // Ohne Namen kein Start - sonst landen leere Eintraege in der Bestenliste.
    private void tryStart() {
        if (playerName.trim().isEmpty()) {
            return;
        }
        startNewGame();
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
        text("CROSSY LANE", centerX, centerY - 290);

        drawNameField(centerX, centerY - 200);
        drawHighscores(centerX, centerY - 130);

        playButton.display();
        exitButton.display();

        fill(220);
        textAlign(CENTER, CENTER);
        textSize(18);
        text("Name tippen, dann ENTER     Pfeiltasten = bewegen", centerX, centerY + 232);
    }

    private void drawGameOver() {
        drawDimOverlay();

        float centerX = Constants.WIDTH / 2f;
        float centerY = Constants.HEIGHT / 2f;

        fill(255);
        textAlign(CENTER, CENTER);
        textSize(56);
        text("GAME OVER", centerX, centerY - 285);
        textSize(38);
        text(playerName + ": " + player.getScore(), centerX, centerY - 205);

        drawHighscores(centerX, centerY - 130);

        playAgainButton.display();
        exitButton.display();

        fill(220);
        textAlign(CENTER, CENTER);
        textSize(18);
        text("ENTER = nochmal", centerX, centerY + 232);
    }

    // Eingabefeld fuer den Namen. Der blinkende Strich zeigt, dass hier getippt wird.
    private void drawNameField(float centerX, float y) {
        textAlign(CENTER, CENTER);
        textSize(18);
        fill(190);
        text("DEIN NAME", centerX, y - 26);

        boolean caretVisible = (frameCount / 30) % 2 == 0;
        String shown = playerName + (caretVisible ? "_" : " ");

        fill(255);
        textSize(30);
        text(shown, centerX, y + 6);

        stroke(160);
        strokeWeight(1);
        line(centerX - 120, y + 26, centerX + 120, y + 26);
        noStroke();
    }

    private void drawHighscores(float centerX, float topY) {
        java.util.List<HighscoreEntry> top = highscores.getTop();

        textAlign(CENTER, CENTER);
        textSize(17);
        fill(190);
        text("BESTENLISTE", centerX, topY);

        if (top.isEmpty()) {
            textSize(18);
            fill(150);
            text("noch keine Eintraege", centerX, topY + 34);
            return;
        }

        textSize(21);
        float y = topY + 34;
        for (int i = 0; i < top.size(); i++) {
            HighscoreEntry entry = top.get(i);
            fill(i == 0 ? 255 : 210);

            textAlign(LEFT, CENTER);
            text((i + 1) + ". " + entry.name(), centerX - 120, y);

            textAlign(RIGHT, CENTER);
            text(entry.score(), centerX + 120, y);
            y += 26;
        }
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
            tryStart();
        } else if (exitButton.contains(mouseX, mouseY)) {
            exit();
        }
    }

    @Override
    public void keyPressed() {
        if (state == GameState.START) {
            handleNameInput();
            return;
        }

        if (state == GameState.GAME_OVER) {
            if (key == ENTER || key == RETURN) {
                // Name bleibt bestehen, man muss ihn nicht neu tippen.
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

    /**
     * Sammelt die getippten Zeichen fuer den Namen.
     * ENTER startet - nicht mehr SPACE, denn das Leerzeichen gehoert jetzt
     * zu den erlaubten Zeichen im Namen.
     */
    private void handleNameInput() {
        if (key == ENTER || key == RETURN) {
            tryStart();
            return;
        }
        if (key == BACKSPACE) {
            if (!playerName.isEmpty()) {
                playerName = playerName.substring(0, playerName.length() - 1);
            }
            return;
        }
        if (key == CODED || playerName.length() >= Constants.NAME_MAX_LENGTH) {
            return;
        }
        if (Character.isLetterOrDigit(key) || key == ' ') {
            playerName += key;
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
