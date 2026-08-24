package crossyLane;

public class Constants {
    //Playground
    static final int WIDTH = 600;
    static final int HEIGHT = 800;
    static final int LANE_COUNT = 10;
    static final float LANE_HEIGHT = (float) HEIGHT / LANE_COUNT;
    static final int CAMERA_LOCK_ROW = 4;

    //Images & Player
    static final float SPRITE_SCALE = (float) (50.0 / 128);
    static final float MOVE_SPEED = 4;

    //Cars
    static final float CAR_SCALE = (float) (50.0 / 369);
    static final float CAR_SPEED = 3;

    //Logs
    static final float LOG_SCALE = (float) (50.0 / 369);
    static final float LOG_SPEED = 2;
    static final int LOGS_PER_LANE = 4;

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
}
