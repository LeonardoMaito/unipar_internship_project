package com.leonardomaito.autocommobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.controllers.RegisterController;
import com.leonardomaito.autocommobile.models.User;

import autocommobile.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText edRegisterEmail, edRegisterPassword;
    private Button btnRegister;

    private FirebaseAuth auth;

    private FirebaseFirestore db;

    private RegisterController registerController = new RegisterController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edRegisterEmail = findViewById(R.id.edRegisterEmail);
        edRegisterPassword = findViewById(R.id.edRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    public void registerUser(View view) {
        if (registerController.checkAllFields(edRegisterEmail, edRegisterPassword)) {
            auth.fetchSignInMethodsForEmail(registerController.getEmail())
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                            if (isNewUser) {
                                auth.createUserWithEmailAndPassword(registerController.getEmail(), registerController.getPassword())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    User user = new User(registerController.getEmail(), registerController.getPassword());
                                                    db.collection("user")
                                                            .add(user);
                                                    FirebaseAuth.getInstance().signOut();
                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    finish();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Email ja cadastrado", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}