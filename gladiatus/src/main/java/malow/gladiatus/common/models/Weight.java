package malow.gladiatus.common.models;

public class Weight
{
    public final static float STRENGTH_MODIFIER = 10.0f;
    public final static float LIGHT_ENCUMBRENCE_MODIFIER = 0.3f;
    public final static float HEAVY_ENCUMBRENCE_MODIFIER = 0.6f;

    public static float GetCarryLimitBeforeLightEncumbrence(float strength)
    {
        return (strength * STRENGTH_MODIFIER) * LIGHT_ENCUMBRENCE_MODIFIER;
    }

    public static float GetCarryLimitBeforeHeavyEncumbrence(float strength)
    {
        return (strength * STRENGTH_MODIFIER) * HEAVY_ENCUMBRENCE_MODIFIER;
    }

    public static float GetCarryLimitMax(float strength)
    {
        return strength * STRENGTH_MODIFIER;
    }
}
