package com.example.android.busbookings.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.busbookings.R;
import com.example.android.busbookings.Activitys.SearchResults;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchFragment extends Fragment {

    AutoCompleteTextView fromLocation,toLocation;
    TextView Date;

    Calendar calendar;
    int yy,mm,dd;
    Button search;

    String date_final = "";
    String from_final = "",to_final = "";

    DatePickerDialog datePickerDialog;

    //TODO: Add better city search.

    String locationsFrom[] = {"Chennai","Bangalore","Coimbatore","Madurai","Trichy","Mumbai"};
    String locationsTo[] = {"Pondicherry","Mysore","Tirunalveli","Kanyakumari","Kancheepuram","Goa"};

    String locations[] = {"Chennai","Bangalore","Coimbatore","Madurai","Trichy","Pondicherry","Mysore",
            "Tirunalveli","Kanyakumari","Kancheepuram"};
    ArrayList<String> usedLocations = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_buses,container,false);
        fromLocation = view.findViewById(R.id.FromLocation);
        toLocation = view.findViewById(R.id.ToLocation);
        Date = view.findViewById(R.id.Datepick);
        search = view.findViewById(R.id.Search_btn);


        ArrayList<String> listfrom = new ArrayList<String>();
        ArrayList<String> listto    = new ArrayList<String>();

        for(int i =0;i<6;i++)
        {
            listfrom.add(locationsFrom[i]);
            listto.add(locationsTo[i]);
        }

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "myfont.ttf");
        fromLocation.setTypeface(face);
        toLocation.setTypeface(face);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listfrom);
        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listto);

        fromLocation.setThreshold(0);
        fromLocation.setAdapter(arrayAdapter);

        toLocation.setThreshold(0);
        toLocation.setAdapter(arrayAdapter2);

        fromLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                from_final = adapterView.getItemAtPosition(i).toString();
            }
        });

        toLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                to_final = adapterView.getItemAtPosition(i).toString();
            }
        });

        calendar = Calendar.getInstance();
        yy = calendar.get(Calendar.YEAR);
        mm = calendar.get(Calendar.MONTH);
        dd = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Date.setText(new StringBuffer("Date of Journey : "+i2+"/"+(i1+1)+"/"+i));
                yy= i;
                mm = i1+1;
                dd= i2;
                date_final = dd+"/"+mm+"/"+yy;
            }
        },yy,mm,dd);


        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date_final.isEmpty() || from_final.isEmpty() || to_final.isEmpty())
                {
                    Toast.makeText(getContext(),"Field(s) empty!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent launchSearch = new Intent(getActivity(),SearchResults.class);
                    launchSearch.putExtra("from",from_final);
                    launchSearch.putExtra("to",to_final);
                    launchSearch.putExtra("date",date_final);
                    startActivity(launchSearch);

                }
            }
        });

        return view;
    }

}
