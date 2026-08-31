package crossyLane;

import processing.core.PApplet;
import processing.core.PImage;

import static crossyLane.FileLoader.getImage;

public class Player {
    private final PApplet pApplet;

    private final PImage front;
    private final PImage back;
    private final PImage left;
    private final PImage right;

    private final float w;
    private final float h;
    private final float hitW;

    private PImage current;
    private float x;
    private int laneIndex;
    private int maxLaneReached;
    private float displayedCameraOffset;

    private int jumpTimer;

    private float rideTargetY;
    private float rideOffsetY;

    public Player(PApplet pApplet) {
        this.pApplet = pApplet;

        front = getImage(pApplet, "MainCharacterFront.png");
        back = getImage(pApplet, "MainCharacterBack.png");
        left = getImage(pApplet, "MainCharacterLeft.png");
        right = getImage(pApplet, "MainCharacterRight.png");
        current = front;
        float scale = FileLoader.scaleForVisibleHeight(front, Constants.PLAYER_VISIBLE_HEIGHT);
        w = front.width * scale;
        h = front.height * scale;
        hitW = w * FileLoader.getVisibleWidthFraction(front) * Constants.HITBOX_FACTOR;

        x = (float) Constants.WIDTH / 2;
        laneIndex = 0;
        maxLaneReached = 0;
        displayedCameraOffset = 0;
        jumpTimer = 0;
        rideTargetY = 0;
        rideOffsetY = 0;
    }

    public int getLaneIndex() {
        return laneIndex;
    }

    public int getScore() {
        return maxLaneReached;
    }

    public float getHitLeft() {
        return x - hitW / 2;
    }

    public float getHitRight() {
        return x + hitW / 2;
    }

    // Ziel wo die Kamera hin soll, springt immer um einen wert
    public int getCameraOffsetTarget() {
        return Math.max(0, laneIndex - Constants.CAMERA_LOCK_ROW);
    }

    /**
     * Einmal pro Frame: Kamera nachziehen und die Sprung-Animation weiterzaehlen.
     */
    public void update() {
        float target = getCameraOffsetTarget();
        displayedCameraOffset += (target - displayedCameraOffset) * Constants.CAMERA_SMOOTH_FACTOR;

        if (jumpTimer > 0) {
            jumpTimer--;
        }

        rideOffsetY += (rideTargetY - rideOffsetY) * Constants.RIDE_SMOOTH_FACTOR;
        rideTargetY = 0;
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
        current = left;
    }

    public void moveRight() {
        x = Math.min(Constants.WIDTH - w / 2, x + Constants.MOVE_SPEED);
        current = right;
    }

    public void carry(float logSpeed, float logVisibleHeight) {
        x = Math.min(Constants.WIDTH - w / 2, Math.max(w / 2, x + logSpeed));
        rideTargetY = -(Constants.PLAYER_VISIBLE_HEIGHT / 2
                + logVisibleHeight / 2
                - Constants.RIDE_OVERLAP);
    }

    public void jumpUp() {
        laneIndex++;
        maxLaneReached = Math.max(maxLaneReached, laneIndex);
        current = back;
        jumpTimer = Constants.JUMP_FRAMES;
    }

    public void jumpDown() {
        laneIndex = Math.max(0, laneIndex - 1);
        current = front;
        jumpTimer = Constants.JUMP_FRAMES;
    }

    public void display() {
        // 0 am Anfang und Ende des Sprungs, 1 in der Mitte, deshalb sinus!
        float arc = jumpTimer == 0
                ? 0
                : PApplet.sin(PApplet.PI * (1 - jumpTimer / (float) Constants.JUMP_FRAMES));

        float drawW = w * (1 - arc * Constants.JUMP_SQUEEZE);
        float drawH = h * (1 + arc * Constants.JUMP_STRETCH);

        // Nach oben strecken statt um die Mitte, damit die Fuesse stehen bleiben.
        float footCorrection = (drawH - h) / 2;
        float offsetX = FileLoader.getVisibleOffsetXFraction(current) * drawW;

        pApplet.image(current,
                x - offsetX,
                getY() + rideOffsetY - footCorrection - arc * Constants.JUMP_HOP_HEIGHT,
                drawW, drawH);
    }
}
