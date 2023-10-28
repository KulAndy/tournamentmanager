package com.example.lolmanager.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public interface Engine {
    static int generatePairing(Tournament tournamento) throws IOException, InterruptedException {
        return 0;
    }

    static boolean checkPairing(Tournament tournament, ArrayList<Integer> pairing) {
        return false;
    }

    static Tournament generateRandomTournament() {
        return null;
    }
}
