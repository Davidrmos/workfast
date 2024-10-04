package com.example.workfast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workfast.Adaptador.JobAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import com.example.workfast.Modelo.Job;

public class EmployeeActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button filterButton;
    private RecyclerView jobRecyclerView;
    private JobAdapter jobAdapter;
    private FirebaseFirestore db;
    private List<Job> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializar vistas
        searchEditText = findViewById(R.id.searchEditText);
        filterButton = findViewById(R.id.filterButton);
        jobRecyclerView = findViewById(R.id.jobRecyclerView);

        // Configurar RecyclerView
        jobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        jobAdapter = new JobAdapter(jobList);
        jobRecyclerView.setAdapter(jobAdapter);

        // Cargar todos los trabajos al iniciar
        loadJobs("");

        // Configurar botón de filtro
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString().trim();

                // Validar si el campo de búsqueda está vacío
                if (!TextUtils.isEmpty(searchText)) {
                    // Cargar trabajos filtrados
                    loadJobs(searchText);
                } else {
                    // Mostrar mensaje si el campo está vacío
                    Toast.makeText(EmployeeActivity.this, "Por favor ingrese un término de búsqueda", Toast.LENGTH_SHORT).show();
                    // Cargar todos los trabajos si no hay búsqueda
                    loadJobs("");
                }
            }
        });
    }

    // Método para cargar trabajos desde Firestore
    private void loadJobs(String searchQuery) {
        db.collection("jobs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        jobList.clear(); // Limpiar lista antes de agregar nuevos datos
                        QuerySnapshot documents = task.getResult();

                        if (documents != null && !documents.isEmpty()) {
                            for (QueryDocumentSnapshot document : documents) {
                                // Obtener los campos y verificar si son nulos
                                String title = document.getString("title");
                                String description = document.getString("description");
                                String requirements = document.getString("requirements");
                                String salary = document.getString("salary");
                                String location = document.getString("location");

                                // Si el título o la ubicación son nulos, omitir este trabajo
                                if (title == null) title = "";
                                if (location == null) location = "";

                                // Filtrar por el término de búsqueda solo si no están vacíos
                                if (TextUtils.isEmpty(searchQuery) || title.toLowerCase().contains(searchQuery.toLowerCase()) || location.toLowerCase().contains(searchQuery.toLowerCase())) {
                                    Job job = new Job(title, description, requirements, salary, location);
                                    jobList.add(job);
                                }
                            }
                            jobAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                        } else {
                            Toast.makeText(EmployeeActivity.this, "No se encontraron trabajos.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("EmployeeActivity", "Error al cargar trabajos: ", task.getException());
                        Toast.makeText(EmployeeActivity.this, "Error al cargar trabajos.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("EmployeeActivity", "Error de conexión: ", e);
                    Toast.makeText(EmployeeActivity.this, "Error de conexión: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}