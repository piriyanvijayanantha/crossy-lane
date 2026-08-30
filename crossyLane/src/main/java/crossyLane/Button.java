package crossyLane;

import processing.core.PApplet;

/**
 * Anklickbarer Button. Merkt sich seine Mitte, damit er wie die Sprites
 * über den Mittelpunkt positioniert wird.
 */
public class Button {
    private final PApplet pApplet;
    private final String label;
    private final float centerX;
    private final float centerY;
    private final float w;
    private final float h;

    public Button(PApplet pApplet, String label, float centerX, float centerY) {
        this.pApplet = pApplet;
        this.label = label;
        this.centerX = centerX;
        this.centerY = centerY;
        this.w = Constants.BUTTON_WIDTH;
        this.h = Constants.BUTTON_HEIGHT;
    }

    public boolean contains(float px, float py) {
        return px > centerX - w / 2 && px < centerX + w / 2
                && py > centerY - h / 2 && py < centerY + h / 2;
    }

    private boolean isHovered() {
        return contains(pApplet.mouseX, pApplet.mouseY);
    }

    public void display() {
        pApplet.noStroke();
        if (isHovered()) {
            pApplet.fill(Constants.BUTTON_HOVER_R, Constants.BUTTON_HOVER_G, Constants.BUTTON_HOVER_B);
        } else {
            pApplet.fill(Constants.BUTTON_R, Constants.BUTTON_G, Constants.BUTTON_B);
        }
        pApplet.rect(centerX - w / 2, centerY - h / 2, w, h, Constants.BUTTON_RADIUS);

        pApplet.fill(255);
        pApplet.textAlign(PApplet.CENTER, PApplet.CENTER);
        pApplet.textSize(Constants.BUTTON_TEXT_SIZE);
        pApplet.text(label, centerX, centerY);
    }
}
