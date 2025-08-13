package data;

import org.rspeer.game.position.Position;
import org.rspeer.game.position.area.Area;

public interface HueyData {
    Area COMBAT_AREA = Area.polygonal(
            Position.from(1502,3293),
            Position.from(1521,3293),
            Position.from(1521,3286),
            Position.from(1534,3286),
            Position.from(1534,3268),
            Position.from(1507,3268),
            Position.from(1507,3275),
            Position.from(1502,3275)
    );

    Area START_AREA = Area.rectangular(1523, 3297, 1533, 3287,0);

    int NPC_PET_ID = 14045;
    int ITEM_PET_ID = 30152;
    int FLAG_1 = 55203; //Normal
    int FLAG_2 = 55204; //Instance

    int[] WAVE_IDS = {2977, 2978, 2979, 2981, 2982, 2983, 2984, 2981, 2999};
    int GLOWING_SYMBOL = 55209;
    int FLASH = 3001;

    int MAGIC_ATTACK = 2975;
    int RANGE_ATTACK = 2972;
    int MELEE_ATTACK = 2969;
}