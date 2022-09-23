package com.leonardomaito.autocommobile.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.leonardomaito.autocommobile.models.ServiceDocument;

import autocommobile.R;


public class FirestoreAdapter extends FirestoreRecyclerAdapter<ServiceDocument, FirestoreAdapter.ViewHolder> {

    public FirestoreAdapter(@NonNull FirestoreRecyclerOptions<ServiceDocument> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ServiceDocument model) {
        holder.osIdItem.setText(String.valueOf(model.serviceOrder.getId()));
        holder.osClientItem.setText(model.serviceOrder.getClient().getName());
        holder.osDateItem.setText(model.serviceOrder.getDate());
        holder.osValueItem.setText(String.valueOf("R$: " + model.serviceOrder.getTotalValue()));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_os_item, parent, false);
        return new FirestoreAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private  TextView osIdItem;
        private  TextView osClientItem;
        private  TextView osDateItem;
        private  TextView osValueItem;

        public ViewHolder(View itemView) {
            super(itemView);

            osIdItem =  itemView.findViewById(R.id.osIDItem);
            osClientItem =   itemView.findViewById(R.id.osClientNameItem);
            osDateItem =  itemView.findViewById(R.id.osDateItem);
            osValueItem =  itemView.findViewById(R.id.osValueItem);
        }
    }
}
