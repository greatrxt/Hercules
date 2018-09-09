package com.xenodochium.hercules.ui;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.woxthebox.draglistview.DragListView;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.DragAndDropItemAdapter;

import java.util.ArrayList;

public class RoutineActivity extends AppCompatActivity {

    private DragListView mDragListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        getSupportActionBar().hide();
        initializeViews();
    }


    private void initializeViews() {
        mDragListView = findViewById(R.id.drag_list_view_routine);
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

        ArrayList<Pair<Long, String>> mItemArray = new ArrayList<>();
        mItemArray.add(new Pair<Long, String>((long) 1, "abcd"));
        mItemArray.add(new Pair<Long, String>((long) 2, "efgh"));
        mItemArray.add(new Pair<Long, String>((long) 3, "ijkl"));

        DragAndDropItemAdapter listAdapter = new DragAndDropItemAdapter(mItemArray, R.layout.list_item, R.id.image_view_handle, true);
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);
    }
}
