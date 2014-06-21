package malow.gladiatus.common.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ability
{
    public final int id;
    public final String name;
    public final String description;
    public final String tags;

    @JsonCreator
    public Ability(@JsonProperty("id") int id, @JsonProperty("name") String name,
                   @JsonProperty("description") String description, @JsonProperty("tags") String tags)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof Ability){
            Ability ptr = (Ability) v;
            retVal = ptr.id == this.id;
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
        return hash;
    }
}
