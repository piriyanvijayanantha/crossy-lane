package crossyLane;

import processing.core.PApplet;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighscoreStore {
    private final PApplet pApplet;
    private final String path;
    private final List<HighscoreEntry> entries = new ArrayList<>();

    public HighscoreStore(PApplet pApplet) {
        this.pApplet = pApplet;
        // sketchPath macht daraus einen absoluten Pfad. Ohne das wuerde je nach
        // Startart in einem anderen Ordner gesucht als geschrieben wird.
        this.path = pApplet.sketchPath(Constants.HIGHSCORE_FILE);
        load();
    }

    public List<HighscoreEntry> getTop() {
        return List.copyOf(entries);
    }

    public void add(String name, int score) {
        entries.add(new HighscoreEntry(name, score));
        entries.sort(Comparator.comparingInt(HighscoreEntry::score).reversed());

        while (entries.size() > Constants.HIGHSCORE_COUNT) {
            entries.remove(entries.size() - 1);
        }
        save();
    }

    private void load() {
        // Beim ersten Start gibt es die Datei noch nicht, dann bleibt die Liste leer
        if (!new File(path).exists()) {
            return;
        }
        for (String line : pApplet.loadStrings(path)) {
            String[] parts = line.split(";");
            if (parts.length == 2) {
                entries.add(new HighscoreEntry(parts[0], Integer.parseInt(parts[1])));
            }
        }
    }

    private void save() {
        String[] lines = new String[entries.size()];
        for (int i = 0; i < entries.size(); i++) {
            lines[i] = entries.get(i).name() + ";" + entries.get(i).score();
        }
        pApplet.saveStrings(path, lines);
    }
}
