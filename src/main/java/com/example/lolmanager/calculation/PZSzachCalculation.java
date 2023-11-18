package com.example.lolmanager.calculation;

import com.example.lolmanager.model.Game;
import com.example.lolmanager.model.Player;
import com.example.lolmanager.model.Result;
import com.example.lolmanager.model.Title;

import java.util.Objects;

public class PZSzachCalculation {
    public static int getRatingPerformance(Player player) {
        return getAverageRating(player) + getRatingDelta(player);
    }

    public static int getRatingDelta(Player player) {
        int n = 1;
        int wins = 0;
        int loses = 0;
        for (Game game : player.getRounds()) {
            if (!game.isForfeit()) {
                if (game.getWhite() == player) {
                    if (game.getWhiteResult() != null) {
                        if (game.getWhiteResult() == Result.WIN) {
                            wins++;
                        } else if (game.getWhiteResult() == Result.LOSE) {
                            loses++;
                        }
                        n++;
                    }
                } else {
                    if (game.getBlackResult() != null) {
                        if (game.getBlackResult() == Result.WIN) {
                            wins++;
                        } else if (game.getBlackResult() == Result.LOSE) {
                            loses++;
                        }
                        n++;
                    }
                }
            }
        }
        return (400 / n) * (wins - loses);
    }

    public static int getAverageRating(Player player) {
        int sum = player.getLocalRating();
        int count = 1;
        for (Game round : player.getRounds()) {
            if (!round.isForfeit()) {
                if (round.getWhite() == player) {
                    sum += round.getBlack().getLocalRating();
                } else {
                    sum += round.getWhite().getLocalRating();
                }
                count++;
            }
        }

        return sum / count;
    }


    public static Integer getTitleValue(Title title, Player.Sex sex) {
        if (title == null){
            return 1000;
        }
        switch (title) {
            case GM -> {
                return 2600;
            }
            case IM -> {
                return 2450;
            }
            case WGM -> {
                return 2400;
            }
            case FM -> {
                return 2300;
            }
            case WIM -> {
                return 2250;
            }
            case CM -> {
                return 2200;
            }
            case WFM -> {
                return 2100;
            }
            case WCM -> {
                return 2000;
            }
            default -> {
                if (sex == null || Objects.requireNonNull(sex) == Player.Sex.MALE) {
                    switch (title) {
                        case M -> {
                            return 2400;
                        }
                        case K_PLUS_PLUS, K_PLUS -> {
                            return 2300;
                        }
                        case K -> {
                            return 2200;
                        }
                        case I_PLUS_PLUS, I_PLUS -> {
                            return 2100;
                        }
                        case I -> {
                            return 2000;
                        }
                        case II_PLUS -> {
                            return 1900;
                        }
                        case II -> {
                            return 1800;
                        }
                        case III -> {
                            return 1600;
                        }
                        case IV -> {
                            return 1400;
                        }
                        case V -> {
                            return 1200;
                        }
                        default -> {
                            return 1000;
                        }
                    }
                }
                switch (title) {
                    case M -> {
                        return 2200;
                    }
                    case K_PLUS_PLUS, K_PLUS -> {
                        return 2100;
                    }
                    case K -> {
                        return 2000;
                    }
                    case I_PLUS_PLUS, I_PLUS -> {
                        return 1900;
                    }
                    case I -> {
                        return 1800;
                    }
                    case II_PLUS -> {
                        return 1700;
                    }
                    case II -> {
                        return 1600;
                    }
                    case III -> {
                        return 1400;
                    }
                    case IV -> {
                        return 1250;
                    }
                    case V -> {
                        return 1100;
                    }
                    default -> {
                        return 1000;
                    }
                }
            }
        }
    }

    public static Title getNorm(int ratingPerformance, int gamesNo, Player.Sex sex) {
        if (gamesNo >= 9) {
            if (getNorm9Rounds(ratingPerformance, sex) != null) {
                return getNorm9Rounds(ratingPerformance, sex);
            }
            if (getNorm7Rounds(ratingPerformance, sex) != null) {
                return getNorm7Rounds(ratingPerformance, sex);
            }
            return getNorm5Rounds(ratingPerformance, sex);
        } else if (gamesNo >= 7) {
            if (getNorm7Rounds(ratingPerformance, sex) != null) {
                return getNorm7Rounds(ratingPerformance, sex);
            }
            return getNorm5Rounds(ratingPerformance, sex);
        } else if (gamesNo >= 5) {
            return getNorm5Rounds(ratingPerformance, sex);
        }
        return null;
    }

    private static Title getNorm5Rounds(int ratingPerformance, Player.Sex sex) {
        if (sex == Player.Sex.FEMALE) {
            if (ratingPerformance >= 1150) {
                return Title.IV;
            } else if (ratingPerformance >= 1000) {
                return Title.V;
            }
        } else {
            if (ratingPerformance >= 1300) {
                return Title.IV;
            } else if (ratingPerformance >= 1050) {
                return Title.V;
            }
        }
        return null;
    }

    private static Title getNorm7Rounds(int ratingPerformance, Player.Sex sex) {
        if (sex == Player.Sex.FEMALE) {
            if (ratingPerformance >= 1800) {
                return Title.I;
            } else if (ratingPerformance >= 1600) {
                return Title.II;
            } else if (ratingPerformance >= 1350) {
                return Title.III;
            }
        } else {
            if (ratingPerformance >= 1800) {
                return Title.II;
            } else if (ratingPerformance >= 1550) {
                return Title.III;
            }
        }
        return null;
    }

    private static Title getNorm9Rounds(int ratingPerformance, Player.Sex sex) {
        if (sex == Player.Sex.FEMALE) {
            if (ratingPerformance >= 2200) {
                return Title.M;
            } else if (ratingPerformance >= 2000) {
                return Title.K;
            }
        } else {
            if (ratingPerformance >= 2400) {
                return Title.M;
            } else if (ratingPerformance >= 2200) {
                return Title.K;
            }
        }
        return null;
    }


}
