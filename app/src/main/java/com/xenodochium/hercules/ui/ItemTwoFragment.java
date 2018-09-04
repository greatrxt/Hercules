/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.xenodochium.hercules.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.woxthebox.draglistview.DragListView;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.adapter.DragAndDropItemAdapter;

import java.util.ArrayList;

public class ItemTwoFragment extends Fragment {
    private DragListView mDragListView;
    private View fragmentView;

    public static ItemTwoFragment newInstance() {
        ItemTwoFragment fragment = new ItemTwoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initializeViews() {
        mDragListView = (DragListView) fragmentView.findViewById(R.id.drag_list_view);
        mDragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
                Toast.makeText(getActivity(), "Start - position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    Toast.makeText(getActivity(), "End - position: " + toPosition, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDragListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<android.support.v4.util.Pair<Long, String>> mItemArray = new ArrayList<>();
        mItemArray.add(new Pair<Long, String>((long) 1, "abcd"));
        mItemArray.add(new Pair<Long, String>((long) 2, "efgh"));
        mItemArray.add(new Pair<Long, String>((long) 3, "ijkl"));

        DragAndDropItemAdapter listAdapter = new DragAndDropItemAdapter(mItemArray, R.layout.list_item, R.id.image, true);
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_item_two, container, false);
        initializeViews();
        return fragmentView;
    }
}
