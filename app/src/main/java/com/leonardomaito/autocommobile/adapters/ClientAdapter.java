package com.leonardomaito.autocommobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.leonardomaito.autocommobile.controllers.ClientController;
import com.leonardomaito.autocommobile.controllers.ServiceOrderController;
import com.leonardomaito.autocommobile.models.ClientDocument;

import autocommobile.R;


public class ClientAdapter extends FirestoreRecyclerAdapter<ClientDocument, ClientAdapter.ViewHolder> {

    private ClientController clientController = new ClientController();

    public ClientAdapter(@NonNull FirestoreRecyclerOptions<ClientDocument> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ClientDocument model) {
        holder.clientIdItem.setText(String.valueOf(model.client.getId()));
        holder.clientNameItem.setText(model.client.getName());
        holder.clientCpfItem.setText(model.client.getCpf());
        holder.clientPhoneItem.setText(model.client.getTelephone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getBindingAdapterPosition();
                updateClient(position, holder);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getBindingAdapterPosition();
                deleteClient(position, holder);
                return true;
            }
        });

    }

    private void deleteClient(int position, ViewHolder holder) {
        String documentId = getSnapshots().getSnapshot(position).getId();
        clientController.deleteDataFromFirestore(holder, documentId);

    }

    private void updateClient(int position, ViewHolder holder) {
        String documentId = getSnapshots().getSnapshot(position).getId();
        clientController.updateDataFromFirestore(holder, documentId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_client_item, parent, false);
        return new ClientAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView clientIdItem;
        private  TextView clientNameItem;
        private  TextView clientPhoneItem;
        private  TextView clientCpfItem;

        public ViewHolder(View itemView) {
            super(itemView);

            clientIdItem =  itemView.findViewById(R.id.clientIdItem);
            clientNameItem =   itemView.findViewById(R.id.clientNameItem);
            clientPhoneItem =  itemView.findViewById(R.id.clientPhoneItem);
            clientCpfItem =  itemView.findViewById(R.id.clientCpfItem);
        }
    }
}
