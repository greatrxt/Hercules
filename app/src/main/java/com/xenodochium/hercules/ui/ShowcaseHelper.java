package com.xenodochium.hercules.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.wooplr.spotlight.SpotlightView;
import com.wooplr.spotlight.utils.SpotlightListener;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.StandardHomeListItemAdapter;
import com.xenodochium.hercules.application.Hercules;

public class ShowcaseHelper {


    /**
     * For first time users
     */
    private static void displayShowcase(Activity activity, SpotlightListener spotlightListener, String header, String text, String usageId, View targetView) {
        if(targetView == null)
            return;

        try {
            SpotlightView.Builder spotlightVieBuilder = new SpotlightView.Builder(activity).setConfiguration(Hercules.getSpotlightConfig())
                    .headingTvText(header)
                    .subHeadingTvText(text)
                    .target(targetView)
                    .usageId(usageId); //UNIQUE ID

            spotlightVieBuilder.setListener(spotlightListener);
            spotlightVieBuilder.show();
        } catch (Exception e){
            e.printStackTrace();
            FirebaseAnalytics.getInstance(activity).logEvent(e.getStackTrace().toString(), new Bundle());
        }
    }

    /**
     * Help screen for workout tab
     */
    public static class WorkoutHelperBuilder
            implements SpotlightListener {

        Activity activity;
        ListView listViewExercise;
        FloatingActionButton buttonAddExercise;
        ImageButton buttonHelpWorkout;

        public WorkoutHelperBuilder(Activity activity, ListView listViewExercise, FloatingActionButton buttonAddExercise, ImageButton buttonHelpWorkout){
            this.activity = activity;
            this.listViewExercise = listViewExercise;
            this.buttonAddExercise = buttonAddExercise;
            this.buttonHelpWorkout = buttonHelpWorkout;
        }

        public void startShowcase(String usageId){
            onUserClicked(usageId);
        }

        public void onUserClicked(String usageId) {
            String runningId = usageId.split("_")[0];
            usageId = usageId.split("_")[1];
            switch (usageId){
                case "add":
                    displayShowcase(activity,this,"Add Workout","You can add your own workout\nsuch as walking, squats, dumb-bell row and so on",
                            runningId + "_label", buttonAddExercise);
                    break;
                case "label":
                    if(listViewExercise != null)
                        displayShowcase(activity, this,"Workout list","List of all workouts",
                            runningId + "_play", ((StandardHomeListItemAdapter)listViewExercise.getAdapter()).getChildAtPosition(0).findViewById(R.id.text_view_list_item_label));
                    break;
                case "play":
                    if(listViewExercise != null)
                        displayShowcase(activity, this,"Play Workout","Play a workout using this button",
                            runningId + "_delete", ((StandardHomeListItemAdapter)listViewExercise.getAdapter()).getChildAtPosition(0).findViewById(R.id.image_button_play));
                    break;
                case "delete":
                    if(listViewExercise != null)
                        displayShowcase(activity, this,"Delete Workout","Delete a workout using this button",
                            runningId + "_help", ((StandardHomeListItemAdapter)listViewExercise.getAdapter()).getChildAtPosition(0).findViewById(R.id.image_button_delete));
                    break;
                case "help":
                    displayShowcase(activity, this,"Help","You can view this help screen again by hitting this button",
                            runningId + "_done", buttonHelpWorkout);
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * Help screen for routine tab
     */
    public static class RoutineHelperBuilder
            implements SpotlightListener {

        Activity activity;
        ListView listViewExercise;
        FloatingActionButton buttonAddExercise;
        ImageButton buttonHelpWorkout;

        public RoutineHelperBuilder(Activity activity, ListView listViewExercise, FloatingActionButton buttonAddExercise, ImageButton buttonHelpWorkout){
            this.activity = activity;
            this.listViewExercise = listViewExercise;
            this.buttonAddExercise = buttonAddExercise;
            this.buttonHelpWorkout = buttonHelpWorkout;
        }

        public void startShowcase(String usageId){
            onUserClicked(usageId);
        }

        public void onUserClicked(String usageId) {
            String runningId = usageId.split("_")[0];
            usageId = usageId.split("_")[1];
            switch (usageId){
                case "add":
                    displayShowcase(activity,this,"Add Routine","You can add your own routine.\nA Routine is a collection of workouts.",
                            runningId + "_label", buttonAddExercise);
                    break;
                case "label":
                    if(listViewExercise != null)
                        displayShowcase(activity, this,"Routine list","List of all routines",
                            runningId + "_play", ((StandardHomeListItemAdapter)listViewExercise.getAdapter()).getChildAtPosition(0).findViewById(R.id.text_view_list_item_label));
                    break;
                case "play":
                    if(listViewExercise != null)
                        displayShowcase(activity, this,"Play Routine","Play all workouts in a routine using this button",
                            runningId + "_delete", ((StandardHomeListItemAdapter)listViewExercise.getAdapter()).getChildAtPosition(0).findViewById(R.id.image_button_play));
                    break;
                case "delete":
                    if(listViewExercise != null)
                        displayShowcase(activity, this,"Delete Routine","Delete a routine using this button",
                            runningId + "_help", ((StandardHomeListItemAdapter)listViewExercise.getAdapter()).getChildAtPosition(0).findViewById(R.id.image_button_delete));
                    break;
                case "help":
                    displayShowcase(activity, this,"Help","You can view help screen by hitting this button",
                            runningId + "_done", buttonHelpWorkout);
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * Help screen for body part tab
     */
    public static class BodyPartHelperBuilder
            implements SpotlightListener {

        Activity activity;
        ListView listViewExercise;
        FloatingActionButton buttonAddExercise;
        ImageButton buttonHelpWorkout;

        public BodyPartHelperBuilder(Activity activity, ListView listViewExercise, FloatingActionButton buttonAddExercise, ImageButton buttonHelpWorkout){
            this.activity = activity;
            this.listViewExercise = listViewExercise;
            this.buttonAddExercise = buttonAddExercise;
            this.buttonHelpWorkout = buttonHelpWorkout;
        }

        public void startShowcase(String usageId){
            onUserClicked(usageId);
        }

        public void onUserClicked(String usageId) {
            String runningId = usageId.split("_")[0];
            usageId = usageId.split("_")[1];
            switch (usageId){
                case "add":
                    displayShowcase(activity,this,"Add Body Part","You can add your own routine.\nA Routine is a collection of workouts.",
                            runningId + "_label", buttonAddExercise);
                    break;
                case "label":
                    if(listViewExercise != null)
                        displayShowcase(activity, this,"Routine list","List of all routines",
                                runningId + "_play", ((StandardHomeListItemAdapter)listViewExercise.getAdapter()).getChildAtPosition(0).findViewById(R.id.text_view_list_item_label));
                    break;
                case "play":
                    if(listViewExercise != null)
                        displayShowcase(activity, this,"Play Routine","Play a workout using this button",
                                runningId + "_delete", ((StandardHomeListItemAdapter)listViewExercise.getAdapter()).getChildAtPosition(0).findViewById(R.id.image_button_play));
                    break;
                case "delete":
                    if(listViewExercise != null)
                        displayShowcase(activity, this,"Delete Routine","Delete a workout using this button",
                                runningId + "_help", ((StandardHomeListItemAdapter)listViewExercise.getAdapter()).getChildAtPosition(0).findViewById(R.id.image_button_delete));
                    break;
                case "help":
                    displayShowcase(activity, this,"Help","You can view this help screen again by hitting this button",
                            runningId + "_done", buttonHelpWorkout);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Help screen for player screen
     */
    public static class PlayerHelperBuilder
            implements SpotlightListener {

        Activity activity;
        View view1, view2, view3, view4, view5;


        public PlayerHelperBuilder(Activity activity, View view1, View view2, View view3, View view4, View view5){
            this.activity = activity;
            this.view1 = view1;
            this.view2 = view2;
            this.view3 = view3;
            this.view4 = view4;
            this.view5 = view5;
        }

        public void startShowcase(String usageId){
            onUserClicked(usageId);
        }

        public void onUserClicked(String usageId) {
            String runningId = usageId.split("_")[0];
            usageId = usageId.split("_")[1];
            switch (usageId){
                case "layoutInfo":
                    displayShowcase(activity,this,"Workout Info","You'll find all workout related info here",
                            runningId + "_repetitionsCounter", view1);
                    break;
                case "repetitionsCounter":
                    displayShowcase(activity, this,"Repetitions Counter","Counts the number of repetitions while you work out",
                                runningId + "_secondsCounter", view2);
                    break;
                case "secondsCounter":
                    displayShowcase(activity, this,"Timer","Counts seconds while you work out",
                                runningId + "_layoutControl", view3);
                    break;
                case "layoutControl":
                    displayShowcase(activity, this,"Controls","Play / Pause workouts using these controls",
                                runningId + "_imageButtonSliderToggle", view4);
                    break;
                case "imageButtonSliderToggle":
                    displayShowcase(activity, this,"View Playlist","View your entire workout playlist here",
                            runningId + "_done", view5);
                    break;
                default:
                    break;
            }
        }
    }

}
