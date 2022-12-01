package com.leonardomaito.autocommobile.controllers;


import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputFilter;
import android.text.Spanned;
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

    private AlertDialog alertDialog;

     public Client returnNewClient(EditText etClientName,
                                   MaskEditText etClientCpf,
                                   EditText etClientAddress,
                                   EditText etClientTelephone){

             clientName = etClientName.getText().toString();
             clientCpf = etClientCpf.getText().toString();
             clientAddress = etClientAddress.getText().toString();
             clientTelephone = etClientTelephone.getText().toString();

         Client newClient = new Client.ClientBuilder(clientName, clientCpf)
                 .address(clientAddress)
                 .telephone(clientTelephone)
                 .build();

         sendDataToFirestore(newClient);

         return newClient;

     }

    private void sendDataToFirestore(Client newClient) {

        CollectionReference docRef =
                db.collection("userData")
                        .document(user.getUid())
                        .collection("Client");

        DocumentReference idRef =
                db.collection("userData")
                        .document(user.getUid())
                        .collection("reservedID")
                        .document("reservedClientId");

        idRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    idValue =  (long) document.get("id");

                }
            }
        });

        data.put("client", newClient);
        docRef.add(data).addOnSuccessListener(documentReference -> {
            String id = documentReference.getId();
            docRef.document(id).update("client.id", FieldValue.increment(idValue));
            idRef.update("id", FieldValue.increment(1));

        });
    }

    public void deleteDataFromFirestore(ClientSearchAdapter.ViewHolder holder, String documentId){

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

    public void updateDataFromFirestore(ClientSearchAdapter.ViewHolder holder, String documentId){

        AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
        alert.setCancelable(false);
        alert.setTitle("Alterar ou Visualizar Cliente");
        alert.setMessage("Você deseja alterar ou editar o cadastro do cliente?");
        alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent updateClientActivity  = new Intent(holder.itemView.getContext(), ClientActivity.class);
                updateClientActivity.putExtra("documentId", documentId);
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

                Intent updateClientActivity  = new Intent(holder.itemView.getContext(), ClientActivity.class);
                updateClientActivity.putExtra("documentId", documentId);
                holder.itemView.getContext().startActivity(updateClientActivity);
                OsRecyclerActivity.self_intent.finish();

            }
        });
        alertDialog = alert.create();
        alertDialog.show();

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
