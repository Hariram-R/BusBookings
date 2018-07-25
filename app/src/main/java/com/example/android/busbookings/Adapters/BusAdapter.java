package com.example.android.busbookings.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.busbookings.Activitys.MainActivity;
import com.example.android.busbookings.Objects.BusModel;
import com.example.android.busbookings.R;
import com.example.android.busbookings.Activitys.SelectSeat;

import java.util.ArrayList;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusVH>{

    ArrayList<BusModel> buses;
    Context context;

    public BusAdapter(ArrayList<BusModel> buses, Context context) {
        this.buses = buses;
        this.context = context;
    }

    @NonNull
    @Override
    public BusVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BusVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bus,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BusVH busVH, int i) {
        busVH.populateBus(buses.get(i));
    }

    @Override
    public int getItemCount() {
        return buses.size();
    }

    public class BusVH extends RecyclerView.ViewHolder
    {
        TextView from,to,date,time,free_seat,cost;
        Button book;

        public BusVH(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.from_bus);
            to = itemView.findViewById(R.id.to_bus);
            date = itemView.findViewById(R.id.date_bus);
            time = itemView.findViewById(R.id.time_bus);
            free_seat = itemView.findViewById(R.id.seat_bus);
            book = itemView.findViewById(R.id.Bookbtn);
            cost = itemView.findViewById(R.id.cost);
        }

        void populateBus(final BusModel busModel)
        {
            from.setText(busModel.getFrom());
            to.setText(busModel.getTo());
            date.setText(busModel.getDate());
            time.setText(busModel.getTime());
            cost.setText(new StringBuffer("₹"+busModel.getCost()));
            free_seat.setText(new StringBuilder("Free Seats : "+busModel.getFreeSeats()));
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent launchSeatSelect = new Intent(context,SelectSeat.class);
                    launchSeatSelect.putExtra("busID",busModel.getID());
                    launchSeatSelect.putExtra("Email", MainActivity.emailID);
                    context.startActivity(launchSeatSelect);
                    ((Activity)context).finish();
                }
            });
        }
    }
}
