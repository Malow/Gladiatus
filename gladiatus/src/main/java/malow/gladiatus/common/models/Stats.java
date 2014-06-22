package malow.gladiatus.common.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Stats
{
    public float health;
    public float strength;
    public float dexterity;
    public float intelligence;
    public float willpower;


    @JsonCreator
    public Stats(@JsonProperty("health") float health,
                 @JsonProperty("strength") float strength,
                 @JsonProperty("dexterity") float dexterity,
                 @JsonProperty("intelligence") float intelligence,
                 @JsonProperty("willpower") float willpower)
    {
        this.health = health;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.willpower = willpower;
    }

    public Stats()
    {

    }
}
