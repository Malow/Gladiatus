package malow.gladiatus.common.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item
{
    public final String name;
    public final String type;

    @JsonCreator
    public Item(@JsonProperty("name") String name,
                @JsonProperty("type") String type)
    {
        this.name = name;
        this.type = type;
    }
}
