package malow.gladiatus.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import malow.gladiatus.Globals;
import malow.gladiatus.NetworkClient;
import malow.gladiatus.R;
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.CharacterInfoRequest;
import malow.gladiatus.common.models.responses.CharacterInfoResponse;
import malow.gladiatus.common.models.responses.NoCharacterFoundResponse;
import malow.malowlib.RequestResponseClient;

public class CharacterInfoTasks
{
    public static void updateCharacterInfoTexts()
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
                        TextView characterName = (TextView) Globals.characterInfoActivity.findViewById(R.id.characterNameText);
                        characterName.setText(((CharacterInfoResponse) response).characterName);

                        TextView health = (TextView) Globals.characterInfoActivity.findViewById(R.id.healthText);
                        health.setText(((CharacterInfoResponse) response).health);

                        TextView armor = (TextView) Globals.characterInfoActivity.findViewById(R.id.armorText);
                        armor.setText(((CharacterInfoResponse) response).armor);

                        TextView strength = (TextView) Globals.characterInfoActivity.findViewById(R.id.strengthText);
                        strength.setText(((CharacterInfoResponse) response).strength);

                        TextView dexterity = (TextView) Globals.characterInfoActivity.findViewById(R.id.dexterityText);
                        dexterity.setText(((CharacterInfoResponse) response).dexterity);

                        TextView initiative = (TextView) Globals.characterInfoActivity.findViewById(R.id.initiativeText);
                        initiative.setText(((CharacterInfoResponse) response).initiative);
                    }
                    else if(response instanceof NoCharacterFoundResponse)
                    {
                        GoToCharacterCreate();
                    }
                }
                catch (RequestResponseClient.ConnectionBrokenException e)
                {
                    Log.i(this.getClass().getSimpleName(), "CharacterInfoRequest failed, couldn't connect to server:");
                }

                return null;
            }
        }.execute();
    }

    public static void GoToCharacterCreate()
    {
        Intent i = new Intent(Globals.characterInfoActivity, CharacterCreateActivity.class);
        Globals.characterInfoActivity.startActivity(i);
    }
}
