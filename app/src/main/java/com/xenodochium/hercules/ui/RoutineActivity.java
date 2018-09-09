package com.xenodochium.hercules.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.DragAndDropItemAdapter;

import java.util.ArrayList;

public class RoutineActivity extends AppCompatActivity implements View.OnClickListener {

    private DragListView mDragListView;
    private com.github.clans.fab.FloatingActionButton buttonAddExercise, buttonAddRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        getSupportActionBar().hide();
        initializeViews();
    }


    private void initializeViews() {

        buttonAddExercise = findViewById(R.id.menu_item_add_exercise);
        buttonAddRest = findViewById(R.id.menu_item_add_rest);

        buttonAddRest.setOnClickListener(this);
        buttonAddExercise.setOnClickListener(this);

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

        DragAndDropItemAdapter listAdapter = new DragAndDropItemAdapter(mItemArray, R.layout.list_item, R.id.grab_handle, true);
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(true);

        mDragListView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
            @Override
            public void onItemSwipeStarted(ListSwipeItem item) {

            }

            @Override
            public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {

                // Swipe to delete on left
                if (swipedDirection == ListSwipeItem.SwipeDirection.RIGHT) {
                    Pair<Long, String> adapterItem = (Pair<Long, String>) item.getTag();
                    int pos = mDragListView.getAdapter().getPositionForItem(adapterItem);
                    mDragListView.getAdapter().removeItem(pos);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(RoutineActivity.this, SelectRoutineItemActivity.class);
        switch (view.getId()) {
            case R.id.menu_item_add_exercise:
                intent.putExtra(SelectRoutineItemActivity.ROUTINE_ITEM_TYPE, SelectRoutineItemActivity.EXERCISE);
                break;
            case R.id.menu_item_add_rest:
                intent.putExtra(SelectRoutineItemActivity.ROUTINE_ITEM_TYPE, SelectRoutineItemActivity.BODY_PART);
                break;
        }

        startActivity(intent);
    }
}
