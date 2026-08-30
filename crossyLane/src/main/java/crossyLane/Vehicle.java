package crossyLane;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Hindernis auf der Strasse: Auto, Truck oder Motorrad.
 * Beruehrung fuehrt zu Game Over.
 */
public class Vehicle extends MovingSprite {

    public Vehicle(PApplet pApplet, PImage image, VehicleType type, float startX, float speed) {
        super(pApplet, image, scaleFor(image, type), startX, speed);
    }

    private static float scaleFor(PImage image, VehicleType type) {
        return FileLoader.scaleForVisibleHeight(image, type.getVisibleHeight());
    }
}
