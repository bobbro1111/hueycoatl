import org.rspeer.commons.math.Distance;
import org.rspeer.game.adapter.scene.EffectObject;
import org.rspeer.game.movement.Movement;
import org.rspeer.game.position.Position;
import org.rspeer.game.position.area.Area;
import org.rspeer.game.scene.*;
import prepare.OrderTask;

import java.util.ArrayList;
import java.util.List;
//effect left 2982 2984 2981 2977 2981 2999
//effect right 2978 2982 2984 2981 2977 2981 2999
//
public class Wave {
    private static final int[] WAVE_IDS = {2977, 2978, 2979, 2981, 2982, 2983, 2984, 2981, 2999};
    private static final int[] TAIL_IDS = {14014, 14015};
    public static final Area DODGE_AREA = Area.rectangular(1504, 3285, 1519, 3289,0);
    public static void dodgeWave() {
        //Enable run if not already enabled
        if (!Movement.isRunEnabled()) {
            Movement.toggleRun(true);
        }
        if (!Players.self().getPosition().fromInstance().within(DODGE_AREA)) {
            Movement.walkTowards(new Position(Players.self().getX(), Players.self().getY()+3));
        }
        OrderTask.setSubtask("Dodging wave");

        //Get all current falling rock positions
        List<EffectObject> waveEffect = EffectObjects.query().ids(WAVE_IDS).results().asList();
        List<Position> wavePositions = new ArrayList<>();
        for (EffectObject wave : waveEffect) {
            wavePositions.add(wave.getPosition());
        }
        Integer playerX = Players.self().getX();
        Integer playerY = Players.self().getY();
        for (Position wavePos : wavePositions) {
            Integer tailX = Npcs.query().ids(TAIL_IDS).results().first().getPosition().fromInstance().getX();
            if (tailX==1502) {
                if (Distance.between(wavePos, Players.self().getPosition()) < 4) {
                    Movement.walkTowards(Position.from(playerX - 2, playerY - 2));
                    TailTask.setDodge();
                    break;
                }
            } else {
                if (Distance.between(wavePos, Players.self().getPosition()) < 4) {
                    Movement.walkTowards(Position.from(playerX + 2, playerY - 2));
                    TailTask.setDodge();
                    break;
                }
            }
        }
    }
}