package malow.gladiatus.common.models;


import malow.gladiatus.common.models.responses.CharacterInfoResponse;

public class Armor
{
    public final static float BASE_ARMOR = 10.0f;
    public final static float DEXTERITY_MODIFIER = 0.2f;

    public static float GetArmor(float dexterity)
    {
        return BASE_ARMOR + dexterity * DEXTERITY_MODIFIER;
    }

    public static float GetArmor(CharacterInfoResponse character)
    {
        return GetArmor(Float.valueOf(character.dexterity));
    }
}
