package malow.gladiatus.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import malow.gladiatus.Globals;
import malow.gladiatus.NetworkClient;
import malow.gladiatus.R;
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.LoginRequest;
import malow.gladiatus.common.models.responses.LoginResponse;

public class MainTasks
{
    public static void LogIn(final LoginRequest loginRequest)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ModelInterface loginResponse = NetworkClient.sendAndReceive(loginRequest);
                if(loginResponse instanceof LoginResponse)
                {
                    final String errorCode = ((LoginResponse) loginResponse).errorCode;
                    if(errorCode.isEmpty())
                    {
                        Globals.sessionId = ((LoginResponse) loginResponse).sessionId;
                        Log.i(this.getClass().getSimpleName(), "Login successfull, sessionId: " + Globals.sessionId);
                        GoToCharacterInfo();
                    }
                    else
                    {
                        SetErrorText(errorCode);
                        Log.i(this.getClass().getSimpleName(), "Login failed, errorCode: " + errorCode);
                    }
                }
                else
                {
                    SetErrorText("Error: Unexpected response");
                    Log.i(this.getClass().getSimpleName(), "Login failed, unexpected response: " + loginResponse);
                }
                return null;
            }
        }.execute();
    }

    public static void GoToCharacterInfo()
    {
            Intent i = new Intent(Globals.mainActivity, CharacterInfoActivity.class);
            Globals.mainActivity.startActivity(i);
    }

    public static void SetErrorText(final String errorCode)
    {
        Globals.mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                TextView errorText = (TextView) Globals.mainActivity.findViewById(R.id.loginErrorText);
                errorText.setText(errorCode);
            }
        });
    }
}
