package malow.gladiatus.common.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import malow.gladiatus.common.models.Ability;
import malow.gladiatus.common.models.ModelInterface;


public class BasicAbilitiesResponse implements ModelInterface
{
    public final List<Ability> abilities;

    @JsonCreator
    public BasicAbilitiesResponse(@JsonProperty("sessionId") List<Ability> abilities)
    {
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
