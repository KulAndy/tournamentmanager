package com.example.lolmanager.model;

import java.io.IOException;
import java.util.ArrayList;

public interface Engine {
    public ArrayList<Game> generatePairing(Tournament tournament) throws IOException, InterruptedException;

    public boolean checkPairing(Tournament tournament, ArrayList<Integer> pairing);

    public Tournament generateRandomTournament();
}
