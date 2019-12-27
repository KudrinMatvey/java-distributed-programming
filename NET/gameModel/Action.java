package gameModel;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;

// todo: implements Serializable для того чтобы можно было отправлять обьект

public class Action implements Serializable {
    private Flag type;

    private Id id;
    private int x;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int y;

    private List<List<PointFlag>> map;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Action(Id id) {
        type = Flag.START;
        this.id = id;
    }
    public Action(List<List<PointFlag>> map) {
        type = Flag.SET_FLEET;
        this.map = map;
    }

    public Action(Pair<Integer, Integer> attemptCoordinates) {
        this.type = Flag.TRY;
        this.attemptCoordinates = attemptCoordinates;
    }
    public Action(Flag type, Pair<Integer, Integer> attemptCoordinates) {
        this.type = type;
        this.attemptCoordinates = attemptCoordinates;
    }

    public Pair<Integer, Integer> getAttemptCoordinates() {
        return attemptCoordinates;
    }

    public void setAttemptCoordinates(Pair<Integer, Integer> attemptCoordinates) {
        this.attemptCoordinates = attemptCoordinates;
    }

    Pair<Integer, Integer> attemptCoordinates;
    public List<List<PointFlag>> getMap() {
        return map;
    }

    public void setMap(List<List<PointFlag>> map) {
        this.map = map;
    }

    public Action(Flag type) {
        this.type = type;
    }

    public Action(Flag type, Object payload) {
        this.type = type;
    }
    public Action(Flag type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Action{" +
                "type=" + type +
                ", id=" + id +
                ", map=" + map +
                ", attemptCoordinates=" + attemptCoordinates +
                '}';
    }

    public Flag getType() {
        return type;
    }

    public void setType(Flag type) {
        this.type = type;
    }

}
