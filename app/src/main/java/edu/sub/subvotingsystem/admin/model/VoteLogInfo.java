package edu.sub.subvotingsystem.admin.model;

import java.util.List;

public class VoteLogInfo {
    private String candidateName;
    private long voteCount;
    private List<String> voters;

    public VoteLogInfo(String candidateName, long voteCount, List<String> voters) {
        this.candidateName = candidateName;
        this.voteCount = voteCount;
        this.voters = voters;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public List<String> getVoters() {
        return voters;
    }
}

