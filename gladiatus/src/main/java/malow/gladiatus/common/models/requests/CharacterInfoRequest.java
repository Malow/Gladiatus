package malow.gladiatus.common.models.requests;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import malow.gladiatus.common.models.ModelInterface;

public class CharacterInfoRequest implements ModelInterface
{
    public final String sessionId;

    @JsonCreator
    public CharacterInfoRequest(@JsonProperty("sessionId") String sessionId)
    {
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
