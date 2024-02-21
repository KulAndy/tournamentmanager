package com.example.tournamentmanager.comparator;

import com.example.tournamentmanager.model.Player;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Random;

public class StartListComparator implements Comparator<Player>, Serializable {
    private SortCriteria criteria1;
    private SortCriteria criteria2;
    private SortCriteria criteria3;
    private SortCriteria criteria4;
    private SortCriteria criteria5;

    public StartListComparator() {
        this(
                SortCriteria.FIDE_RATING,
                SortCriteria.LOCAL_RATING,
                SortCriteria.TITLE,
                SortCriteria.ALPHABETIC,
                SortCriteria.RANDOM
        );
    }

    public StartListComparator(
            SortCriteria criteria1,
            SortCriteria criteria2,
            SortCriteria criteria3,
            SortCriteria criteria4,
            SortCriteria criteria5
    ) {
        setCriteria1(criteria1);
        setCriteria2(criteria2);
        setCriteria3(criteria3);
        setCriteria4(criteria4);
        setCriteria5(criteria5);
    }

    @Override
    public int compare(Player player1, Player player2) {
        int result;
        result = compare(player1, player2, getCriteria1());
        if (result != 0) {
            return result;
        }
        result = compare(player1, player2, getCriteria2());
        if (result != 0) {
            return result;
        }
        result = compare(player1, player2, getCriteria3());
        if (result != 0) {
            return result;
        }
        result = compare(player1, player2, getCriteria4());
        if (result != 0) {
            return result;
        }
        result = compare(player1, player2, getCriteria5());
        return result;
    }

    public int compare(Player player1, Player player2, SortCriteria criteria) {
        switch (criteria) {
            case TITLE -> {
                return
                        Integer.compare(
                                player1.getTitle().ordinal(),
                                player2.getTitle().ordinal()
                        );
            }
            case FIDE_RATING -> {
                return -Integer.compare(player1.getFideRating(), player2.getFideRating());
            }
            case LOCAL_RATING -> {
                return -Integer.compare(player1.getLocalRating(), player2.getLocalRating());
            }
            case SEX -> {
                if (
                        player1.getSex() == Player.Sex.MALE && player2.getSex() == Player.Sex.FEMALE
                ) {
                    return -1;
                } else if (
                        player1.getSex() == Player.Sex.FEMALE && player2.getSex() == Player.Sex.MALE
                ) {
                    return 1;
                } else {
                    return 0;
                }
            }
            case CLUB -> {
                if (player1.getClub() == null){
                    if (player2.getClub() == null){
                        return 0;
                    }else{
                        return 1;
                    }
                }
                return player1.getClub().compareToIgnoreCase(player2.getClub());
            }
            case FEDERATION -> {
                if (player1.getFederation() == null){
                    if (player2.getFederation() == null){
                        return 0;
                    }else{
                        return 1;
                    }
                }
                return -player1.getFederation().toString().compareToIgnoreCase(player2.getFederation().toString());
            }
            case DATE_OF_BIRTH -> {
                return -player1.getDateOfBirth().compareTo(player2.getDateOfBirth());
            }
            case ALPHABETIC -> {
                Collator collator = Collator.getInstance(new Locale.Builder().setLanguage("pl").setRegion("PL").build());
                return collator.compare(player1.getName(), player2.getName());
            }
            case RANDOM -> {
                player1.generateRandomValue();
                player2.generateRandomValue();
                return -Integer.compare(player1.getRandomValue(), player2.getRandomValue());
            }
            default -> {
                return 0;
            }
        }
    }

    public SortCriteria getCriteria1() {
        return criteria1;
    }

    public void setCriteria1(SortCriteria criteria1) {
        this.criteria1 = criteria1;
    }

    public SortCriteria getCriteria2() {
        return criteria2;
    }

    public void setCriteria2(SortCriteria criteria2) {
        this.criteria2 = criteria2;
    }

    public SortCriteria getCriteria3() {
        return criteria3;
    }

    public void setCriteria3(SortCriteria criteria3) {
        this.criteria3 = criteria3;
    }

    public SortCriteria getCriteria4() {
        return criteria4;
    }

    public void setCriteria4(SortCriteria criteria4) {
        this.criteria4 = criteria4;
    }

    public SortCriteria getCriteria5() {
        return criteria5;
    }

    public void setCriteria5(SortCriteria criteria5) {
        this.criteria5 = criteria5;
    }

    public enum SortCriteria {
        TITLE,
        FIDE_RATING,
        LOCAL_RATING,
        SEX,
        CLUB,
        FEDERATION,
        DATE_OF_BIRTH,
        ALPHABETIC,
        RANDOM;

        public static SortCriteria getSortCriteria(String symbol) {
            switch (symbol) {
                case "1" -> {
                    return FIDE_RATING;
                }
                case "2" -> {
                    return LOCAL_RATING;
                }
                case "3" -> {
                    return TITLE;
                }
                case "4" -> {
                    return ALPHABETIC;
                }
                case "5" -> {
                    return RANDOM;
                }
                case "7" -> {
                    return CLUB;
                }
                default -> {
                    return null;
                }
            }
        }

        @Override
        public String toString() {
            String value = super.toString();
            value = value.replace('_', ' ');
            return value;
        }
    }
}
