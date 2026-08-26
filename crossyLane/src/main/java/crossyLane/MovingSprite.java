package crossyLane;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class MovingSprite {
    protected final PApplet pApplet;
    protected final PImage image;
    protected final float w;
    protected final float h;
    protected final float speed;

    protected final float hitW;

    protected float x;

    protected MovingSprite(PApplet pApplet, PImage image, float scale, float startX, float speed) {
        this.pApplet = pApplet;
        this.image = image;
        this.w = image.width * scale;
        this.h = image.height * scale;
        this.x = startX;
        this.speed = speed;
        this.hitW = w * FileLoader.getVisibleWidthFraction(image) * Constants.HITBOX_FACTOR;
    }

    public float getSpeed() {
        return speed;
    }

    public float getHitLeft() {
        return x - hitW / 2;
    }

    public float getHitRight() {
        return x + hitW / 2;
    }

    public void update() {
        x += speed;
        if (speed > 0 && x - w / 2 > Constants.WIDTH) {
            x = -w / 2;
        } else if (speed < 0 && x + w / 2 < 0) {
            x = Constants.WIDTH + w / 2;
        }
    }

    public void display(float laneCenterY) {
        pApplet.image(image, x, laneCenterY, w, h);
    }
}
