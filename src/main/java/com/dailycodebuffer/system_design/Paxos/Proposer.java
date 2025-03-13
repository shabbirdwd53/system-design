package com.dailycodebuffer.system_design.Paxos;

import java.util.List;

class Proposer {
    private int proposalNumber = 0;
    private String proposedValue;
    private List<Acceptor> acceptors;

    public Proposer(String value, List<Acceptor> acceptors) {
        this.proposedValue = value;
        this.acceptors = acceptors;
    }

    public void propose() {
        proposalNumber++;
        int majority = (acceptors.size() / 2) + 1;
        int acceptCount = 0;
        String acceptedValue = null;

        // Prepare Phase
        for (Acceptor acceptor : acceptors) {
            Proposal response = acceptor.prepare(proposalNumber);
            if (response != null && response.value != null) {
                acceptedValue = response.value;
            }
        }

        if (acceptedValue != null) {
            proposedValue = acceptedValue;
        }

        // Accept Phase
        Proposal proposal = new Proposal(proposalNumber, proposedValue);
        for (Acceptor acceptor : acceptors) {
            if (acceptor.accept(proposal)) {
                acceptCount++;
            }
        }

        // Learn Phase
        if (acceptCount >= majority) {
            System.out.println("Consensus Reached: " + proposedValue);
        } else {
            System.out.println("Consensus Failed, retrying...");
        }
    }
}
