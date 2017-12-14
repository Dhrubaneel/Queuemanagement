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
import com.das.dhrubaneel.queuemanagement.fragments.FinalizeBooking;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Available_Slot;

import java.util.ArrayList;

/**
 * Created by Dhruba on 14-Jan-17.
 */
public class SlotsAdapter extends BaseAdapter implements View.OnClickListener {
    /*********** Declare Used Variables *********/
    private FragmentActivity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    public Available_Slot objAvailableSlot=null;
    public String apDate,apDocId,apChId,apDocChId,apDocChmac,apDocName,chambarLocation;

    /*************  CustomAdapter Constructor *****************/
    public SlotsAdapter(Activity a, ArrayList d, Resources resLocal, String apd, String apdi, String apci, String apdci,String cma, String apdn,String cl ) {


        /********** Take passed values **********/
        activity = (FragmentActivity)a;
        data=d;
        res = resLocal;
        apDate = apd;
        apDocId = apdi;
        apChId = apci;
        apDocChId = apdci;
        apDocChmac = cma;
        apDocName = apdn;
        chambarLocation = cl;

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
        public TextView slotTime;
        public Button slotBook;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.slots_adapter, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.slotTime=(TextView) vi.findViewById(R.id.slot_time);
            holder.slotBook=(Button) vi.findViewById(R.id.book_slot);


            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            //hide the list

        } else {

            /***** Get each Model object from ArrayList ********/
            objAvailableSlot= null;
            objAvailableSlot = (Available_Slot) data.get(position);

            /************  Set Model values in Holder elements ***********/
            //show only time not date
            String[] parts=objAvailableSlot.getAp_time().split(" ");
            String lastPart=parts[parts.length-1];
            holder.slotTime.setText(lastPart);
        }

        holder.slotBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Available_Slot as=(Available_Slot) data.get(position);

                Bundle bundle = new Bundle();
                bundle.putString("apDate",apDate);
                bundle.putString("apTime",as.getAp_time());
                bundle.putString("apOrder",as.getAp_order());
                bundle.putString("apDocId",apDocId);
                bundle.putString("apDocName",apDocName);
                bundle.putString("chambarLocation",chambarLocation);
                bundle.putString("apChId",apChId);
                bundle.putString("apDocChId",apDocChId);
                bundle.putString("apDocChamMac",apDocChmac);

                FinalizeBooking objFinalazeBooking=new FinalizeBooking();
                objFinalazeBooking.setArguments(bundle);
                objFinalazeBooking.show(activity.getSupportFragmentManager(),null);
            }
        });

        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("Book Slot Adapter", "=====Row button clicked=====");
    }

}
