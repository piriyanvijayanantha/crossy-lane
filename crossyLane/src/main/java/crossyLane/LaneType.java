package crossyLane;

public enum LaneType {
    START("Start"),
    SAFE("Safe"),
    ROAD("Gefahr"),
    RIVER("Fluss");

    private final String label;

    LaneType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
