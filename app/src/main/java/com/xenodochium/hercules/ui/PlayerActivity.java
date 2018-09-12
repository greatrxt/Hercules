package com.xenodochium.hercules.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.woxthebox.draglistview.DragListView;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.DragAndDropRoutineEntryItemAdapter;
import com.xenodochium.hercules.model.RoutineEntry;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    private DragListView mDragListView;

    private void initializeViews() {
        mDragListView = findViewById(R.id.drag_list_view);

        mDragListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ArrayList<RoutineEntry> routineEntryList = (ArrayList<RoutineEntry>) getIntent().getExtras().getSerializable("workoutList");

        DragAndDropRoutineEntryItemAdapter listAdapter = new DragAndDropRoutineEntryItemAdapter(PlayerActivity.this, routineEntryList, R.layout.drag_view_list_item, R.id.grab_handle, true);
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().hide();
        initializeViews();
    }
}
