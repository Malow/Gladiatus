package malow.gladiatus.common;

import java.util.Arrays;
import java.util.List;

public class Level
{
    public static List<Integer> levelRequirements = Arrays.asList(
        100,    //1
        250,    //2
        500,    //3
        1000,   //4
        2000,   //5
        4000);  //6

    public static int GetXpRequiredToLevelUp(int level)
    {
        return levelRequirements.get(level + 1);
    }
}
