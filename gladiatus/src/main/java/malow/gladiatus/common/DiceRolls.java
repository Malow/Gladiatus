package malow.gladiatus.common;

import malow.gladiatus.common.models.responses.CharacterInfoResponse;

public class DiceRolls
{
    public static int GetDamageDice(CharacterInfoResponse response)
    {
        return 10;
    }

    public static int GetHitDice(CharacterInfoResponse response)
    {
        return 10;
    }

    public static int GetDamageBonus(CharacterInfoResponse response)
    {
        return 7;
    }

    public static int GetHitBonus(CharacterInfoResponse response)
    {
        return 4;
    }
}
