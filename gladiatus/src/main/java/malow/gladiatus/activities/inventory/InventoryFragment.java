package malow.gladiatus.activities.inventory;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import malow.gladiatus.R;
import malow.gladiatus.activities.characterinfo.CharacterInfoTasks;

public class InventoryFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.inventory_screen, container, false);

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Hide the layout
        LinearLayout hiddenInfo = (LinearLayout) this.getView().findViewById(R.id.inventoryListLayout);
        hiddenInfo.setVisibility(View.INVISIBLE);

        InventoryTasks.UpdateInventory(this.getActivity(), this.getView().findViewById(R.id.inventoryListLayout));
    }
}
