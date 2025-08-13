import data.HueyData;
import org.rspeer.commons.logging.Log;
import org.rspeer.game.Game;
import org.rspeer.game.adapter.component.inventory.Bank;
import org.rspeer.game.adapter.scene.Npc;
import org.rspeer.game.component.tdi.Prayer;
import org.rspeer.game.component.tdi.Prayers;
import org.rspeer.game.component.tdi.Skill;
import org.rspeer.game.component.tdi.Skills;
import org.rspeer.game.position.Position;
import org.rspeer.game.position.area.Area;
import org.rspeer.game.scene.Npcs;
import org.rspeer.game.scene.Players;
import org.rspeer.game.scene.Projectiles;
import org.rspeer.game.script.Task;
import org.rspeer.game.script.TaskDescriptor;

import java.util.List;

//Remove combat prayers?
//turn off combat prayers if not attacking

@TaskDescriptor(name = "Prayer Management")
public class PrayerTask extends Task {
    private static Prayer combatPrayer;
    private static int timer = 0;
    private static int[] enemies = new int[]{14017,14014,14009};
    Area combatArea = Area.polygonal(
            Position.from(1502,3293),
            Position.from(1521,3293),
            Position.from(1521,3286),
            Position.from(1534,3286),
            Position.from(1534,3268),
            Position.from(1507,3268),
            Position.from(1507,3275),
            Position.from(1502,3275)
    );
    @Override
    public boolean execute() {
        if (Bank.isOpen()) {return true;}

        Npc enemy = Npcs.query().ids(enemies).actions("Attack").results().first();
        if (!Players.self().getPosition().fromInstance().within(combatArea) || enemy==null) {
            allPrayerOff();
            return true;
        }
        //Prayer flick if targeting?
        if (Players.self().getPosition().fromInstance().within(combatArea) && Players.self().getTarget()!=null) { //Melee prayers
            if (Skills.getLevel(Skill.PRAYER) > 69 && Prayers.isUnlocked(Prayer.Modern.PIETY)) {
                if (!Prayers.isActive(Prayer.Modern.PIETY)) {
                    Prayers.select(true, Prayer.Modern.PIETY);
                }
            } else if (Skills.getLevel(Skill.PRAYER) > 33) {
                if (!Prayers.isActive(Prayer.Modern.INCREDIBLE_REFLEXES)) {
                    Prayers.select(true, Prayer.Modern.INCREDIBLE_REFLEXES);
                }
                if (!Prayers.isActive(Prayer.Modern.ULTIMATE_STRENGTH)) {
                    Prayers.select(true, Prayer.Modern.ULTIMATE_STRENGTH);
                }
            }
        }

        if (Projectiles.query().ids(new int[]{2969,2972,2975}).results().nearest()!=null) {
            timer = Game.getTick() + 1;
        }

        if (Projectiles.query().ids(HueyData.MAGIC_ATTACK).results().nearest()!=null && !Prayers.isActive(Prayer.Modern.PROTECT_FROM_MAGIC)) {
            Prayers.select(true, Prayer.Modern.PROTECT_FROM_MAGIC);
        }

        Boolean missiles = Prayers.isActive(Prayer.Modern.PROTECT_FROM_MISSILES);
        if (Projectiles.query().ids(HueyData.RANGE_ATTACK).results().nearest()!=null && !Prayers.isActive(Prayer.Modern.PROTECT_FROM_MISSILES)) {
            Prayers.select(true, Prayer.Modern.PROTECT_FROM_MISSILES);
        }

        Boolean melee = Prayers.isActive(Prayer.Modern.PROTECT_FROM_MELEE);
        if (Projectiles.query().ids(HueyData.MELEE_ATTACK).results().nearest()!=null && !Prayers.isActive(Prayer.Modern.PROTECT_FROM_MELEE)) {
            Prayers.select(true, Prayer.Modern.PROTECT_FROM_MELEE);
        }
        if (!Prayers.getActive().isEmpty())
            Prayers.flick(Prayers.getActive());
        return true;
    }

    private static void allPrayerOff() {
        List<Prayer> active = Prayers.getActive();
        if (active.isEmpty()) {
            return;
        }
        for (Prayer p : active) {
            Prayers.toggle(false, p);
        }
    }
}

//55209 floor damage
//2975 blue
//2972 green
//2969 red
