import data.HueyData;
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
import org.rspeer.game.scene.EffectObjects;
import org.rspeer.game.scene.Npcs;
import org.rspeer.game.scene.Players;
import org.rspeer.game.scene.SceneObjects;
import org.rspeer.game.script.Task;
import org.rspeer.game.script.TaskDescriptor;
import prepare.OrderTask;


@TaskDescriptor(name = "Smacking THE big boy")
public class BossTask extends Task {

    @Override
    public boolean execute() {
        Npc boss = Npcs.query().ids(14009).results().nearest();
        Npc tail = Npcs.query().ids(14014).actions("Attack").results().nearest();
        Npc body = Npcs.query().nameContains("Hueycoatl body").results().nearest();

        if (tail!=null || body!=null || boss==null) {
            return false;
        }
        OrderTask.setTask("Boss phase");

        SceneObject glowingSymbols = SceneObjects.query().ids(HueyData.GLOWING_SYMBOL).results().first();
        EffectObject flash = EffectObjects.query().ids(HueyData.FLASH).results().first();
        if (glowingSymbols != null && flash == null) {
            DodgeTask.dodgeSymbols();
            return true;
        }

        EffectObject wave = EffectObjects.query().ids(HueyData.WAVE_IDS).results().nearest();
        if (wave != null) {
            WaveBoss.dodgeWave();
            return true;
        }

        boss.interact("Attack");
        OrderTask.setSubtask("Attacking boss");
        return true;
    }
}