package com.das.dhrubaneel.queuemanagement.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.das.dhrubaneel.queuemanagement.R;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Cham_details;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Doc_details;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Slot;
import com.das.dhrubaneel.queuemanagement.utilities.Multiuse;

import java.util.ArrayList;

/**
 * Created by Dhruba on 08-Jan-17.
 */
public class BookApptOuterAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    public String selected_date;
    public Cham_details objChamDetails=null;

    /*************  CustomAdapter Constructor *****************/
    public BookApptOuterAdapter(Activity a, ArrayList d, Resources resLocal, String selectedDate) {


        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;
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
        public TextView chamberName;
        public ListView availableDoctors;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.bookappt_outeradapter, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.chamberName=(TextView) vi.findViewById(R.id.chember_name);
            holder.availableDoctors=(ListView) vi.findViewById(R.id.doctor_list);


            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            //hide the list

        } else {

            /***** Get each Model object from ArrayList ********/
            objChamDetails = null;
            objChamDetails = (Cham_details) data.get(position);

            /************  Set Model values in Holder elements ***********/
            holder.chamberName.setText(objChamDetails.getCham_location());
            ArrayList<Doc_details> doctorDetails=new ArrayList<Doc_details>();
            doctorDetails=objChamDetails.getDoc_details();
            BookapptInnerAdapter objBookapptInnerAdapter=new BookapptInnerAdapter(activity,doctorDetails,res,objChamDetails,selected_date);
            holder.availableDoctors.setAdapter(objBookapptInnerAdapter);
            Multiuse.setListViewHeightBasedOnChildren(holder.availableDoctors);
        }

        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("Book Appt Adapter", "=====Row button clicked=====");
    }




}
