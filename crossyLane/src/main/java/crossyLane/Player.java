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
    private float displayedCameraOffset;

    public Player(PApplet pApplet) {
        this.pApplet = pApplet;
        image = getImage(pApplet, "player.png");
        w = image.width * Constants.SPRITE_SCALE;
        h = image.height * Constants.SPRITE_SCALE;

        x = (float) Constants.WIDTH / 2;
        laneIndex = 0;
        displayedCameraOffset = 0;
    }

    public int getLaneIndex() {
        return laneIndex;
    }

    // Ziel wo die Kamera hin soll, springt immer um einen wert
    public int getCameraOffsetTarget() {
        return Math.max(0, laneIndex - Constants.CAMERA_LOCK_ROW);
    }

    // Wo die Kamera gerade ist aktuell, zieht jeden Frame ein stück Richtung Ziel nach
    public void updateCamera() {
        float target = getCameraOffsetTarget();
        displayedCameraOffset += (target - displayedCameraOffset) * Constants.CAMERA_SMOOTH_FACTOR;
    }

    public float getCameraOffset() {
        return displayedCameraOffset;
    }

    private float getY() {
        float row = laneIndex - displayedCameraOffset;
        return Constants.HEIGHT - (row + 0.5f) * Constants.LANE_HEIGHT;
    }

    public void moveLeft() {
        x = Math.max(w / 2, x - Constants.MOVE_SPEED);
    }

    public void moveRight() {
        x = Math.min(Constants.WIDTH - w / 2, x + Constants.MOVE_SPEED);
    }

    public void jumpUp() {
        laneIndex++;
    }

    public void jumpDown() {
        laneIndex = Math.max(0, laneIndex - 1);
    }

    public void display() {
        pApplet.image(image, x, getY(), w, h);
    }
}
