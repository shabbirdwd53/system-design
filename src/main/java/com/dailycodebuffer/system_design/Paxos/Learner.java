package com.dailycodebuffer.system_design.Paxos;

class Learner {
    public void learn(Proposal proposal) {
        System.out.println("Learned value: " + proposal.value);
    }
}

