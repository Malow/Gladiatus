package malow.gladiatus.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import malow.gladiatus.Globals;
import malow.gladiatus.R;

public class CharacterCreateActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_create);
        Globals.characterCreateActivity = this;

        ImageButton imageButton1 = (ImageButton) findViewById(R.id.image_1);
        imageButton1.setOnClickListener(CharacterCreateOnClick.selectImage(1));
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.image_2);
        imageButton2.setOnClickListener(CharacterCreateOnClick.selectImage(2));
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.image_3);
        imageButton3.setOnClickListener(CharacterCreateOnClick.selectImage(3));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
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
