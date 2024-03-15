package edu.sub.subvotingsystem.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.sub.subvotingsystem.R;
import edu.sub.subvotingsystem.admin.model.VoteLogInfo;

public class VoteLogsAdapter extends RecyclerView.Adapter<VoteLogsAdapter.ViewHolder> {

    private List<VoteLogInfo> voteLogsList;

    public VoteLogsAdapter(List<VoteLogInfo> voteLogsList) {
        this.voteLogsList = voteLogsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vote_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VoteLogInfo voteLog = voteLogsList.get(position);
        holder.bind(voteLog);
    }

    @Override
    public int getItemCount() {
        return voteLogsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView candidateNameTextView;
        private TextView voteCountTextView;
        private TextView votersTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateNameTextView = itemView.findViewById(R.id.candidateNameTextView);
            voteCountTextView = itemView.findViewById(R.id.voteCountTextView);
            votersTextView = itemView.findViewById(R.id.votersTextView);
        }

        public void bind(VoteLogInfo voteLog) {
            candidateNameTextView.setText(voteLog.getCandidateName());
            voteCountTextView.setText("Vote Count: " + voteLog.getVoteCount());

            List<String> voters = voteLog.getVoters();
            StringBuilder votersText = new StringBuilder("Voters: ");
            for (String voter : voters) {
                votersText.append(voter).append(", ");
            }
            if (voters.size() > 0) {
                votersText.delete(votersText.length() - 2, votersText.length()); // Remove the last comma and space
            }
            votersTextView.setText(votersText.toString());
        }
    }
}

