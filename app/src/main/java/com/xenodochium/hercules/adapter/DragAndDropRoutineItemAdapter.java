package com.xenodochium.hercules.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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
import com.xenodochium.hercules.model.BodyPart;
import com.xenodochium.hercules.model.Workout;

import java.util.List;

public class DragAndDropRoutineItemAdapter extends DragItemAdapter<Workout, DragAndDropRoutineItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private Context context;

    public DragAndDropRoutineItemAdapter(Context context, List<Workout> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        this.context = context;
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        setItemList(list);
    }

    /**
     * @return
     */
    public List getWorkoutsList() {
        return mItemList;
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
        String text = mItemList.get(position).getName();
        holder.textViewExerciseOrder.setText(String.valueOf(position + 1));
        holder.textViewExerciseName.setText(text);
        holder.textViewExerciseSets.setText(
                mItemList.get(position).getTimeToGetInPosition() + " Seconds To Get In Position\n"
                        + mItemList.get(position).getStandardNumberOfRepetitions() + " Repetitions X "
                        + mItemList.get(position).getStandardNumberOfSets() + " Sets\n"
                        + mItemList.get(position).getRestTimeAfterExercise() + "  Seconds Rest After Each Set\n");
        holder.itemView.setTag(mItemList.get(position));
        holder.bindPosition = position;
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).getWorkoutId();
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
            //allow user to edit routine item
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppThemeDialog));

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogView = inflater.inflate(R.layout.activity_workout, null);
            dialogBuilder.setView(dialogView);

            final Workout workout = mItemList.get(bindPosition);
            ((TextView) dialogView.findViewById(R.id.text_view_label_workout)).setText(context.getResources().getText(R.string.edit_workout));
            ((TextInputLayout) dialogView.findViewById(R.id.til_exercise_name)).getEditText().setText(workout.getName());
            ((TextInputLayout) dialogView.findViewById(R.id.til_number_of_sets)).getEditText().setText(String.valueOf(workout.getStandardNumberOfSets()));
            ((TextInputLayout) dialogView.findViewById(R.id.til_repetitions)).getEditText().setText(String.valueOf(workout.getStandardNumberOfRepetitions()));
            ((TextInputLayout) dialogView.findViewById(R.id.til_duration)).getEditText().setText(String.valueOf(workout.getDuration()));
            ((TextInputLayout) dialogView.findViewById(R.id.til_time_to_get_in_position)).getEditText().setText(String.valueOf(workout.getTimeToGetInPosition()));
            ((TextInputLayout) dialogView.findViewById(R.id.til_rest_time_after_exercise)).getEditText().setText(String.valueOf(workout.getRestTimeAfterExercise()));
            ((CheckBox) dialogView.findViewById(R.id.checkbox_count_duration_in_reverse)).setChecked(workout.getCountDurationInReverse());
            ((CheckBox) dialogView.findViewById(R.id.checkbox_count_repetitions_in_reverse)).setChecked(workout.getCountDurationInReverse());

            List<BodyPart> bodyPartsNameList = Hercules.getInstance().getDaoSession().getBodyPartDao().loadAll();
            ArrayAdapter<BodyPart> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, bodyPartsNameList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((Spinner) dialogView.findViewById(R.id.spinner_body_part)).setAdapter(spinnerAdapter);
            for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                if ((spinnerAdapter.getItem(i)).getBodyPartId() == workout.getBodyPartId()) {
                    ((Spinner) dialogView.findViewById(R.id.spinner_body_part)).setSelection(i);
                }
            }
            final AlertDialog alertDialog = dialogBuilder.create();
            dialogView.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    workout.setName(((TextInputLayout) dialogView.findViewById(R.id.til_exercise_name)).getEditText().getText().toString());
                    workout.setStandardNumberOfSets(Integer.parseInt(((TextInputLayout) dialogView.findViewById(R.id.til_number_of_sets)).getEditText().getText().toString()));
                    workout.setStandardNumberOfRepetitions(Integer.parseInt(((TextInputLayout) dialogView.findViewById(R.id.til_repetitions)).getEditText().getText().toString()));
                    workout.setCountRepetitionsInReverse(((CheckBox) dialogView.findViewById(R.id.checkbox_count_repetitions_in_reverse)).isChecked());
                    workout.setDuration(Integer.parseInt(((TextInputLayout) dialogView.findViewById(R.id.til_duration)).getEditText().getText().toString()));
                    workout.setCountDurationInReverse(((CheckBox) dialogView.findViewById(R.id.checkbox_count_duration_in_reverse)).isChecked());
                    if (!((TextInputLayout) dialogView.findViewById(R.id.til_time_to_get_in_position)).getEditText().getText().toString().isEmpty())
                        workout.setTimeToGetInPosition(Integer.parseInt(((TextInputLayout) dialogView.findViewById(R.id.til_time_to_get_in_position)).getEditText().getText().toString()));
                    if (!((TextInputLayout) dialogView.findViewById(R.id.til_rest_time_after_exercise)).getEditText().getText().toString().isEmpty())
                        workout.setRestTimeAfterExercise(Integer.parseInt(((TextInputLayout) dialogView.findViewById(R.id.til_rest_time_after_exercise)).getEditText().getText().toString()));
                    workout.setBodyPartId(((BodyPart) ((Spinner) dialogView.findViewById(R.id.spinner_body_part)).getSelectedItem()).getBodyPartId());
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
        }

        @Override
        public boolean onItemLongClicked(View view) {
            //Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
