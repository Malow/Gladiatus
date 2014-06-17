package malow.gladiatus.common.models.responses;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import malow.gladiatus.common.models.ModelInterface;

public class LoginResponse implements ModelInterface
{
    public final String sessionId;
    public final String errorCode;

    @JsonCreator
    public LoginResponse(@JsonProperty("sessionId") String sessionId, @JsonProperty("errorCode") String errorCode)
    {
        this.sessionId = sessionId;
        this.errorCode = errorCode;
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
