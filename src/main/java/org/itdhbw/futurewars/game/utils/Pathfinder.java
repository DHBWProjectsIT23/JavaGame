package org.itdhbw.futurewars.game.utils;

import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

import java.util.*;
import java.util.logging.Logger;

public class Pathfinder {
    private static final Logger LOGGER = Logger.getLogger(Pathfinder.class.getSimpleName());
    private final TileRepository tileRepository;

    public Pathfinder() {
        this.tileRepository = Context.getTileRepository();
    }

    public List<TileModel> findPath(TileModel startTile, TileModel endTile) {
        Map<TileModel, Integer> fScore = new HashMap<>();
        PriorityQueue<TileModel> openSet = new PriorityQueue<>(Comparator.comparingInt(fScore::get));
        Map<TileModel, TileModel> cameFrom = new HashMap<>();
        Map<TileModel, Integer> gScore = new HashMap<>();
        UnitModel unit = startTile.getOccupyingUnit();

        openSet.add(startTile);
        gScore.put(startTile, 0);
        fScore.put(startTile, startTile.distanceTo(endTile));

        while (!openSet.isEmpty()) {
            TileModel current = openSet.poll();
            if (current == null) {
                break;
            }

            if (current.equals(endTile)) {
                LOGGER.info("Found path, reconstructing...");
                return reconstructPath(cameFrom, current, unit);
            }

            for (TileModel neighbor : getNeighbors(current)) {
                if ((neighbor.isOccupied() &&
                     !neighbor.getOccupyingUnit().canMergeWith(startTile.getOccupyingUnit())) ||
                    unit.canNotTraverse(neighbor.getMovementType())) {
                    continue;
                }

                int tentativeGScore = gScore.get(current) + unit.getTravelCost(neighbor.getMovementType());

                if (!gScore.containsKey(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + neighbor.distanceTo(endTile));

                    openSet.add(neighbor);
                }
            }
        }
        LOGGER.warning("No path found!");
        return new ArrayList<>();
    }

    private List<TileModel> reconstructPath(Map<TileModel, TileModel> cameFrom, TileModel current, UnitModel unit) {
        List<TileModel> totalPath = new ArrayList<>();
        totalPath.add(current);
        int unitRange = unit.getMovementRange();
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.addFirst(current);
        }

        int totalCost = 0;
        boolean first = true;
        List<TileModel> cutPath = new ArrayList<>();
        for (TileModel tile : totalPath) {
            if (first) {
                first = false;
                continue;
            }
            totalCost += unit.getTravelCost(tile.getMovementType());
            if (totalCost > unitRange) {
                break;
            }
            cutPath.add(tile);
        }

        LOGGER.info("Total travel cost: " + totalCost);
        LOGGER.info("Returning path with " + cutPath.size() + " tiles");
        return cutPath;
    }

    private List<TileModel> getNeighbors(TileModel tile) {
        List<TileModel> neighbors = new ArrayList<>();
        Position position = tile.getPosition();

        // Calculate the positions directly
        neighbors.add(tileRepository.getTileModel(new Position(position.getX() - 1, position.getY())));
        neighbors.add(tileRepository.getTileModel(new Position(position.getX() + 1, position.getY())));
        neighbors.add(tileRepository.getTileModel(new Position(position.getX(), position.getY() - 1)));
        neighbors.add(tileRepository.getTileModel(new Position(position.getX(), position.getY() + 1)));

        // Remove null neighbors
        neighbors.removeIf(Objects::isNull);

        return neighbors;
    }

    public Set<TileModel> getReachableTiles(TileModel startTile) {
        Set<TileModel> visited = new HashSet<>();
        Queue<TileModel> queue = new LinkedList<>();
        Map<TileModel, Integer> distance = new HashMap<>();
        UnitModel unit = startTile.getOccupyingUnit();
        int movementRange = unit.getMovementRange();

        queue.add(startTile);
        distance.put(startTile, 0);

        while (!queue.isEmpty()) {
            TileModel current = queue.poll();
            visited.add(current);

            for (TileModel neighbor : getNeighbors(current)) {
                if (neighbor.isOccupied() || startTile.getOccupyingUnit().canNotTraverse(neighbor.getMovementType())) {
                    continue;
                }

                int tentativeDistance = distance.get(current) + unit.getTravelCost(neighbor.getMovementType());
                if (tentativeDistance <= movementRange &&
                    (!distance.containsKey(neighbor) || tentativeDistance < distance.get(neighbor))) {
                    distance.put(neighbor, tentativeDistance);
                    queue.add(neighbor);
                }
            }
        }
        return visited;
    }

    public Set<TileModel> getAttackableTiles(TileModel startTile, UnitModel attackingUnit) {
        Set<TileModel> visited = new HashSet<>();
        Queue<TileModel> queue = new LinkedList<>();
        Map<TileModel, Integer> distance = new HashMap<>();
        int attackRange = attackingUnit.getAttackRange();
        int movementRange = attackingUnit.getMovementRange();

        queue.add(startTile);
        distance.put(startTile, 0);

        while (!queue.isEmpty()) {
            TileModel current = queue.poll();
            visited.add(current);

            for (TileModel neighbor : getNeighbors(current)) {
                int distanceTotal = neighbor.distanceTo(attackingUnit.currentTileProperty().get());

                int tentativeDistance =
                        distance.get(current) + 1; // Assuming each tile is at a distance of 1 from its neighbors
                if (tentativeDistance <= attackRange && distanceTotal <= movementRange + attackRange &&
                    (!distance.containsKey(neighbor) || tentativeDistance < distance.get(neighbor))) {
                    distance.put(neighbor, tentativeDistance);
                    queue.add(neighbor);
                }
            }
        }

        // Filter out the tiles that are not occupied
        visited.removeIf(tile -> !tile.isOccupied());
        visited.removeIf(tile -> tile.getOccupyingUnit().getTeam() == attackingUnit.getTeam());
        visited.removeIf(tile -> !attackingUnit.canAttackUnit(tile.getOccupyingUnit()));

        return visited;
    }

    public Set<TileModel> getMergeableTiles(TileModel startTile, UnitModel mergingUnit) {
        Set<TileModel> visited = new HashSet<>();
        Queue<TileModel> queue = new LinkedList<>();
        Map<TileModel, Integer> distance = new HashMap<>();
        UnitModel unit = startTile.getOccupyingUnit();
        int movementRange = unit.getMovementRange();

        queue.add(startTile);
        distance.put(startTile, 0);

        while (!queue.isEmpty()) {
            TileModel current = queue.poll();
            visited.add(current);

            for (TileModel neighbor : getNeighbors(current)) {
                if (startTile.getOccupyingUnit().canNotTraverse(neighbor.getMovementType())) {
                    continue;
                }

                int tentativeDistance = distance.get(current) + unit.getTravelCost(neighbor.getMovementType());
                if (tentativeDistance <= movementRange &&
                    (!distance.containsKey(neighbor) || tentativeDistance < distance.get(neighbor))) {
                    distance.put(neighbor, tentativeDistance);
                    queue.add(neighbor);
                }
            }
        }
        // Filter out the tiles that are not occupied
        visited.removeIf(tile -> !tile.isOccupied());
        visited.removeIf(tile -> tile == startTile);
        visited.removeIf(tile -> !tile.getOccupyingUnit().canMergeWith(mergingUnit));

        return visited;
    }

    @Override
    public String toString() {
        return "Pathfinder{" + "tileRepository=" + tileRepository + '}';
    }
}
