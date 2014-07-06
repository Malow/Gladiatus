package malow.gladiatus.common;

public class Training
{
    // Time required in seconds to get to 2.0 in a skill when you're currently 1.0.
    public static final float TIME_REQUIRED_SIMPLE = 100.0f;
    public static final float EXPONENTIAL_INCREASER = 1.2f;

    public static final float STAT_DECAY_PERCENT_PER_DAY = 5.0f;

    // Required time in seconds for training up a stat at current skill:
    /* public static List<Integer> skillRequirements = Arrays.asList(
            100,    //1
            250,    //2
            500,    //3
            1000,   //4
            2000,   //5
            4000);  //6
            */

    public static float GetTimeToSkill(float currentSkill)
    {
        int currentSkillInt = (int) currentSkill;
        float percentCompleted = currentSkill - ((int) currentSkill);
        float timeRequired = (float) (TIME_REQUIRED_SIMPLE * Math.pow(EXPONENTIAL_INCREASER, currentSkillInt + 1));
        float timeLeft = timeRequired * (1.0f - percentCompleted);
        return timeLeft;
    }

    public static String GetTimeToSkillString(float currentSkill)
    {
        float timeLeft = GetTimeToSkill(currentSkill);
        int hours = (int) (timeLeft / 3600.0f);
        int minutes = (int) ((timeLeft - hours * 3600) / 60.0f);
        int seconds = (int) (timeLeft - hours * 3600 - minutes * 60);

        String time = "";
        if(hours > 0)
            time += hours + "h ";
        if(minutes > 0)
            time += minutes + "m ";
        if(seconds > 0)
            time += seconds + "s ";

        return time;
    }
}
