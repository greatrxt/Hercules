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
import com.xenodochium.hercules.model.RoutineItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class MultiSelectArrayAdapter extends BaseAdapter {

    Context context;
    List<RoutineItem> routineItems;
    TreeMap<String, List> bodyPartWiseRoutineItems;
    HashMap<Integer, View> viewList;

    public MultiSelectArrayAdapter(@NonNull Context context, List<RoutineItem> routineItems) {
        this.context = context;
        this.routineItems = routineItems;
        bodyPartWiseRoutineItems = new TreeMap<>();
        viewList = new HashMap<>();
        Iterator<RoutineItem> routineItemIterator = this.routineItems.iterator();
        while (routineItemIterator.hasNext()) {
            RoutineItem routineItem = routineItemIterator.next();
            BodyPart bodyPart = Hercules.getInstance().getDaoSession().getBodyPartDao().load(routineItem.getBodyPartId());
            if (!bodyPartWiseRoutineItems.containsKey(bodyPart.getName())) {
                bodyPartWiseRoutineItems.put(bodyPart.getName(), new ArrayList());
            }

            List routineItemsForBodyPartList = bodyPartWiseRoutineItems.get(bodyPart.getName());
            routineItemsForBodyPartList.add(routineItem);
        }
    }

    @Override
    public int getCount() {
        return routineItems.size() + bodyPartWiseRoutineItems.keySet().size();
    }

    @Override
    public Object getItem(int position) {
        int counter = -1;
        Iterator<String> bodyPartsNameIterator = bodyPartWiseRoutineItems.keySet().iterator();
        while (bodyPartsNameIterator.hasNext()) {
            counter++;
            String bodyPartName = bodyPartsNameIterator.next();
            if (counter == position) {
                //header
                return bodyPartName;
            }

            List<RoutineItem> routineItems = bodyPartWiseRoutineItems.get(bodyPartName);
            for (int r = 0; r < routineItems.size(); r++) {
                counter++;
                if (counter == position) {
                    return routineItems.get(r);
                }
            }
        }

        return null;
    }

    @Override
    public long getItemId(int i) {
        return routineItems.get(i).getRoutineItemId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            int counter = -1;
            Iterator<String> bodyPartsNameIterator = bodyPartWiseRoutineItems.keySet().iterator();
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
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            //check all items of this body part
                            for (int m = 0; m < getCount(); m++) {
                                if (getItem(m) instanceof RoutineItem) {
                                    RoutineItem routineItem = (RoutineItem) getItem(m);
                                    BodyPart bodyPart = Hercules.getInstance().getDaoSession().getBodyPartDao().load(routineItem.getBodyPartId());
                                    if (bodyPart.getName().equals(bodyPartName)) {
                                        LinearLayout ll = (LinearLayout) viewList.get(m);
                                        for (int l = 0; l < ll.getChildCount(); l++) {
                                            if (ll.getChildAt(l) instanceof CheckBox) {
                                                ((CheckBox) ll.getChildAt(l)).setChecked(isChecked);
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    });
                    break;
                }

                List<RoutineItem> routineItems = bodyPartWiseRoutineItems.get(bodyPartName);
                for (int r = 0; r < routineItems.size(); r++) {
                    counter++;
                    if (counter == position) {
                        //add item
                        view = layoutInflater.inflate(R.layout.multiselect_list_item, null);
                        TextView textView = view.findViewById(R.id.text_view_selectable_item_label);
                        textView.setText(routineItems.get(r).getName());
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
