package com.example.lolmanager.model;

import java.io.IOException;
import java.util.ArrayList;

public interface Engine {
    static int generatePairing(Tournament tournament, boolean reversColors) throws IOException, InterruptedException {
        return 0;
    }

    static boolean checkPairing(Tournament tournament, ArrayList<Integer> pairing) {
        return false;
    }

    static Tournament generateRandomTournament() {
        return null;
    }
}
