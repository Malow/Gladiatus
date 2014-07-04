package malow.gladiatus.common.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import malow.gladiatus.common.models.Ability;
import malow.gladiatus.common.models.ModelInterface;

public class CharacterInfoResponse implements ModelInterface
{
    public final String characterName;
    public final String characterImage;
    public final int level;
    public final int xp;
    public final String status;
    public final float currentHealth;
    public final float health;
    public final float strength;
    public final float dexterity;
    public final float intelligence;
    public final float willpower;
    public final int money;
    public final List<Ability> abilities;
    public final String currentlyTraining;

    @JsonCreator
    public CharacterInfoResponse(@JsonProperty("characterName") String characterName,
                                 @JsonProperty("characterImage") String characterImage,
                                 @JsonProperty("level") int level,
                                 @JsonProperty("xp") int xp,
                                 @JsonProperty("status") String status,
                                 @JsonProperty("currentHealth") float currentHealth,
                                 @JsonProperty("health") float health,
                                 @JsonProperty("strength") float strength,
                                 @JsonProperty("dexterity") float dexterity,
                                 @JsonProperty("intelligence") float intelligence,
                                 @JsonProperty("willpower") float willpower,
                                 @JsonProperty("money") int money,
                                 @JsonProperty("abilities") List<Ability> abilities,
                                 @JsonProperty("currentlyTraining") String currentlyTraining)
    {
        this.characterName = characterName;
        this.characterImage = characterImage;
        this.level = level;
        this.xp = xp;
        this.status = status;
        this.currentHealth = currentHealth;
        this.health = health;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.willpower = willpower;
        this.money = money;
        this.abilities = abilities;
        this.currentlyTraining = currentlyTraining;
    }

    @Override
    public String toNetworkString()
    {
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try
        {
             str = mapper.writeValueAsString(this);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return str;
    }
}
