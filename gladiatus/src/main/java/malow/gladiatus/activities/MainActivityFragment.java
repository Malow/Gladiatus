package malow.gladiatus.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import malow.gladiatus.Globals;
import malow.gladiatus.R;

public class MainActivityFragment extends Fragment
{
    public static final String TAB_NUMBER = "tab_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        int tab = getArguments().getInt(TAB_NUMBER);
        if(tab == 0)
        {
            View v = inflater.inflate(R.layout.login_screen, container, false);
            Button loginButton = (Button) v.findViewById(R.id.login_button);
            loginButton.setOnClickListener(MainActivityOnClick.login());
            return v;
        }
        else if(tab == 1)
        {
            View v = inflater.inflate(R.layout.register_screen, container, false);
            Button registerButton = (Button) v.findViewById(R.id.register_button);
            registerButton.setOnClickListener(MainActivityOnClick.register());
            return v;
        }
        else
        {
            return inflater.inflate(R.layout.recover_screen, container, false);
        }
    }
}