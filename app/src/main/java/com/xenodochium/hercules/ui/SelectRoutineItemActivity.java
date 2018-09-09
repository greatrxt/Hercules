package com.xenodochium.hercules.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.MultiSelectArrayAdapter;
import com.xenodochium.hercules.application.Hercules;
import com.xenodochium.hercules.model.RoutineItem;

import java.util.List;

public class SelectRoutineItemActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ROUTINE_ITEM_TYPE = "routineItemType", EXERCISE = "exercise", BODY_PART = "bodyPart";
    ListView listViewRoutineItems;
    Button buttonOk, buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_routine_item);
        getSupportActionBar().hide();

        initializeViews();
        populateListViewWithRoutineItems();
    }

    private void populateListViewWithRoutineItems() {
        List<RoutineItem> routineItems = Hercules.getInstance().getDaoSession().getRoutineItemDao().loadAll();
        MultiSelectArrayAdapter multiSelectArrayAdapter = new MultiSelectArrayAdapter(getApplicationContext(), routineItems);
        listViewRoutineItems.setAdapter(multiSelectArrayAdapter);
    }

    /**
     * Initialize all views
     */
    private void initializeViews() {
        listViewRoutineItems = findViewById(R.id.list_view_routine_items);
        buttonCancel = findViewById(R.id.button_cancel);
        buttonOk = findViewById(R.id.button_ok);
        buttonCancel.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
                finish();
                break;
            case R.id.button_ok:

                break;
        }
    }
}
