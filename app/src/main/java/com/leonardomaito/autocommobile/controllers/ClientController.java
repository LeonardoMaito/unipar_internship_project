package com.leonardomaito.autocommobile.controllers;


import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.activities.ClientActivity;
import com.leonardomaito.autocommobile.activities.ClientMenuActivity;
import com.leonardomaito.autocommobile.activities.OsRecyclerActivity;
import com.leonardomaito.autocommobile.adapters.ClientSearchAdapter;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ClientDocument;
import com.santalu.maskara.widget.MaskEditText;

import java.util.HashMap;
import java.util.Map;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

public class ClientController {

    private String clientName;
    private String clientCpf;
    private String clientAddress;
    private String clientTelephone;
    private long idValue;
    private Map<String, Object> data = new HashMap<>();
    private CPFValidator cpfValidator = new CPFValidator();

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference docRef =
            db.collection("userData")
                    .document(user.getUid())
                    .collection("Client");

    private boolean result;

    private int updateOption;

     public Client returnNewClient(EditText etClientName,
                                   MaskEditText etClientCpf,
                                   EditText etClientAddress,
                                   EditText etClientTelephone, int updateOption, String documentId){

             clientName = etClientName.getText().toString();
             clientCpf = etClientCpf.getText().toString();
             clientAddress = etClientAddress.getText().toString();
             clientTelephone = etClientTelephone.getText().toString();

         Client newClient = new Client.ClientBuilder(clientName, clientCpf)
                 .address(clientAddress)
                 .telephone(clientTelephone)
                 .build();

         if(updateOption == 0){
             sendDataToFirestore(newClient);
         }
         else{
             updateDataFromFirestore(documentId, newClient);
         }



         return newClient;

     }

    private void sendDataToFirestore(Client newClient) {

        CollectionReference docRef =
                db.collection("userData")
                        .document(user.getUid())
                        .collection("Client");

        data.put("client", newClient);
        docRef.add(data).addOnSuccessListener(documentReference -> {
            String id = documentReference.getId();
            docRef.document(id).update("client.id", id);

        });
    }

    public void updateDataFromFirestore(String documentId, Client newClient){

        DocumentReference docRef =
                db.collection("userData")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("Client")
                        .document(documentId);

        docRef.update("client.name", newClient.getName());
        docRef.update("client.cpf", newClient.getCpf());
        docRef.update("client.address", newClient.getAddress());
        docRef.update("client.telephone", newClient.getTelephone());

    }

    public boolean checkAllClientFields(EditText etClientName, MaskEditText etClientCpf){

        String blockedCharacters = "#|%*!=+-/?[]{},@%¨.;";
        int minChar = 3;
        int maxChar = 25;

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence != null && blockedCharacters.contains(("" + charSequence))) {
                    etClientName.setError("Caracteres Inválidos");
                    return "";
                }
                return null;
            }
        };
        etClientName.setFilters(new InputFilter[]{filter});

        if (etClientName.getText().length() < minChar) {
            etClientName.setError("Mínimo de 3 Caracteres");
            etClientName.requestFocus();
            return false;
        }

        else if(etClientName.getText().length() > maxChar){
            etClientName.setError("Máximo de 25 Caracteres");
            etClientName.requestFocus();
            return false;
        }

        try{
            cpfValidator.assertValid(etClientCpf.getUnMasked());

        } catch (InvalidStateException e){
            etClientCpf.setError("Insira um CPF válido");
            return false;
        }
        return true;
    }
}
