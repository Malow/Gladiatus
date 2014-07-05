package malow.gladiatus.activities.characterinfo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import malow.gladiatus.GeneralOnClick;
import malow.gladiatus.Globals;
import malow.gladiatus.R;

public class CharacterInfoFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Globals.characterInfoFragment = this;

        View v = inflater.inflate(R.layout.character_info_screen, container, false);

        TextView healthLabel = (TextView) v.findViewById(R.id.partial_stats_health_label);
        healthLabel.setOnClickListener(GeneralOnClick.openStatInfoPopup("health", this.getActivity(), v.findViewById(R.id.character_info_screen_hidden)));

        TextView strengthLabel = (TextView) v.findViewById(R.id.partial_stats_strength_label);
        strengthLabel.setOnClickListener(GeneralOnClick.openStatInfoPopup("strength", this.getActivity(), v.findViewById(R.id.character_info_screen_hidden)));

        TextView dexterityLabel = (TextView) v.findViewById(R.id.partial_stats_dexterity_label);
        dexterityLabel.setOnClickListener(GeneralOnClick.openStatInfoPopup("dexterity", this.getActivity(), v.findViewById(R.id.character_info_screen_hidden)));

        TextView intelligenceLabel = (TextView) v.findViewById(R.id.partial_stats_intelligence_label);
        intelligenceLabel.setOnClickListener(GeneralOnClick.openStatInfoPopup("intelligence", this.getActivity(), v.findViewById(R.id.character_info_screen_hidden)));

        TextView willpowerLabel = (TextView) v.findViewById(R.id.partial_stats_willpower_label);
        willpowerLabel.setOnClickListener(GeneralOnClick.openStatInfoPopup("willpower", this.getActivity(), v.findViewById(R.id.character_info_screen_hidden)));

        TextView armorLabel = (TextView) v.findViewById(R.id.partial_stats_armor_label);
        armorLabel.setOnClickListener(GeneralOnClick.openStatInfoPopup("armor", this.getActivity(), v.findViewById(R.id.character_info_screen_hidden)));

        TextView initiativeLabel = (TextView) v.findViewById(R.id.partial_stats_initiative_label);
        initiativeLabel.setOnClickListener(GeneralOnClick.openStatInfoPopup("initiative", this.getActivity(), v.findViewById(R.id.character_info_screen_hidden)));

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Hide the layout
        LinearLayout hiddenInfo = (LinearLayout) this.getView().findViewById(R.id.character_info_screen_hidden);
        hiddenInfo.setVisibility(View.INVISIBLE);

        CharacterInfoTasks.UpdateCharacterInfo();
    }
}