package malow.gladiatus.common.models.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import malow.gladiatus.common.models.ModelInterface;

public class RegisterRequest implements ModelInterface
{
    public final String username;
    public final String password;
    public final String email;

    @JsonCreator
    public RegisterRequest(@JsonProperty("username") String username, @JsonProperty("password") String password,
                           @JsonProperty("email") String email)
    {
        this.username = username;
        this.password = password;
        this.email = email;
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
