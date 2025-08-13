import org.rspeer.game.movement.Movement;
import org.rspeer.game.position.Position;
import org.rspeer.game.scene.*;
import prepare.OrderTask;

//effect left 2982 2984 2981 2977 2981 2999
//effect right 2978 2982 2984 2981 2977 2981 2999
//
public class WaveBoss {
    private static Position safePos = new Position(1517,3289);
    public static void dodgeWave() {
        //Enable run if not already enabled
        if (!Movement.isRunEnabled()) {
            Movement.toggleRun(true);
        }

        OrderTask.setSubtask("Using safe spot"); //1516 3289 right safe //1508 3289

        Integer tailX = Npcs.query().ids(new int[]{14014, 14015}).results().first().getPosition().fromInstance().getX();
        if (tailX==1502) {
            safePos = new Position(1516,3289);
        } else {
            safePos = new Position(1508,3289);
        }
        if (!Players.self().getPosition().fromInstance().equals(safePos)) {
            Movement.walkTowards(safePos.getInstancePositions().get(0));
        }
        return;
    }
}