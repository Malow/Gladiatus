package malow.gladiatus;

import android.app.Activity;
import android.view.View;

public class GeneralOnClick
{
    private static GeneralOnClick self = new GeneralOnClick();

    public static OpenStatInfoPopup openStatInfoPopup(String stat, Activity activity, View showAtLocationView)
    {
        return self.new OpenStatInfoPopup(stat, activity, showAtLocationView);
    }
    public class OpenStatInfoPopup implements  View.OnClickListener
    {
        public String stat;
        public Activity activity;
        public View showAtLocationView;

        public OpenStatInfoPopup(String stat, Activity activity, View showAtLocationView)
        {
            this.stat = stat;
            this.activity = activity;
            this.showAtLocationView = showAtLocationView;
        }
        @Override
        public void onClick(View v)
        {
            GeneralTasks.OpenStatInfoPopup(this.stat, this.activity, this.showAtLocationView);
        }
    }
}
