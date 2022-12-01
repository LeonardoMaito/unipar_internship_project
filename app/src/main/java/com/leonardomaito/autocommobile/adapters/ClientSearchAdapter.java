package com.leonardomaito.autocommobile.adapters;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.activities.ClientMenuActivity;
import com.leonardomaito.autocommobile.controllers.ClientController;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ClientOs;

import java.util.List;

import autocommobile.R;

public class ClientSearchAdapter extends RecyclerView.Adapter<ClientSearchAdapter.ViewHolder>{

    private List<ClientOs> clientList;
    private ClientMenuActivity clientMenuActivity;

    private ClientController clientController = new ClientController();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public ClientSearchAdapter(List<ClientOs> clientList, ClientMenuActivity clientMenuActivity) {
        this.clientList = clientList;
        this.clientMenuActivity = clientMenuActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_client_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ClientOs currentClient = clientList.get(position);
        holder.clientIdItem.setText(String.valueOf(currentClient.getId()));
        holder.clientNameItem.setText(currentClient.getName());
        holder.clientCpfItem.setText(currentClient.getCpf());
        holder.clientPhoneItem.setText(currentClient.getTelephone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateClient(holder.getBindingAdapterPosition(), holder);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteClient(holder.getBindingAdapterPosition(), holder);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
      return clientList.size();
    }

    public void filterList(List<ClientOs> filteredList) {
        clientList = filteredList;
        notifyDataSetChanged();
    }

    private void deleteClient(int position, ClientSearchAdapter.ViewHolder holder) {
        ClientOs currentItem = clientList.get(position);
        String documentId =  String.valueOf(currentItem.getId());
        clientController.deleteDataFromFirestore(holder, documentId);
        clientMenuActivity.updateRecycle();

    }

    private void updateClient(int position, ClientSearchAdapter.ViewHolder holder) {
        ClientOs currentItem = clientList.get(position);
        String documentId =  currentItem.getId();
        clientController.updateDataFromFirestore(holder, documentId);
        clientMenuActivity.updateRecycle();

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
