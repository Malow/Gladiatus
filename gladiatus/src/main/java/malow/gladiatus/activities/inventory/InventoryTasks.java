package malow.gladiatus.activities.inventory;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import malow.gladiatus.R;
import malow.gladiatus.common.models.Item;
import malow.gladiatus.common.models.responses.CharacterInventoryResponse;

public class InventoryTasks
{
    public static void UpdateInventory(final Activity activity, final View inventoryView)
    {
        List<Item> items = new ArrayList<Item>();
        items.add(new Item("Healing Potion", "Consumable"));
        items.add(new Item("Bastard Sword", "One-handed Weapon"));
        items.add(new Item("Bulwark of Steel", "Shield"));

        UpdateInventoryGUI(new CharacterInventoryResponse(items), activity);
        inventoryView.setVisibility(View.VISIBLE);
    }

    public static void UpdateInventoryGUI(final CharacterInventoryResponse response, final Activity activity)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                LayoutInflater inflater = activity.getLayoutInflater();
                LinearLayout itemsList = (LinearLayout) activity.findViewById(R.id.inventoryListLayout);
                itemsList.removeAllViews();

                for (Item item : response.items)
                {
                    View itemView = inflater.inflate(R.layout.item, null);
                    ((TextView) itemView.findViewById(R.id.item_name)).setText(item.name);
                    ((TextView) itemView.findViewById(R.id.item_type)).setText(item.type);

                    itemsList.addView(itemView);
                }
            }
        });
    }
}