package com.example.backendexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

import static java.lang.Integer.parseInt;
import static java.util.UUID.randomUUID;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ImageView image;
    private StorageReference mStorageRef;
    private Uri uriImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Button btnAddUser = findViewById(R.id.register_user);
        Button btnLoadImage = findViewById(R.id.load_image);
        EditText nombre = findViewById(R.id.nombre);
        EditText apellidos = findViewById(R.id.apellidos);
        EditText direccion = findViewById(R.id.direccion);
        EditText edad = findViewById(R.id.edad);
        EditText telefono = findViewById(R.id.telefono);
        image = findViewById(R.id.imageView);
        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = firebaseDatabase.getReference();
        btnAddUser.setOnClickListener(v -> {
            String n = nombre.getText().toString();
            String a = apellidos.getText().toString();
            String dir = direccion.getText().toString();
            String ed = edad.getText().toString();
            String tel = telefono.getText().toString();
            if(n.length() > 0 && a.length() > 0 && dir.length() > 0 && ed.length() > 0 && tel.length() > 0){
                if(uriImg != null){
                    StorageReference ref = mStorageRef.child("images/" + randomUUID().toString());
                    ref.putFile(uriImg).addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        String urlImg = Objects.requireNonNull(uri.getResult()).toString();
                        User user = new User(n, a, dir,
                                urlImg, parseInt(ed), parseInt(tel));
                        databaseReference.child("users").child(String.valueOf(randomUUID())).setValue(user);
                        nombre.setText("");
                        apellidos.setText("");
                        direccion.setText("");
                        edad.setText("");
                        telefono.setText("");
                        Toast.makeText(RegisterActivity.this, "Usuario agregado", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_LONG).show());
                }
                else Toast.makeText(RegisterActivity.this, "Elija otra imagen", Toast.LENGTH_LONG).show();
            } else Toast.makeText(RegisterActivity.this, "Complete todos los campos", Toast.LENGTH_LONG).show();
        });
        btnLoadImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 0);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                assert data != null;
                uriImg = data.getData();
                image.setImageURI(uriImg);
            }
        }
    }
}