package com.example.firebasechat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.firebasechat.MOdels.User;
import com.example.firebasechat.R;

import java.util.ArrayList;

public class UserListAdapter extends ArrayAdapter {
    Context context;
    ArrayList<User> arrayList;

    public UserListAdapter(Context context, ArrayList<User> arrayList) {
        super(context,0, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.user_listview_shape,parent,false);
        }
        TextView textView = view.findViewById(R.id.nameTv);

        textView.setText(arrayList.get(position).getName());

        return view;
    }
}
