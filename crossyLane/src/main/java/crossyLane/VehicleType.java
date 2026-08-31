package crossyLane;

public enum VehicleType {
    CAR("CarBlueRightDriving.png", "CarRedLeftDriving.png",
            Constants.CAR_VISIBLE_HEIGHT),

    TRUCK("TruckRightDriving.png", "TruckLeftDriving.png",
            Constants.TRUCK_VISIBLE_HEIGHT),

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
