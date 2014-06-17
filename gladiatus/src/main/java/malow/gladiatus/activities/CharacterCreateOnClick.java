package malow.gladiatus.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import malow.gladiatus.Globals;
import malow.gladiatus.R;
import malow.gladiatus.common.models.requests.LoginRequest;

public class CharacterCreateOnClick
{

    private static CharacterCreateOnClick self = new CharacterCreateOnClick();

    public static SelectImage selectImage(int id)
    {
        return self.new SelectImage(id);
    }
    public class SelectImage implements View.OnClickListener
    {
        private int id;

        public SelectImage(int id)
        {
            this.id = id;
        }
        @Override
        public void onClick(View v)
        {
            ImageButton button1 = ((ImageButton) Globals.characterCreateActivity.findViewById(R.id.image_1));
            ImageButton button2 = ((ImageButton) Globals.characterCreateActivity.findViewById(R.id.image_2));
            ImageButton button3 = ((ImageButton) Globals.characterCreateActivity.findViewById(R.id.image_3));
            ImageButton selected = ((ImageButton) v);

            button1.setAlpha(0.3f);
            button2.setAlpha(0.3f);
            button3.setAlpha(0.3f);
            selected.setAlpha(1.0f);
        }
    }

    public static CreateCharacter createCharacter()
    {
        return self.new CreateCharacter();
    }
    public class CreateCharacter implements  View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            String characterName = ((EditText) Globals.characterCreateActivity.findViewById(R.id.characterNameField)).getText().toString();
            String characterImage;
            String health;
            String armor;
            String strength;
            String dexterity;
            String initiative;
        }
    }
}
