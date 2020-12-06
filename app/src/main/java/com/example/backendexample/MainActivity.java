package com.example.backendexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        EditText tEmail = findViewById(R.id.email);
        EditText tPassword = findViewById(R.id.password);
        Button btnSignUp = findViewById(R.id.sign_up);
        Button btnLogin = findViewById(R.id.login);
        btnSignUp.setOnClickListener(v -> {
            String email = tEmail.getText().toString();
            String password = tPassword.getText().toString();
            if(email.length() > 0 && password.length() > 0){
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startWithPermissions();
                    } else {
                        Toast.makeText(MainActivity.this, "Error al registrarse", Toast.LENGTH_LONG).show();
                    }
                });
            }else Toast.makeText(MainActivity.this, "Campos vacíos", Toast.LENGTH_LONG).show();
        });
        btnLogin.setOnClickListener(v -> {
            String email = tEmail.getText().toString();
            String password = tPassword.getText().toString();
            if(email.length() > 0 && password.length() > 0){
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startWithPermissions();
                    } else{
                        Toast.makeText(MainActivity.this, "Error al iniciar sesión", Toast.LENGTH_LONG).show();
                    }
                });
            }else Toast.makeText(MainActivity.this, "Campos vacíos", Toast.LENGTH_LONG).show();
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(this, UsersActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
            finish();
        }
    }
    void startWithPermissions(){
        FirebaseUser user = mAuth.getCurrentUser();
        Intent intent = new Intent(this, UsersActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}