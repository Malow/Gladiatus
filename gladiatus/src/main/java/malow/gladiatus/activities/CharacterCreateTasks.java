package malow.gladiatus.activities;


import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import malow.gladiatus.Globals;
import malow.gladiatus.NetworkClient;
import malow.gladiatus.R;
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.CharacterCreateRequest;
import malow.gladiatus.common.models.responses.CharacterCreationSuccessful;

public class CharacterCreateTasks
{
    public static void CreateCharacter(final CharacterCreateRequest request)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids)
            {
                ModelInterface response = NetworkClient.sendAndReceive(request);
                if(response instanceof CharacterCreationSuccessful)
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
                return null;
            }
        }.execute();
    }

    public static void GoToCharacterInfo()
    {
        Intent i = new Intent(Globals.characterCreateActivity, CharacterInfoActivity.class);
        Globals.characterCreateActivity.startActivity(i);
    }
}
