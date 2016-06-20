package com.sjwoh.grabgas.init;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sjwoh.grabgas.R;

/**
 * Created by choonho on 17/6/2016.
 */
public class InitAdapter extends RecyclerView.Adapter<InitViewHolder> {
    public InitAdapter() {
        fetchInit();
    }

    @Override
    public void onBindViewHolder(InitViewHolder initViewHolder, int index) {
    }

    @Override
    public InitViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_init, viewGroup, false);

        return new InitViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private void fetchInit() {
        ValueEventListener initListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        FirebaseDatabase.getInstance().getReference().addValueEventListener(initListener);
    }
}
