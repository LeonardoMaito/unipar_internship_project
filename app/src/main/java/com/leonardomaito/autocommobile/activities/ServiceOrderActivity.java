package com.leonardomaito.autocommobile.activities;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.controllers.ServiceOrderController;
import com.leonardomaito.autocommobile.controllers.UpdateOSController;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ClientOs;
import com.leonardomaito.autocommobile.models.Vehicle;
import com.santalu.maskara.widget.MaskEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import autocommobile.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ServiceOrderActivity extends AppCompatActivity {

    private Button returnMenuOs;
    private EditText etService;
    private EditText etObservation;
    private EditText etPaymentForm;
    private MaskEditText etDate;
    private EditText etValue;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate currentTime = LocalDate.now();

    private String documentId;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ServiceOrderController serviceOrderController = new ServiceOrderController();;
    private UpdateOSController updateOSController = new UpdateOSController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_os_service);

        Bundle data = getIntent().getExtras();
        ClientOs newClient = data.getParcelable("novoCliente");
        Vehicle newVehicle = data.getParcelable("novoCarro");

        documentId = data.getString("documentId");

        returnMenuOs = findViewById(R.id.btReturnMenuOs);
        returnMenuOs.setBackgroundColor(getResources().getColor(R.color.negative_button));

        etService = findViewById(R.id.etServiceInput);
        etObservation = findViewById(R.id.etObservationInput);
        etPaymentForm = findViewById(R.id.etPaymentFormInput);
        etDate = findViewById(R.id.etDateInput);
        etValue = findViewById(R.id.etValue);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        etValue.setText("0");
        etDate.setText(formatter.format(currentTime));
        setView(newClient, newVehicle);

        }


    private void setView(ClientOs newClient, Vehicle newVehicle) {

        returnMenuOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (serviceOrderController.checkAllServiceFields(etValue, etDate)) {

                    serviceOrderController.returnNewServiceOrder(etService, etObservation, etPaymentForm, newClient,
                            newVehicle, etDate, etValue);

                    Intent intent = new Intent(getApplicationContext(), OsRecyclerActivity.class);
                    startActivity(intent);
                    VehicleActivity.self_intent.finish();
                    finish();
                }
            }
        });

    }

   /* private void setViewForUpdate(String documentId, Client newClient, Vehicle newVehicle, Integer updateOption) {
        Log.e("UpdateOption"," " + updateOption);
        DocumentReference idRef =
                db.collection("userData")
                        .document(user.getUid())
                        .collection("ServiceOrder")
                        .document(documentId);

        idRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Result", "DocumentSnapshot data: " + document.getData());
                        ServiceOrder serviceDocument = document.toObject(ServiceDocument.class).serviceOrder;
                        etService.setText(serviceDocument.getService());
                        etObservation.setText(serviceDocument.getObservation());
                        etPaymentForm.setText(serviceDocument.getPaymentForm());
                        etDate.setText(serviceDocument.getDate());
                        etValue.setText(String.valueOf(serviceDocument.getTotalValue()));
                    }
                }
            }
        });
        returnMenuOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateOption ==1){
                    if (serviceOrderController.checkAllServiceFields(etValue, etDate)) {

                        updateOSController.returnNewServiceOrder(etService, etObservation, etPaymentForm, newClient,
                                newVehicle, etDate, etValue, documentId);

                        Intent intent = new Intent(getApplicationContext(), OsRecyclerActivity.class);
                        startActivity(intent);
                        finish();

                }
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), OsRecyclerActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        }*/
}