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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.activities.ClientActivity;
import com.leonardomaito.autocommobile.activities.ClientMenuActivity;
import com.leonardomaito.autocommobile.activities.OsRecyclerActivity;
import com.leonardomaito.autocommobile.activities.VehicleActivity;
import com.leonardomaito.autocommobile.controllers.ServiceOrderController;
import com.leonardomaito.autocommobile.models.ServiceOrder;

import java.util.List;

import autocommobile.R;

public class ServiceSearchAdapter extends RecyclerView.Adapter<ServiceSearchAdapter.ViewHolder>{

    private List<ServiceOrder> serviceOrderList;
    private OsRecyclerActivity osRecyclerActivity;

    private ServiceOrderController serviceOrderController = new ServiceOrderController();

    private AlertDialog alertDialog;

    private int updateOption;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference docRef =
            db.collection("userData")
                    .document(user.getUid())
                    .collection("ServiceOrder");

    public ServiceSearchAdapter(List<ServiceOrder> serviceOrderList, OsRecyclerActivity osRecyclerActivity) {
        this.serviceOrderList = serviceOrderList;
        this.osRecyclerActivity = osRecyclerActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_os_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceOrder currentOs = serviceOrderList.get(position);
        holder.osClientItem.setText(currentOs.client.getName());
        holder.osDateItem.setText("Data: " + currentOs.getDate());
        holder.osValueItem.setText("R$: " + currentOs.getTotalValue());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOs(holder.getBindingAdapterPosition(), holder);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteOs(holder.getBindingAdapterPosition(), holder);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceOrderList.size();
    }

    public void filterList(List<ServiceOrder> filteredList) {
        serviceOrderList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private  TextView osClientItem;
        private  TextView osDateItem;
        private  TextView osValueItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            osClientItem =   itemView.findViewById(R.id.osClientNameItem);
            osDateItem =  itemView.findViewById(R.id.osDateItem);
            osValueItem =  itemView.findViewById(R.id.osValueItem);

        }
    }

    public void updateOs(Integer position, ServiceSearchAdapter.ViewHolder holder){
        ServiceOrder currentItem = serviceOrderList.get(position);
        String documentId = currentItem.getId();

        AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
        alert.setCancelable(false);
        alert.setTitle("Alterar ou Visualizar O.S");
        alert.setMessage("Você deseja alterar ou visualizar a O.S?");
        alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateOption = 2;
                Intent updateOsActivity  = new Intent(holder.itemView.getContext(), VehicleActivity.class);
                updateOsActivity.putExtra("documentId", documentId);
                updateOsActivity.putExtra("updateOption", updateOption);
                holder.itemView.getContext().startActivity(updateOsActivity);
                osRecyclerActivity.self_intent.finish();

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
                Intent updateOsActivity  = new Intent(holder.itemView.getContext(), VehicleActivity.class);
                updateOsActivity.putExtra("documentId", documentId);
                updateOsActivity.putExtra("updateOption", updateOption);
                holder.itemView.getContext().startActivity(updateOsActivity);
                osRecyclerActivity.self_intent.finish();

            }
        });
        alertDialog = alert.create();
        alertDialog.show();



    }

    public void deleteOs(Integer position, ServiceSearchAdapter.ViewHolder holder) {
        ServiceOrder currentItem = serviceOrderList.get(position);
        String documentId = currentItem.getId();

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
