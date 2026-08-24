package crossyLane;

import processing.core.PApplet;
import processing.core.PImage;

import static crossyLane.FileLoader.getImage;

public class Player {
    private final PApplet pApplet;
    private final PImage image;
    private final float w;
    private final float h;

    private float x;
    private int laneIndex;

    public Player(PApplet pApplet) {
        this.pApplet = pApplet;
        image = getImage(pApplet, "player.png");
        w = image.width * Constants.SPRITE_SCALE;
        h = image.height * Constants.SPRITE_SCALE;

        x = (float) Constants.WIDTH / 2;
        laneIndex = 0;
    }

    private float getY() {
        return Constants.HEIGHT - (laneIndex + 0.5f) * Constants.LANE_HEIGHT;
    }

    public void moveLeft() {
        x = Math.max(w / 2, x - Constants.MOVE_SPEED);
    }

    public void moveRight() {
        x = Math.min(Constants.WIDTH - w / 2, x + Constants.MOVE_SPEED);
    }

    public void jumpUp() {
        laneIndex = Math.min(Constants.LANE_COUNT - 1, laneIndex + 1);
    }

    public void jumpDown() {
        laneIndex = Math.max(0, laneIndex - 1);
    }

    public void display() {
        pApplet.image(image, x, getY(), w, h);
    }
}
