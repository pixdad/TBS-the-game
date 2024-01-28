package fr.pixdad.Game.battle.core;

import fr.pixdad.Game.data.entities.GameLevel;
import fr.pixdad.Game.battle.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


//TODO: Refine with AIPlayer, and PlayablePlayer ?
/**
 * Represent a player.
 */
public abstract class Player {

    String name;

    private final List<Fighter> team = new ArrayList<Fighter>(); //all the fighters
    public final List<Fighter> fightersToAct = new ArrayList<Fighter>(); //selectable fighters remaining

    private Fighter source;
    private BoardCellObject target;
    private Action<Object, Object> action;

    public Player(String name) {
        this.name = name;
    }

    public void initTurn() {
        fightersToAct.clear();
        fightersToAct.addAll(getTeam(false));
    }

    public List<Fighter> getTeam(boolean deadIncluded) {
        return deadIncluded ?
                team :
                team.stream()
                .filter(x -> !x.isDead())
                .collect(Collectors.toList());
    }

    public void setFightersVisibility(Boolean isVisible) {
        for (Fighter fighter : fightersToAct) {
            fighter.isVisible = isVisible;
        }
    }

    public void addTeamMember(Fighter fighter) {
        team.add(fighter);
    }

    public void fighterHasActed(Fighter fighter) {
        if (fighter.player == this) {
            fightersToAct.remove(fighter);
        }
    }

    //region player actions
    public abstract Fighter getSource(GameLevel level);
    public abstract Fighter computeTarget(GameLevel level);
    public abstract void computeAction(GameLevel level);

    //public abstract void onClickOnFighter();
}
