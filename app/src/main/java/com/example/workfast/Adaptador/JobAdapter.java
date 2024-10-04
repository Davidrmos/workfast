package com.example.workfast.Adaptador;
import com.example.workfast.Modelo.Job; // Asegúrate de tener el paquete correcto
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workfast.R;

import java.util.List;


public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<Job> jobList;

    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.titleTextView.setText(job.getTitle());
        holder.descriptionTextView.setText(job.getDescription());
        holder.salaryTextView.setText("Salario: " + job.getSalary());
        holder.locationTextView.setText("Ubicación: " + job.getLocation());
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, descriptionTextView, salaryTextView, locationTextView;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.jobTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.jobDescriptionTextView);
            salaryTextView = itemView.findViewById(R.id.jobSalaryTextView);
            locationTextView = itemView.findViewById(R.id.jobLocationTextView);
        }
    }
}