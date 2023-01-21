package fr.pixdad.Game.data.entities;

public class Equipment {

    public enum TYPE {
        CHEST, LEGS, HEAD, NECK, FINGER, HANDS
    }

    public StatsValue possessedStatsValue = new StatsValue();
    public StatsValue equipedStatsValue = new StatsValue();
    public StatsValue usedStatsValue = new StatsValue();

}
