package malow.gladiatus.activities.training;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import malow.gladiatus.Globals;
import malow.gladiatus.NetworkClient;
import malow.gladiatus.R;
import malow.gladiatus.common.Training;
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.CharacterInfoRequest;
import malow.gladiatus.common.models.requests.SetCurrentlyTrainingRequest;
import malow.gladiatus.common.models.responses.CharacterInfoResponse;
import malow.gladiatus.common.models.responses.NoCharacterFoundResponse;
import malow.gladiatus.common.models.responses.SetCurrentlyTrainingSuccessfulResponse;
import malow.malowlib.RequestResponseClient;

public class TrainingTasks
{
    public static void UpdateTrainingGUI(final CharacterInfoResponse response, final Activity activity, final View trainingView)
    {
        SetCurrentlyTrainingGUI(response.currentlyTraining, activity, trainingView);

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                // Set progress bars
                ProgressBar healthBar = (ProgressBar) trainingView.findViewById(R.id.character_training_health_bar);
                ProgressBar strengthBar = (ProgressBar) trainingView.findViewById(R.id.character_training_strength_bar);
                ProgressBar dexterityBar = (ProgressBar) trainingView.findViewById(R.id.character_training_dextierty_bar);
                ProgressBar intelligenceBar = (ProgressBar) trainingView.findViewById(R.id.character_training_intelligence_bar);
                ProgressBar willpowerBar = (ProgressBar) trainingView.findViewById(R.id.character_training_willpower_bar);

                int healthPercent = (int) ((response.health - ((int) response.health)) * 100);
                int strengthPercent = (int) ((response.strength - ((int) response.strength)) * 100);
                int dexterityPercent = (int) ((response.dexterity - ((int) response.dexterity)) * 100);
                int intelligencePercent = (int) ((response.intelligence - ((int) response.intelligence)) * 100);
                int willpowerPercent = (int) ((response.willpower - ((int) response.willpower)) * 100);

                healthBar.setProgress(healthPercent);
                strengthBar.setProgress(strengthPercent);
                dexterityBar.setProgress(dexterityPercent);
                intelligenceBar.setProgress(intelligencePercent);
                willpowerBar.setProgress(willpowerPercent);

                // Set current skill texts
                TextView healthText = (TextView) trainingView.findViewById(R.id.character_training_current_health);
                healthText.setText(String.valueOf((int) response.health));
                TextView strengthText = (TextView) trainingView.findViewById(R.id.character_training_current_strength);
                strengthText.setText(String.valueOf((int) response.strength));
                TextView dexterityText = (TextView) trainingView.findViewById(R.id.character_training_current_dexterity);
                dexterityText.setText(String.valueOf((int) response.dexterity));
                TextView intelligenceText = (TextView) trainingView.findViewById(R.id.character_training_current_intelligence);
                intelligenceText.setText(String.valueOf((int) response.intelligence));
                TextView willpowerText = (TextView) trainingView.findViewById(R.id.character_training_current_willpower);
                willpowerText.setText(String.valueOf((int) response.willpower));

                // Set time to skill texts
                TextView healthTTSText = (TextView) trainingView.findViewById(R.id.character_training_health_time);
                healthTTSText.setText(Training.GetTimeToSkillString(response.health));
                TextView strengthTTSText = (TextView) trainingView.findViewById(R.id.character_training_strength_time);
                strengthTTSText.setText(Training.GetTimeToSkillString(response.strength));
                TextView dexterityTTSText = (TextView) trainingView.findViewById(R.id.character_training_dexterity_time);
                dexterityTTSText.setText(Training.GetTimeToSkillString(response.dexterity));
                TextView intelligenceTTSText = (TextView) trainingView.findViewById(R.id.character_training_intelligence_time);
                intelligenceTTSText.setText(Training.GetTimeToSkillString(response.intelligence));
                TextView willpowerTTSText = (TextView) trainingView.findViewById(R.id.character_training_willpower_time);
                willpowerTTSText.setText(Training.GetTimeToSkillString(response.willpower));

                // Hide the layout
                trainingView.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void UpdateTraining(final Activity activity, final View trainingView)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                CharacterInfoRequest characterInfoRequest = new CharacterInfoRequest(Globals.sessionId);
                ModelInterface response = null;
                try
                {
                    response = NetworkClient.sendAndReceive(characterInfoRequest);
                    if(response instanceof CharacterInfoResponse)
                    {
                        UpdateTrainingGUI((CharacterInfoResponse) response, activity, trainingView);
                    }
                    else if(response instanceof NoCharacterFoundResponse)
                    {
                        Log.e(this.getClass().getSimpleName(), "CharacterInfoRequest failed, NoCharacterFound!");
                    }
                    else
                    {
                        Log.e(this.getClass().getSimpleName(), "CharacterInfoRequest failed, Unexpected response: " + response);
                    }
                }
                catch (RequestResponseClient.ConnectionBrokenException e)
                {
                    Log.i(this.getClass().getSimpleName(), "CharacterInfoRequest failed, couldn't connect to server.");
                }
                return null;
            }
        }.execute();
    }

    public static void SetCurrentlyTraining(final String stat, final Activity activity, final View trainingView)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                SetCurrentlyTrainingRequest request = new SetCurrentlyTrainingRequest(Globals.sessionId, stat);
                ModelInterface response = null;
                try
                {
                    response = NetworkClient.sendAndReceive(request);
                    if(response instanceof SetCurrentlyTrainingSuccessfulResponse)
                    {
                        SetCurrentlyTrainingGUI(stat, activity, trainingView);
                    }
                    else
                    {
                        Log.e(this.getClass().getSimpleName(), "SetCurrentlyTraining failed, Unexpected response: " + response);
                    }
                }
                catch (RequestResponseClient.ConnectionBrokenException e)
                {
                    Log.i(this.getClass().getSimpleName(), "SetCurrentlyTraining failed, couldn't connect to server.");
                }
                return null;
            }
        }.execute();
    }

    private static void SetCurrentlyTrainingGUI(final String stat, final Activity activity, final View trainingView)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                LinearLayout health = (LinearLayout) trainingView.findViewById(R.id.character_training_health_border);
                ProgressBar healthBar = (ProgressBar) trainingView.findViewById(R.id.character_training_health_bar);
                LinearLayout strength = (LinearLayout) trainingView.findViewById(R.id.character_training_strength_border);
                ProgressBar strengthBar = (ProgressBar) trainingView.findViewById(R.id.character_training_strength_bar);
                LinearLayout dexterity = (LinearLayout) trainingView.findViewById(R.id.character_training_dexterity_border);
                ProgressBar dexterityBar = (ProgressBar) trainingView.findViewById(R.id.character_training_dextierty_bar);
                LinearLayout intelligence = (LinearLayout) trainingView.findViewById(R.id.character_training_intelligence_border);
                ProgressBar intelligenceBar = (ProgressBar) trainingView.findViewById(R.id.character_training_intelligence_bar);
                LinearLayout willpower = (LinearLayout) trainingView.findViewById(R.id.character_training_willpower_border);
                ProgressBar willpowerBar = (ProgressBar) trainingView.findViewById(R.id.character_training_willpower_bar);

                health.setBackgroundColor(activity.getResources().getColor(R.color.GladiatusColor10));
                healthBar.setProgressDrawable(trainingView.getResources().getDrawable(R.drawable.progress_bar_drawable_blue));
                strength.setBackgroundColor(activity.getResources().getColor(R.color.GladiatusColor10));
                strengthBar.setProgressDrawable(trainingView.getResources().getDrawable(R.drawable.progress_bar_drawable_blue));
                dexterity.setBackgroundColor(activity.getResources().getColor(R.color.GladiatusColor10));
                dexterityBar.setProgressDrawable(trainingView.getResources().getDrawable(R.drawable.progress_bar_drawable_blue));
                intelligence.setBackgroundColor(activity.getResources().getColor(R.color.GladiatusColor10));
                intelligenceBar.setProgressDrawable(trainingView.getResources().getDrawable(R.drawable.progress_bar_drawable_blue));
                willpower.setBackgroundColor(activity.getResources().getColor(R.color.GladiatusColor10));
                willpowerBar.setProgressDrawable(trainingView.getResources().getDrawable(R.drawable.progress_bar_drawable_blue));

                if(stat != null && stat.equals("health"))
                {
                    health.setBackgroundColor(activity.getResources().getColor(R.color.GladiatusHealthGreenColor));
                    healthBar.setProgressDrawable(trainingView.getResources().getDrawable(R.drawable.progress_bar_drawable_health));
                }
                if(stat != null && stat.equals("strength"))
                {
                    strength.setBackgroundColor(activity.getResources().getColor(R.color.GladiatusHealthGreenColor));
                    strengthBar.setProgressDrawable(trainingView.getResources().getDrawable(R.drawable.progress_bar_drawable_health));
                }
                if(stat != null && stat.equals("dexterity"))
                {
                    dexterity.setBackgroundColor(activity.getResources().getColor(R.color.GladiatusHealthGreenColor));
                    dexterityBar.setProgressDrawable(trainingView.getResources().getDrawable(R.drawable.progress_bar_drawable_health));
                }
                if(stat != null && stat.equals("intelligence"))
                {
                    intelligence.setBackgroundColor(activity.getResources().getColor(R.color.GladiatusHealthGreenColor));
                    intelligenceBar.setProgressDrawable(trainingView.getResources().getDrawable(R.drawable.progress_bar_drawable_health));
                }
                if(stat != null && stat.equals("willpower"))
                {
                    willpower.setBackgroundColor(activity.getResources().getColor(R.color.GladiatusHealthGreenColor));
                    willpowerBar.setProgressDrawable(trainingView.getResources().getDrawable(R.drawable.progress_bar_drawable_health));
                }
            }
        });
    }
}
