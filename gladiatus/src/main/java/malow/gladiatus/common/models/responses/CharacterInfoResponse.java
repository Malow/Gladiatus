package malow.gladiatus.common.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import malow.gladiatus.common.models.ModelInterface;

public class CharacterInfoResponse implements ModelInterface
{
    public final String characterName;
    public final String characterImage;
    public final String health;
    public final String armor;
    public final String strength;
    public final String dexterity;
    public final String initiative;

    @JsonCreator
    public CharacterInfoResponse(@JsonProperty("characterName") String characterName,
                                 @JsonProperty("characterImage") String characterImage,
                                 @JsonProperty("health") String health,
                                 @JsonProperty("armor") String armor,
                                 @JsonProperty("strength") String strength,
                                 @JsonProperty("dexterity") String dexterity,
                                 @JsonProperty("initiative") String initiative)
    {
        this.characterName = characterName;
        this.characterImage = characterImage;
        this.health = health;
        this.armor = armor;
        this.strength = strength;
        this.dexterity = dexterity;
        this.initiative = initiative;
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
