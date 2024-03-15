package edu.sub.subvotingsystem.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.sub.subvotingsystem.CandidateInfo;
import edu.sub.subvotingsystem.R;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {
    private List<CandidateInfo> candidateList;
    private OnItemClickListener itemClickListener;
    private int selectedPosition = -1;

    public CandidateAdapter(List<CandidateInfo> candidateList) {
        this.candidateList = candidateList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setSelectedPosition(int position) {
        if (selectedPosition != position) {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CandidateInfo candidate = candidateList.get(position);
        holder.candidateNameTextView.setText(candidate.getCandidateName());
        holder.candidateGenderTextView.setText(candidate.getCandidateGender());
        holder.getCandidateCenterTextView.setText(candidate.getCandidateCenterName());
        holder.candidatePartyTextView.setText(candidate.getCandidatePartyName());

        // Set the visual indication for the selected item
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.purple_500));
            holder.itemView.setBackgroundResource(R.drawable.ic_bg_box);
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));
            holder.itemView.setBackgroundResource(0);
        }
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView candidateNameTextView;
        TextView candidateGenderTextView;
        TextView getCandidateCenterTextView;
        TextView candidatePartyTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateNameTextView = itemView.findViewById(R.id.candidateNameTextView);
            candidateGenderTextView = itemView.findViewById(R.id.candidateGenderTextView);
            getCandidateCenterTextView = itemView.findViewById(R.id.candidateCenterTextView);
            candidatePartyTextView = itemView.findViewById(R.id.candidatePartyTextView);

            // Set click listener for the entire item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            setSelectedPosition(position); // Update selected position
                            itemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
