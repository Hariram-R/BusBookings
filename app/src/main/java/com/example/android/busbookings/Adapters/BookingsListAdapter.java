package com.example.android.busbookings.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.busbookings.Activitys.MapsActivity;
import com.example.android.busbookings.Objects.BookingModel;
import com.example.android.busbookings.R;

import java.util.ArrayList;

public class BookingsListAdapter extends RecyclerView.Adapter<BookingsListAdapter.BookingVH>{

    ArrayList<BookingModel> bookingList;
    Context context;

    public BookingsListAdapter(ArrayList<BookingModel> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BookingVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_booking,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingVH bookingVH, int i) {
        bookingVH.populateBooking(bookingList.get(i));
        System.out.println(">>>>>>>>>>>>>"+bookingList.get(i));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class BookingVH extends RecyclerView.ViewHolder
    {
        TextView from,to,date,time,seat;

        Button preview;

        public BookingVH(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            seat = itemView.findViewById(R.id.seat);

            preview = itemView.findViewById(R.id.PreviewBtn);

        }

        void populateBooking(final BookingModel bookingModel)
        {
            from.setText(bookingModel.getFromDest());
            to.setText(bookingModel.getToDest());
            date.setText(bookingModel.getDateTravel());
            time.setText(bookingModel.getTime());
            seat.setText(bookingModel.getSeat());

            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openMapPreview = new Intent(context, MapsActivity.class);
                    openMapPreview.putExtra("from",bookingModel.getFromDest());
                    openMapPreview.putExtra("to",bookingModel.getToDest());
                    context.startActivity(openMapPreview);
                }
            });
        }
    }
}
