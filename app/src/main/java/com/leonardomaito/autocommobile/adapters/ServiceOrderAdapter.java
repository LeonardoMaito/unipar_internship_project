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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.controllers.ServiceOrderController;
import com.leonardomaito.autocommobile.models.ServiceDocument;


import autocommobile.R;

public class ServiceOrderAdapter extends FirestoreRecyclerAdapter<ServiceDocument, ServiceOrderAdapter.ViewHolder> {

    private ServiceOrderController serviceOrderController = new ServiceOrderController();

    public ServiceOrderAdapter(@NonNull FirestoreRecyclerOptions<ServiceDocument> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ServiceDocument model) {

        holder.osIdItem.setText(String.valueOf(model.serviceOrder.getId()));
        holder.osClientItem.setText(model.serviceOrder.getClient().getName());
        holder.osDateItem.setText(model.serviceOrder.getDate());
        holder.osValueItem.setText("R$: " + model.serviceOrder.getTotalValue());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getBindingAdapterPosition();
                updateOs(position, holder);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getBindingAdapterPosition();
                deleteOs(position, holder);
                return true;
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_os_item, parent, false);
        return new ServiceOrderAdapter.ViewHolder(view);
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

    public void updateOs(Integer position, ViewHolder holder){
        String documentId = getSnapshots().getSnapshot(position).getId();
        serviceOrderController.updateDataFromFirestore(position, holder, documentId);

    }

    public void deleteOs(Integer position, ViewHolder holder) {
        String documentId = getSnapshots().getSnapshot(position).getId();
        Log.e("Erro","" + documentId);
        serviceOrderController.deleteDataFromFirestore(holder, documentId);

    }
}

