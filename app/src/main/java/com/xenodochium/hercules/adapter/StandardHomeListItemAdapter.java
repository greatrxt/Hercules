package com.xenodochium.hercules.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xenodochium.hercules.R;

import java.util.List;

public class StandardHomeListItemAdapter<T> extends BaseAdapter {

    Fragment fragment;
    List<T> objects;

    public StandardHomeListItemAdapter(Fragment fragment, @NonNull List objects) {
        this.fragment = fragment;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.standard_list_item, null);
            ((TextView) view.findViewById(R.id.text_view_list_item_label)).setText(objects.get(position).toString());

            (view.findViewById(R.id.text_view_list_item_label)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OnItemClickListener) fragment).onItemClick(getItem(position));
                }
            });

            (view.findViewById(R.id.image_button_play)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OnItemClickListener) fragment).onItemPlay(getItem(position));
                }
            });

            (view.findViewById(R.id.image_button_delete)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OnItemClickListener) fragment).onItemDelete(getItem(position));
                }
            });
        }
        return view;
    }

    public interface OnItemClickListener {
        void onItemClick(Object selectedItem);

        void onItemPlay(Object selectedItem);

        void onItemDelete(Object selectedItem);
    }
}
