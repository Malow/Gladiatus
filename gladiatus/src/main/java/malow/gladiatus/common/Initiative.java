package malow.gladiatus.common;

import malow.gladiatus.common.models.responses.CharacterInfoResponse;

public class Initiative
{
    public static float GetInitiative(float dexterity)
    {
        return dexterity * 0.5f;
    }

    public static float GetInitiative(CharacterInfoResponse character)
    {
        return GetInitiative(Float.valueOf(character.dexterity));
    }
}
