package malow.gladiatus.activities.training;

import android.app.Activity;
import android.view.View;

public class TrainingOnClick
{
    private static TrainingOnClick self = new TrainingOnClick();

    public static SetCurrentlyTraining setCurrentlyTraining(String stat, Activity activity, View trainingView)
    {
        return self.new SetCurrentlyTraining(stat, activity, trainingView);
    }
    public class SetCurrentlyTraining implements View.OnClickListener
    {
        public String stat;
        public Activity activity;
        public View trainingView;

        public SetCurrentlyTraining(String stat, Activity activity, View trainingView)
        {
            this.stat = stat;
            this.activity = activity;
            this.trainingView = trainingView;
        }
        @Override
        public void onClick(View v)
        {
            TrainingTasks.SetCurrentlyTraining(this.stat, this.activity, this.trainingView);
        }
    }
}
