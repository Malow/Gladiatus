package malow.gladiatus.common.models;

public class Initiative
{
    public static float GetInitiative(float dexterity)
    {
        return dexterity * 0.5f;
    }
}
