package malow.gladiatus.activities;


import android.view.View;
import android.widget.EditText;

import malow.gladiatus.Globals;
import malow.gladiatus.R;
import malow.gladiatus.common.models.requests.LoginRequest;
import malow.gladiatus.common.models.requests.RegisterRequest;
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
            String username = ((EditText) Globals.mainActivity.findViewById(R.id.login_usernameField)).getText().toString();
            String password = ((EditText) Globals.mainActivity.findViewById(R.id.login_passwordField)).getText().toString();

            String encryptedPassword = MD5Encrypter.encrypt(password);

            MainTasks.LoginTask(new LoginRequest(username, encryptedPassword));
        }
    }


    public static Register register()
    {
        return self.new Register();
    }
    public class Register implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            String username = ((EditText) Globals.mainActivity.findViewById(R.id.register_usernameField)).getText().toString();
            String password = ((EditText) Globals.mainActivity.findViewById(R.id.register_passwordField)).getText().toString();
            String email = ((EditText) Globals.mainActivity.findViewById(R.id.register_emailField)).getText().toString();

            String encryptedPassword = MD5Encrypter.encrypt(password);

            MainTasks.Register(new RegisterRequest(username, encryptedPassword, email));
        }
    }
}