package com.example.workfast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Obtener el rol del usuario
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String role = task.getResult().getString("role");

                // Redirigir según el rol
                if ("employer".equals(role)) {
                    Intent intent = new Intent(MainActivity.this, EmployerActivity.class);
                    startActivity(intent);
                } else if ("employee".equals(role)) {
                    Intent intent = new Intent(MainActivity.this, EmployeeActivity.class);
                    startActivity(intent);
                }
                finish(); // Cerrar MainActivity
            }
        });
    }
}