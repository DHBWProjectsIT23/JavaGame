package org.itdhbw.futurewars.util;

import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.unit.UnitModel;

import java.util.*;

public class AStarPathfinder {
    private final TileRepository tileRepository;

    public AStarPathfinder() {
        this.tileRepository = Context.getTileRepository();
    }

    public List<TileModel> findPath(TileModel startTile, TileModel endTile) {
        Set<TileModel> openSet = new HashSet<>();
        Map<TileModel, TileModel> cameFrom = new HashMap<>();
        Map<TileModel, Integer> gScore = new HashMap<>();
        Map<TileModel, Integer> fScore = new HashMap<>();
        UnitModel unit = startTile.getOccupyingUnit();

        openSet.add(startTile);
        gScore.put(startTile, 0);
        fScore.put(startTile, startTile.distanceTo(endTile));

        while (!openSet.isEmpty()) {
            TileModel current = getTileWithLowestFScore(openSet, fScore);
            if (current == null) {
                //throw new RuntimeException("No path found");
            }

            if (current.equals(endTile)) {
                return reconstructPath(cameFrom, current);
            }

            openSet.remove(current);

            for (TileModel neighbor : getNeighbors(current)) {
                if (neighbor.isOccupied() || !unit.canTraverse(neighbor.getTileType())) {
                    continue;
                }

                int tentativeGScore = gScore.get(current) + neighbor.getTravelCost();

                if (!gScore.containsKey(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + neighbor.distanceTo(endTile));

                    openSet.add(neighbor);
                }
            }
        }
        return new ArrayList<>();
        //throw new RuntimeException("No path found");
    }

    private List<TileModel> reconstructPath(Map<TileModel, TileModel> cameFrom, TileModel current) {
        List<TileModel> totalPath = new ArrayList<>();
        totalPath.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(0, current);
        }

        return totalPath;
    }

    private TileModel getTileWithLowestFScore(Set<TileModel> openSet, Map<TileModel, Integer> fScore) {
        TileModel tileWithLowestFScore = null;

        for (TileModel tile : openSet) {
            if (tileWithLowestFScore == null || fScore.get(tile) < fScore.get(tileWithLowestFScore)) {
                tileWithLowestFScore = tile;
            }
        }

        return tileWithLowestFScore;
    }

    private List<TileModel> getNeighbors(TileModel tile) {
        List<TileModel> neighbors = new ArrayList<>();
        Position position = tile.getPosition();

        // Add the tiles to the left, right, above, and below the current tile if they exist
        addNeighborIfExists(neighbors, new Position(position.getX() - 1, position.getY(), true));
        addNeighborIfExists(neighbors, new Position(position.getX() + 1, position.getY(), true));
        addNeighborIfExists(neighbors, new Position(position.getX(), position.getY() - 1, true));
        addNeighborIfExists(neighbors, new Position(position.getX(), position.getY() + 1, true));

        return neighbors;
    }

    private void addNeighborIfExists(List<TileModel> neighbors, Position position) {
        if (tileRepository.getTileModel(position) != null) {
            neighbors.add(tileRepository.getTileModel(position));
        }
    }
}
