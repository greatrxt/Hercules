package com.xenodochium.hercules.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.woxthebox.draglistview.DragListView;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.DragAndDropRoutineItemAdapter;
import com.xenodochium.hercules.model.Workout;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    private DragListView mDragListView;

    private void initializeViews() {
        mDragListView = findViewById(R.id.drag_list_view);
        mDragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
                Toast.makeText(getApplicationContext(), "Start - position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    Toast.makeText(getApplicationContext(), "End - position: " + toPosition, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDragListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ArrayList<Workout> mItemArray = new ArrayList<>();

        DragAndDropRoutineItemAdapter listAdapter = new DragAndDropRoutineItemAdapter(getApplicationContext(), mItemArray, R.layout.drag_view_list_item, R.id.image, true);
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initializeViews();
    }
}
