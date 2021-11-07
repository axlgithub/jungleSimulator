package com.isne.animals;

import com.isne.board.Board;

public class Crocodile extends Carnivorous {
    public final String Symbol = " c ";
    private int speedOnWatter;

    public Crocodile() {
        this.setHunger(25);
        this.setCanSwim(true);
        this.setLife(50);
        this.setStrength(10);
        this.setSpeedOnGround(3);
        this.setSpecies("Crocodile");
        this.setSymbol(Symbol);
        this.setWasStuckOnY(false);
    }

    public int getSpeedOnWatter() {
        return speedOnWatter;
    }

    public void setSpeedOnWatter(int speedOnWatter) {
        this.speedOnWatter = speedOnWatter;
    }

    public boolean condition(Board board, int x, int y) {
        return ((board.getCaseAt(x, y).content != null) && (board.getCaseAt(x, y).content.getSpecies() == "Giraffe" || board.getCaseAt(x, y).content.getSpecies() == "Hippopotamus"));
    }
}
