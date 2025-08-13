package cots;

import cots.COTSData;
import org.rspeer.game.adapter.scene.Npc;
import org.rspeer.game.component.*;
import org.rspeer.game.component.tdi.Quest;
import org.rspeer.game.component.tdi.Quests;
import org.rspeer.game.movement.Movement;
import org.rspeer.game.position.Position;
import org.rspeer.game.scene.Npcs;
import org.rspeer.game.scene.Players;
import org.rspeer.game.script.Task;
import org.rspeer.game.script.TaskDescriptor;
import prepare.OrderTask;


@TaskDescriptor(name = "please god kill me", blocking = true, blockIfSleeping = true)
public class COTSQuest extends Task {
    private static Position tempPos = Position.from(3233,3427);
    private static Boolean[] actions = {false,false,false,false,false};

    @Override
    public boolean execute() {
        if (Quests.getProgress(Quest.Members.CHILDREN_OF_THE_SUN).equals(Quest.Progress.FINISHED)) {return false;}
        if (Dialog.canContinue()) {return Dialog.processContinue();}
        if (Dialog.isProcessing()) {return true;}
        if (Dialog.isViewingChatOptions()) {
            return Dialog.process(
                    "Yes.",
                    "When will this delegation arrive?"
            );
        }
        if (!Movement.isRunEnabled()) {Movement.toggleRun(true);}

        Integer state = Quests.getState(Quest.Members.CHILDREN_OF_THE_SUN);
        if (state==12) {
            OrderTask.setSubtask("Marking guards");
            if (!actions[0]) {
                Npc guard1Marked = Npcs.query().ids(COTSData.MARKED_GUARD_1).results().nearest();
                if (guard1Marked!=null) {
                    return actions[0] = true;
                }
                Npc guard1 = Npcs.query().ids(COTSData.GUARD_1).results().nearest();
                if (guard1!=null) {
                    guard1.interact("Mark");
                    sleepUntil(() -> guard1==null, 10);
                } else {
                    Movement.walkTo(new Position(3208,3425));
                }
                return true;
            }
            if (!actions[1]) {
                Npc guard2Marked = Npcs.query().ids(COTSData.MARKED_GUARD_2).results().nearest();
                if (guard2Marked!=null) {
                    return actions[1] = true;
                }
                Npc guard2 = Npcs.query().ids(COTSData.GUARD_2).results().nearest();
                if (guard2!=null) {
                    guard2.interact("Mark");
                    sleepUntil(() -> guard2==null, 10);
                } else {
                    Movement.walkTo(Position.from(3220,3429));
                }
                return true;
            }
            if (!actions[2]) {
                Npc guard3Marked = Npcs.query().ids(COTSData.MARKED_GUARD_3).results().nearest();
                if (guard3Marked!=null) {
                    return actions[2] = true;
                }
                Npc guard3 = Npcs.query().ids(COTSData.GUARD_3).results().nearest();
                if (guard3!=null) {
                    guard3.interact("Mark");
                    sleepUntil(() -> guard3==null, 10);
                } else {
                    Movement.walkTo(Position.from(3235,3430));
                }
                return true;
            }
            if (!actions[3]) {
                Npc guard4Marked = Npcs.query().ids(COTSData.MARKED_GUARD_4).results().nearest();
                if (guard4Marked!=null) {
                    return actions[3] = true;
                }
                Npc guard4 = Npcs.query().ids(COTSData.GUARD_4).results().nearest();
                if (guard4!=null) {
                    guard4.interact("Mark");
                    sleepUntil(() -> guard4==null, 10);
                } else {
                    Movement.walkTo(Position.from(3242,3430));
                }
                return true;
            }
            if (!actions[4]) {
                Npc tobyn = Npcs.query().ids(COTSData.TOBYN_ID).results().nearest();
                if (tobyn!=null) {
                    return tobyn.interact("Talk-to");
                } else {
                    Movement.walkTowards(Position.from(3212,3436));
                }
                return true;
            }
        }
        if (state==10) {
            Npc tobyn = Npcs.query().ids(COTSData.TOBYN_ID).results().nearest();
            if (tobyn!=null) {
                tobyn.interact("Talk-to");
                actions[0] = true;
            } else {
                Movement.walkTowards(Position.from(3212,3436));
            }
            return true;
        }
        if (state==6) { //Following sequence
            Npc guard = Npcs.query().ids(COTSData.SNEAKING_GUARD).results().nearest();
            if (guard!=null) {
                if (guard.getPosition().fromInstance().equals(COTSData.GUARD_POSITION_1)) {
                    tempPos = COTSData.HIDE_POSITION_1;
                } else if (guard.getPosition().fromInstance().equals(COTSData.GUARD_POSITION_2)) {
                    tempPos = COTSData.HIDE_POSITION_2;
                } else if (guard.getPosition().fromInstance().equals(COTSData.GUARD_POSITION_3)) {
                    tempPos = COTSData.HIDE_POSITION_3;
                } else if (guard.getPosition().fromInstance().equals(COTSData.GUARD_POSITION_4)) {
                    tempPos = COTSData.HIDE_POSITION_4;
                } else if (guard.getPosition().fromInstance().equals(COTSData.Guard_POSITION_5)) {
                    tempPos = COTSData.HIDE_POSITION_5;
                }

                if (!Players.self().getPosition().fromInstance().equals(tempPos)) {
                    Movement.walkTowards(tempPos.getInstancePositions().get(0));
                }
                OrderTask.setSubtask("Following guard");
                return true;
            }
        }
        if (state==0 || state==2 || state==6) {
            Npc alina = Npcs.query().nameContains("Alina").results().nearest();
            if (alina==null) {
                OrderTask.setSubtask("Running to start");
                return Movement.walkTo(COTSData.ALINA_POSITION);
            }
            alina.interact("Talk-to");
            OrderTask.setSubtask("Starting dialog");
        }
        return true;
    }
}