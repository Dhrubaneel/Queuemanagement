package com.das.dhrubaneel.queuemanagement.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.das.dhrubaneel.queuemanagement.R;

/**
 * Created by Dhruba on 07-Jan-17.
 */
public class PatientLandingFragment extends Fragment {

    private static LinearLayout bkAppt;
    private static LinearLayout myBooking;
    private static FragmentActivity activity;
    private static View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.patient_landing_layout, container, false);
        activity=(FragmentActivity)getActivity();

        bkAppt=(LinearLayout)v.findViewById(R.id.book_appt);
        myBooking=(LinearLayout)v.findViewById(R.id.patient_view_booking);

        bkAppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call fragment to book an appointment
                BookAppointment objBookAppointment=new BookAppointment();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, objBookAppointment).addToBackStack("BookAppointment").commit();
            }
        });

        myBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call fragment to show my bookings
                PatientMyBookings objPatientMyBookings=new PatientMyBookings();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, objPatientMyBookings).addToBackStack("PatientMyBookings").commit();
            }
        });

        return v;

    }
}
