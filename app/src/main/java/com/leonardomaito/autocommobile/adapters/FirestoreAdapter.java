package com.leonardomaito.autocommobile.adapters;




import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.activities.UpdateClientActivity;
import com.leonardomaito.autocommobile.models.ServiceDocument;


import autocommobile.R;

public class FirestoreAdapter extends FirestoreRecyclerAdapter<ServiceDocument, FirestoreAdapter.ViewHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference docRef =
            db.collection("cliente")
                    .document("clienteTeste")
                    .collection("ServiceOrder");

    private AlertDialog alertDialog;

    public FirestoreAdapter(@NonNull FirestoreRecyclerOptions<ServiceDocument> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ServiceDocument model) {
        holder.osIdItem.setText(String.valueOf(model.serviceOrder.getId()));
        holder.osClientItem.setText(model.serviceOrder.getClient().getName());
        holder.osDateItem.setText(model.serviceOrder.getDate());
        holder.osValueItem.setText(String.valueOf("R$: " + model.serviceOrder.getTotalValue()));

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

    public void updateOs(Integer position, ViewHolder holder){
        String documentId = getSnapshots().getSnapshot(position).getId();
        Intent newOsStepSecondIntent  = new Intent(holder.itemView.getContext(), UpdateClientActivity.class);
        newOsStepSecondIntent.putExtra("documentId", documentId);
        holder.itemView.getContext().startActivity(newOsStepSecondIntent);

    }

    public void deleteOs(Integer position, ViewHolder holder) {

        String documentId = getSnapshots().getSnapshot(position).getId();
        AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
        alert.setCancelable(false);
        alert.setTitle("Excluir O.S");
        alert.setMessage("Você tem certeza que quer excluir essa O.S?");
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
}

