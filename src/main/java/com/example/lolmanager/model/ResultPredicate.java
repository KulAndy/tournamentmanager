package com.example.lolmanager.model;

import com.example.lolmanager.calculation.PZSzachCalculation;

import java.util.Objects;
import java.util.function.Predicate;

public class ResultPredicate<T extends Player> implements Predicate<T> {
    private String filterName;
    private CompareOperator sexCompareOperator;
    private Player.Sex sex;
    private CompareOperator yearCompareOperator;
    private Integer year;
    private CompareOperator titleCompareOperator;
    private Title title;
    private CompareOperator localRtgCompareOperator;
    private Integer localRtg;
    private CompareOperator fideRtgCompareOperator;
    private Integer fideRtg;
    private CompareOperator fedCompareOperator;
    private Federation federation;
    private CompareOperator stateCompareOperator;
    private String state;

    public ResultPredicate(
            String filterName, Player.Sex sex, CompareOperator yearCompareOperator,
            Integer year, CompareOperator titleCompareOperator, Title title, CompareOperator localRtgCompareOperator,
            Integer localRtg, CompareOperator fideRtgCompareOperator, Integer fideRtg, CompareOperator fedCompareOperator,
            Federation federation, CompareOperator stateCompareOperator, String state
    ) {
        setFilterName(filterName);
        setSex(sex);
        if (yearCompareOperator == null || year <= 0) {
            setYearCompareOperator(null);
            setYear(null);
        } else {
            setYearCompareOperator(yearCompareOperator);
            setYear(year);
        }

        if (titleCompareOperator == null || title == null || title == Title.bk) {
            setTitleCompareOperator(null);
            setTitle(null);
        } else {
            setTitleCompareOperator(titleCompareOperator);
            setTitle(title);
        }

        if (localRtgCompareOperator == null || localRtg <= 0) {
            setLocalRtgCompareOperator(null);
            setLocalRtg(null);
        } else {
            setLocalRtgCompareOperator(localRtgCompareOperator);
            setLocalRtg(localRtg);
        }

        if (fideRtgCompareOperator == null || fideRtg <= 0) {
            setFideRtgCompareOperator(null);
            setFideRtg(null);
        } else {
            setFideRtgCompareOperator(fideRtgCompareOperator);
            setFideRtg(fideRtg);
        }

        if (fedCompareOperator == null || federation == null) {
            setFedCompareOperator(null);
            setFederation(null);
        } else {
            setFedCompareOperator(fedCompareOperator);
            setFederation(federation);
        }

        if (stateCompareOperator == null || state.isEmpty()) {
            setStateCompareOperator(null);
            setState(null);
        } else {
            setStateCompareOperator(stateCompareOperator);
            setState(state);
        }
        if (sex != null) {
            setSexCompareOperator(CompareOperator.EQUAL);
        }
    }

    @Override
    public boolean test(T t) {
        if (t != null) {
            boolean sexCompare = compare(getSexCompareOperator(), getSex(), t.getSex());
            boolean yearCompare = compareNumber(getYearCompareOperator(), getYear(), t.getYearOfBirth());
            boolean titleCompare = compareNumber(getTitleCompareOperator(), PZSzachCalculation.getTitleValue(getTitle(), t.getSex()), PZSzachCalculation.getTitleValue(t.getTitle(), t.getSex()));
            boolean localRtgCompare = compareNumber(getLocalRtgCompareOperator(), getLocalRtg(), t.getLocalRating());
            boolean fideRtgCompare = compareNumber(getFideRtgCompareOperator(), getFideRtg(), t.getFideRating());
            boolean fedCompare = compare(getFedCompareOperator(), getFederation(), t.getFederation());
            boolean stateCompare = compare(getStateCompareOperator(), getState(), t.getState());
            return sexCompare && yearCompare && titleCompare && localRtgCompare && fideRtgCompare && fedCompare && stateCompare;
        }
        return false;
    }

