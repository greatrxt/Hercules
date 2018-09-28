
package com.xenodochium.hercules.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.StandardHomeListItemAdapter;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.BodyPart;
import com.xenodochium.hercules.model.RoutineEntry;

import java.io.Serializable;
import java.util.List;

public class ItemThreeFragment extends Fragment implements View.OnClickListener, StandardHomeListItemAdapter.OnItemClickListener {
    ListView listViewBodyPart;
    private FirebaseAnalytics mFirebaseAnalytics;
    private View fragmentView;
    private FloatingActionButton buttonAddBodyPart;

    public static ItemThreeFragment newInstance() {
        ItemThreeFragment fragment = new ItemThreeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initializeViews() {
        listViewBodyPart = fragmentView.findViewById(R.id.list_view_body_part);
        buttonAddBodyPart = fragmentView.findViewById(R.id.button_add_body_part);
        buttonAddBodyPart.setOnClickListener(this);
        populateBodyPartListView();
    }

    /**
     * Populate with stored body parts
     */
    private void populateBodyPartListView() {
        List<BodyPart> bodyPartList = Hercules.getInstance().getDaoSession().getBodyPartDao().loadAll();
        StandardHomeListItemAdapter<BodyPart> dataAdapter = new StandardHomeListItemAdapter<>(this, bodyPartList);
        listViewBodyPart.setAdapter(dataAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_item_three, container, false);
        initializeViews();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        return fragmentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_body_part:
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
                View mView = layoutInflaterAndroid.inflate(R.layout.alert_box_add_body_part, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
                alertDialogBuilderUserInput.setView(mView);

                alertDialogBuilderUserInput.setCancelable(false);
                final AlertDialog alertDialogAddExercise = alertDialogBuilderUserInput.create();
                Button buttonAlertCancel = mView.findViewById(R.id.button_alert_cancel);
                Button buttonAlertOk = mView.findViewById(R.id.button_alert_ok);
                final EditText editTextBodyPart = mView.findViewById(R.id.edit_text_body_part);
                buttonAlertOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editTextBodyPart.getText().toString().isEmpty()) {
                            editTextBodyPart.setError(getResources().getString(R.string.cannot_be_empty));
                        } else {
                            BodyPart bodyPart = new BodyPart();
                            Bundle bundle = new Bundle();   //for firebase analytics
                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, bodyPart.getClass().getSimpleName());

                            bodyPart.setName(editTextBodyPart.getText().toString().trim());
                            bundle.putString("body_part_name", editTextBodyPart.getText().toString().trim());
                            mFirebaseAnalytics.logEvent("save_body_part", bundle);

                            Hercules.getInstance().getDaoSession().getBodyPartDao().insertOrReplace(bodyPart);
                            populateBodyPartListView();
                            alertDialogAddExercise.dismiss();
                        }
                    }
                });
                buttonAlertCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogAddExercise.dismiss();
                    }
                });


                alertDialogAddExercise.show();
                break;
        }
    }

    @Override
    public void onItemClick(final Object selectedItem) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View mView = layoutInflaterAndroid.inflate(R.layout.alert_box_add_body_part, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);

        ((TextView) mView.findViewById(R.id.alert_text_header_body_part)).setText(getString(R.string.edit_body_part_name));
        alertDialogBuilderUserInput.setCancelable(false);
        final AlertDialog alertDialogAddExercise = alertDialogBuilderUserInput.create();
        Button buttonAlertCancel = mView.findViewById(R.id.button_alert_cancel);
        Button buttonAlertOk = mView.findViewById(R.id.button_alert_ok);
        final EditText editTextBodyPart = mView.findViewById(R.id.edit_text_body_part);
        editTextBodyPart.setText(((BodyPart) selectedItem).getName());
        buttonAlertOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextBodyPart.getText().toString().isEmpty()) {
                    editTextBodyPart.setError(getResources().getString(R.string.cannot_be_empty));
                } else {
                    BodyPart bodyPart = new BodyPart();
                    bodyPart.setBodyPartId(((BodyPart) selectedItem).getBodyPartId());
                    bodyPart.setName(editTextBodyPart.getText().toString().trim());
                    Hercules.getInstance().getDaoSession().getBodyPartDao().insertOrReplace(bodyPart);
                    populateBodyPartListView();
                    alertDialogAddExercise.dismiss();
                }
            }
        });
        buttonAlertCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAddExercise.dismiss();
            }
        });


        alertDialogAddExercise.show();
    }

    @Override
    public void onItemPlay(Object selectedItem) {
        BodyPart bodyPart = (BodyPart) selectedItem;

        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra("label", bodyPart.getName());
        intent.putExtra("workoutList", (Serializable) RoutineEntry.convertWorkoutListToRoutineEntryList((long) 0, bodyPart.getLinkedWorkouts()));
        startActivity(intent);
    }

    @Override
    public void onItemDelete(final Object selectedItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.delete) + " Body Part ?")
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_item))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Hercules.getInstance().getDaoSession().getBodyPartDao().delete((BodyPart) selectedItem);
                        populateBodyPartListView();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
