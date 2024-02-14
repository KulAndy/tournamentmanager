package com.example.tournamentmanager.model;

import java.util.Date;

public record RemoteTournament(

        String _id,
        String name,
        Date startDate,
        Date endDate,
        String place,
        short gameTime,
        short increment,
        byte controlMove,
        byte controlAddition,
        Tournament.Type type,
        Tournament.TournamentSystem system
) {
}
