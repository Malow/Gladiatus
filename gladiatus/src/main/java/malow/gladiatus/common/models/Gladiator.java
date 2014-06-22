package malow.gladiatus.common.models;

import java.util.ArrayList;
import java.util.List;

public class Gladiator
{
    public String name = "";
    public String image = "0";
    public List<Ability> abilities = new ArrayList<Ability>();

    public Stats baseStats = new Stats();
    public Stats extraStats = new Stats(0, 0, 0, 0, 0);
    public Stats totalStats = new Stats();

    public int statPointsLeft = 10;

    public Gladiator()
    {

    }
}
