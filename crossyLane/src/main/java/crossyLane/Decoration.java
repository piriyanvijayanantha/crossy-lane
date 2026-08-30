package crossyLane;

import processing.core.PApplet;
import processing.core.PImage;

public class Decoration {
    private final PApplet pApplet;
    private final PImage image;
    private final float w;
    private final float h;
    private final float visibleW;
    private final float visibleH;
    private final float x;

    public Decoration(PApplet pApplet, DecorationType type, float x) {
        this.pApplet = pApplet;
        this.image = FileLoader.getImage(pApplet, type.getImageFile());
        this.x = x;

        float scale = FileLoader.scaleForVisibleHeight(image, type.getVisibleHeight());
        this.w = image.width * scale;
        this.h = image.height * scale;
        this.visibleW = w * FileLoader.getVisibleWidthFraction(image);
        this.visibleH = type.getVisibleHeight();
    }

    public void display(float laneCenterY) {
        // Schatten zuerst dann Objekt darüber
        float baseY = laneCenterY + visibleH / 2;
        pApplet.noStroke();
        pApplet.fill(0, Constants.SHADOW_ALPHA);
        pApplet.ellipse(x, baseY, visibleW * 0.85f, visibleH * 0.18f);

        pApplet.image(image, x, laneCenterY, w, h);
    }
}
