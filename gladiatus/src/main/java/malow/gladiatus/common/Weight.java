package malow.gladiatus.common;

import malow.gladiatus.Globals;
import malow.gladiatus.R;
import malow.gladiatus.common.models.responses.CharacterInfoResponse;

public class Weight
{
    public final static float STRENGTH_MODIFIER = 10.0f;
    public final static float LIGHT_ENCUMBRANCE_MODIFIER = 0.3f;
    public final static float HEAVY_ENCUMBRANCE_MODIFIER = 0.6f;

    public static float GetCarryLimitBeforeLightEncumbrance(float strength)
    {
        return (strength * STRENGTH_MODIFIER) * LIGHT_ENCUMBRANCE_MODIFIER;
    }

    public static float GetCarryLimitBeforeHeavyEncumbrance(float strength)
    {
        return (strength * STRENGTH_MODIFIER) * HEAVY_ENCUMBRANCE_MODIFIER;
    }

    public static float GetCarryLimitMax(float strength)
    {
        return strength * STRENGTH_MODIFIER;
    }

    public static float GetNextCarryLimit(float strength, CharacterInfoResponse response)
    {
        float currentWeight = GetWeight(response);
        float light = GetCarryLimitBeforeLightEncumbrance(strength);
        float heavy = GetCarryLimitBeforeHeavyEncumbrance(strength);
        float max = GetCarryLimitMax(strength);

        if(currentWeight < light)
            return light;
        else if(currentWeight < heavy)
            return heavy;
        else
            return max;
    }

    public static String GetEncumbrance(float strength, CharacterInfoResponse response)
    {
        float currentWeight = GetWeight(response);
        float light = GetCarryLimitBeforeLightEncumbrance(strength);
        float heavy = GetCarryLimitBeforeHeavyEncumbrance(strength);
        float max = GetCarryLimitMax(strength);

        if(currentWeight < light)
            return Globals.mainActivity.getString(R.string.no_encumbrance);
        else if(currentWeight < heavy)
            return Globals.mainActivity.getString(R.string.light_encumbrance);
        else
            return Globals.mainActivity.getString(R.string.heavy_encumbrance);
    }

    public static float GetWeight(CharacterInfoResponse response)
    {
        return 70.0f;
    }
}
