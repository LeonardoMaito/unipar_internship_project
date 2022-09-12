package com.leonardomaito.autocommobile.controllers;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.leonardomaito.autocommobile.models.Client;

import autocommobile.R;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

public class ClientController {

    private String clientName;
    private String clientCpf;
    private String clientAddress;
    private String clientTelephone;

     public Client returnNewClient(EditText etClientName,
                                   EditText etClientCpf,
                                   EditText etClientAddress,
                                   EditText etClientTelephone,
                                   Button nextOs){

             clientName = etClientName.getText().toString().isEmpty() ? "Teste" : etClientName.getText().toString() ;
             clientCpf = etClientCpf.getText().toString();
             clientAddress = etClientAddress.getText().toString();
             clientTelephone = etClientTelephone.getText().toString();

         Client newClient = new Client.ClientBuilder(clientName, clientCpf)
                 .address(clientAddress)
                 .telephone(clientTelephone)
                 .build();

         return newClient;

     }

     public void etClientNameWatcher(EditText etClientName, Button nextOs){

         etClientName.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!verifyClientName(etClientName, nextOs)){
                    nextOs.setBackgroundColor(nextOs.getContext().getResources().getColor(R.color.negative_button));
                    etClientName.setError("O nome precisa conter cinco caracteres no mínimo");

                }
                else{
                    nextOs.setBackgroundColor(nextOs.getContext().getResources().getColor(R.color.autocom_blue));

                }
             }

             @Override
             public void afterTextChanged(Editable editable) {

             }
         });

     }

    public void etClientCpfWatcher(EditText etClientCpf, Button nextOs){

        etClientCpf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!verifyClientName(etClientCpf, nextOs)){
                    etClientCpf.setError("O CPF precisa ser preenchido");
                    nextOs.setBackgroundColor(nextOs.getContext().getResources().getColor(R.color.negative_button));


                }
                else{
                    nextOs.setBackgroundColor(nextOs.getContext().getResources().getColor(R.color.autocom_blue));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public boolean verifyClientCpf(EditText etClientCpf, Button nextOs){

        CPFValidator cpfValidator = new CPFValidator();

         try{
             cpfValidator.assertValid(etClientCpf.getText().toString());
             return true;
         } catch (InvalidStateException e){
             etClientCpfWatcher(etClientCpf, nextOs);
             return false;
         }
    }

     public boolean verifyClientName(EditText etClientName, Button nextOs){

         int yourDesiredLength = 5;
         if (etClientName.getText().length() < yourDesiredLength) {
             etClientNameWatcher(etClientName, nextOs);
             return false;
         }
         else{
             return true;
         }
     }


}
