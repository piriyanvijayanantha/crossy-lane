package crossyLane;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static crossyLane.FileLoader.getImage;

public class LaneManager {
    private static final String[] LOG_FILES = {"log_short.png", "log_midsize.png", "log_big.png"};

    private final PApplet pApplet;
    private final Random random = new Random();
    private final List<LaneType> lanes = new ArrayList<>();
    private final Map<Integer, List<Car>> cars = new HashMap<>();
    private final Map<Integer, List<Log>> logs = new HashMap<>();

    public LaneManager(PApplet pApplet) {
        this.pApplet = pApplet;
    }

    public LaneType getType(int index) {
        ensureLane(index);
        return lanes.get(index);
    }

    public List<Car> getCars(int index) {
        ensureLane(index);
        return cars.getOrDefault(index, List.of());
    }

    public List<Log> getLogs(int index) {
        ensureLane(index);
        return logs.getOrDefault(index, List.of());
    }

    private void ensureLane(int index) {
        while (lanes.size() <= index) {
            buildBlock(lanes.size() / 2);
        }
    }

    private void buildBlock(int block) {
        LaneType type = typeForBlock(block);
        int lowerLane = block * 2;
        int upperLane = lowerLane + 1;

        lanes.add(type);
        lanes.add(type);

        switch (type) {
            case ROAD -> {
                cars.put(upperLane, List.of(new Car(pApplet,
                        getImage(pApplet, "CarBlueRightDriving.png"), 0, Constants.CAR_SPEED)));
                cars.put(lowerLane, List.of(new Car(pApplet,
                        getImage(pApplet, "CarRedLeftDriving.png"), Constants.WIDTH, -Constants.CAR_SPEED)));
            }
            case RIVER -> {
                logs.put(upperLane, createLogs(Constants.LOG_SPEED));
                logs.put(lowerLane, createLogs(-Constants.LOG_SPEED));
            }
            default -> {
            }
        }
    }

    private LaneType typeForBlock(int block) {
        if (block == 0){
            return LaneType.START;
        }
        if (block % 2 == 0){
            return LaneType.SAFE;
        }
        return (block - 1) / 2 % 2 == 0 ? LaneType.ROAD : LaneType.RIVER;
    }

    private List<Log> createLogs(float speed) {
        List<Log> result = new ArrayList<>();
        float spacing = (float) Constants.WIDTH / Constants.LOGS_PER_LANE;

        for (int i = 0; i < Constants.LOGS_PER_LANE; i++) {
            String file = LOG_FILES[random.nextInt(LOG_FILES.length)];
            result.add(new Log(pApplet, getImage(pApplet, file), i * spacing, speed));
        }
        return result;
    }
}
