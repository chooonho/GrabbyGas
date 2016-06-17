package com.sjwoh.grabgas.order;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;
import com.sjwoh.grabgas.R;
import com.sjwoh.grabgas.customer.Customer;
import com.sjwoh.grabgas.supplier.SupplierAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectSupplierFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectSupplierFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectSupplierFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SupplierAdapter mSupplierAdapter;

    private OnFragmentInteractionListener mListener;

    public SelectSupplierFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectSupplierFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectSupplierFragment newInstance(String param1, String param2) {
        SelectSupplierFragment fragment = new SelectSupplierFragment();
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

        Location customerLocation = findLocationWithAddress();

        if(customerLocation != null) {
            LatLng customerLatLng = new LatLng(customerLocation.getLatitude(), customerLocation.getLongitude());
            mSupplierAdapter = new SupplierAdapter(FirebaseDatabase.getInstance().getReference(), customerLatLng);
        }
        else {
            mSupplierAdapter = new SupplierAdapter(FirebaseDatabase.getInstance().getReference(), null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vRoot = inflater.inflate(R.layout.fragment_select_supplier, container, false);

        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        if(appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setTitle("Select Supplier");
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setHomeButtonEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView)vRoot.findViewById(R.id.recyclerViewSuppliers);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mSupplierAdapter);

        return vRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mSupplierAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SelectBrandFragment selectBrandFragment = new SelectBrandFragment();

                Bundle bSupplier = new Bundle();
                bSupplier.putParcelable("SELECTED_SUPPLIER_OBJECT", mSupplierAdapter.getItem(position));
                selectBrandFragment.setArguments(bSupplier);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.rootLayoutMakeOrder, selectBrandFragment, "SELECT_BRAND")
                        .addToBackStack("SELECT_BRAND")
                        .commit();
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Location findLocationWithAddress() {
        double longitude = 0, latitude = 0;
        Customer customer = getActivity().getIntent().getParcelableExtra("USER_OBJECT");

        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(customer.getAddress(), 1);

            if(addresses != null && addresses.size() > 0) {
                longitude = addresses.get(0).getLongitude();
                latitude = addresses.get(0).getLatitude();

                Location addressLocation = new Location("");
                addressLocation.setLatitude(latitude);
                addressLocation.setLongitude(longitude);

                return addressLocation;
            }

            return null;
        }
        catch (IOException ex) {
            return null;
        }
    }
}
