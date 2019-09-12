package com.ciprian.eventcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final List<Event> events;


    public EventsAdapter(Context context, List events) {
        super(context, R.layout.events_layout, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View eventsView = inflater.inflate(R.layout.events_layout, parent, false);

        TextView tvShowEvent = eventsView.findViewById(R.id.tvShowEvent);
        TextView tvHideObjectId = eventsView.findViewById(R.id.tvHideObjectId);
        tvHideObjectId.setVisibility(View.GONE);

        tvShowEvent.setText(events.get(position).getNameEvent());

        return eventsView;
    }

}
