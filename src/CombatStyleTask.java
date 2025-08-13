import org.rspeer.commons.logging.Log;
import org.rspeer.game.adapter.component.inventory.Equipment;
import org.rspeer.game.combat.Combat;
import org.rspeer.game.component.Inventories;
import org.rspeer.game.component.tdi.Skill;
import org.rspeer.game.script.Task;
import org.rspeer.game.script.TaskDescriptor;
import org.rspeer.game.component.tdi.Skills;



@TaskDescriptor(name = "Combat Styles")
public class CombatStyleTask extends Task {
    private static Integer attack;
    private static Integer strength;
    private static Integer defence;

    @Override
    public boolean execute() { //Focuses attack first
        attack = Skills.getLevel(Skill.ATTACK);
        strength = Skills.getLevel(Skill.STRENGTH);
        defence = Skills.getLevel(Skill.DEFENCE);
        if (Combat.isAutoRetaliateOn()) {
            Combat.toggleAutoRetaliate(false);
        }
        Equipment equipment = Inventories.equipment();
        if (!equipment.contains(iq -> iq.nameContains("Dual macuahuitl").results())) {
            return false;
        }//avoids switching styles if not using double hit weapon
        if (strength < (attack + 4) && strength < 99) {
            if (Combat.getSelectedStyle()!= Combat.AttackStyle.AGGRESSIVE) {
                Combat.select(Combat.AttackStyle.AGGRESSIVE);
            }
            return true;
        }

        if (attack < (defence + 4) && attack < 99) {
            if (Combat.getSelectedStyle()!= Combat.AttackStyle.ACCURATE) {
                Combat.select(Combat.AttackStyle.ACCURATE);
            }
            return true;
        }

        if (Combat.getSelectedStyle()!= Combat.AttackStyle.DEFENSIVE) {
            Combat.select(Combat.AttackStyle.DEFENSIVE);
        }
        return true;
    }
}