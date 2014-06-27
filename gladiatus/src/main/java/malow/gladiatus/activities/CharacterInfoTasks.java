package malow.gladiatus.activities;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import malow.gladiatus.Globals;
import malow.gladiatus.NetworkClient;
import malow.gladiatus.R;
import malow.gladiatus.common.Armor;
import malow.gladiatus.common.DiceRolls;
import malow.gladiatus.common.Initiative;
import malow.gladiatus.common.Level;
import malow.gladiatus.common.Money;
import malow.gladiatus.common.Weight;
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.CharacterInfoRequest;
import malow.gladiatus.common.models.responses.CharacterInfoResponse;
import malow.gladiatus.common.models.responses.NoCharacterFoundResponse;
import malow.malowlib.RequestResponseClient;

public class CharacterInfoTasks
{
    public static void UpdateCharacterInfoGUI(final CharacterInfoResponse response)
    {
        Globals.characterInfoActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                // Top stuff
                SetImage(response.characterImage);

                TextView name = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_name);
                name.setText(response.characterName);

                TextView level = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_level);
                level.setText(String.valueOf(response.level));

                String xpText = response.xp + " / " + Level.GetXpRequiredToLevelUp(response.level);
                TextView xp = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_xp_text);
                xp.setText(xpText);

                ProgressBar xpBar = (ProgressBar) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_xp_bar);
                xpBar.setProgress((int) ((response.xp / ((float)Level.GetXpRequiredToLevelUp(response.level))) * 100));

                String currentHealthText = (int) response.currentHealth + " / " + (int) response.health;
                TextView currentHealth = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_current_health_text);
                currentHealth.setText(currentHealthText);

                ProgressBar healthBar = (ProgressBar) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_health_bar);
                healthBar.setProgress((int) ((response.currentHealth / response.health) * 100));

                // Stats

                TextView health = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_health_text);
                health.setText(String.valueOf((int) response.health));
                TextView healthBonus = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_health_bonus_text);
                healthBonus.setText("");

                TextView strength = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_strength_text);
                strength.setText(String.valueOf((int) response.strength));
                TextView strengthBonus = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_strength_bonus_text);
                strengthBonus.setText("");
                TextView strengthTraining = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_strength_training);
                strengthTraining.setText(">>>");

                TextView dexterity = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_dexterity_text);
                dexterity.setText(String.valueOf((int) response.dexterity));
                TextView dexterityBonus = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_dexterity_bonus_text);
                dexterityBonus.setText("");

                TextView intelligence = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_intelligence_text);
                intelligence.setText(String.valueOf((int) response.intelligence));
                TextView intelligenceBonus = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_intelligence_bonus_text);
                intelligenceBonus.setText("");

                TextView willpower = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_willpower_text);
                willpower.setText(String.valueOf((int) response.willpower));
                TextView willpowerBonus = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_willpower_bonus_text);
                willpowerBonus.setText("");

                TextView armor = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_armor_text);
                armor.setText(String.valueOf((int) Armor.GetArmor(response)));
                TextView armorBonus = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_armor_bonus_text);
                armorBonus.setText("");

                TextView initiative = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_initiative_text);
                initiative.setText(String.valueOf((int) Initiative.GetInitiative(response)));
                TextView initiativeBonus = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_initiative_bonus_text);
                initiativeBonus.setText("");

                // Dice Rolls
                TextView damageDice = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_damage_roll);
                damageDice.setText(DiceRolls.GetDamageDice(response) + " + " + DiceRolls.GetDamageBonus(response));

                TextView hitDice = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_hit_roll);
                hitDice.setText(DiceRolls.GetHitDice(response) + " + " + DiceRolls.GetHitBonus(response));

                // Info texts
                TextView weightText = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_weight_text);
                weightText.setText(Weight.GetWeight(response) + " - " + Weight.GetEncumbrance(response.strength, response));

                TextView statusText = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_status);
                statusText.setText(response.status);

                TextView goldText = (TextView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_gold_text);
                goldText.setText(Money.GetMoneyAsString(response.money));
            }
        });
    }

    public static void UpdateCharacterInfo()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                CharacterInfoRequest characterInfoRequest = new CharacterInfoRequest(Globals.sessionId);
                ModelInterface response = null;
                try
                {
                    response = NetworkClient.sendAndReceive(characterInfoRequest);
                    if(response instanceof CharacterInfoResponse)
                    {
                        UpdateCharacterInfoGUI((CharacterInfoResponse) response);
                    }
                    else if(response instanceof NoCharacterFoundResponse)
                    {
                        Log.e(this.getClass().getSimpleName(), "CharacterInfoRequest failed, NoCharacterFound!");
                    }
                    else
                    {
                        Log.e(this.getClass().getSimpleName(), "CharacterInfoRequest failed, Unexpected response: " + response);
                    }
                }
                catch (RequestResponseClient.ConnectionBrokenException e)
                {
                    Log.i(this.getClass().getSimpleName(), "CharacterInfoRequest failed, couldn't connect to server.");
                }
                return null;
            }
        }.execute();
    }

    public static void SwitchToTab(int tab)
    {
        HideFragmentKeyboard();

        Fragment fragment = new CharacterInfoActivityFragment();
        Bundle args = new Bundle();
        args.putInt(CharacterInfoActivityFragment.TAB_NUMBER, tab);
        fragment.setArguments(args);
        Globals.characterInfoActivity.getFragmentManager().beginTransaction().replace(R.id.character_info_main_view, fragment).commit();

        if(tab == 0 && CharacterInfoActivity.isResumed)
            CharacterInfoTasks.UpdateCharacterInfo();
    }

    public static void HideFragmentKeyboard()
    {
        View target = null;
        if(Globals.characterInfoFragment != null)
            target = Globals.characterInfoFragment.getView().findFocus();
        if (target != null) {
            InputMethodManager imm = (InputMethodManager) target.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }
    }

    public static void SetImage(final String id)
    {
        Globals.characterInfoActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ImageView img = (ImageView) Globals.characterInfoFragment.getView().findViewById(R.id.character_info_image);
                if (id.equals("1"))
                {
                    img.setImageResource(R.drawable.gladiator_template);
                }
                else if (id.equals("2"))
                {
                    img.setImageResource(R.drawable.gladiator_template2);
                }
                else if (id.equals("3"))
                {
                    img.setImageResource(R.drawable.gladiator_template3);
                }
            }
        });
    }
}
