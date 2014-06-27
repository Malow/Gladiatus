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
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.CharacterInfoRequest;
import malow.gladiatus.common.models.requests.LoginRequest;
import malow.gladiatus.common.models.requests.RegisterRequest;
import malow.gladiatus.common.models.responses.CharacterInfoResponse;
import malow.gladiatus.common.models.responses.LoginFailedResponse;
import malow.gladiatus.common.models.responses.LoginSuccessfulResponse;
import malow.gladiatus.common.models.responses.NoCharacterFoundResponse;
import malow.gladiatus.common.models.responses.RegisterFailedResponse;
import malow.malowlib.RequestResponseClient;

public class MainTasks
{
    public static void LoginTask(final LoginRequest loginRequest)
    {
        HideFragmentKeyboard();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ModelInterface loginResponse = null;
                try
                {
                    loginResponse = NetworkClient.sendAndReceive(loginRequest);
                    if(loginResponse instanceof LoginSuccessfulResponse)
                    {
                        Globals.sessionId = ((LoginSuccessfulResponse) loginResponse).sessionId;
                        FinishLogin();
                    }
                    else if(loginResponse instanceof LoginFailedResponse)
                    {
                        SetLoginErrorText("Login Failed: " + ((LoginFailedResponse) loginResponse).errorCode);
                        Log.i(this.getClass().getSimpleName(), "Login Failed: " + ((LoginFailedResponse) loginResponse).errorCode);
                    }
                    else
                    {
                        SetLoginErrorText("Error: Unexpected response");
                        Log.i(this.getClass().getSimpleName(), "Login failed, unexpected response: " + loginResponse);
                    }
                }
                catch (RequestResponseClient.ConnectionBrokenException e)
                {
                    Log.i(this.getClass().getSimpleName(), "LoginRequest failed, couldn't connect to server:");
                    SetLoginErrorText("Login failed, couldn't connect to server.");
                }

                return null;
            }
        }.execute();
    }

    public static void RegisterTask(final RegisterRequest registerRequest)
    {
        HideFragmentKeyboard();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ModelInterface registerResponse = null;
                try
                {
                    registerResponse = NetworkClient.sendAndReceive(registerRequest);
                    if(registerResponse instanceof LoginSuccessfulResponse)
                    {
                        Globals.sessionId = ((LoginSuccessfulResponse) registerResponse).sessionId;
                        FinishLogin();
                    }
                    else if(registerResponse instanceof LoginFailedResponse)
                    {
                        SwitchToTab(0); // Switch to login screen if register was successful but login failed.
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
                }
                catch (RequestResponseClient.ConnectionBrokenException e)
                {
                    Log.i(this.getClass().getSimpleName(), "RegisterRequest failed, couldn't connect to server:");
                    SetRegisterErrorText("Register failed, couldn't connect to server.");
                }

                return null;
            }
        }.execute();
    }

    private static void FinishLogin()
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
                        GoToCharacterInfo((CharacterInfoResponse) response);
                    }
                    else if(response instanceof NoCharacterFoundResponse)
                    {
                        GoToCharacterCreate();
                    }
                    else
                    {
                        Log.e(this.getClass().getSimpleName(), "CharacterInfoRequest failed, Unexpected response: " + response);
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

    public static void GoToCharacterInfo(CharacterInfoResponse response)
    {
        Intent i = new Intent(Globals.mainActivity, CharacterInfoActivity.class);
        i.putExtra("characterInfoResponse", response.toNetworkString());
        Globals.mainActivity.startActivity(i);
    }

    public static void GoToCharacterCreate()
    {
        Intent i = new Intent(Globals.mainActivity, CharacterCreateActivity.class);
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
        HideFragmentKeyboard();

        Fragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivityFragment.TAB_NUMBER, tab);
        fragment.setArguments(args);
        Globals.mainActivity.getFragmentManager().beginTransaction().replace(R.id.LoginTabView, fragment).commit();
    }

    public static void HideFragmentKeyboard()
    {
        View target = null;
        if(Globals.mainActivityFragment != null)
            target = Globals.mainActivityFragment.getView().findFocus();
        if (target != null) {
            InputMethodManager imm = (InputMethodManager) target.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }
    }
}
