package com.leonardomaito.autocommobile.controllers;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ServiceOrder;
import com.leonardomaito.autocommobile.models.Vehicle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServiceOrderController {

    private String osService;
    private String osObservation;
    private String osPaymentForm;
    private double osValue;
    private long idValue;
    Map<String, Object> data = new HashMap<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference docRef =
            db.collection("cliente")
                    .document("clienteTeste")
                    .collection("ServiceOrder");

    private DocumentReference idRef =
            db.collection("cliente")
                    .document("clienteTeste")
                    .collection("ServiceOrder")
                    .document("reservedId");

    public void returnNewServiceOrder(EditText etService, EditText etObservation, EditText etPaymentForm
    , Client newClient, Vehicle newVehicle, EditText etDate, EditText etValue){

        osService = etService.getText().toString();
        osObservation = etObservation.getText().toString();
        osPaymentForm = etPaymentForm.getText().toString();
        osValue = Double.parseDouble(etValue.getText().toString());

        ServiceOrder newServiceOrder = new ServiceOrder.ServiceOrderBuilder(newClient,
                newVehicle,osService,osPaymentForm, 0, osValue, String.valueOf(etDate.getText()))
                .observation(osObservation)
                .build();

        sendDataToFirestore(newServiceOrder);
    }

    public void sendDataToFirestore(ServiceOrder serviceOrder){



        idRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    idValue =  (long) document.get("id");
                    Log.e("Result","ID" + idValue);

                }
            }
        });

        data.put("serviceOrder", serviceOrder);
        docRef.add(data).addOnSuccessListener(documentReference -> {
            String id = documentReference.getId();
            docRef.document(id).update("serviceOrder.id", FieldValue.increment(idValue));
            idRef.update("id", FieldValue.increment(1));
            Log.e("Result","Funcionou");

        });
    }
}
