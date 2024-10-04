package com.example.workfast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, companyEditText, skillsEditText;
    private RadioGroup roleRadioGroup;
    private RadioButton radioEmployer, radioEmployee;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar Firebase Auth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referenciar las vistas
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        companyEditText = findViewById(R.id.companyEditText);
        skillsEditText = findViewById(R.id.skillsEditText);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);
        radioEmployer = findViewById(R.id.radioEmployer);
        radioEmployee = findViewById(R.id.radioEmployee);
        registerButton = findViewById(R.id.registerButton);

        // Manejo de selección de roles
        roleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioEmployer) {
                // Si selecciona "Empleador", mostramos el campo de empresa y ocultamos habilidades
                companyEditText.setVisibility(View.VISIBLE);
                skillsEditText.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioEmployee) {
                // Si selecciona "Empleado", mostramos el campo de habilidades y ocultamos empresa
                companyEditText.setVisibility(View.GONE);
                skillsEditText.setVisibility(View.VISIBLE);
            }
        });

        // Manejo del botón de registro
        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String company = companyEditText.getText().toString();
        String skills = skillsEditText.getText().toString();
        int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || selectedRoleId == -1) {
            Toast.makeText(RegisterActivity.this, "Por favor, completa todos los campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Obtener el UID del usuario
                String userId = mAuth.getCurrentUser().getUid();

                // Crear el mapa de datos para Firestore
                Map<String, Object> user = new HashMap<>();
                user.put("name", name);
                if (selectedRoleId == R.id.radioEmployer) {
                    user.put("role", "employer");
                    user.put("company", company);
                } else if (selectedRoleId == R.id.radioEmployee) {
                    user.put("role", "employee");
                    user.put("skills", skills);
                }

                // Guardar en Firestore
                db.collection("users").document(userId).set(user).addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    // Redirigir a MainActivity
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Error al registrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(RegisterActivity.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}