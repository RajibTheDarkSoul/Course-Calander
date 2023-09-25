package com.example.coursecalander;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class viewDateInfo extends DialogFragment {
    private DialogInterface.OnDismissListener onDismissListener;
    Switch aSwitch;
    TextView day,month,year;
    ImageView imageView;
    LinearLayout llayout;
    ListView listView;

    ArrayList<String> months = new ArrayList<>();
    ArrayList<String>fcourses=new ArrayList<>();
    ArrayList<Integer>fattended=new ArrayList<>();
    ArrayList<Integer>fTotalClass=new ArrayList<>();




    public viewDateInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_view_date_info,container,false);

        // Add the months of the year to the ArrayList
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");

        aSwitch=view.findViewById(R.id.switchClassPlaced);
        day=view.findViewById(R.id.textViewDay);
        month=view.findViewById(R.id.textViewMonth);
        year=view.findViewById(R.id.textViewYear);
        imageView=view.findViewById(R.id.imageViewCancel);
        llayout=view.findViewById(R.id.layoutAsk);
        listView=view.findViewById(R.id.listViewInfo);



        Bundle args = getArguments();
        if (args != null) {
            ArrayList<String> arrayList1 = (ArrayList<String>) args.getSerializable("courses");
            ArrayList<Integer> arrayList2 = (ArrayList<Integer>) args.getSerializable("attended");
            ArrayList<Integer> arrayList3 = (ArrayList<Integer>) args.getSerializable("TotalClass");
            if (arrayList1 != null) {
                // Now you have access to the ArrayList
                fcourses=arrayList1;
            }
            if (arrayList2 != null) {
                // Now you have access to the ArrayList
                fattended=arrayList2;
            }
            if (arrayList3 != null) {
                // Now you have access to the ArrayList
                fTotalClass=arrayList3;
            }

        }
        CustomAdapter customAdapter = new CustomAdapter(getContext(), fcourses);
        listView.setAdapter(customAdapter);
        Log.d("ArrayListItems", TextUtils.join(", ", fcourses));


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    listView.setVisibility(View.VISIBLE);
                    llayout.setVisibility(View.VISIBLE);
                    Log.d("listview VISIbility","Visibility is on");
                }
                else{
                    listView.setVisibility(View.INVISIBLE);
                    llayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).backFromFragment();
                dismiss();
            }
        });

        // Inflate the layout for this fragment
        return  view;
       // return inflater.inflate(R.layout.fragment_view_date_info, container, false);
    }





    //Creating custom adapter

    public class CustomAdapter extends BaseAdapter {
        private Context context;
        private List<String> items;
        private List<Boolean> selectionList;

        public CustomAdapter(Context context, List<String> items) {
            this.context = context;
            this.items = items;
            this.selectionList = new ArrayList<>(Collections.nCopies(items.size(), false));
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.custom_listview_1, parent, false);
            }

            TextView textView = convertView.findViewById(R.id.textView);
            Switch switchSelection = convertView.findViewById(R.id.switchSelection);
            final CheckBox checkBox = convertView.findViewById(R.id.checkBox);

            textView.setText(items.get(position));
            checkBox.setChecked(selectionList.get(position));

            // Enable/disable the checkbox based on the switch state
            checkBox.setEnabled(switchSelection.isChecked());

            switchSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Enable/disable the checkbox based on the switch state
                    checkBox.setEnabled(isChecked);
                    if (isChecked){
                    fTotalClass.set(position, fTotalClass.get(position) + 1);}
                    else{
                        fTotalClass.set(position, fTotalClass.get(position) - 1);
                    }
                    updateArrayList(fattended,fTotalClass);
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectionList.set(position, isChecked);
                    if (isChecked){
                        fattended.set(position, fattended.get(position) + 1);

                    }
                    else
                    {
                        fattended.set(position, fattended.get(position) - 1);
                    }
                    updateArrayList(fattended,fTotalClass);
                    Log.d("AttendItem", TextUtils.join(", ", fattended));
                }

            });

            return convertView;
        }

        // Provide a method to get the selection state
        public List<Boolean> getSelectionList() {
            return selectionList;
        }
    }

    public interface OnArrayListUpdatedListener {
        void onArrayListUpdated(ArrayList<Integer> updatedAttended,ArrayList<Integer> updatedTotalClass);
    }

    private OnArrayListUpdatedListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnArrayListUpdatedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArrayListUpdatedListener");
        }
    }

    // Use this method to update the ArrayList
    private void updateArrayList(ArrayList<Integer> updatedAttended,ArrayList<Integer> updatedTotalClass) {
        if (listener != null) {
            listener.onArrayListUpdated(updatedAttended,updatedTotalClass);

        }
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener!=null){
            ((MainActivity)getActivity()).backFromFragment();
        }
    }
}