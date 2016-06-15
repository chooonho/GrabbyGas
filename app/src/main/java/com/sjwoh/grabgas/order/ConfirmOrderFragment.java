package com.sjwoh.grabgas.order;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sjwoh.grabgas.R;
import com.sjwoh.grabgas.logins.Customer;
import com.sjwoh.grabgas.logins.Supplier;

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

    private TextView textViewCustomerName, textViewCustomerUsername,
                        textViewSupplierName, textViewSupplierUsername, textViewBrandName,
                        textViewSinglePrice, textViewTotalPrice;
    private Spinner spinnerQuantity;
    private DatePicker datePickerDeliveryDate;
    private TimePicker timePickerDeliveryTime;
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
        // Inflate the layout for this fragment
        View vRoot = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        textViewCustomerName = (TextView)vRoot.findViewById(R.id.textViewCustomerName);
        textViewCustomerUsername = (TextView)vRoot.findViewById(R.id.textViewCustomerUsername);
        textViewSupplierName = (TextView)vRoot.findViewById(R.id.textViewSupplierName);
        textViewSupplierUsername = (TextView)vRoot.findViewById(R.id.textViewSupplierUsername);
        textViewBrandName = (TextView)vRoot.findViewById(R.id.textViewBrandName);
        spinnerQuantity = (Spinner)vRoot.findViewById(R.id.spinnerQuantity);
        textViewSinglePrice = (TextView)vRoot.findViewById(R.id.textViewSinglePrice);
        textViewTotalPrice = (TextView)vRoot.findViewById(R.id.textViewTotalPrice);

        datePickerDeliveryDate = (DatePicker)vRoot.findViewById(R.id.datePickerDeliveryDate);
        timePickerDeliveryTime = (TimePicker)vRoot.findViewById(R.id.timePickerDeliveryTime);

        Integer[] values = new Integer[MAX_QUANTITY];
        for(int i = 0; i < MAX_QUANTITY; i++) {
            values[i] = i + 1;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), R.layout.support_simple_spinner_dropdown_item, values);
        spinnerQuantity.setAdapter(adapter);

        return vRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initOrder();
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
        textViewSupplierName.setText(supplier.getName());
        textViewSupplierUsername.setText(supplier.getUsername());
        textViewBrandName.setText(gas.getName().toUpperCase());
        textViewSinglePrice.setText("RM " + String.format(Locale.getDefault(), "%.2f", gas.getPrice()) + " ea");
        textViewTotalPrice.setText("RM 0.00");
    }
}
