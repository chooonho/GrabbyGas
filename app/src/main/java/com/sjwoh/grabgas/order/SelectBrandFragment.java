package com.sjwoh.grabgas.order;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjwoh.grabgas.R;
import com.sjwoh.grabgas.supplier.Supplier;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectBrandFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectBrandFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectBrandFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private BrandAdapter mBrandAdapter;

    public SelectBrandFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectBrandFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectBrandFragment newInstance(String param1, String param2) {
        SelectBrandFragment fragment = new SelectBrandFragment();
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

        Supplier supplier = getArguments().getParcelable("SELECTED_SUPPLIER_OBJECT");
        mBrandAdapter = new BrandAdapter(supplier);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vRoot = inflater.inflate(R.layout.fragment_select_brand, container, false);

        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        if(appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setTitle("Select Brand");
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setHomeButtonEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView)vRoot.findViewById(R.id.recyclerViewBrands);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(glm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mBrandAdapter);

        return vRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mBrandAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ConfirmOrderFragment confirmOrderFragment = new ConfirmOrderFragment();

                Bundle bOrder = new Bundle();
                bOrder.putParcelable("SELECTED_SUPPLIER_OBJECT", getArguments().getParcelable("SELECTED_SUPPLIER_OBJECT"));
                bOrder.putParcelable("SELECTED_GAS_OBJECT", mBrandAdapter.getItem(position));
                confirmOrderFragment.setArguments(bOrder);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.rootLayoutMakeOrder, confirmOrderFragment, "CONFIRM_ORDER")
                        .addToBackStack("CONFIRM_ORDER")
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
