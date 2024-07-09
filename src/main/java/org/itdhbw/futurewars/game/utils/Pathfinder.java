package org.itdhbw.futurewars.game.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

import java.util.*;

public class Pathfinder {
    private static final Logger LOGGER = LogManager.getLogger(Pathfinder.class);
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
                LOGGER.info("Path found from tile {} to tile {}", startTile.modelId, endTile.modelId);
                return reconstructPath(cameFrom, current, unit, gScore);
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
        LOGGER.warn("No path found from tile {} to tile {}", startTile.modelId, endTile.modelId);
        return new ArrayList<>();
        //throw new RuntimeException("No path found");
    }

    private List<TileModel> reconstructPath(Map<TileModel, TileModel> cameFrom, TileModel current, UnitModel unit, Map<TileModel, Integer> gScore) {
        LOGGER.info("Reconstructing path...");
        List<TileModel> totalPath = new ArrayList<>();
        totalPath.add(current);
        int unitRange = unit.getMovementRange();
        LOGGER.info("Unit range: {}", unitRange);
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

        LOGGER.info("Total travel cost: {}", totalCost);
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
        LOGGER.info("Calculating reachable tiles for unit {} on tile {}", startTile.getOccupyingUnit().modelId,
                    startTile.modelId);
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
        LOGGER.info("Reachable tiles: {}", visited.size());
        return visited;
    }

    public Set<TileModel> getAttackableTiles(TileModel startTile, UnitModel attackingUnit) {
        LOGGER.info("Calculating attackable tiles for unit {} on tile {}", attackingUnit.modelId, startTile.modelId);
        Set<TileModel> visited = new HashSet<>();
        Queue<TileModel> queue = new LinkedList<>();
        Map<TileModel, Integer> distance = new HashMap<>();
        int attackRange = attackingUnit.getAttackRange();

        queue.add(startTile);
        distance.put(startTile, 0);

        while (!queue.isEmpty()) {
            TileModel current = queue.poll();
            visited.add(current);

            for (TileModel neighbor : getNeighbors(current)) {
                int tentativeDistance =
                        distance.get(current) + 1; // Assuming each tile is at a distance of 1 from its neighbors
                if (tentativeDistance <= attackRange &&
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
        LOGGER.info("Attackable tiles: {}", visited.size());
        for (TileModel tile : visited) {
            LOGGER.info("Tile {} is attackable in Pathfinder", tile.modelId);
        }

        LOGGER.info("Attackable tiles: {}", visited.size());
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
        visited.removeIf(tile -> {
            LOGGER.info("Checking if tile {} can be merged with {}", tile.getPosition(), mergingUnit.getPosition());
            boolean b = !tile.getOccupyingUnit().canMergeWith(mergingUnit);
            return b;
        });

        LOGGER.info("Mergeable tiles: {}", visited.size());
        return visited;
    }
}
