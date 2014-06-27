package malow.gladiatus.common;

public class Money
{
    public static String GetMoneyAsString(int money)
    {
        int gold = money / 10000;
        int silver = (money - gold * 10000) / 100;
        int copper = (money - gold * 10000 - silver * 100);
        return gold + "g " + silver + "s " + copper + "c ";
    }
}
