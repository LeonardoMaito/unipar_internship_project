package com.leonardomaito.autocommobile.adapters;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.activities.ClientActivity;
import com.leonardomaito.autocommobile.activities.ClientMenuActivity;
import com.leonardomaito.autocommobile.activities.OsRecyclerActivity;
import com.leonardomaito.autocommobile.controllers.ClientController;
import com.leonardomaito.autocommobile.models.ClientOs;

import java.util.List;

import autocommobile.R;

public class ClientSearchAdapter extends RecyclerView.Adapter<ClientSearchAdapter.ViewHolder>{

    private List<ClientOs> clientList;
    private ClientMenuActivity clientMenuActivity;

    private ClientController clientController = new ClientController();

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference docRef =
            db.collection("userData")
                    .document(user.getUid())
                    .collection("Client");


    public ClientSearchAdapter(List<ClientOs> clientList, ClientMenuActivity clientMenuActivity) {
        this.clientList = clientList;
        this.clientMenuActivity = clientMenuActivity;
    }

    private int updateOption;

    private AlertDialog alertDialog;

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
        holder.clientNameItem.setText(currentClient.getName());
        holder.clientCpfItem.setText("CPF: "+ currentClient.getCpf());
        holder.clientPhoneItem.setText("Telefone: " + currentClient.getTelephone());

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

        AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
        alert.setCancelable(false);
        alert.setTitle("Excluir Cliente");
        alert.setMessage("Você tem certeza que quer excluir esse cliente?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                docRef.document(documentId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(holder.itemView.getContext(), "Excluido com sucesso!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.itemView.getContext(), "Erro ao tentar excluir!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alert.create();
        alertDialog.show();
    }

    private void updateClient(int position, ClientSearchAdapter.ViewHolder holder) {

        ClientOs currentItem = clientList.get(position);
        String documentId =  currentItem.getId();

        AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
        alert.setCancelable(false);
        alert.setTitle("Alterar ou Visualizar Cliente");
        alert.setMessage("Você deseja alterar ou editar o cadastro do cliente?");
        alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateOption = 2;
                Intent updateClientActivity  = new Intent(holder.itemView.getContext(), ClientActivity.class);
                updateClientActivity.putExtra("documentId", documentId);
                updateClientActivity.putExtra("updateOption", updateOption);
                holder.itemView.getContext().startActivity(updateClientActivity);
                ClientMenuActivity.self_intent.finish();

            }
        });
        alert.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                alertDialog.dismiss();

            }
        });
        alert.setNegativeButton("Visualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateOption = 1;
                Intent updateClientActivity  = new Intent(holder.itemView.getContext(), ClientActivity.class);
                updateClientActivity.putExtra("documentId", documentId);
                updateClientActivity.putExtra("updateOption", updateOption);
                holder.itemView.getContext().startActivity(updateClientActivity);
                clientMenuActivity.self_intent.finish();

            }
        });
        alertDialog = alert.create();
        alertDialog.show();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private  TextView clientNameItem;
        private  TextView clientPhoneItem;
        private  TextView clientCpfItem;

        public ViewHolder(View itemView) {
            super(itemView);

            clientNameItem =   itemView.findViewById(R.id.clientNameItem);
            clientPhoneItem =  itemView.findViewById(R.id.clientPhoneItem);
            clientCpfItem =  itemView.findViewById(R.id.clientCpfItem);
        }
    }
}
