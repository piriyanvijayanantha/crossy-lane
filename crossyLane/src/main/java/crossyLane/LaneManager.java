package crossyLane;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static crossyLane.FileLoader.getImage;

public class LaneManager {
    private static final VehicleType[] VEHICLE_TYPES = VehicleType.values();
    //weniger Trees weil trees grösser sind
    private static final DecorationType[] DECORATION_POOL = {
            DecorationType.TREE,
            DecorationType.BUSH, DecorationType.BUSH,
            DecorationType.ROCK, DecorationType.ROCK,
            DecorationType.FLOWERS, DecorationType.FLOWERS, DecorationType.FLOWERS
    };
    private static final String[] LOG_FILES = {"log_short.png", "log_midsize.png", "log_big.png"};

    private final PApplet pApplet;
    private final Random random = new Random();
    private final List<LaneType> lanes = new ArrayList<>();
    private final Map<Integer, List<Vehicle>> vehicles = new HashMap<>();
    private final Map<Integer, List<Log>> logs = new HashMap<>();
    private final Map<Integer, List<Decoration>> decorations = new HashMap<>();

    public LaneManager(PApplet pApplet) {
        this.pApplet = pApplet;
    }

    public LaneType getType(int index) {
        ensureLane(index);
        return lanes.get(index);
    }

    public List<Vehicle> getVehicles(int index) {
        ensureLane(index);
        return vehicles.getOrDefault(index, List.of());
    }

    public List<Log> getLogs(int index) {
        ensureLane(index);
        return logs.getOrDefault(index, List.of());
    }

    public List<Decoration> getDecorations(int index) {
        ensureLane(index);
        return decorations.getOrDefault(index, List.of());
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
                float speed = Difficulty.carSpeed(block);
                int count = Difficulty.carsPerLane(block);
                // Tempo pro Spur, Typ pro Fahrzeug: in einer Spur fahren
                // Auto, Truck und Motorrad gemischt, aber alle gleich schnell.
                vehicles.put(upperLane, createVehicles(true, varySpeed(speed), count));
                vehicles.put(lowerLane, createVehicles(false, -varySpeed(speed), count));
            }
            case RIVER -> {
                float speed = Difficulty.logSpeed(block);
                int count = Difficulty.logsPerLane(block);
                logs.put(upperLane, createLogs(varySpeed(speed), count));
                logs.put(lowerLane, createLogs(-varySpeed(speed), count));
            }
            default -> {
                decorations.put(upperLane, createDecorations());
                decorations.put(lowerLane, createDecorations());
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

    // Ein Zufallstempo für eine ganze Spur. Alle Objekte fahren gleich
    // schnell, sonst würden sich schnellere durch langsamere hindurchschieben.
    private float varySpeed(float base) {
        float span = Constants.SPEED_VARIATION_MAX - Constants.SPEED_VARIATION_MIN;
        return base * (Constants.SPEED_VARIATION_MIN + random.nextFloat() * span);
    }

    private VehicleType randomVehicleType() {
        return VEHICLE_TYPES[random.nextInt(VEHICLE_TYPES.length)];
    }

    private List<Vehicle> createVehicles(boolean drivingRight, float speed, int count) {
        List<Vehicle> result = new ArrayList<>();
        float spacing = (float) Constants.WIDTH / count;

        for (int i = 0; i < count; i++) {
            VehicleType type = randomVehicleType();
            PImage image = getImage(pApplet, type.imageFor(drivingRight));
            result.add(new Vehicle(pApplet, image, type, i * spacing, speed));
        }
        return result;
    }


    private List<Decoration> createDecorations() {
        int count = Constants.DECORATIONS_MIN
                + random.nextInt(Constants.DECORATIONS_MAX - Constants.DECORATIONS_MIN + 1);

        List<Decoration> result = new ArrayList<>();
        float slot = (float) Constants.WIDTH / count;

        for (int i = 0; i < count; i++) {
            float jitter = (random.nextFloat() - 0.5f) * slot * Constants.DECORATION_JITTER * 2;
            float x = (i + 0.5f) * slot + jitter;
            DecorationType type = DECORATION_POOL[random.nextInt(DECORATION_POOL.length)];
            result.add(new Decoration(pApplet, type, x));
        }
        return result;
    }

    private List<Log> createLogs(float speed, int count) {
        List<Log> result = new ArrayList<>();
        float spacing = (float) Constants.WIDTH / count;

        for (int i = 0; i < count; i++) {
            String file = LOG_FILES[random.nextInt(LOG_FILES.length)];
            result.add(new Log(pApplet, getImage(pApplet, file), i * spacing, speed));
        }
        return result;
    }
}
