
package com.xenodochium.hercules.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.BodyPart;

import java.util.List;

public class ItemThreeFragment extends Fragment implements View.OnClickListener {
    ListView listViewBodyPart;

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

    private void populateBodyPartListView() {
        List<BodyPart> bodyPartList = Hercules.getInstance().getDaoSession().getBodyPartDao().loadAll();
        ArrayAdapter<BodyPart> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, bodyPartList);
        listViewBodyPart.setAdapter(dataAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_item_three, container, false);
        initializeViews();
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
                break;
        }
    }
}
