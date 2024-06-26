package com.example.tournamentmanager.calculation;

import com.example.tournamentmanager.model.Federation;
import com.example.tournamentmanager.model.Game;
import com.example.tournamentmanager.model.Player;
import com.example.tournamentmanager.model.Title;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FIDECalculation {
    public static short FIDE_FLOOR = 1400;

    public static float getAverageRating(ArrayList<Player> players) {
        float sum = 0;
        float count = 0;
        for (Player player : players) {
            sum += player.getFideRating();
            count++;
        }
        if (count == 0.0) {
            return 0;
        } else {
            return sum / count;
        }
    }

    public static float[] getExpectedResult(int rating1, int rating2) {
        float[] expected;
        int difference = rating1 - rating2;
        if (difference < 0) {
            expected = getExpectedResult(rating2, rating1);
            return new float[]{expected[1], expected[0]};
        }
        if (difference <= 3) {
            expected = new float[]{0.5F, 0.5F};
        } else if (difference <= 10) {
            expected = new float[]{0.51F, 0.49F};
        } else if (difference <= 17) {
            expected = new float[]{0.52F, 0.48F};
        } else if (difference <= 25) {
            expected = new float[]{0.53F, 0.47F};
        } else if (difference <= 32) {
            expected = new float[]{0.54F, 0.46F};
        } else if (difference <= 39) {
            expected = new float[]{0.55F, 0.45F};
        } else if (difference <= 46) {
            expected = new float[]{0.56F, 0.44F};
        } else if (difference <= 53) {
            expected = new float[]{0.57F, 0.43F};
        } else if (difference <= 61) {
            expected = new float[]{0.58F, 0.42F};
        } else if (difference <= 68) {
            expected = new float[]{0.59F, 0.41F};
        } else if (difference <= 76) {
            expected = new float[]{0.6F, 0.4F};
        } else if (difference <= 83) {
            expected = new float[]{0.61F, 0.39F};
        } else if (difference <= 91) {
            expected = new float[]{0.62F, 0.38F};
        } else if (difference <= 98) {
            expected = new float[]{0.63F, 0.37F};
        } else if (difference <= 106) {
            expected = new float[]{0.64F, 0.36F};
        } else if (difference <= 113) {
            expected = new float[]{0.65F, 0.35F};
        } else if (difference <= 121) {
            expected = new float[]{0.66F, 0.34F};
        } else if (difference <= 129) {
            expected = new float[]{0.67F, 0.33F};
        } else if (difference <= 137) {
            expected = new float[]{0.68F, 0.32F};
        } else if (difference <= 145) {
            expected = new float[]{0.69F, 0.31F};
        } else if (difference <= 153) {
            expected = new float[]{0.7F, 0.3F};
        } else if (difference <= 162) {
            expected = new float[]{0.71F, 0.29F};
        } else if (difference <= 170) {
            expected = new float[]{0.72F, 0.28F};
        } else if (difference <= 179) {
            expected = new float[]{0.73F, 0.27F};
        } else if (difference <= 188) {
            expected = new float[]{0.74F, 0.26F};
        } else if (difference <= 197) {
            expected = new float[]{0.75F, 0.25F};
        } else if (difference <= 209) {
            expected = new float[]{0.76F, 0.24F};
        } else if (difference <= 215) {
            expected = new float[]{0.77F, 0.23F};
        } else if (difference <= 225) {
            expected = new float[]{0.78F, 0.22F};
        } else if (difference <= 235) {
            expected = new float[]{0.79F, 0.21F};
        } else if (difference <= 245) {
            expected = new float[]{0.8F, 0.2F};
        } else if (difference <= 256) {
            expected = new float[]{0.81F, 0.19F};
        } else if (difference <= 267) {
            expected = new float[]{0.82F, 0.18F};
        } else if (difference <= 278) {
            expected = new float[]{0.83F, 0.17F};
        } else if (difference <= 290) {
            expected = new float[]{0.84F, 0.16F};
        } else if (difference <= 302) {
            expected = new float[]{0.85F, 0.15F};
        } else if (difference <= 315) {
            expected = new float[]{0.86F, 0.14F};
        } else if (difference <= 328) {
            expected = new float[]{0.87F, 0.13F};
        } else if (difference <= 344) {
            expected = new float[]{0.88F, 0.12F};
        } else if (difference <= 357) {
            expected = new float[]{0.89F, 0.11F};
        } else if (difference <= 374) {
            expected = new float[]{0.9F, 0.1F};
        } else if (difference <= 391) {
            expected = new float[]{0.91F, 0.09F};
        } else if (difference <= 411) {
            expected = new float[]{0.92F, 0.08F};
        } else if (difference <= 432) {
            expected = new float[]{0.93F, 0.07F};
        } else if (difference <= 456) {
            expected = new float[]{0.94F, 0.06F};
        } else if (difference <= 484) {
            expected = new float[]{0.95F, 0.05F};
        } else if (difference <= 517) {
            expected = new float[]{0.96F, 0.04F};
        } else if (difference <= 559) {
            expected = new float[]{0.97F, 0.03F};
        } else if (difference <= 619) {
            expected = new float[]{0.98F, 0.02F};
        } else if (difference <= 735) {
            expected = new float[]{0.99F, 0.01F};
        } else {
            expected = new float[]{1.0F, 0.0F};

        }
        return expected;
    }

    public static float getRatingPerformance(ArrayList<Player> players, float points) {
        if (players.isEmpty()) {
            return 0;
        } else {
            float averageRating = getAverageRating(players);
            float percent = points / players.size();
            return averageRating + getDP(percent);
        }
    }

    public static float getInitRating(ArrayList<Player> players, float points) {
        if (players.isEmpty()) {
            return 0;
        }
        float averageRating = getAverageRating(players);
        float percent = points / players.size();
        if (percent <= 0.5) {
            return averageRating + getDP(percent);
        } else {
            int pluses = (int) Math.round((points - players.size() / 2.0) / 0.5);
            return averageRating + pluses * 20;
        }

    }

    public static int getDP(float p) {
        if (p >= 1.0) {
            return 800;
        } else if (p >= 0.99) {
            return 677;
        } else if (p >= 0.98) {
            return 589;
        } else if (p >= 0.97) {
            return 538;
        } else if (p >= 0.96) {
            return 501;
        } else if (p >= 0.95) {
            return 470;
        } else if (p >= 0.94) {
            return 444;
        } else if (p >= 0.93) {
            return 422;
        } else if (p >= 0.92) {
            return 401;
        } else if (p >= 0.91) {
            return 383;
        } else if (p >= 0.9) {
            return 366;
        } else if (p >= 0.89) {
            return 351;
        } else if (p >= 0.88) {
            return 336;
        } else if (p >= 0.87) {
            return 322;
        } else if (p >= 0.86) {
            return 309;
        } else if (p >= 0.85) {
            return 296;
        } else if (p >= 0.84) {
            return 284;
        } else if (p >= 0.83) {
            return 273;
        } else if (p >= 0.82) {
            return 262;
        } else if (p >= 0.81) {
            return 251;
        } else if (p >= 0.8) {
            return 240;
        } else if (p >= 0.79) {
            return 230;
        } else if (p >= 0.78) {
            return 220;
        } else if (p >= 0.77) {
            return 211;
        } else if (p >= 0.76) {
            return 202;
        } else if (p >= 0.75) {
            return 193;
        } else if (p >= 0.74) {
            return 184;
        } else if (p >= 0.73) {
            return 175;
        } else if (p >= 0.72) {
            return 166;
        } else if (p >= 0.71) {
            return 158;
        } else if (p >= 0.7) {
            return 149;
        } else if (p >= 0.69) {
            return 141;
        } else if (p >= 0.68) {
            return 133;
        } else if (p >= 0.67) {
            return 125;
        } else if (p >= 0.66) {
            return 117;
        } else if (p >= 0.65) {
            return 110;
        } else if (p >= 0.64) {
            return 102;
        } else if (p >= 0.63) {
            return 95;
        } else if (p >= 0.62) {
            return 87;
        } else if (p >= 0.61) {
            return 80;
        } else if (p >= 0.6) {
            return 72;
        } else if (p >= 0.59) {
            return 65;
        } else if (p >= 0.58) {
            return 57;
        } else if (p >= 0.57) {
            return 50;
        } else if (p >= 0.56) {
            return 43;
        } else if (p >= 0.55) {
            return 36;
        } else if (p >= 0.54) {
            return 29;
        } else if (p >= 0.53) {
            return 21;
        } else if (p >= 0.52) {
            return 14;
        } else if (p >= 0.51) {
            return 1;
        } else if (p >= 0.50) {
            return 0;
        } else {
            return -getDP(1.0F - p);
        }
    }

    public static Title getNorm(Player player, int minGames, boolean twoFeds) {
        if (player.getTitle() == Title.GM) {
            return null;
        }
        int played = player.getFidePlayedGamedNumber();
        ArrayList<Game> games = player.getFideRounds();
        if (games.size() < minGames || played < minGames - 1) {
            return null;
        }

        byte gms = 0;
        byte ims = 0;
        byte fms = 0;
        byte wgms = 0;
        byte wims = 0;
        byte wfms = 0;
        float points = 0;
        ArrayList<Player> opponents = new ArrayList<>();
        HashMap<Federation, Byte> feds = new HashMap<>();
        ArrayList<Integer> ratings = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            if (game.isForfeit() && i < games.size() - 1) {
                continue;
            }
            Player opponent = player.getOpponent(game);
            if (opponent.getFideId() == null || opponent.getFideId() < 0) {
                continue;
            }

            opponents.add(opponent);
            ratings.add(opponent.getFideRating());
            if (!game.isForfeit()) {
                points += player.getRoundPoints(game);
            }
            switch (opponent.getTitle()) {
                case GM -> gms++;
                case IM -> ims++;
                case FM -> fms++;
                case WGM -> wgms++;
                case WIM -> wims++;
                case WFM -> wfms++;
            }

            switch (player.getRoundResult(game)) {
                case WIN -> points++;
                case LOSE -> points += 0.5f;
            }

            Federation opponentFed = opponent.getFederation();
            if (feds.containsKey(opponentFed)) {
                byte currentValue = feds.get(opponentFed);
                feds.put(opponentFed, (byte) (currentValue + 1));
            } else {
                feds.put(opponentFed, (byte) 1);
            }

            if (twoFeds) {
                if (opponentFed == player.getFederation() && feds.get(opponentFed) > Math.round(played * (3f / 5f))) {
                    return null;
                } else if (opponentFed != player.getFederation() && feds.get(opponentFed) > Math.round(played * (2f / 3f))) {
                    return null;
                }
            }
        }

        if (opponents.size() < minGames || points < opponents.size() * 0.35f) {
            return null;
        }

        Collections.sort(ratings);
        int avg;
        Title maxTitle;
        ArrayList<Integer> ratingsTmp = new ArrayList<>(ratings);
        ratingsTmp.set(ratingsTmp.size() - 1, Integer.max(ratingsTmp.get(ratingsTmp.size() - 1), 2200));
        avg = (int) Math.round(ratingsTmp.stream().mapToDouble(Integer::doubleValue).average().orElse(0));
        if (avg >= 2380) {
            maxTitle = Title.GM;
        } else {
            ratingsTmp = new ArrayList<>(ratings);
            ratingsTmp.set(ratingsTmp.size() - 1, Integer.max(ratingsTmp.get(ratingsTmp.size() - 1), 2050));
            avg = (int) Math.round(ratingsTmp.stream().mapToDouble(Integer::doubleValue).average().orElse(0));
            if (avg >= 2230 && player.getTitle() != Title.IM) {
                maxTitle = Title.IM;
            } else {
                ratingsTmp = new ArrayList<>(ratings);
                ratingsTmp.set(ratingsTmp.size() - 1, Integer.max(ratingsTmp.get(ratingsTmp.size() - 1), 2000));
                avg = (int) Math.round(ratingsTmp.stream().mapToDouble(Integer::doubleValue).average().orElse(0));
                if (player.getSex() == Player.Sex.FEMALE && avg >= 2180 && player.getTitle() != Title.WGM) {
                    maxTitle = Title.WGM;
                } else {
                    ratingsTmp = new ArrayList<>(ratings);
                    ratingsTmp.set(ratingsTmp.size() - 1, Integer.max(ratingsTmp.get(ratingsTmp.size() - 1), 1850));
                    avg = (int) Math.round(ratingsTmp.stream().mapToDouble(Integer::doubleValue).average().orElse(0));
                    if (player.getSex() == Player.Sex.FEMALE && avg >= 2030 && player.getTitle() != Title.WIM) {
                        maxTitle = Title.WIM;
                    } else {
                        return null;
                    }
                }
            }
        }

        if (gms < played / 3.0f) {
            maxTitle = PZSzachCalculation.lowerTitle(maxTitle, Title.IM);
        }
        if ((gms + ims) < played / 3.0f) {
            if (player.getSex() == Player.Sex.MALE) {
                return null;
            }
            maxTitle = PZSzachCalculation.lowerTitle(maxTitle, Title.WGM);
        }
        if ((gms + ims + wgms) < played / 3.0f) {
            if (player.getSex() == Player.Sex.MALE) {
                return null;
            }
            maxTitle = PZSzachCalculation.lowerTitle(maxTitle, Title.WIM);
        }
        if ((gms + ims + wgms + wims) < played / 3.0f) {
            return null;
        }

        if ((gms + ims + fms + wgms + wims + wfms) < played / 2.0f) {
            return null;
        }

        if (twoFeds) {
            int count = 0;

            for (Map.Entry<Federation, Byte> entry : feds.entrySet()) {
                if (!entry.getKey().equals(player.getFederation()) && !entry.getKey().equals(Federation.FIDE)) {
                    count++;
                }
            }

            if (count < 2) {
                return null;
            }
        }


        float delta = getDP(points / opponents.size());
        float perf = avg + delta;
        Title gainedTitle;
        if (perf >= 2600) {
            gainedTitle = Title.GM;
        } else if (perf >= 2450) {
            gainedTitle = Title.IM;
        } else if (player.getSex() == Player.Sex.FEMALE && perf >= 2400) {
            gainedTitle = Title.WGM;
        } else if (player.getSex() == Player.Sex.FEMALE && perf >= 2250) {
            gainedTitle = Title.WIM;
        } else {
            return null;
        }
        gainedTitle = PZSzachCalculation.lowerTitle(gainedTitle, maxTitle);
        if (PZSzachCalculation.getTitleValue(gainedTitle, player.getSex()) > PZSzachCalculation.getTitleValue(player.getTitle(), player.getSex())) {
            return gainedTitle;
        } else {
            return null;
        }
    }
}
