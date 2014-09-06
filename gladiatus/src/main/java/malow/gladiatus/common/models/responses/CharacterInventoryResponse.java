package malow.gladiatus.common.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import malow.gladiatus.common.models.Item;
import malow.gladiatus.common.models.ModelInterface;

public class CharacterInventoryResponse implements ModelInterface
{
    public final List<Item> items;

    @JsonCreator
    public CharacterInventoryResponse(@JsonProperty("abilities") List<Item> items)
    {
        this.items = items;
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
