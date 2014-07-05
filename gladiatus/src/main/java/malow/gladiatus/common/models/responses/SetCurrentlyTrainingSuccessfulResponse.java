package malow.gladiatus.common.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import malow.gladiatus.common.models.ModelInterface;

public class SetCurrentlyTrainingSuccessfulResponse implements ModelInterface
{

    @JsonCreator
    public SetCurrentlyTrainingSuccessfulResponse()
    {
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