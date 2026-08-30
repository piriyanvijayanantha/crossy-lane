package crossyLane;

/**
 * Berechnet die Schwierigkeit aus dem Block-Index. Reine Rechenfunktionen ohne
 * eigenen Zustand - derselbe Block liefert immer dieselben Werte.
 *
 * Zwei Dinge steigen gleichzeitig:
 *  - stufenweise die Anzahl (mehr Autos, weniger Logs) beim Levelwechsel
 *  - stufenlos das Tempo, auch innerhalb eines Levels
 */
public class Difficulty {

    public static int level(int block) {
        if (block < Constants.LEVEL_2_BLOCK) {
            return 1;
        }
        if (block < Constants.LEVEL_3_BLOCK) {
            return 2;
        }
        return 3;
    }

    // Mehr Autos pro Spur je Level: 1, 2, 3
    public static int carsPerLane(int block) {
        return Constants.CARS_PER_LANE_LEVEL_1 + level(block) - 1;
    }

    // Weniger Logs pro Spur je Level: 4, 3, 2
    public static int logsPerLane(int block) {
        return Constants.LOGS_PER_LANE_LEVEL_1 - level(block) + 1;
    }

    public static float carSpeed(int block) {
        return Constants.CAR_SPEED * ramp(block, Constants.CAR_SPEED_RAMP, Constants.CAR_SPEED_MAX_FACTOR);
    }

    // Logs steigen flacher als Autos: ein schnelles Log traegt den Spieler
    // sonst zu rasch an den Rand, wo er ertrinkt.
    public static float logSpeed(int block) {
        return Constants.LOG_SPEED * ramp(block, Constants.LOG_SPEED_RAMP, Constants.LOG_SPEED_MAX_FACTOR);
    }

    private static float ramp(int block, float perBlock, float maxFactor) {
        return Math.min(1f + block * perBlock, maxFactor);
    }
}
