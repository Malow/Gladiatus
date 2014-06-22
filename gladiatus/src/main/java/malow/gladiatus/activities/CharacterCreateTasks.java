package malow.gladiatus.activities;


import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import malow.gladiatus.Globals;
import malow.gladiatus.NetworkClient;
import malow.gladiatus.R;
import malow.gladiatus.common.models.Ability;
import malow.gladiatus.common.models.Armor;
import malow.gladiatus.common.models.Initiative;
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.BasicAbilitiesRequest;
import malow.gladiatus.common.models.requests.CharacterCreateRequest;
import malow.gladiatus.common.models.responses.BasicAbilitiesResponse;
import malow.gladiatus.common.models.responses.CharacterCreationSuccessfulResponse;
import malow.malowlib.RandomNumberGenerator;
import malow.malowlib.RequestResponseClient;

public class CharacterCreateTasks
{
    public static void CreateCharacter(final CharacterCreateRequest request)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids)
            {
                ModelInterface response = null;
                try
                {
                    response = NetworkClient.sendAndReceive(request);
                    if(response instanceof CharacterCreationSuccessfulResponse)
                    {
                        GoToCharacterInfo();
                    }
                    /*else if()
                    {
                        // Edit error text to say something like "Char name taken"
                    }*/
                    else
                    {
                        Log.i(this.getClass().getSimpleName(), "CharacterCreation failed, unexpected response: " + response);
                    }

                }
                catch (RequestResponseClient.ConnectionBrokenException e)
                {
                    Log.i(this.getClass().getSimpleName(), "CharacterCreationRequest failed, couldn't connect to server:");
                }
                return null;
            }
        }.execute();
    }

    public static void GoToCharacterInfo()
    {
        Intent i = new Intent(Globals.characterCreateActivity, CharacterInfoActivity.class);
        Globals.characterCreateActivity.startActivity(i);
    }

    public static void UpdateAbilitiesList()
    {
        Globals.characterCreateActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                LayoutInflater inflater = Globals.characterCreateActivity.getLayoutInflater();
                LinearLayout abilitiesList = (LinearLayout) Globals.characterCreateActivity.findViewById(R.id.abilitiesListLayout);
                abilitiesList.removeAllViews();

                for (Ability ability : CharacterCreateActivity.creatingCharacter.abilities)
                {
                    View abilityView = inflater.inflate(R.layout.ability, null);
                    ((TextView) abilityView.findViewById(R.id.ability_name)).setText(ability.name);
                    ((TextView) abilityView.findViewById(R.id.ability_description)).setText(ability.description);
                    ((TextView) abilityView.findViewById(R.id.ability_tags)).setText(ability.tags);

                    abilityView.setOnClickListener(CharacterCreateOnClick.openAbilityPickerPopup(ability));
                    abilitiesList.addView(abilityView);
                }

            }
        });
    }

    public static void OpenImagePickerPopup()
    {
        LayoutInflater layoutInflater = (LayoutInflater)Globals.characterCreateActivity.getBaseContext().getSystemService(Globals.characterCreateActivity.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.image_picker, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        ArrayList<ImageButton> images = new ArrayList<ImageButton>();
        images.add((ImageButton)popupView.findViewById(R.id.image1));
        images.add((ImageButton)popupView.findViewById(R.id.image2));
        images.add((ImageButton)popupView.findViewById(R.id.image3));

        int i = 1;
        for(ImageButton image: images)
        {
            final String id = "" + i;
            image.setOnClickListener(new ImageButton.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CharacterCreateActivity.creatingCharacter.image = id;
                    CharacterCreateTasks.SetImage(id);
                    popupWindow.dismiss();
                }});
            i++;
        }

        popupWindow.showAtLocation(Globals.characterCreateActivity.findViewById(R.id.scrollView), Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.update();
    }

    public static void SetImage(final String id)
    {
        Globals.characterCreateActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ImageButton img = (ImageButton) Globals.characterCreateActivity.findViewById(R.id.create_character_pick_image);
                if(id.equals("1"))
                {
                    img.setImageResource(R.drawable.gladiator_template);
                }
                else if(id.equals("2"))
                {
                    img.setImageResource(R.drawable.gladiator_template2);
                }
                else if(id.equals("3"))
                {
                    img.setImageResource(R.drawable.gladiator_template3);
                }
            }
        });
    }

    public static void OpenAbilityPickerPopup(final Ability removeAbility)
    {
        if(removeAbility.id == 1)
            return; // Don't let user replace Attack.

        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                ModelInterface response = null;
                try
                {
                    response = NetworkClient.sendAndReceive(new BasicAbilitiesRequest(Globals.sessionId));
                    if(response instanceof BasicAbilitiesResponse)
                    {
                    }
                    else
                    {
                        Log.i(this.getClass().getSimpleName(), "BasicAbilitiesRequest failed, unexpected response: " + response);
                        return null;
                    }

                }
                catch (RequestResponseClient.ConnectionBrokenException e)
                {
                    Log.i(this.getClass().getSimpleName(), "BasicAbilitiesRequest failed, couldn't connect to server:");
                    return null;
                }

                if(response == null)
                {
                    return null;
                }

                final List<Ability> abilities = ((BasicAbilitiesResponse) response).abilities;

                Globals.characterCreateActivity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        CharacterCreateActivity.creatingCharacter.abilities.remove(removeAbility);

                        LayoutInflater layoutInflater = (LayoutInflater)Globals.characterCreateActivity.getBaseContext().getSystemService(Globals.characterCreateActivity.LAYOUT_INFLATER_SERVICE);
                        View popupView = layoutInflater.inflate(R.layout.ability_list, null);
                        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        popupWindow.showAtLocation(Globals.characterCreateActivity.findViewById(R.id.scrollView), Gravity.CENTER, 0, 0);
                        popupWindow.setFocusable(true);
                        popupWindow.update();


                        LayoutInflater inflater = Globals.characterCreateActivity.getLayoutInflater();
                        LinearLayout abilitiesList = (LinearLayout) popupView.findViewById(R.id.ability_list_layout);
                        abilitiesList.removeAllViews();
                        List<Ability> availableAbilities = abilities;

                        for (final Ability ability : availableAbilities)
                        {
                            if(!CharacterCreateActivity.creatingCharacter.abilities.contains(ability))
                            {
                                View abilityView = inflater.inflate(R.layout.ability, null);
                                ((TextView) abilityView.findViewById(R.id.ability_name)).setText(ability.name);
                                ((TextView) abilityView.findViewById(R.id.ability_description)).setText(ability.description);
                                ((TextView) abilityView.findViewById(R.id.ability_tags)).setText(ability.tags);

                                abilityView.setOnClickListener(new ImageButton.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        CharacterCreateActivity.creatingCharacter.abilities.add(ability);
                                        CharacterCreateTasks.UpdateAbilitiesList();
                                        popupWindow.dismiss();
                                    }
                                });

                                abilitiesList.addView(abilityView);
                            }
                        }
                    }
                });
                return null;
            }
        }.execute();
    }

    public static void RandomizeBaseStats()
    {
        CharacterCreateActivity.creatingCharacter.baseStats.health = 10 + RandomNumberGenerator.RollD(10);
        CharacterCreateActivity.creatingCharacter.baseStats.strength = 5 + RandomNumberGenerator.RollD(6);
        CharacterCreateActivity.creatingCharacter.baseStats.dexterity = 5 + RandomNumberGenerator.RollD(6);
        CharacterCreateActivity.creatingCharacter.baseStats.intelligence = 5 + RandomNumberGenerator.RollD(6);
        CharacterCreateActivity.creatingCharacter.baseStats.willpower = 5 + RandomNumberGenerator.RollD(6);

        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_health_base)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.baseStats.health));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_strength_base)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.baseStats.strength));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_dexterity_base)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.baseStats.dexterity));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_intelligence_base)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.baseStats.intelligence));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_willpower_base)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.baseStats.willpower));

        UpdateTotalStats();
    }

    public static void UpdateTotalStats()
    {
        CharacterCreateActivity.creatingCharacter.totalStats.health = CharacterCreateActivity.creatingCharacter.baseStats.health + CharacterCreateActivity.creatingCharacter.extraStats.health;
        CharacterCreateActivity.creatingCharacter.totalStats.strength = CharacterCreateActivity.creatingCharacter.baseStats.strength + CharacterCreateActivity.creatingCharacter.extraStats.strength;
        CharacterCreateActivity.creatingCharacter.totalStats.dexterity = CharacterCreateActivity.creatingCharacter.baseStats.dexterity + CharacterCreateActivity.creatingCharacter.extraStats.dexterity;
        CharacterCreateActivity.creatingCharacter.totalStats.intelligence = CharacterCreateActivity.creatingCharacter.baseStats.intelligence + CharacterCreateActivity.creatingCharacter.extraStats.intelligence;
        CharacterCreateActivity.creatingCharacter.totalStats.willpower = CharacterCreateActivity.creatingCharacter.baseStats.willpower + CharacterCreateActivity.creatingCharacter.extraStats.willpower;

        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_health_total)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.totalStats.health));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_strength_total)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.totalStats.strength));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_dexterity_total)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.totalStats.dexterity));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_intelligence_total)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.totalStats.intelligence));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_willpower_total)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.totalStats.willpower));

        float armor = Armor.GetArmor(CharacterCreateActivity.creatingCharacter.totalStats.dexterity);
        float initiative = Initiative.GetInitiative(CharacterCreateActivity.creatingCharacter.totalStats.dexterity);
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_armor)).setText(Integer.toString((int) armor));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_initiative)).setText(Integer.toString((int) initiative));
    }

    public static void ChangeExtraStats(boolean plus, String stat)
    {
        if(plus && CharacterCreateActivity.creatingCharacter.statPointsLeft == 0)
            return;// Dont have the points for it.


        if(stat.equals("health"))
        {
            if(plus)
            {
                CharacterCreateActivity.creatingCharacter.extraStats.health += 2.0f;
                CharacterCreateActivity.creatingCharacter.statPointsLeft--;
            }
            else
            {
                if(CharacterCreateActivity.creatingCharacter.extraStats.health > 0.0f)
                {
                    CharacterCreateActivity.creatingCharacter.extraStats.health -= 2.0f;
                    CharacterCreateActivity.creatingCharacter.statPointsLeft++;
                }
            }
        }

        if(stat.equals("strength"))
        {
            if(plus)
            {
                CharacterCreateActivity.creatingCharacter.extraStats.strength += 1.0f;
                CharacterCreateActivity.creatingCharacter.statPointsLeft--;
            }
            else
            {
                if(CharacterCreateActivity.creatingCharacter.extraStats.strength > 0.0f)
                {
                    CharacterCreateActivity.creatingCharacter.extraStats.strength -= 1.0f;
                    CharacterCreateActivity.creatingCharacter.statPointsLeft++;
                }
            }
        }

        if(stat.equals("dexterity"))
        {
            if(plus)
            {
                CharacterCreateActivity.creatingCharacter.extraStats.dexterity += 1.0f;
                CharacterCreateActivity.creatingCharacter.statPointsLeft--;
            }
            else
            {
                if(CharacterCreateActivity.creatingCharacter.extraStats.dexterity > 0.0f)
                {
                    CharacterCreateActivity.creatingCharacter.extraStats.dexterity -= 1.0f;
                    CharacterCreateActivity.creatingCharacter.statPointsLeft++;
                }
            }
        }

        if(stat.equals("intelligence"))
        {
            if(plus)
            {
                CharacterCreateActivity.creatingCharacter.extraStats.intelligence += 1.0f;
                CharacterCreateActivity.creatingCharacter.statPointsLeft--;
            }
            else
            {
                if(CharacterCreateActivity.creatingCharacter.extraStats.intelligence > 0.0f)
                {
                    CharacterCreateActivity.creatingCharacter.extraStats.intelligence -= 1.0f;
                    CharacterCreateActivity.creatingCharacter.statPointsLeft++;
                }
            }
        }

        if(stat.equals("willpower"))
        {
            if(plus)
            {
                CharacterCreateActivity.creatingCharacter.extraStats.willpower += 1.0f;
                CharacterCreateActivity.creatingCharacter.statPointsLeft--;
            }
            else
            {
                if(CharacterCreateActivity.creatingCharacter.extraStats.willpower > 0.0f)
                {
                    CharacterCreateActivity.creatingCharacter.extraStats.willpower -= 1.0f;
                    CharacterCreateActivity.creatingCharacter.statPointsLeft++;
                }
            }
        }

        UpdateTotalStats();

        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_health_extra)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.extraStats.health));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_strength_extra)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.extraStats.strength));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_dexterity_extra)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.extraStats.dexterity));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_intelligence_extra)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.extraStats.intelligence));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_willpower_extra)).setText(Integer.toString((int) CharacterCreateActivity.creatingCharacter.extraStats.willpower));
        ((TextView) Globals.characterCreateActivity.findViewById(R.id.character_create_points_left)).setText(Integer.toString(CharacterCreateActivity.creatingCharacter.statPointsLeft));
    }
}
