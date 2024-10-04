package com.example.workfast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EmployerActivity extends AppCompatActivity {

    private EditText jobTitleEditText, jobDescriptionEditText, jobRequirementsEditText, jobSalaryEditText;
    private Button postJobButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);

        // Inicializar Firebase Firestore y FirebaseAuth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Referenciar las vistas
        jobTitleEditText = findViewById(R.id.jobTitleEditText);
        jobDescriptionEditText = findViewById(R.id.jobDescriptionEditText);
        jobRequirementsEditText = findViewById(R.id.jobRequirementsEditText);
        jobSalaryEditText = findViewById(R.id.jobSalaryEditText);
        postJobButton = findViewById(R.id.postJobButton);

        // Manejar clic en el botón para publicar trabajo
        postJobButton.setOnClickListener(v -> postJob());
    }

    private void postJob() {
        String jobTitle = jobTitleEditText.getText().toString();
        String jobDescription = jobDescriptionEditText.getText().toString();
        String jobRequirements = jobRequirementsEditText.getText().toString();
        String jobSalary = jobSalaryEditText.getText().toString();
        String employerId = mAuth.getCurrentUser().getUid();  // UID del empleador que publica el trabajo

        // Validar que los campos no estén vacíos
        if (jobTitle.isEmpty() || jobDescription.isEmpty() || jobRequirements.isEmpty() || jobSalary.isEmpty()) {
            Toast.makeText(EmployerActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un mapa de los datos del trabajo
        Map<String, Object> job = new HashMap<>();
        job.put("title", jobTitle);
        job.put("description", jobDescription);
        job.put("requirements", jobRequirements);
        job.put("salary", jobSalary);
        job.put("employerId", employerId);  // Para identificar al empleador

        // Guardar el trabajo en Firestore
        db.collection("jobs").add(job).addOnSuccessListener(documentReference -> {
            Toast.makeText(EmployerActivity.this, "Trabajo publicado exitosamente", Toast.LENGTH_SHORT).show();
            // Limpiar los campos después de la publicación
            jobTitleEditText.setText("");
            jobDescriptionEditText.setText("");
            jobRequirementsEditText.setText("");
            jobSalaryEditText.setText("");
        }).addOnFailureListener(e -> {
            Toast.makeText(EmployerActivity.this, "Error al publicar trabajo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}