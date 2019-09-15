package com.ciprian.eventcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MembersAdapter extends ArrayAdapter<Member> {

    private final Context context;
    private final List<Member> members;


    public MembersAdapter(Context context, List members) {
        super(context, R.layout.members_layout, members);


        this.context = context;
        this.members = members;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View membersView = inflater.inflate(R.layout.members_layout, parent, false);

        TextView tvShowMember = membersView.findViewById(R.id.tvShowMember);
        TextView tvHideId = membersView.findViewById(R.id.tvHideId);
        tvHideId.setVisibility(View.GONE);

        tvShowMember.setText(members.get(position).getName());
        return membersView;
    }
}
