package com.dailycodebuffer.system_design.Paxos;

class Acceptor {
    private int promisedNumber = -1;  // Highest proposal number promised
    private Proposal acceptedProposal = null; // Accepted proposal

    // Prepare Phase: Accept the proposal if it has a higher number
    public synchronized Proposal prepare(int proposalNumber) {
        if (proposalNumber > promisedNumber) {
            promisedNumber = proposalNumber;
            return acceptedProposal;
        }
        return null;
    }

    // Accept Phase: Accept the proposal if it meets the criteria
    public synchronized boolean accept(Proposal proposal) {
        if (proposal.number >= promisedNumber) {
            acceptedProposal = proposal;
            return true;
        }
        return false;
    }

    public Proposal getAcceptedProposal() {
        return acceptedProposal;
    }
}

