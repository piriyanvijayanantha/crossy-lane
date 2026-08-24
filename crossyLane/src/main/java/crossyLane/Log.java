package crossyLane;

import processing.core.PApplet;
import processing.core.PImage;

public class Log extends MovingSprite {

    public Log(PApplet pApplet, PImage image, float startX, float speed) {
        super(pApplet, image, Constants.LOG_SCALE, startX, speed);
    }
}
