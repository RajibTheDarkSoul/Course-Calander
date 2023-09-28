package com.example.coursecalander;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class viewDateInfo extends DialogFragment {
    MainActivity.Day dayObject;
    Button viewN,add;
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
        add=view.findViewById(R.id.buttonAddNote);
        viewN=view.findViewById(R.id.buttonViewNote);



        Bundle args = getArguments();
        if (args != null) {
            ArrayList<String> arrayList1 = (ArrayList<String>) args.getSerializable("courses");
            //ArrayList<Integer> arrayList2 = (ArrayList<Integer>) args.getSerializable("attended");
            //ArrayList<Integer> arrayList3 = (ArrayList<Integer>) args.getSerializable("TotalClass");

            MainActivity.Day Object = (MainActivity.Day) args.getSerializable("dayObject");
            day.setText(Integer.toString(Object.day));
            month.setText(months.get(Object.month));
            year.setText(Integer.toString(Object.year));

            if (arrayList1 != null) {
                // Now you have access to the ArrayList
                fcourses=arrayList1;
            }

            if (Object!=null){
                dayObject=Object;
            }
           // Log.d("View date Class placed:", TextUtils.join(", ", dayObject.ClassPlaced));

        }
        CustomAdapter customAdapter = new CustomAdapter(getContext(),dayObject.attended,dayObject.ClassPlaced );
        listView.setAdapter(customAdapter);
        Log.d("ArrayListItems", TextUtils.join(", ", fcourses));


        //Log.d("View date Class placed:", TextUtils.join(", ", dayObject.ClassPlaced));


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




                //updateArrayList(fattended,fTotalClass);
                Log.d("Updated class:",TextUtils.join(",",dayObject.ClassPlaced));
               // Log.d("ArrayListItems", TextUtils.join(", ", fcourses));
                ((MainActivity)getActivity()).backFromFragment(dayObject);

                dismiss();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ImageFragment imageFragment=new ImageFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Object", dayObject);
                bundle.putSerializable("courses",fcourses);

                imageFragment.setArguments(bundle);

                imageFragment.show(getChildFragmentManager(),"ImageFragment");
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
        private ArrayList<Integer>attended;
        private ArrayList<Integer>ClassPlaced;
        public CustomAdapter(Context context, ArrayList<Integer> attended, ArrayList<Integer> ClassPlaced) {
            this.context = context;
            //this.items = items;
            //this.selectionList = new ArrayList<>(Collections.nCopies(.size(), false));
            this.attended = attended;
            this.ClassPlaced = ClassPlaced;

            Log.d("this is after everything, means after view updated"," testing");
        }

        @Override
        public int getCount() {
            return fcourses.size();
        }
//
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

            textView.setText(fcourses.get(position));
            boolean curCB=false;
            if (attended.get(position)==1){
                curCB=true;
            }
            checkBox.setChecked(curCB);
            switchSelection.setChecked(ClassPlaced.get(position) == 1);

            // Enable/disable the checkbox based on the switch state
            checkBox.setEnabled(ClassPlaced.get(position) == 1);



            switchSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Log.d("Onlick",Integer.toString(view.));
                }
            });


//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    attended.set(position, isChecked ? 1 : 0);
//
//                   // Log.d("Inside custom Adapter", TextUtils.join(", ", ClassPlaced));
//                }
//            });
//
//            switchSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    Log.d("Before Inside custom Adapter", TextUtils.join(", ", ClassPlaced));
//                    ClassPlaced.set(position, isChecked ? 1 : 0);
//                    Log.d("After Inside custom Adapter", TextUtils.join(", ", ClassPlaced));
//                }
//            });

            switchSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = switchSelection.isChecked();
                    // Update your ClassPlaced array based on isChecked
                    if (isChecked) {
                        dayObject.ClassPlaced.set(position, 1);
                    } else {
                        dayObject.ClassPlaced.set(position, 0);
                    }
                    notifyDataSetChanged(); // Notify the adapter of data change
                }
            });

            // Add OnClickListener for the checkbox
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = checkBox.isChecked();
                    // Update your attended array based on isChecked
                    if (isChecked) {
                        dayObject.attended.set(position, 1);
                    } else {
                        dayObject.attended.set(position, 0);
                    }
                    notifyDataSetChanged(); // Notify the adapter of data change
                }
            });

            notifyDataSetChanged();

            return convertView;

        }

        // Provide a method to get the selection state
//        public List<Boolean> getSelectionList() {
//            return selectionList;
//        }
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
        if (dayObject != null) {
            // Ensure dayObject is not null before using it
            ((MainActivity) getActivity()).backFromFragment(dayObject);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dayObject != null) {
            // Ensure dayObject is not null before using it
            ((MainActivity) getActivity()).backFromFragment(dayObject);
        }
    }

    public  void backfromImageFragemnt(MainActivity.Day provided){
        dayObject=provided;
        Log.d("Images are saved","ar");
    }


}