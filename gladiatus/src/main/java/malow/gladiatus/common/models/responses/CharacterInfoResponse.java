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
    public final String health;
    public final String strength;
    public final String dexterity;
    public final String intelligence;
    public final String willpower;
    public final int money;
    public final List<Ability> abilities;

    @JsonCreator
    public CharacterInfoResponse(@JsonProperty("characterName") String characterName,
                                 @JsonProperty("characterImage") String characterImage,
                                 @JsonProperty("health") String health,
                                 @JsonProperty("strength") String strength,
                                 @JsonProperty("dexterity") String dexterity,
                                 @JsonProperty("intelligence") String intelligence,
                                 @JsonProperty("willpower") String willpower,
                                 @JsonProperty("money") int money,
                                 @JsonProperty("abilities") List<Ability> abilities)
    {
        this.characterName = characterName;
        this.characterImage = characterImage;
        this.health = health;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.willpower = willpower;
        this.money = money;
        this.abilities = abilities;
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
