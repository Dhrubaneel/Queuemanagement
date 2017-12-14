package com.das.dhrubaneel.queuemanagement.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.das.dhrubaneel.queuemanagement.R;
import com.das.dhrubaneel.queuemanagement.fragments.SlotBooking;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Cham_details;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Doc_details;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Slot;

import java.util.ArrayList;

/**
 * Created by Dhruba on 08-Jan-17.
 */
public class BookapptInnerAdapter extends BaseAdapter implements View.OnClickListener {
    /*********** Declare Used Variables *********/
    private FragmentActivity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    public Doc_details objDocDetails=null;
    public Cham_details objChamDetails;
    public String selected_date;

    /*************  CustomAdapter Constructor *****************/
    public BookapptInnerAdapter(Activity a, ArrayList d, Resources resLocal, Cham_details cd, String selectedDate) {


        /********** Take passed values **********/
        activity = (FragmentActivity)a;
        data=d;
        res = resLocal;
        objChamDetails=cd;
        selected_date=selectedDate;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
        public TextView doctorname;
        public TextView doctortiming;
        public Button bookappt;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;


        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.bookappt_inneradapter, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.doctorname=(TextView) vi.findViewById(R.id.doctor_name);
            holder.doctortiming=(TextView) vi.findViewById(R.id.doctor_timing);
            holder.bookappt=(Button) vi.findViewById(R.id.book_appt);


            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            //hide the list

        } else {

            /***** Get each Model object from ArrayList ********/
            objDocDetails = null;
            objDocDetails = (Doc_details) data.get(position);

            /************  Set Model values in Holder elements ***********/
            holder.doctorname.setText(objDocDetails.getUser_name());
            ArrayList<Slot> objslot=objDocDetails.getSlot();
            for(Slot s:objslot){
                holder.doctortiming.setText(s.getD_c_start_time()+"-"+s.getD_c_end_time());
            }

        }

        holder.bookappt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Doc_details dd=(Doc_details)data.get(position);
                ArrayList<Slot> objslot=dd.getSlot();

                Bundle bundle = new Bundle();
                bundle.putString("cham_id",objChamDetails.getCham_id());
                bundle.putString("cham_location",objChamDetails.getCham_location());
                bundle.putString("doctor_name",dd.getUser_name());
                bundle.putString("doctor_id",dd.getUser_id());
                for(Slot s:objslot) {
                    bundle.putString("d_c_id",s.getD_c_id() );
                    bundle.putString("cham_mac_address",s.getD_c_mac());
                }
                bundle.putString("booking_date",selected_date);
                SlotBooking objSlotBooking=new SlotBooking();
                objSlotBooking.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, objSlotBooking).addToBackStack("SlotBooking").commit();
            }
        });


        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("Book Appt Adapter", "=====Row button clicked=====");
    }


}
