package malow.gladiatus.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import malow.gladiatus.Globals;
import malow.gladiatus.NetworkClient;
import malow.gladiatus.R;
import malow.gladiatus.common.models.Armor;
import malow.gladiatus.common.models.Initiative;
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.CharacterInfoRequest;
import malow.gladiatus.common.models.responses.CharacterInfoResponse;
import malow.gladiatus.common.models.responses.NoCharacterFoundResponse;
import malow.malowlib.RequestResponseClient;

public class CharacterInfoTasks
{
    public static void UpdateCharacterInfoGUI(CharacterInfoResponse response)
    {
        /*
        TextView characterName = (TextView) Globals.characterInfoActivity.findViewById(R.id.characterNameText);
        characterName.setText(((CharacterInfoResponse) response).characterName);

        TextView health = (TextView) Globals.characterInfoActivity.findViewById(R.id.healthText);
        health.setText(((CharacterInfoResponse) response).health);

        TextView armor = (TextView) Globals.characterInfoActivity.findViewById(R.id.armorText);
        armor.setText(Integer.toString((int) Armor.GetArmor((CharacterInfoResponse) response)));

        TextView strength = (TextView) Globals.characterInfoActivity.findViewById(R.id.strengthText);
        strength.setText(((CharacterInfoResponse) response).strength);

        TextView dexterity = (TextView) Globals.characterInfoActivity.findViewById(R.id.dexterityText);
        dexterity.setText(((CharacterInfoResponse) response).dexterity);

        TextView initiative = (TextView) Globals.characterInfoActivity.findViewById(R.id.initiativeText);
        initiative.setText(Integer.toString((int) Initiative.GetInitiative((CharacterInfoResponse) response)));
        */
    }

    public static void UpdateCharacterInfo()
    {
        //UpdateCharacterInfoGUI(something);
    }

    public static void SwitchToTab(int tab)
    {
        HideFragmentKeyboard();

        Fragment fragment = new CharacterInfoActivityFragment();
        Bundle args = new Bundle();
        args.putInt(CharacterInfoActivityFragment.TAB_NUMBER, tab);
        fragment.setArguments(args);
        Globals.characterInfoActivity.getFragmentManager().beginTransaction().replace(R.id.character_info_main_view, fragment).commit();
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
}
