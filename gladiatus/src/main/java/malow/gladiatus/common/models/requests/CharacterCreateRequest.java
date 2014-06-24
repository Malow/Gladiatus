package malow.gladiatus.common.models.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import malow.gladiatus.common.models.Ability;
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.Stats;

public class CharacterCreateRequest implements ModelInterface
{
    public final String characterName;
    public final String characterImage;
    public final Stats stats;
    public final List<Integer> abilities;
    public final String sessionId;

    @JsonCreator
    public CharacterCreateRequest(@JsonProperty("characterName") String characterName,
                                 @JsonProperty("characterImage") String characterImage,
                                 @JsonProperty("stats") Stats stats,
                                 @JsonProperty("abilities") List<Integer> abilities,
                                 @JsonProperty("sessionId") String sessionId)
    {
        this.characterName = characterName;
        this.characterImage = characterImage;
        this.stats = stats;
        this.abilities = abilities;
        this.sessionId = sessionId;
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