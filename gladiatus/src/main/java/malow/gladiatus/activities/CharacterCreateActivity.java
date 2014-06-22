package malow.gladiatus.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

import malow.gladiatus.Globals;
import malow.gladiatus.R;
import malow.gladiatus.common.models.Ability;
import malow.gladiatus.common.models.Gladiator;

public class CharacterCreateActivity extends Activity
{
    public static Gladiator creatingCharacter = new Gladiator();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_create);
        Globals.characterCreateActivity = this;

        ImageButton imagePickButton = (ImageButton) findViewById(R.id.create_character_pick_image);
        imagePickButton.setOnClickListener(CharacterCreateOnClick.openImagePickerPopup());

        creatingCharacter.abilities.add(new Ability(1, "Attack", "Basic attack.", "[Active] [Range:Weapon]"));
        creatingCharacter.abilities.add(new Ability(-1, "", "<Press here to choose an ability>", ""));
        creatingCharacter.abilities.add(new Ability(-1, "", "<Press here to choose an ability>", ""));
        CharacterCreateTasks.UpdateAbilitiesList();

        CharacterCreateTasks.RandomizeBaseStats();
        Button randomizeButton = (Button) findViewById(R.id.character_create_randomize_button);
        randomizeButton.setOnClickListener(CharacterCreateOnClick.randomizeBaseStats());

        Button plusHealthButton = (Button) findViewById(R.id.character_create_health_plus_button);
        plusHealthButton.setOnClickListener(CharacterCreateOnClick.changeExtraStats(true, "health"));
        Button minusHealthButton = (Button) findViewById(R.id.character_create_health_minus_button);
        minusHealthButton.setOnClickListener(CharacterCreateOnClick.changeExtraStats(false, "health"));

        Button plusStrengthButton = (Button) findViewById(R.id.character_create_strength_plus_button);
        plusStrengthButton.setOnClickListener(CharacterCreateOnClick.changeExtraStats(true, "strength"));
        Button minusStrengthButton = (Button) findViewById(R.id.character_create_strength_minus_button);
        minusStrengthButton.setOnClickListener(CharacterCreateOnClick.changeExtraStats(false, "strength"));

        Button plusDexterityButton = (Button) findViewById(R.id.character_create_dexterity_plus_button);
        plusDexterityButton.setOnClickListener(CharacterCreateOnClick.changeExtraStats(true, "dexterity"));
        Button minusDexterityButton = (Button) findViewById(R.id.character_create_dexterity_minus_button);
        minusDexterityButton.setOnClickListener(CharacterCreateOnClick.changeExtraStats(false, "dexterity"));

        Button plusIntelligenceButton = (Button) findViewById(R.id.character_create_intelligence_plus_button);
        plusIntelligenceButton.setOnClickListener(CharacterCreateOnClick.changeExtraStats(true, "intelligence"));
        Button minusIntelligenceButton = (Button) findViewById(R.id.character_create_intelligence_minus_button);
        minusIntelligenceButton.setOnClickListener(CharacterCreateOnClick.changeExtraStats(false, "intelligence"));

        Button plusWillpowerButton = (Button) findViewById(R.id.character_create_willpower_plus_button);
        plusWillpowerButton.setOnClickListener(CharacterCreateOnClick.changeExtraStats(true, "willpower"));
        Button minusWillpowerButton = (Button) findViewById(R.id.character_create_willpower_minus_button);
        minusWillpowerButton.setOnClickListener(CharacterCreateOnClick.changeExtraStats(false, "willpower"));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
