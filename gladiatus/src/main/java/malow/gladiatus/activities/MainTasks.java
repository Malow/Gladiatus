package malow.gladiatus.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import malow.gladiatus.Globals;
import malow.gladiatus.NetworkClient;
import malow.gladiatus.R;
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.LoginRequest;
import malow.gladiatus.common.models.requests.RegisterRequest;
import malow.gladiatus.common.models.responses.LoginResponse;
import malow.gladiatus.common.models.responses.RegisterFailedResponse;

public class MainTasks
{
    public static void LoginTask(final LoginRequest loginRequest)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ModelInterface loginResponse = NetworkClient.sendAndReceive(loginRequest);
                if(loginResponse instanceof LoginResponse)
                {
                    FinishLoginAttempt((LoginResponse) loginResponse);
                }
                else
                {
                    SetLoginErrorText("Error: Unexpected response");
                    Log.i(this.getClass().getSimpleName(), "Login failed, unexpected response: " + loginResponse);
                }
                return null;
            }
        }.execute();
    }

    public static void RegisterTask(final RegisterRequest registerRequest)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ModelInterface registerResponse = NetworkClient.sendAndReceive(registerRequest);
                // If we receive LoginResponse it means that register was successful and we logged in.
                if(registerResponse instanceof LoginResponse)
                {
                    if(!FinishLoginAttempt((LoginResponse) registerResponse))
                    {
                        SwitchToTab(0); // Switch to login screen if register was successful but login failed.
                    }
                }
                else if(registerResponse instanceof RegisterFailedResponse)
                {
                    String errorCode = ((RegisterFailedResponse) registerResponse).errorCode;
                    SetRegisterErrorText(errorCode);
                    Log.i("MainTasks", "RegisterTask failed, errorCode: " + errorCode);
                }
                else
                {
                    SetRegisterErrorText("Error: Unexpected response");
                    Log.i(this.getClass().getSimpleName(), "RegisterTask failed, unexpected response: " + registerResponse);
                }
                return null;
            }
        }.execute();
    }

    public static boolean FinishLoginAttempt(LoginResponse loginResponse)
    {
        final String errorCode = loginResponse.errorCode;
        if(errorCode.isEmpty())
        {
            Globals.sessionId = loginResponse.sessionId;
            Log.i("MainTasks", "Login successful, sessionId: " + Globals.sessionId);
            GoToCharacterInfo();
            return true;
        }
        else
        {
            SetLoginErrorText(errorCode);
            Log.i("MainTasks", "Login failed, errorCode: " + errorCode);
            return false;
        }
    }

    public static void GoToCharacterInfo()
    {
            Intent i = new Intent(Globals.mainActivity, CharacterInfoActivity.class);
            Globals.mainActivity.startActivity(i);
    }

    public static void SetLoginErrorText(final String errorCode)
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

    public static void SetRegisterErrorText(final String errorCode)
    {
        Globals.mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                TextView errorText = (TextView) Globals.mainActivity.findViewById(R.id.registerErrorText);
                errorText.setText(errorCode);
            }
        });
    }

    public static void SwitchToTab(int tab)
    {
        Fragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivityFragment.TAB_NUMBER, tab);
        fragment.setArguments(args);
        Globals.mainActivity.getFragmentManager().beginTransaction().replace(R.id.LoginTabView, fragment).commit();
    }
}
