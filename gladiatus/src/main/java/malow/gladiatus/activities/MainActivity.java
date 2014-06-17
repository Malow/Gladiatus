package malow.gladiatus.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentActivity;

import malow.gladiatus.Globals;
import malow.gladiatus.R;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Globals.mainActivity = this;
        setContentView(R.layout.activity_main);

        //Button loginButton = (Button) findViewById(R.id.loginButton);
        //loginButton.setOnClickListener(MainActivityOnClick.login());


        // Set up the action bar to show tabs.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // for each of the sections in the app, add a tab to the action bar.
        actionBar.addTab(actionBar.newTab().setText(R.string.login_tab_1).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.login_tab_2).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.login_tab_3).setTabListener(this));
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
        // When the given tab is selected, show the tab contents in the
        // container view.
        Fragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivityFragment.TAB_NUMBER, tab.getPosition());
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.LoginTabView, fragment).commit();
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static class MainActivityFragment extends Fragment
    {
        public static final String TAB_NUMBER = "tab_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            int tab = getArguments().getInt(TAB_NUMBER);
            if(tab == 0)
            {
                return inflater.inflate(R.layout.login_screen, container, false);
            }
            else if(tab == 1)
            {
                return inflater.inflate(R.layout.register_screen, container, false);
            }
            else
            {
                return inflater.inflate(R.layout.recover_screen, container, false);
            }
        }
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
