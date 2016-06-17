package com.sjwoh.grabgas.order;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sjwoh.grabgas.R;
import com.sjwoh.grabgas.customer.Customer;
import com.sjwoh.grabgas.supplier.Gas;
import com.sjwoh.grabgas.supplier.Supplier;
import com.sjwoh.grabgas.utility.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfirmOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfirmOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmOrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabaseReference;
    private TextView textViewCustomerName, textViewCustomerUsername,
                        textViewSupplierName, textViewSupplierUsername, textViewBrandName,
                        textViewSinglePrice, textViewTotalPrice, textViewAddress;
    private Spinner spinnerQuantity;
    private DatePicker datePickerDeliveryDate;
    private TimePicker timePickerDeliveryTime;
    private Button buttonCancel, buttonConfirm;
    private Integer[] values;
    private final int MAX_QUANTITY = 20;

    private OnFragmentInteractionListener mListener;

    public ConfirmOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmOrderFragment newInstance(String param1, String param2) {
        ConfirmOrderFragment fragment = new ConfirmOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Inflate the layout for this fragment
        View vRoot = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        if(appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setTitle("Confirm Order");
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setHomeButtonEnabled(true);
        }

        textViewCustomerName = (TextView)vRoot.findViewById(R.id.textViewCustomerName);
        textViewCustomerUsername = (TextView)vRoot.findViewById(R.id.textViewCustomerUsername);
        textViewAddress = (TextView)vRoot.findViewById(R.id.textViewAddress);
        textViewSupplierName = (TextView)vRoot.findViewById(R.id.textViewSupplierName);
        textViewSupplierUsername = (TextView)vRoot.findViewById(R.id.textViewSupplierUsername);
        textViewBrandName = (TextView)vRoot.findViewById(R.id.textViewBrandName);
        spinnerQuantity = (Spinner)vRoot.findViewById(R.id.spinnerQuantity);
        textViewSinglePrice = (TextView)vRoot.findViewById(R.id.textViewSinglePrice);
        textViewTotalPrice = (TextView)vRoot.findViewById(R.id.textViewTotalPrice);

        datePickerDeliveryDate = (CustomDatePicker)vRoot.findViewById(R.id.datePickerDeliveryDate);
        timePickerDeliveryTime = (TimePicker)vRoot.findViewById(R.id.timePickerDeliveryTime);

        buttonCancel = (Button)vRoot.findViewById(R.id.buttonCancel);
        buttonConfirm = (Button)vRoot.findViewById(R.id.buttonConfirm);

        values = new Integer[MAX_QUANTITY];
        for(int i = 0; i < MAX_QUANTITY; i++) {
            values[i] = i + 1;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_dropdown_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantity.setAdapter(adapter);

        return vRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initOrder();

        spinnerQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Gas gas = getArguments().getParcelable("SELECTED_GAS_OBJECT");
                int quantity = Integer.parseInt(adapterView.getItemAtPosition(position).toString());
                double totalPrice = (gas != null) ? quantity * gas.getPrice() : 0;

                textViewTotalPrice.setText("RM " + String.format(Locale.getDefault(), "%.2f", totalPrice));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                textViewTotalPrice.setText("RM 0.00");
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrder();
                Toast.makeText(getActivity(), "Order has been placed!", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initOrder() {
        Customer customer = getActivity().getIntent().getParcelableExtra("USER_OBJECT");
        Supplier supplier = getArguments().getParcelable("SELECTED_SUPPLIER_OBJECT");
        Gas gas = getArguments().getParcelable("SELECTED_GAS_OBJECT");

        textViewCustomerName.setText(customer.getName());
        textViewCustomerUsername.setText(customer.getUsername());
        textViewAddress.setText(customer.getAddress());
        textViewSupplierName.setText(supplier.getName());
        textViewSupplierUsername.setText(supplier.getUsername());
        textViewBrandName.setText(gas.getBrand().toUpperCase());
        textViewSinglePrice.setText("RM " + String.format(Locale.getDefault(), "%.2f", gas.getPrice()) + " ea");
        textViewTotalPrice.setText("RM 0.00");
    }

    private void submitOrder() {
        Gas gas = getArguments().getParcelable("SELECTED_GAS_OBJECT");

        Calendar deliveryCalendar = Calendar.getInstance();
        String orderDateText = new SimpleDateFormat("EEE, MMM dd, yyyy").format(new Date());
        String orderTimeText = new SimpleDateFormat("hh:MM a, z").format(new Date());
        int deliveryHour;
        int deliveryMinute;

        if(Build.VERSION.SDK_INT >= 23) {
            deliveryHour = timePickerDeliveryTime.getHour();
            deliveryMinute = timePickerDeliveryTime.getMinute();
        }
        else {
            deliveryHour = timePickerDeliveryTime.getCurrentHour();
            deliveryMinute = timePickerDeliveryTime.getCurrentMinute();
        }
        deliveryCalendar.set(datePickerDeliveryDate.getYear(), datePickerDeliveryDate.getMonth(),
                datePickerDeliveryDate.getDayOfMonth(), deliveryHour, deliveryMinute);

        Order order = new Order();

        order.setOrderedBy(textViewCustomerUsername.getText().toString());
        order.setSuppliedBy(textViewSupplierUsername.getText().toString());
        order.setAddress(textViewAddress.getText().toString());
        order.setOrderDateText(orderDateText);
        order.setOrderTimeText(orderTimeText);
        order.setDeliveryDateText(new SimpleDateFormat("EEE, MMM dd, yyyy").format(deliveryCalendar.getTime()));
        order.setDeliveryTimeText(new SimpleDateFormat("hh:MM a, z").format(deliveryCalendar.getTime()));
        order.setQuantity(Integer.parseInt(spinnerQuantity.getSelectedItem().toString()));
        order.setStatus(Order.ORDER_PENDING);
        order.setGas(gas);

        String orderRef = mDatabaseReference.child("order").push().getKey();

        mDatabaseReference.child("order").child(orderRef).child("address").setValue(order.getAddress());
        mDatabaseReference.child("order").child(orderRef).child("gasOrdered").child("brand").setValue(order.getGas().getBrand());
        mDatabaseReference.child("order").child(orderRef).child("gasOrdered").child("price").setValue(order.getGas().getPrice());
        mDatabaseReference.child("order").child(orderRef).child("gasOrdered").child("quantity").setValue(order.getQuantity());
        mDatabaseReference.child("order").child(orderRef).child("orderDate").setValue(order.getOrderDateText());
        mDatabaseReference.child("order").child(orderRef).child("orderTime").setValue(order.getOrderTimeText());
        mDatabaseReference.child("order").child(orderRef).child("orderedBy").setValue(order.getOrderedBy());
        mDatabaseReference.child("order").child(orderRef).child("deliveryDate").setValue(order.getDeliveryDateText());
        mDatabaseReference.child("order").child(orderRef).child("deliveryTime").setValue(order.getDeliveryTimeText());
        mDatabaseReference.child("order").child(orderRef).child("status").setValue(order.getStatus());
        mDatabaseReference.child("order").child(orderRef).child("suppliedBy").setValue(order.getSuppliedBy());

        mDatabaseReference.child("supplier").child(order.getSuppliedBy()).child("order").child(orderRef).setValue(order.getStatus());
        mDatabaseReference.child("customer").child(order.getOrderedBy()).child("order").child(orderRef).setValue(order.getStatus());
    }
}
