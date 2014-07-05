package malow.gladiatus.common.models.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import malow.gladiatus.common.models.ModelInterface;

public class SetCurrentlyTrainingRequest implements ModelInterface
{
    public final String sessionId;
    public final String stat;

    @JsonCreator
    public SetCurrentlyTrainingRequest(@JsonProperty("sessionId") String sessionId,
                                       @JsonProperty("stat") String stat)
    {
        this.sessionId = sessionId;
        this.stat = stat;
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