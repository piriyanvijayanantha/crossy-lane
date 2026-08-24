package crossyLane;

import processing.core.PApplet;
import processing.core.PImage;

public class Car extends MovingSprite {

    public Car(PApplet pApplet, PImage image, float startX, float speed) {
        super(pApplet, image, Constants.CAR_SCALE, startX, speed);
    }
}
