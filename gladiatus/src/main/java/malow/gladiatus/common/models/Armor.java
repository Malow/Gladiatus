package malow.gladiatus.common.models;

public class Armor
{
    public final static float BASE_ARMOR = 10.0f;
    public final static float DEXTERITY_MODIFIER = 0.2f;

    public static float GetArmor(float dexterity)
    {
        return BASE_ARMOR + dexterity * DEXTERITY_MODIFIER;
    }
}
