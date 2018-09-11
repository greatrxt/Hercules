package com.xenodochium.hercules.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.BodyPart;
import com.xenodochium.hercules.model.Workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class MultiSelectArrayAdapter extends BaseAdapter {

    Context context;
    List<Workout> workouts;
    TreeMap<String, List> bodyPartWiseWorkouts;
    HashMap<Integer, View> viewList;

    public MultiSelectArrayAdapter(@NonNull Context context, List<Workout> workouts) {
        this.context = context;
        this.workouts = workouts;
        bodyPartWiseWorkouts = new TreeMap<>();
        viewList = new HashMap<>();
        Iterator<Workout> workoutIterator = this.workouts.iterator();
        while (workoutIterator.hasNext()) {
            Workout workout = workoutIterator.next();
            BodyPart bodyPart = Hercules.getInstance().getDaoSession().getBodyPartDao().load(workout.getBodyPartId());
            if (!bodyPartWiseWorkouts.containsKey(bodyPart.getName())) {
                bodyPartWiseWorkouts.put(bodyPart.getName(), new ArrayList());
            }

            List workoutsForBodyPartList = bodyPartWiseWorkouts.get(bodyPart.getName());
            workoutsForBodyPartList.add(workout);
        }
    }

    /**
     * Returns a list of all routine items selected by user
     *
     * @return
     */
    public List<Workout> getSelectedItems() {
        List<Workout> selectedWorkouts = new ArrayList<>();
        for (int m = 0; m < getCount(); m++) {
            if (getItem(m) instanceof Workout) {
                LinearLayout ll = (LinearLayout) viewList.get(m);
                if (((CheckBox) ll.findViewById(R.id.checkbox_selectable_item)).isChecked()) {
                    selectedWorkouts.add(((Workout) getItem(m)).copy());
                }
            }
        }

        return selectedWorkouts;
    }

    /**
     * Return sum of size of routine items and the headers
     *
     * @return
     */
    @Override
    public int getCount() {
        return workouts.size() + bodyPartWiseWorkouts.keySet().size();
    }

    /**
     * If position corresponds to header then return header else return item
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        int counter = -1;
        Iterator<String> bodyPartsNameIterator = bodyPartWiseWorkouts.keySet().iterator();
        while (bodyPartsNameIterator.hasNext()) {
            counter++;
            String bodyPartName = bodyPartsNameIterator.next();
            if (counter == position) {
                //header
                return bodyPartName;
            }

            List<Workout> workouts = bodyPartWiseWorkouts.get(bodyPartName);
            for (int r = 0; r < workouts.size(); r++) {
                counter++;
                if (counter == position) {
                    return workouts.get(r);
                }
            }
        }

        return null;
    }

    @Override
    public long getItemId(int i) {
        return workouts.get(i).getWorkoutId();
    }

    /**
     * If position corresponds to header then return header view else return item view
     * @param position
     * @param view
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            int counter = -1;
            Iterator<String> bodyPartsNameIterator = bodyPartWiseWorkouts.keySet().iterator();
            while (bodyPartsNameIterator.hasNext()) {
                counter++;
                final String bodyPartName = bodyPartsNameIterator.next();
                if (counter == position) {
                    //add header
                    view = layoutInflater.inflate(R.layout.multiselect_list_group_header, null);
                    TextView textView = view.findViewById(R.id.text_view_selectable_group_label);
                    textView.setText(bodyPartName);
                    CheckBox checkBox = view.findViewById(R.id.checkbox_selectable_group);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        //check all items for the body part if the header is selected
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            //check all items of this body part
                            for (int m = 0; m < getCount(); m++) {
                                if (getItem(m) instanceof Workout) {
                                    Workout workout = (Workout) getItem(m);
                                    BodyPart bodyPart = Hercules.getInstance().getDaoSession().getBodyPartDao().load(workout.getBodyPartId());
                                    if (bodyPart.getName().equals(bodyPartName)) {
                                        LinearLayout ll = (LinearLayout) viewList.get(m);
                                        ((CheckBox) ll.findViewById(R.id.checkbox_selectable_item)).setChecked(isChecked);
                                        /*for (int l = 0; l < ll.getChildCount(); l++) {
                                            if (ll.getChildAt(l) instanceof CheckBox) {
                                                ((CheckBox) ll.getChildAt(l)).setChecked(isChecked);
                                            }
                                        }*/
                                    }
                                }
                            }

                        }
                    });
                    break;
                }

                List<Workout> workouts = bodyPartWiseWorkouts.get(bodyPartName);
                for (int r = 0; r < workouts.size(); r++) {
                    counter++;
                    if (counter == position) {
                        //add item
                        view = layoutInflater.inflate(R.layout.multiselect_list_item, null);
                        TextView textView = view.findViewById(R.id.text_view_selectable_item_label);
                        textView.setText(workouts.get(r).getName());
                        CheckBox checkBox = view.findViewById(R.id.checkbox_selectable_item);
                        break;
                    }
                }
            }
            viewList.put(position, view);
        }

        return view;
    }
}
