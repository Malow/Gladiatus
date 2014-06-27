package malow.gladiatus.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import malow.gladiatus.Globals;
import malow.gladiatus.R;
import malow.gladiatus.common.models.ConvertStringToModel;
import malow.gladiatus.common.models.responses.CharacterInfoResponse;

public class CharacterInfoActivity extends FragmentActivity implements ActionBar.TabListener
{
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    public static boolean isResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_info);
        Globals.characterInfoActivity = this;

        // Set up the action bar to show tabs.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // for each of the sections in the app, add a tab to the action bar.
        actionBar.addTab(actionBar.newTab().setText(R.string.character_info_tab_1).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.character_info_tab_2).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.character_info_tab_3).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.character_info_tab_4).setTabListener(this));

        // TODO: Use this
        //CharacterInfoResponse startResponse = (CharacterInfoResponse) ConvertStringToModel.toModel(getIntent().getStringExtra("characterInfoResponse"));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Restore the previously serialized current tab position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM))
        {
            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        // Serialize the current tab position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CharacterInfoTasks.SwitchToTab(tab.getPosition());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        CharacterInfoTasks.UpdateCharacterInfo();
        isResumed = true;
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
