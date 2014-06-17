package malow.gladiatus.activities;


import android.view.View;
import android.widget.EditText;

import malow.gladiatus.Globals;
import malow.gladiatus.R;
import malow.gladiatus.common.models.requests.LoginRequest;
import malow.malowlib.MD5Encrypter;

public class MainActivityOnClick
{
    private static MainActivityOnClick self = new MainActivityOnClick();

    public static Login login()
    {
        return self.new Login();
    }
    public class Login implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            String username = ((EditText) Globals.mainActivity.findViewById(R.id.usernameField)).getText().toString();
            String password = ((EditText) Globals.mainActivity.findViewById(R.id.passwordField)).getText().toString();

            String encryptedPassword = MD5Encrypter.encrypt(password);

            MainTasks.LogIn(new LoginRequest(username, encryptedPassword));
        }
    }
}