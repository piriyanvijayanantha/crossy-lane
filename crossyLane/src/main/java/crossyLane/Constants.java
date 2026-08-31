package crossyLane;

public class Constants {
    //Playground
    static final int WIDTH = 600;
    static final int HEIGHT = 1000;
    static final int LANE_COUNT = 10;
    static final float LANE_HEIGHT = (float) HEIGHT / LANE_COUNT;
    static final int CAMERA_LOCK_ROW = 4; //kamera mitte
    static final float CAMERA_SMOOTH_FACTOR = 0.05f;

    //Images & Player
    static final float PLAYER_VISIBLE_HEIGHT = 58;
    static final float MOVE_SPEED = 4;

    //Sprung-Animation
    static final int JUMP_FRAMES = 12;
    static final float JUMP_STRETCH = 0.20f;
    static final float JUMP_SQUEEZE = 0.14f;
    static final float JUMP_HOP_HEIGHT = 16;

    //Stehen auf dem Log: wie tief die Fuesse in die Stammoberseite einsinken
    //duerfen, und wie weich der Uebergang beim Auf- und Abspringen laeuft.
    static final float RIDE_OVERLAP = 8;
    static final float RIDE_SMOOTH_FACTOR = 0.25f;

    //Anteil der Fläche der wirklich hittet
    static final float HITBOX_FACTOR = 0.8f;

    //Vehicles
    // Zielhoehe des SICHTBAREN Fahrzeugs auf dem Bildschirm, nicht der Bilddatei.
    static final float CAR_VISIBLE_HEIGHT = 38;
    static final float TRUCK_VISIBLE_HEIGHT = 50;
    static final float MOTORBIKE_VISIBLE_HEIGHT = 28;
    static final float CAR_SPEED = 3;
    static final int CARS_PER_LANE_LEVEL_1 = 2;

    //Logs
    static final float LOG_SCALE = (float) (60.0 / 369);
    static final float LOG_SPEED = 2;
    static final int LOGS_PER_LANE_LEVEL_1 = 4;

    //Levels
    static final int LEVEL_2_BLOCK = 20;
    static final int LEVEL_3_BLOCK = 40;
    static final float CAR_SPEED_RAMP = 0.03f;
    static final float CAR_SPEED_MAX_FACTOR = 2.0f;
    static final float LOG_SPEED_RAMP = 0.02f;
    static final float LOG_SPEED_MAX_FACTOR = 1.6f;
    // Jede Spur bekommt ihr eigenes Tempo in dieser Bandbreite
    static final float SPEED_VARIATION_MIN = 0.92f;
    static final float SPEED_VARIATION_MAX = 1.08f;

    //River
    static final int RIVER_R = 64;
    static final int RIVER_G = 148;
    static final int RIVER_B = 214;

    //Road
    static final int ROAD_R = 110;
    static final int ROAD_G = 110;
    static final int ROAD_B = 110;
    static final float ROAD_DASH_LENGTH = 30;
    static final float ROAD_DASH_GAP = 20;
    static final float ROAD_DASH_WEIGHT = 3;

    //Highscore
    static final String HIGHSCORE_FILE = "highscores.txt";
    static final int HIGHSCORE_COUNT = 5;
    static final int NAME_MAX_LENGTH = 12;

    //HUD
    static final float SCORE_MARGIN = 18;

    //Buttons
    static final float BUTTON_WIDTH = 240;
    static final float BUTTON_HEIGHT = 62;
    static final float BUTTON_RADIUS = 10;
    static final float BUTTON_TEXT_SIZE = 26;
    static final int BUTTON_R = 64;
    static final int BUTTON_G = 148;
    static final int BUTTON_B = 21;
    static final int BUTTON_HOVER_R = 96;
    static final int BUTTON_HOVER_G = 196;
    static final int BUTTON_HOVER_B = 44;

    //Decorations
    static final float TREE_VISIBLE_HEIGHT = 72;
    static final float BUSH_VISIBLE_HEIGHT = 40;
    static final float ROCK_VISIBLE_HEIGHT = 26;
    static final float FLOWERS_VISIBLE_HEIGHT = 20;
    static final int DECORATIONS_MIN = 2;
    static final int DECORATIONS_MAX = 5;
    // Wie weit eine Deko aus ihrem Slot wandern darf (Anteil der Slotbreite).
    static final float DECORATION_JITTER = 0.35f;
    static final int SHADOW_ALPHA = 55;

    //Lawn
    static final int LAWN_R = 64;
    static final int LAWN_G = 148;
    static final int LAWN_B = 21;
}