    private boolean compareNumber(CompareOperator compareOperator, Integer predicateValue, Integer value) {
        if (compareOperator == null || predicateValue == null) {
            return true;
        }

        if (value == null) {
            return false;
        }


        return switch (compareOperator) {
            case EQUAL -> Objects.equals(value, predicateValue);
            case UNEQUAL -> !Objects.equals(value, predicateValue);
            case LESS -> value < predicateValue;
            case LESS_OR_EQUAL -> value <= predicateValue;
            case GREATER -> value > predicateValue;
            case GREATER_OR_EQUAL -> value >= predicateValue;
        };
    }

    private <T extends Comparable<T>> boolean compare(CompareOperator compareOperator, T predicateValue, T value) {
        if (compareOperator == null) {
            return true;
        }

        switch (compareOperator) {
            case EQUAL -> {
                return predicateValue == value;
            }
            case UNEQUAL -> {
                return predicateValue != value;
            }
        }
        return true;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public CompareOperator getSexCompareOperator() {
        return sexCompareOperator;
    }

    public void setSexCompareOperator(CompareOperator sexCompareOperator) {
        this.sexCompareOperator = sexCompareOperator;
    }

    public Player.Sex getSex() {
        return sex;
    }

    public void setSex(Player.Sex sex) {
        this.sex = sex;
    }

    public CompareOperator getYearCompareOperator() {
        return yearCompareOperator;
    }

    public void setYearCompareOperator(CompareOperator yearCompareOperator) {
        this.yearCompareOperator = yearCompareOperator;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public CompareOperator getTitleCompareOperator() {
        return titleCompareOperator;
    }

    public void setTitleCompareOperator(CompareOperator titleCompareOperator) {
        this.titleCompareOperator = titleCompareOperator;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public CompareOperator getLocalRtgCompareOperator() {
        return localRtgCompareOperator;
    }

    public void setLocalRtgCompareOperator(CompareOperator localRtgCompareOperator) {
        this.localRtgCompareOperator = localRtgCompareOperator;
    }

    public Integer getLocalRtg() {
        return localRtg;
    }

    public void setLocalRtg(Integer localRtg) {
        this.localRtg = localRtg;
    }

    public CompareOperator getFideRtgCompareOperator() {
        return fideRtgCompareOperator;
    }

    public void setFideRtgCompareOperator(CompareOperator fideRtgCompareOperator) {
        this.fideRtgCompareOperator = fideRtgCompareOperator;
    }

    public Integer getFideRtg() {
        return fideRtg;
    }

    public void setFideRtg(Integer fideRtg) {
        this.fideRtg = fideRtg;
    }

    public CompareOperator getFedCompareOperator() {
        return fedCompareOperator;
    }

    public void setFedCompareOperator(CompareOperator fedCompareOperator) {
        this.fedCompareOperator = fedCompareOperator;
    }

    public Federation getFederation() {
        return federation;
    }

    public void setFederation(Federation federation) {
        this.federation = federation;
    }

    public CompareOperator getStateCompareOperator() {
        return stateCompareOperator;
    }

    public void setStateCompareOperator(CompareOperator stateCompareOperator) {
        this.stateCompareOperator = stateCompareOperator;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public enum CompareOperator {
        EQUAL,
        UNEQUAL,
        LESS,
        LESS_OR_EQUAL,
        GREATER,
        GREATER_OR_EQUAL;

        @Override
        public String toString() {
            switch (this) {
                case EQUAL -> {
                    return "=";
                }
                case UNEQUAL -> {
                    return "!=";
                }
                case LESS -> {
                    return "<";
                }
                case LESS_OR_EQUAL -> {
                    return "<=";
                }
                case GREATER -> {
                    return ">";
                }
                case GREATER_OR_EQUAL -> {
                    return ">=";
                }
                default -> {
                    return "";
                }
            }
        }
    }
}
