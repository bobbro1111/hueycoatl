import org.rspeer.commons.logging.Log;
import org.rspeer.game.adapter.scene.EffectObject;
import org.rspeer.game.adapter.scene.Npc;
import org.rspeer.game.adapter.scene.SceneObject;
import org.rspeer.game.component.Inventories;
import org.rspeer.game.component.Item;
import org.rspeer.game.component.tdi.Prayers;
import org.rspeer.game.component.tdi.Skill;
import org.rspeer.game.component.tdi.Skills;
import org.rspeer.game.effect.Health;
import org.rspeer.game.movement.Movement;
import org.rspeer.game.position.Position;
import org.rspeer.game.scene.EffectObjects;
import org.rspeer.game.scene.Npcs;
import org.rspeer.game.scene.Players;
import org.rspeer.game.scene.SceneObjects;
import org.rspeer.game.script.Task;
import org.rspeer.game.script.TaskDescriptor;
import prepare.OrderTask;
import data.HueyData;


@TaskDescriptor(name = "Smacking big boy's tails")
public class TailTask extends Task {

    private static Boolean shouldDodge = false;
    @Override
    public boolean execute() {
        Npc tail = Npcs.query().ids(14014).actions("Attack").results().nearest();
        if (tail==null) {
            return false;
        }
        OrderTask.setTask("Tail phase");

        SceneObject glowingSymbols = SceneObjects.query().ids(HueyData.GLOWING_SYMBOL).results().first();
        EffectObject flash = EffectObjects.query().ids(HueyData.FLASH).results().first();
        if (glowingSymbols != null && flash == null) {
            DodgeTask.dodgeSymbols();
            return true;
        }

        EffectObject wave = EffectObjects.query().ids(HueyData.WAVE_IDS).results().first();
        if (wave != null && shouldDodge) {
            Wave.dodgeWave();
            return true;
        } else if (wave == null && !Players.self().isMoving()) {
            shouldDodge = true;
        }

        if (tail != null) {
            if (tail.getAnimationId()==11721) {
                Movement.walkTowards(new Position(Players.self().getX(), Players.self().getY() + 3));
                return true;
            }
            tail.interact("Attack");
            OrderTask.setSubtask("Attacking tail");
            return true;
        }

        return true;
    }
    public static void setDodge() {
        shouldDodge = false;
    }

}