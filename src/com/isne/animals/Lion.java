package com.isne.animals;

import com.isne.board.Board;

public class Lion extends Carnivorus {

    public boolean condition(Board board, int x, int y){
        return ((board.getCaseAt(x,y).content != null) && ( board.getCaseAt(x,y).content.getSpecies() == "Giraffe" || board.getCaseAt(x,y).content.getSpecies() == "Hippopotamus" ));
    }

}
