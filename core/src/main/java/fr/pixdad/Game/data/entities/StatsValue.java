package fr.pixdad.Game.data.entities;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class StatsValue {

    public enum Stats {
        ATK("ATK"), STR("STR"), DEF("DEF"), HP("HP"), HP_MAX("HP Max");

        private String label;
        Stats(String label) {this.label = label;}
    }

    public static final StatsValue ZERO = new StatsValue();

    public final int atk;
    public final int str;
    public final int def;
    public final int hpMax;

    public StatsValue() {
        this(0, 0, 0, 0);
    }
    public StatsValue(int atk, int str, int def, int hpMax) {
        this.atk = atk;
        this.str = str;
        this.def = def;
        this.hpMax = hpMax;
    }
    public StatsValue cpy() {
        return new StatsValue(atk, str, def, hpMax);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsValue statsValue = (StatsValue) o;
        return atk == statsValue.atk &&
                str == statsValue.str &&
                def == statsValue.def &&
                hpMax == statsValue.hpMax;
    }

    public StatsValue add(StatsValue value) {
        return new StatsValue(atk+value.atk, str+value.str, def+value.def, hpMax+value.hpMax);
    }
    public StatsValue opp() {
        return new StatsValue(-atk, -str, -def, -hpMax);
    }
}
