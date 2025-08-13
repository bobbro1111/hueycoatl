import org.rspeer.game.adapter.scene.Npc;
import org.rspeer.game.adapter.scene.Player;
import org.rspeer.game.adapter.scene.SceneObject;
import org.rspeer.game.movement.Movement;
import org.rspeer.game.position.Position;
import org.rspeer.game.position.area.Area;
import org.rspeer.game.scene.*;
import prepare.OrderTask;

import java.util.ArrayList;
import java.util.List;

public class DodgeTask {
    private static Area center = Area.rectangular(1508,3287,1515,3275,0);
    public static void dodgeSymbols() {
        //Enable run if not already enabled
        if (!Movement.isRunEnabled()) {
            Movement.toggleRun(true);
        }

        //Get all current falling rock positions
        List<SceneObject> glowingSymbols = SceneObjects.query().ids(55209).results().asList(); //NOT EFFECT OBJECT
        List<Position> symbolPositions = new ArrayList<>();
        for (SceneObject symbol : glowingSymbols) {
            symbolPositions.add(symbol.getPosition());
        }

        int[][] potentialOffsets;

        Npc target;
        Npc body = Npcs.query().nameContains("body").actions("Attack").results().nearest();
        Npc tail = Npcs.query().ids(14014).actions("Attack").results().nearest();
        Npc boss = Npcs.query().ids(14009).actions("Attack").results().nearest();
        if (body != null) {
            potentialOffsets = decodeOffset(body.getPosition().fromInstance());
            target = body;
        } else if (tail != null) {
            potentialOffsets = decodeOffset(tail.getPosition().fromInstance());
            target = tail;
        } else if (boss != null){
            potentialOffsets = decodeOffset(boss.getPosition().fromInstance());
            target = boss;
        } else {
            OrderTask.setSubtask("No target for dodge");
            return;
        }

        //Find the first safe position
        //Might be more efficient on cpu than finding best spot
        Position safePosition = null;
        for (int[] offset : potentialOffsets) {
            int targetX = target.getPosition().getX() + offset[0];
            int targetY = target.getPosition().getY() + offset[1];
            Position potentialSafePos = new Position(targetX, targetY, 0);
            boolean isSafe = true;
            for (Position symbolPos : symbolPositions) {
                if (potentialSafePos.equals(symbolPos)) {
                    isSafe = false;
                    break; //stops if tile is not safe
                }
            }
            if (isSafe) {
                safePosition = potentialSafePos;
                break;
            }
        }
        if (safePosition != null) {
            Position currentPlayerPos = Players.self().getPosition();
            if (currentPlayerPos.equals(safePosition)) {
                shouldAttack();
                OrderTask.setSubtask("Already at safe spot: " + safePosition.getX() + "," + safePosition.getY());
                return;
            }
            if (Players.self().isMoving()) {
                OrderTask.setSubtask("Already moving");
                return;
            }
            Movement.walkTowards(safePosition);
            OrderTask.setSubtask("Dodging symbols to: " + safePosition.getX() + "," + safePosition.getY());
        }
    }
    private static int[][] decodeOffset(Position target) {
        int[][] offsets = new int[][] {{-1,0}};
        if (target.getPosition().fromInstance().equals(1530,3276,0)) {
            return new int[][] {
                    {-1, 0}, {-1, -1}, {-1, 1},
                    {-2, 0}, {-2, -1}, {-2, 1},
                    {-3, 0}, {-3, -1}, {-3, 1}};
        }
        if (target.getPosition().fromInstance().equals(1524,3277,0)) {
            return new int[][] {
                    {1, 0}, {1, -1}, {1, 1},
                    {2, 0}, {2, -1}, {2, 1},
                    {3, 0}, {3, -1}, {3, 1}};
        }
        if (target.getPosition().fromInstance().equals(1527,3273,0)) { //1527 3274 //1526 3273
            return new int[][] {
                    {0, 1}, {-1, 0}, {-1, -1},
                    {-2, -1}, {-2, 0}, {-1, 1},
                    {1, 1}, {2, 1}};
        }
        if (target.getPosition().fromInstance().equals(1524,3270,0)) { //1524 3271
            return new int[][] {
                    {0, 1}, {-1, 1}, {1, 1},
                    {0, 2}, {-1, 2}, {1, 2},
                    {0, 3}, {-1, 3}, {1, 3}};
        }
        if (target.getPosition().fromInstance().equals(1520,3273,0)) { //1520 3272
            return new int[][] {
                    {0, -1}, {-1, -1}, {1, -1},
                    {0, -2}, {-1, -2}, {1, -2},
                    {0, -3}, {-1, -3}, {1, -3}};
        }
        if (target.getPosition().fromInstance().equals(1509,3290,0)) { //1509-1515 3289 boss
            return new int[][] { //1512 3289
                    {3, -1}, {4, -1}, {2, -1},
                    {5, -1}, {1, -1}, {6, -1},
                    {7, -1}, {7, -1}, {4, -2}};
        }
        if (target.getPosition().fromInstance().equals(1502,3281,0)) { // Left tail 1502 3281 //1505 3281-3284
            return new int[][] { //1507 3284
                    {5, 2}, {5, 3}, {5, 4},
                    {4, 4}, {5, 5},
                    {4, 5}, {5, 6}};
        }
        if (target.getPosition().fromInstance().equals(1518,3281,0)) { //Right tail 1518 3281 1517 3281
            return new int[][] {//1517 3282-3284 //can attack +2 -1
                    {-3, 1}, {-3, 2}, {-2, -1},
                    {-1, -1}, {-2, 3},
                    {-1, 3}, {-3, 3}};
        }
        return offsets;
    }
    private static void shouldAttack() {
        Player self = Players.self();
        Npc body = Npcs.query().nameContains("body").actions("Attack").results().nearest();
        Npc tail = Npcs.query().nameContains("tail").actions("Attack").results().nearest();
        Npc boss = Npcs.query().ids(14009).actions("Attack").results().nearest();
        if (body != null) {
            if (self.getPosition().fromInstance().equals(1523,3276,0)) {
                body.interact("Attack");
                return;
            }
            if (self.getPosition().fromInstance().equals(1525,3277,0)) {
                body.interact("Attack");
                return;
            }
            if (self.getPosition().fromInstance().equals(1527,3274,0)) {
                body.interact("Attack");
                return;
            }
            if (self.getPosition().fromInstance().equals(1526,3273,0)) {
                body.interact("Attack");
                return;
            }
            if (self.getPosition().fromInstance().equals(1524,3271,0)) {
                body.interact("Attack");
                return;
            }
            if (self.getPosition().fromInstance().equals(1520,3272,0)) {
                body.interact("Attack");
                return;
            }
        }
        if (boss != null) {
            if (self.getPosition().fromInstance().getY()==3289
                && (self.getPosition().fromInstance().getX()>1508 && self.getPosition().fromInstance().getX()<1516)) {
                boss.interact("Attack");
                return;
            }
        }
        if (tail != null) {
            if (self.getPosition().fromInstance().equals(1520,3272,0)) {
                tail.interact("Attack");
                return;
            }
        }
    }
}