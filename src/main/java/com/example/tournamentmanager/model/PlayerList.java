package com.example.tournamentmanager.model;

import com.example.tournamentmanager.comparator.StartListComparator;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.*;

@XmlRootElement(name = "players")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerList extends ArrayList<Player> implements Serializable {
    private final Map<ObjectId, Integer> uuid2startNo = new HashMap<>();
    private Player bye;
    private Player halfbye;
    private Player unpaired;
    private StartListComparator comparator;

    public PlayerList() {
        add("bye");
        add("halfbye");
        add("unpaired");

        setComparator(new StartListComparator());
    }

    public PlayerList(List<Player> players) {
        this.addAll(players);
        rehash();
    }

    public void addAll(List<Player> players) {
        super.addAll(players);
        getUuid2startNo().clear();
        for (Player player : this) {
            getUuid2startNo().put(player.getPlayerid(), getUuid2startNo().size() + 1);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n[\n");
        for (Player player : this) {
            sb.append("\t").append(player).append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(Player player) {
        String name = player.getName();
        switch (name) {
            case "bye" -> setBye(player);
            case "halfbye" -> setHalfbye(player);
            case "unpaired" -> setUnpaired(player);
            default -> {
                super.add(player);
                getUuid2startNo().put(player.getPlayerid(), getUuid2startNo().size() + 1);
            }
        }
        return true;
    }


    public boolean add(String name) {
        Player player = new Player(name);
        return add(player);
    }

    public boolean add(String name, Integer fiderating, Integer localRating) {
        Player player = new Player(name, fiderating, localRating);
        return add(player);
    }

    public boolean add(String name, Title title) {
        Player player = new Player(name, title);
        return add(player);
    }

    public boolean add(String name, Title title, Player.Sex sex) {
        Player player = new Player(name, title, sex);
        return add(player);
    }

    public boolean add(String name, Integer fiderating, Title title) {
        Player player = new Player(name, fiderating, title);
        return add(player);
    }

    public boolean add(String name, Integer fiderating, Title title, Player.Sex sex) {
        Player player = new Player(name, fiderating, title, sex);
        return add(player);
    }

    public void add(
            Federation federation, String state, String name, Title title, Integer localRating, Integer fideRating,
            String club, String dateOfBirth, Player.Sex sex, String eMail, Short phonePrefix, Integer phoneNumber, Integer localId, Integer fideId, String remarks
    ) {
        Player newPlayer = new Player(federation, state, name, title, localRating, fideRating,
                club, dateOfBirth, sex, eMail, phonePrefix, phoneNumber, localId, fideId, remarks
        );
        add(newPlayer);
    }

    public boolean remove(String name) {
        Player player = get(name);
        boolean removed = false;
        if (player != null) {
            remove(player);
            removed = true;
            getUuid2startNo().remove(player.getPlayerid());
        }
        return removed;
    }

    public Player get(String name) {
        for (Player player : this) {
            if (Objects.equals(player.getName(), name)) {
                return player;
            }
        }
        return null;
    }

    public Player get(ObjectId uuid) {
        if (Objects.equals(uuid.toString(), getBye().getPlayerid().toString())) {
            return getBye();
        } else if (Objects.equals(uuid.toString(), getHalfbye().getPlayerid().toString())) {
            return getHalfbye();
        } else if (Objects.equals(uuid.toString(), getUnpaired().getPlayerid().toString())) {
            return getUnpaired();
        } else {
            for (Player player : this) {
                if (Objects.equals(player.getPlayerid().toString(), uuid.toString())) {
                    return player;
                }
            }
        }
        return null;
    }

    public Player getBye() {
        return bye;
    }

    public void setBye(Player bye) {
        bye.setPlayerid(-1);
        this.bye = bye;
    }

    public Player getHalfbye() {
        return halfbye;
    }

    public void setHalfbye(Player halfbye) {
        halfbye.setPlayerid(-2);
        this.halfbye = halfbye;
    }

    public Player getUnpaired() {
        return unpaired;
    }

    public void setUnpaired(Player unpaired) {
        unpaired.setPlayerid(-3);
        this.unpaired = unpaired;
    }

    public StartListComparator getComparator() {
        return comparator;
    }

    public void setComparator(StartListComparator comparator) {
        this.comparator = comparator;
    }


    public void rehash() {
        getUuid2startNo().clear();
        for (Player player : this) {
            getUuid2startNo().put(player.getPlayerid(), getUuid2startNo().size() + 1);
        }
    }

    @Override
    public void sort(Comparator c) {
        super.sort(c);
        rehash();
    }

    public void sort() {
        super.sort(comparator);
        rehash();
    }

    public Map<ObjectId, Integer> getUuid2startNo() {
        return uuid2startNo;
    }

}