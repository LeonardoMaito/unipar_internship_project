package com.leonardomaito.autocommobile.controllers;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;



import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.activities.ClientActivity;
import com.leonardomaito.autocommobile.activities.OsRecyclerActivity;
import com.leonardomaito.autocommobile.adapters.ServiceSearchAdapter;
import com.leonardomaito.autocommobile.models.ClientOs;
import com.leonardomaito.autocommobile.models.ServiceOrder;
import com.leonardomaito.autocommobile.models.Vehicle;
import com.santalu.maskara.widget.MaskEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ServiceOrderController {

    private String osService;
    private String osObservation;
    private String osPaymentForm;
    private double osValue;
    private Map<String, Object> data = new HashMap<>();


    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference docRef =
            db.collection("userData")
                    .document(user.getUid())
                    .collection("ServiceOrder");

    private AlertDialog alertDialog;


    public void returnNewServiceOrder(EditText etService, EditText etObservation, EditText etPaymentForm
    , ClientOs newClient, Vehicle newVehicle, MaskEditText etDate, EditText etValue){

        osService = etService.getText().toString();
        osObservation = etObservation.getText().toString();
        osPaymentForm = etPaymentForm.getText().toString();
        osValue = Double.parseDouble(etValue.getText().toString());

        ServiceOrder newServiceOrder = new ServiceOrder.ServiceOrderBuilder(newClient,
                newVehicle,osService,osPaymentForm, osValue, String.valueOf(etDate.getText()))
                .observation(osObservation)
                .build();

        sendDataToFirestore(newServiceOrder);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkAllServiceFields(EditText osValue, MaskEditText date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dates = date.getText().toString();

        if(Double.parseDouble(osValue.getText().toString()) <= 0 ){
            osValue.setError("O valor precisa ser acima de 0");
            osValue.requestFocus();
            return false;
        }

        try
        {
            LocalDate currentTime = LocalDate.parse(dates,formatter);

        }
        catch (Exception error)
        {
            date.setError("Data inválida");
            return false;
        }

        return true;
    }

    public void sendDataToFirestore(ServiceOrder serviceOrder){

       CollectionReference docRef =
                db.collection("userData")
                        .document(user.getUid())
                        .collection("ServiceOrder");

        data.put("serviceOrder", serviceOrder);
        docRef.add(data).addOnSuccessListener(documentReference -> {
            String id = documentReference.getId();
            docRef.document(id).update("serviceOrder.id", id);


        });
    }

    public void deleteDataFromFirestore(ServiceSearchAdapter.ViewHolder holder, String documentId){

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

    public void updateDataFromFirestore(Integer position, ServiceSearchAdapter.ViewHolder holder, String documentId){

        AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
        alert.setCancelable(false);
        alert.setTitle("Alterar ou Visualizar O.S");
        alert.setMessage("Você deseja alterar ou editar a O.S?");
        alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent updateClientActivity  = new Intent(holder.itemView.getContext(), ClientActivity.class);
                updateClientActivity.putExtra("documentId", documentId);
                holder.itemView.getContext().startActivity(updateClientActivity);
                OsRecyclerActivity.self_intent.finish();

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

                Intent updateClientActivity  = new Intent(holder.itemView.getContext(), ClientActivity.class);
                updateClientActivity.putExtra("documentId", documentId);
                holder.itemView.getContext().startActivity(updateClientActivity);
                OsRecyclerActivity.self_intent.finish();

            }
        });
        alertDialog = alert.create();
        alertDialog.show();

    }
}
