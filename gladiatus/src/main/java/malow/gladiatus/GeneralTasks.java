package malow.gladiatus;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class GeneralTasks
{
    public static void OpenStatInfoPopup(String stat, final Activity actvivity, final View showAtLocationView)
    {
        String header = "";
        String infoText = "";
        if(stat.equals("health"))
        {
            header = "Health";
            infoText = "Health allows you to take more damage before being defeated. It is a cheaper stat than the others.";
        }
        if(stat.equals("strength"))
        {
            header = "Strength";
            infoText = "Strength makes you deal more damage with physical attacks. It also lets you carry more weight, making you able to use heavier armor and weapons.";
        }
        if(stat.equals("dexterity"))
        {
            header = "Dexterity";
            infoText = "Dexterity increases your chance to hit with physical weapons. It also increases your armor slightly by giving you a higher chance to evade attacks. It also increases your Movement speed and Initiative slightly.";
        }
        if(stat.equals("intelligence"))
        {
            header = "Intelligence";
            infoText = "Intelligence increases your chance to hit with spells and staff attacks. It also increases the effectiveness of some abilities.";
        }
        if(stat.equals("willpower"))
        {
            header = "Willpower";
            infoText = "Willpower increases the effect of your spells and reduces the effect of enemy spells on you.";
        }
        if(stat.equals("armor"))
        {
            header = "Armor";
            infoText = "Armor increases your chance to avoid physical attacks. It consists both of evasion gained from for example Dexterity, and straight up armor gained from for example gear.";
        }
        if(stat.equals("initiative"))
        {
            header = "Initiative";
            infoText = "Initiative determines if you or your opponent goes first in a combat round. Initiative depends on weapon used, you Dexterity and your weight among other things.";
        }

        final String statName = header;
        final String statDescription = infoText;

        actvivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                LayoutInflater layoutInflater = (LayoutInflater)actvivity.getBaseContext().getSystemService(actvivity.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_frame, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable (new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.setTouchInterceptor(new View.OnTouchListener()
                {
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        if (event.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            popupWindow.dismiss();
                            return true;
                        }
                        return false;
                    }
                });

                popupWindow.showAtLocation(showAtLocationView, Gravity.CENTER, 0, 0);
                popupWindow.setFocusable(true);
                popupWindow.update();

                LayoutInflater inflater = actvivity.getLayoutInflater();
                LinearLayout layout = (LinearLayout) popupView.findViewById(R.id.popup_frame_layout);
                layout.removeAllViews();


                View abilityView = inflater.inflate(R.layout.stat_info, null);
                ((TextView) abilityView.findViewById(R.id.stat_name)).setText(statName);
                ((TextView) abilityView.findViewById(R.id.stat_description)).setText(statDescription);

                abilityView.setOnClickListener(new ImageButton.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        popupWindow.dismiss();
                    }
                });

                layout.addView(abilityView);
            }
        });
    }
}
