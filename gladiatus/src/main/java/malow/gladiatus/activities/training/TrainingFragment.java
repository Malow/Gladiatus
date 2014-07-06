package malow.gladiatus.activities.training;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import malow.gladiatus.R;

public class TrainingFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.training_screen, container, false);

        LinearLayout maintain = (LinearLayout) v.findViewById(R.id.character_training_maintain_border);
        maintain.setOnClickListener(TrainingOnClick.setCurrentlyTraining(null, this.getActivity(), v.findViewById(R.id.training_layout)));

        LinearLayout health = (LinearLayout) v.findViewById(R.id.character_training_health_border);
        health.setOnClickListener(TrainingOnClick.setCurrentlyTraining("health", this.getActivity(), v.findViewById(R.id.training_layout)));

        LinearLayout strength = (LinearLayout) v.findViewById(R.id.character_training_strength_border);
        strength.setOnClickListener(TrainingOnClick.setCurrentlyTraining("strength", this.getActivity(), v.findViewById(R.id.training_layout)));

        LinearLayout dexterity = (LinearLayout) v.findViewById(R.id.character_training_dexterity_border);
        dexterity.setOnClickListener(TrainingOnClick.setCurrentlyTraining("dexterity", this.getActivity(), v.findViewById(R.id.training_layout)));

        LinearLayout intelligence = (LinearLayout) v.findViewById(R.id.character_training_intelligence_border);
        intelligence.setOnClickListener(TrainingOnClick.setCurrentlyTraining("intelligence", this.getActivity(), v.findViewById(R.id.training_layout)));

        LinearLayout willpower = (LinearLayout) v.findViewById(R.id.character_training_willpower_border);
        willpower.setOnClickListener(TrainingOnClick.setCurrentlyTraining("willpower", this.getActivity(), v.findViewById(R.id.training_layout)));

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Hide the layout
        LinearLayout hiddenInfo = (LinearLayout) this.getView().findViewById(R.id.training_layout);
        hiddenInfo.setVisibility(View.INVISIBLE);

        TrainingTasks.UpdateTraining(this.getActivity(), this.getView().findViewById(R.id.training_layout));
    }
}
