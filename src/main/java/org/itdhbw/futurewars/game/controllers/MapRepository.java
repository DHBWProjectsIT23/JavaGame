package org.itdhbw.futurewars.game.controllers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapRepository {
    private final Map<String, File> maps;

    public MapRepository() {
        this.maps = new HashMap<>();
    }

    public void addMap(String map, File file) {
        maps.put(map, file);
    }

    public Set<String> getMapNames() {
        return maps.keySet();
    }

    public int getMapCount() {
        return maps.size();
    }

    public File getMapFile(String mapName) {
        return maps.get(mapName);
    }
}
