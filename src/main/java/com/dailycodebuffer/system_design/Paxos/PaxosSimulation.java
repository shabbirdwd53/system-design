package com.dailycodebuffer.system_design.Paxos;

import java.util.Arrays;
import java.util.List;

public class PaxosSimulation {
    public static void main(String[] args) {
        // Creating 5 acceptors (nodes)
        List<Acceptor> acceptors = Arrays.asList(new Acceptor(), new Acceptor(), new Acceptor(), new Acceptor(), new Acceptor());

        // Proposers competing for consensus
        Proposer proposer1 = new Proposer("Value-1", acceptors);
        proposer1.propose();

        Proposer proposer2 = new Proposer("Value-2", acceptors);
        proposer2.propose();
    }
}

