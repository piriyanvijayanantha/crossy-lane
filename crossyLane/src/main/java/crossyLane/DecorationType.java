package crossyLane;

public enum DecorationType {
    TREE("tree.png", Constants.TREE_VISIBLE_HEIGHT),
    BUSH("bush.png", Constants.BUSH_VISIBLE_HEIGHT),
    ROCK("rock.png", Constants.ROCK_VISIBLE_HEIGHT),
    FLOWERS("flowers.png", Constants.FLOWERS_VISIBLE_HEIGHT);

    private final String imageFile;
    private final float visibleHeight;

    DecorationType(String imageFile, float visibleHeight) {
        this.imageFile = imageFile;
        this.visibleHeight = visibleHeight;
    }

    public String getImageFile() {
        return imageFile;
    }

    public float getVisibleHeight() {
        return visibleHeight;
    }
}
