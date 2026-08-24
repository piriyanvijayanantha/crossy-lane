package crossyLane;

import java.util.ArrayList;
import java.util.List;

public class LaneManager {
    private final List<LaneType> lanes = new ArrayList<>();

    public LaneType getType(int index) {
        while (lanes.size() <= index) {
            lanes.add(nextType(lanes.size()));
        }
        return lanes.get(index);
    }

    private LaneType nextType(int index) {
        if (index == 0) return LaneType.START;
        return index % 2 == 1 ? LaneType.ROAD : LaneType.SAFE;
    }
}