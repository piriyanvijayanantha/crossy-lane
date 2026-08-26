package crossyLane;

import processing.core.PApplet;
import processing.core.PImage;
import processing.sound.SoundFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

public class FileLoader {

    // Ab diesem Alpha-Wert gilt ein Pixel als sichtbar (nicht transparent)
    private static final int ALPHA_THRESHOLD = 10;

    private static final HashMap<String, PImage> imageCache = new HashMap<>();
    private static final HashMap<String, SoundFile> soundCache = new HashMap<>();
    private static final HashMap<PImage, float[]> visibleFractionCache = new HashMap<>();

    public static String getPath(String filename){
        String path = Objects.requireNonNull(crossyLane.FileLoader.class.getResource("/" + filename)).toExternalForm();
        if(!path.contains(".jar!")) {
            path = URLDecoder.decode(path.replaceFirst("file:", ""), StandardCharsets.UTF_8);
        }
        return Objects.requireNonNull(path);
    }

    public static PImage getImage(PApplet pApplet, String filename) {
        return imageCache.computeIfAbsent(filename, k ->  pApplet.loadImage(getPath("drawable/" + filename)));
    }

    public static SoundFile getSoundFile(PApplet pApplet, String filename) {
        return soundCache.computeIfAbsent(filename, k -> new SoundFile(pApplet, "sound/" + filename));
    }

    public static String[] getData(PApplet pApplet, String filename) {
        return pApplet.loadStrings("data/" + filename);
    }

    /**
     * Breite des sichtbaren Bildinhalts als Anteil der Bildbreite (0..1).
     * Die PNGs haben teils viel transparenten Rand - CarBlue z.B. nur 31% -
     * darum waere die volle Bildbreite als Hitbox voellig falsch.
     */
    public static float getVisibleWidthFraction(PImage image) {
        return visibleFraction(image)[0];
    }

    /** Hoehe des sichtbaren Bildinhalts als Anteil der Bildhoehe (0..1). */
    public static float getVisibleHeightFraction(PImage image) {
        return visibleFraction(image)[1];
    }

    private static float[] visibleFraction(PImage image) {
        return visibleFractionCache.computeIfAbsent(image, FileLoader::computeVisibleFraction);
    }

    // Sucht das kleinste Rechteck, das alle nicht-transparenten Pixel enthaelt.
    private static float[] computeVisibleFraction(PImage image) {
        image.loadPixels();

        int minX = image.width, maxX = -1;
        int minY = image.height, maxY = -1;

        for (int y = 0; y < image.height; y++) {
            for (int x = 0; x < image.width; x++) {
                int alpha = image.pixels[y * image.width + x] >>> 24;
                if (alpha <= ALPHA_THRESHOLD) {
                    continue;
                }
                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
            }
        }

        // Bild ohne sichtbare Pixel: auf volle Groesse zurueckfallen.
        if (maxX < 0) {
            return new float[]{1f, 1f};
        }

        return new float[]{
                (maxX - minX + 1) / (float) image.width,
                (maxY - minY + 1) / (float) image.height
        };
    }
}
