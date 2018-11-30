package com.xenodochium.hercules.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.DragAndDropRoutineEntryItemAdapter;
import com.xenodochium.hercules.engine.RoutineOrchestratorImpl;
import com.xenodochium.hercules.model.RoutineEntry;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class PlayListFragment extends Fragment {

    private DragListView mDragListView;
    private View fragmentView;

    public static PlayListFragment newInstance() {
        PlayListFragment playListFragment = new PlayListFragment();
        return playListFragment;
    }

    private void initializeViews() {
        fragmentView.findViewById(R.id.image_button_slider_toggle_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        mDragListView = fragmentView.findViewById(R.id.drag_list_view);

        mDragListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<RoutineEntry> routineEntryList = (ArrayList<RoutineEntry>) RoutineOrchestratorImpl.getInstance().getRoutineEntryPlayList();

        DragAndDropRoutineEntryItemAdapter listAdapter = new DragAndDropRoutineEntryItemAdapter(getActivity(), routineEntryList, R.layout.drag_view_list_item, R.id.grab_handle, true);
        RoutineOrchestratorImpl.getInstance().setDragAndDropRoutineEntryItemAdapter(listAdapter);
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);
        mDragListView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
            @Override
            public void onItemSwipeStarted(ListSwipeItem item) {

            }

            @Override
            public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
                // Swipe to delete on left
                //if (swipedDirection == ListSwipeItem.SwipeDirection.RIGHT) {
                RoutineEntry adapterItem = (RoutineEntry) item.getTag();
                int pos = mDragListView.getAdapter().getPositionForItem(adapterItem);
                mDragListView.getAdapter().removeItem(pos);
                mDragListView.getAdapter().notifyDataSetChanged();
                //}
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.sliding_fragment_layout, container, false);
        initializeViews();
        return fragmentView;
    }
}
