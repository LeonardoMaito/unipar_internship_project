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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.activities.ClientActivity;
import com.leonardomaito.autocommobile.activities.OsRecyclerActivity;
import com.leonardomaito.autocommobile.adapters.ServiceSearchAdapter;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ClientOs;
import com.leonardomaito.autocommobile.models.ServiceOrder;
import com.leonardomaito.autocommobile.models.Vehicle;
import com.santalu.maskara.widget.MaskEditText;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
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

    private boolean result;


    public void returnNewServiceOrder(EditText etService, EditText etObservation, EditText etPaymentForm,
                                      ClientOs newClient, Vehicle newVehicle,
                                      MaskEditText etDate, EditText etValue,
                                      int updateOption, String documentId){

        osService = etService.getText().toString();
        osObservation = etObservation.getText().toString();
        osPaymentForm = etPaymentForm.getText().toString();
        osValue = Double.parseDouble(etValue.getText().toString());

        ServiceOrder newServiceOrder = new ServiceOrder.ServiceOrderBuilder(newClient,
                newVehicle,osService,osPaymentForm, osValue, String.valueOf(etDate.getText()))
                .observation(osObservation)
                .id(documentId)
                .build();

        if(updateOption == 0){
            sendDataToFirestore(newServiceOrder);
        }
        else{
            updateDataFromFirestore(documentId, newServiceOrder);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkAllServiceFields(EditText osValue, MaskEditText date){
        LocalDate currentTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dates = date.getText().toString();
        String value = osValue.getText().toString();

        if(value.isEmpty()){
            osValue.setError("O valor precisa ser acima de 0");
            osValue.requestFocus();
            return false;
        }

        if(Double.parseDouble(osValue.getText().toString()) <= 0){
            osValue.setError("O valor precisa ser acima de 0");
            osValue.requestFocus();
            return false;
        }

        try
        {
             currentTime = LocalDate.parse(dates,formatter);

        }
        catch (Exception error)
        {
            date.setError("Data inválida");
            return false;
        }

        if(currentTime.isBefore(LocalDate.now())){
            date.setError("A data precisa ser a partir de hoje");
            date.requestFocus();
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

    public void updateDataFromFirestore(String documentId, ServiceOrder serviceOrder){

        DocumentReference docRef =
                db.collection("userData")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("ServiceOrder")
                        .document(documentId);

        docRef.update("serviceOrder", serviceOrder);
    }
}
