package com.example.lolmanager.model;

import java.io.IOException;
import java.util.ArrayList;

public interface Engine {
    ArrayList<Game> generatePairing(Tournament tournament) throws IOException, InterruptedException;

    boolean checkPairing(Tournament tournament, ArrayList<Integer> pairing);

    Tournament generateRandomTournament();
}
