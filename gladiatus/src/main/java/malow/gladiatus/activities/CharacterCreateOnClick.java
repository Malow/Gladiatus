package malow.gladiatus.activities;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import malow.gladiatus.Globals;
import malow.gladiatus.R;
import malow.gladiatus.common.models.Ability;

public class CharacterCreateOnClick
{
    private static CharacterCreateOnClick self = new CharacterCreateOnClick();

    public static OpenImagePickerPopup openImagePickerPopup()
    {
        return self.new OpenImagePickerPopup();
    }
    public class OpenImagePickerPopup implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            CharacterCreateTasks.OpenImagePickerPopup();
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
            //String characterName = ((EditText) Globals.characterCreateActivity.findViewById(R.id.characterNameField)).getText().toString();
            String characterImage;
            String health;
            String armor;
            String strength;
            String dexterity;
            String initiative;
        }
    }

    public static OpenAbilityPickerPopup openAbilityPickerPopup(Ability removeAbility)
    {
        return self.new OpenAbilityPickerPopup(removeAbility);
    }
    public class OpenAbilityPickerPopup implements  View.OnClickListener
    {
        public final Ability removeAbility;

        public OpenAbilityPickerPopup(Ability removeAbility)
        {
            this.removeAbility = removeAbility;
        }

        @Override
        public void onClick(View v)
        {
            CharacterCreateTasks.OpenAbilityPickerPopup(this.removeAbility);
        }
    }

    public static RandomizeBaseStats randomizeBaseStats()
    {
        return self.new RandomizeBaseStats();
    }
    public class RandomizeBaseStats implements  View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            CharacterCreateTasks.RandomizeBaseStats();
        }
    }

    public static ChangeExtraStats changeExtraStats(boolean plus, String stat)
    {
        return self.new ChangeExtraStats(plus, stat);
    }
    public class ChangeExtraStats implements  View.OnClickListener
    {
        public boolean plus;
        public String stat;

        public ChangeExtraStats(boolean plus, String stat)
        {
            this.plus = plus;
            this.stat = stat;
        }
        @Override
        public void onClick(View v)
        {
            CharacterCreateTasks.ChangeExtraStats(this.plus, this.stat);
        }
    }
}
