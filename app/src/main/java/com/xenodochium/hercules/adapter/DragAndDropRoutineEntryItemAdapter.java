package com.xenodochium.hercules.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItemAdapter;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.engine.RoutineOrchestratorImpl;
import com.xenodochium.hercules.model.BodyPart;
import com.xenodochium.hercules.model.RoutineEntry;
import com.xenodochium.hercules.ui.PlayerActivity;
import com.xenodochium.hercules.ui.RoutineActivity;

import java.util.HashMap;
import java.util.List;

public class DragAndDropRoutineEntryItemAdapter extends DragItemAdapter<RoutineEntry, DragAndDropRoutineEntryItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private Context context;
    private HashMap<Integer, ViewHolder> viewHolderMap;

    public DragAndDropRoutineEntryItemAdapter(Context context, List<RoutineEntry> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        this.context = context;
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        viewHolderMap = new HashMap<>();
        setItemList(list);
    }

    public HashMap<Integer, ViewHolder> getViewHolderMap() {
        return viewHolderMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        RoutineEntry routineEntry = mItemList.get(position);
        String text = routineEntry.getName();
        holder.textViewExerciseOrder.setText(String.valueOf(position + 1));
        holder.textViewExerciseName.setText(text);

        if (context instanceof PlayerActivity) {
            if (RoutineOrchestratorImpl.getInstance().getCurrentlyPlayingRoutineEntryNumber() == position) {
                holder.textViewExerciseName.setTypeface(null, Typeface.BOLD);
                holder.textViewExerciseName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }

        if (routineEntry.getRoutineEntryType().equals(RoutineEntry.RoutineEntryType.BREAK)) {
            holder.textViewExerciseSets.setText(routineEntry.getDuration() + " Seconds");
        } else {
            holder.textViewExerciseSets.setText(
                    routineEntry.getTimeToGetInPosition() + " Seconds To Get In Position\n"
                            + routineEntry.getStandardNumberOfRepetitions() + " Repetitions X "
                            + routineEntry.getStandardNumberOfSets() + " Sets\n"
                            + routineEntry.getRestTimeAfterExercise() + "  Seconds Rest After Each Set\n");
        }
        holder.itemView.setTag(routineEntry);
        holder.bindPosition = position;

        viewHolderMap.put(position, holder);
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).getRoutineEntryId();
        //return position;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView textViewExerciseName, textViewExerciseOrder, textViewExerciseSets;
        int bindPosition = -1;

        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            textViewExerciseOrder = itemView.findViewById(R.id.text_view_exercise_order);
            textViewExerciseName = itemView.findViewById(R.id.text_view_exercise_name);
            textViewExerciseSets = itemView.findViewById(R.id.text_view_exercise_sets);
        }

        @Override
        public void onItemClicked(View view) {

            if (context instanceof RoutineActivity) {    //bit dirty. Needs to be improved.
                //allow user to edit routine item
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppThemeDialog));

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.activity_workout, null);
                dialogBuilder.setView(dialogView);

                final RoutineEntry routineEntry = mItemList.get(bindPosition);
                ((TextView) dialogView.findViewById(R.id.text_view_label_workout)).setText(context.getResources().getText(R.string.edit_workout));
                ((TextInputLayout) dialogView.findViewById(R.id.til_exercise_name)).getEditText().setText(routineEntry.getName());
                ((TextInputLayout) dialogView.findViewById(R.id.til_number_of_sets)).getEditText().setText(String.valueOf(routineEntry.getStandardNumberOfSets()));
                ((TextInputLayout) dialogView.findViewById(R.id.til_repetitions)).getEditText().setText(String.valueOf(routineEntry.getStandardNumberOfRepetitions()));
                ((TextInputLayout) dialogView.findViewById(R.id.til_duration)).getEditText().setText(String.valueOf(routineEntry.getDuration()));
                ((TextInputLayout) dialogView.findViewById(R.id.til_time_to_get_in_position)).getEditText().setText(String.valueOf(routineEntry.getTimeToGetInPosition()));
                ((TextInputLayout) dialogView.findViewById(R.id.til_rest_time_after_exercise)).getEditText().setText(String.valueOf(routineEntry.getRestTimeAfterExercise()));
                ((CheckBox) dialogView.findViewById(R.id.checkbox_count_duration_in_reverse)).setChecked(routineEntry.getCountDurationInReverse());
                ((CheckBox) dialogView.findViewById(R.id.checkbox_count_repetitions_in_reverse)).setChecked(routineEntry.getCountDurationInReverse());

                List<BodyPart> bodyPartsNameList = Hercules.getInstance().getDaoSession().getBodyPartDao().loadAll();
                ArrayAdapter<BodyPart> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, bodyPartsNameList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((Spinner) dialogView.findViewById(R.id.spinner_body_part)).setAdapter(spinnerAdapter);
                for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                    if ((spinnerAdapter.getItem(i)).getBodyPartId() == routineEntry.getBodyPartId()) {
                        ((Spinner) dialogView.findViewById(R.id.spinner_body_part)).setSelection(i);
                    }
                }
                final AlertDialog alertDialog = dialogBuilder.create();
                dialogView.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        routineEntry.setName(((TextInputLayout) dialogView.findViewById(R.id.til_exercise_name)).getEditText().getText().toString());
                        routineEntry.setStandardNumberOfSets(Integer.parseInt(((TextInputLayout) dialogView.findViewById(R.id.til_number_of_sets)).getEditText().getText().toString()));
                        routineEntry.setStandardNumberOfRepetitions(Integer.parseInt(((TextInputLayout) dialogView.findViewById(R.id.til_repetitions)).getEditText().getText().toString()));
                        routineEntry.setCountRepetitionsInReverse(((CheckBox) dialogView.findViewById(R.id.checkbox_count_repetitions_in_reverse)).isChecked());
                        routineEntry.setDuration(Integer.parseInt(((TextInputLayout) dialogView.findViewById(R.id.til_duration)).getEditText().getText().toString()));
                        routineEntry.setCountDurationInReverse(((CheckBox) dialogView.findViewById(R.id.checkbox_count_duration_in_reverse)).isChecked());
                        if (!((TextInputLayout) dialogView.findViewById(R.id.til_time_to_get_in_position)).getEditText().getText().toString().isEmpty())
                            routineEntry.setTimeToGetInPosition(Integer.parseInt(((TextInputLayout) dialogView.findViewById(R.id.til_time_to_get_in_position)).getEditText().getText().toString()));
                        if (!((TextInputLayout) dialogView.findViewById(R.id.til_rest_time_after_exercise)).getEditText().getText().toString().isEmpty())
                            routineEntry.setRestTimeAfterExercise(Integer.parseInt(((TextInputLayout) dialogView.findViewById(R.id.til_rest_time_after_exercise)).getEditText().getText().toString()));
                        if ((((Spinner) dialogView.findViewById(R.id.spinner_body_part)).getSelectedItem()) != null) {
                            routineEntry.setBodyPartId(((BodyPart) ((Spinner) dialogView.findViewById(R.id.spinner_body_part)).getSelectedItem()).getBodyPartId());
                        }
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            } else if (context instanceof PlayerActivity) {
                RoutineOrchestratorImpl.getInstance().setCurrentlyPlayingRoutineEntryNumber(bindPosition);
                RoutineOrchestratorImpl.getInstance().play();
                ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.list_fragment_container).getFragmentManager().popBackStack();
            }
        }

        @Override
        public boolean onItemLongClicked(View view) {
            //Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
