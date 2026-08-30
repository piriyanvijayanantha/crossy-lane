package crossyLane;

/**
 * Die drei Fahrzeugarten. Jede bringt ihre Bilder und ihre Zielgroesse mit.
 *
 * Bewusst KEIN eigenes Tempo: in einer Spur mischen sich alle drei Typen, und
 * unterschiedlich schnelle Fahrzeuge wuerden sich gegenseitig einholen und
 * durcheinander hindurchfahren. Der Unterschied liegt in der Groesse - ein
 * Truck ist fast doppelt so breit wie ein Motorrad und damit schwerer zu umgehen.
 */
public enum VehicleType {
    CAR("CarBlueRightDriving.png", "CarRedLeftDriving.png",
            Constants.CAR_VISIBLE_HEIGHT),

    TRUCK("TruckRightDriving.png", "TruckLeftDriving.png",
            Constants.TRUCK_VISIBLE_HEIGHT),

    // Achtung: die Dateinamen sind uneinheitlich geschrieben
    // (grosses B rechts, kleines b links). Exakt so muessen sie stehen.
    MOTORBIKE("MotorBikeRightDriving.png", "MotorbikeLeftDriving.png",
            Constants.MOTORBIKE_VISIBLE_HEIGHT);

    private final String rightImage;
    private final String leftImage;
    private final float visibleHeight;

    VehicleType(String rightImage, String leftImage, float visibleHeight) {
        this.rightImage = rightImage;
        this.leftImage = leftImage;
        this.visibleHeight = visibleHeight;
    }

    public String imageFor(boolean drivingRight) {
        return drivingRight ? rightImage : leftImage;
    }

    public float getVisibleHeight() {
        return visibleHeight;
    }
}
