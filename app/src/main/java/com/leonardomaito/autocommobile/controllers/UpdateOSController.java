package com.leonardomaito.autocommobile.controllers;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ServiceDocument;
import com.leonardomaito.autocommobile.models.ServiceOrder;
import com.leonardomaito.autocommobile.models.Vehicle;
import com.santalu.maskara.widget.MaskEditText;


public class UpdateOSController {

    private String osService;
    private String osObservation;
    private String osPaymentForm;
    private int id;
    private double osValue;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public void returnNewServiceOrder(EditText etService, EditText etObservation, EditText etPaymentForm
            , Client newClient, Vehicle newVehicle, MaskEditText etDate, EditText etValue, String docId){

        DocumentReference idRef =
                db.collection("userData")
                        .document(user.getUid())
                        .collection("ServiceOrder")
                        .document(docId);

        idRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ServiceOrder serviceDocument = document.toObject(ServiceDocument.class).serviceOrder;
                        id = serviceDocument.getId();
                        osService = etService.getText().toString();
                        osObservation = etObservation.getText().toString();
                        osPaymentForm = etPaymentForm.getText().toString();
                        osValue = Double.parseDouble(etValue.getText().toString());

                        ServiceOrder newServiceOrder = new ServiceOrder.ServiceOrderBuilder(newClient,
                                newVehicle, osService, osPaymentForm, osValue, String.valueOf(etDate.getText()))
                                .observation(osObservation)
                                .id(id)
                                .build();

                        sendDataToFirestore(newServiceOrder, docId);
                    }
                }
            }
        });
    }

    public void sendDataToFirestore(ServiceOrder serviceOrder, String docId){

        DocumentReference idRef =
                db.collection("userData")
                        .document(user.getUid())
                        .collection("ServiceOrder")
                        .document(docId);

        idRef.update("serviceOrder", serviceOrder);

    }
}
