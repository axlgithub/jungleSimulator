package com.isne.board;

import com.isne.animals.*;
import com.isne.master.MasterCrocodile;
import com.isne.master.MasterGiraffe;
import com.isne.master.MasterHippopotamus;
import com.isne.master.MasterLion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static com.isne.Main.*;

/**
 * Default constructor, expensive in ressources
 */
public class Board {
    public static final String ANSI_RESET = "\u001B[0m";
    public static volatile int bushNumber = 0;
    public static volatile int treeNumber = 0;
    public final Case[][] grid;

    /**
     * Constructor for Board initialization
     */
    public Board() {
        // Build empty board
        this.grid = new Case[LIMIT][LIMIT];

        // Fill whole board with Ground
        fillGround();

        // Replace some cases with
        placeSafeZones();
        placeWater();
        placeMasters();
        generateBushes();
        generateTrees();

    }

    /**
     * Clear console, only works on execution (not in IDE)
     */
    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Generate number of wanted trees on a random spot
     */
    private void generateTrees() {
        while (treeNumber <= NTREES) {
            int x = new Random().nextInt(30);
            int y = new Random().nextInt(30);

            // not busy and not water and not safezone
            if (!this.getCaseAt(x, y).isBusy() && this.getCaseAt(x, y).getType() != "Water" && this.getCaseAt(x, y).getType() != "SafeZone") {
                this.grid[LIMIT - 1 - x][y] = new Tree();
                this.grid[LIMIT - 1 - x][y].setPosX(x);
                this.grid[LIMIT - 1 - x][y].setPosY(y);
                treeNumber++; // increment number of present trees on board
            }
        }
    }

    /**
     * Generate number of wanted bushes on a random spot
     */
    private void generateBushes() {
        while (bushNumber <= NBUSHES) {
            int x = new Random().nextInt(30);
            int y = new Random().nextInt(30);

            // not busy and not water and not safezone
            if (!this.getCaseAt(x, y).isBusy() && this.getCaseAt(x, y).getType() != "Water" && this.getCaseAt(x, y).getType() != "SafeZone") {
                this.grid[LIMIT - 1 - x][y] = new Bush();
                this.grid[LIMIT - 1 - x][y].setPosX(x);
                this.grid[LIMIT - 1 - x][y].setPosY(y);
                bushNumber++; // increment number of present bushes on board
            }
        }
    }

    /**
     * Fill all board with ground, only used in constructor
     */
    private void fillGround() {
        int x = 0;
        int y = 0;

        for (Case[] i : this.grid) {
            for (Case caseElement : i) {
                caseElement = new Ground();
                caseElement.setPosX(x);
                caseElement.setPosY(y);
                this.grid[LIMIT - 1 - x][y] = caseElement;
                y++;
            }
            y = 0;
            x++;
        }
    }

    /**
     * Place water cases, only used in constructor
     */
    private void placeWater() {
        int x = 0;
        int y = 0;

        for (Case[] i : this.grid) {
            if (x > 9 && x < 23) {
                for (Case caseElement : i) {
                    if (y > 9 && y < 23) {
                        caseElement = new Water();
                        caseElement.setPosX(x);
                        caseElement.setPosY(y);
                        this.grid[LIMIT - 1 - x][y] = caseElement;
                    }
                    y++;
                }
            }
            y = 0;
            x++;
        }
    }

    /**
     * Place all 4 masters, only used in constructor
     */
    private void placeMasters() {
        MasterLion MLion = MasterLion.getInstance();
        this.getCaseAt(0, 0).setBusy(true);
        this.getCaseAt(0, 0).master = MLion;
        MLion.house = this.getCaseAt(0, 0);

        MasterCrocodile MCrocodile = MasterCrocodile.getInstance();
        this.getCaseAt(LIMIT - 1, 0).setBusy(true);
        this.getCaseAt(LIMIT - 1, 0).master = MCrocodile;
        MCrocodile.house = this.getCaseAt(LIMIT - 1, 0);

        MasterGiraffe MGiraffe = MasterGiraffe.getInstance();
        this.getCaseAt(0, LIMIT - 1).setBusy(true);
        this.getCaseAt(0, LIMIT - 1).master = MGiraffe;
        MGiraffe.house = this.getCaseAt(0, LIMIT - 1);

        MasterHippopotamus MHippopotamus = MasterHippopotamus.getInstance();
        this.getCaseAt(LIMIT - 1, LIMIT - 1).setBusy(true);
        this.getCaseAt(LIMIT - 1, LIMIT - 1).master = MHippopotamus;
        MHippopotamus.house = this.getCaseAt(LIMIT - 1, LIMIT - 1);
    }

    /**
     * Place safe zones, used in constructor
     */
    private void placeSafeZones() {
        int x = 0;
        int y = 0;
        for (Case[] i : this.grid) {
            if (x < SAFEZONESIZE) {
                for (Case caseElement : i) {
                    // Top left
                    if (y < SAFEZONESIZE) {
                        caseElement = new SafeZone("Lion");
                        caseElement.setPosX(x);
                        caseElement.setPosY(y);
                        this.grid[LIMIT - 1 - x][y] = caseElement;
                    }

                    // Top right
                    if (y >= LIMIT - SAFEZONESIZE) {
                        caseElement = new SafeZone("Giraffe");
                        caseElement.setPosX(x);
                        caseElement.setPosY(y);
                        this.grid[LIMIT - 1 - x][y] = caseElement;
                    }
                    y++;
                }
            }

            if (x >= LIMIT - SAFEZONESIZE) {
                for (Case caseElement : i) {
                    // Bottom left
                    if (y < SAFEZONESIZE) {
                        caseElement = new SafeZone("Crocodile");
                        caseElement.setPosX(x);
                        caseElement.setPosY(y);
                        this.grid[LIMIT - 1 - x][y] = caseElement;
                    }

                    // Bottom right
                    if (y >= LIMIT - SAFEZONESIZE) {
                        caseElement = new SafeZone("Hippopotamus");
                        caseElement.setPosX(x);
                        caseElement.setPosY(y);
                        this.grid[LIMIT - 1 - x][y] = caseElement;
                    }
                    y++;
                }
            }

            y = 0;
            x++;
        }
    }

    /**
     * Return Case object from coordinates
     *
     * @param x
     * @param y
     * @return
     */
    public Case getCaseAt(int x, int y) {
        return (this.grid[LIMIT - 1 - y][x]);
    }

    /**
     * Decrease hunger at each end of turn. If 0 then dies, removed from grid content and garbage collector should remove the unused object
     */
    public void manageHunger() {
        Animal animal;
        for (int x = 0; x < LIMIT; x++) {
            for (int y = 0; y < LIMIT; y++) {
                if (this.getCaseAt(x, y).content != null) {
                    animal = this.getCaseAt(x, y).content;
                    animal.setHunger(animal.getHunger() - 1);
                    if (animal.getHunger() == 0) {
                        this.getCaseAt(x, y).content = null;
                    }
                }
            }
        }
    }

    public void makeASpeciesMove(String aSpecies) {
        for (int x1 = 0; x1 < LIMIT; x1++) { // first loop to say that no animal of this species has moved
            for (int y2 = 0; y2 < LIMIT; y2++) {
                if (this.getCaseAt(x1, y2).content != null && this.getCaseAt(x1, y2).content.getSpecies() == aSpecies) {
                    this.getCaseAt(x1, y2).content.setHasAlreadyMoved(false);
                }
            }
        }
        for (int x = 0; x < LIMIT; x++) { // loop to make the animal of the species move.
            for (int y = 0; y < LIMIT; y++) {
                if (this.getCaseAt(x, y).content != null && this.getCaseAt(x, y).content.getSpecies() == aSpecies && !this.getCaseAt(x, y).content.getHasAlreadyMoved()) {
                    this.getCaseAt(x, y).content.move(this);
                }
            }
        }
    }

    /**
     * Show board content
     */
    public void show() {
        int iterator = 0;
        for (Case[] i : this.grid) {
            for (Case caseElement : i) {
                // Check if Master instance
                if (caseElement.master != null) {
                    if (caseElement.master instanceof MasterLion) {
                        MasterLion temp = (MasterLion) caseElement.master;
                        System.out.print(temp.getBackground() + temp.getSymbol() + ANSI_RESET);
                    }
                    if (caseElement.master instanceof MasterCrocodile) {
                        MasterCrocodile temp = (MasterCrocodile) caseElement.master;
                        System.out.print(temp.getBackground() + temp.getSymbol() + ANSI_RESET);
                    }

                    if (caseElement.master instanceof MasterGiraffe) {
                        MasterGiraffe temp = (MasterGiraffe) caseElement.master;
                        System.out.print(temp.getBackground() + temp.getSymbol() + ANSI_RESET);
                    }
                    if (caseElement.master instanceof MasterHippopotamus) {
                        MasterHippopotamus temp = (MasterHippopotamus) caseElement.master;
                        System.out.print(temp.getBackground() + temp.getSymbol() + ANSI_RESET);
                    }
                }
                // Check if contains Animal instance
                else if (caseElement.content != null) {
                    System.out.print(caseElement.backgroundColor + caseElement.content.getSymbol() + ANSI_RESET);
                } else {
                    System.out.print(caseElement.backgroundColor + caseElement.getSymbol() + ANSI_RESET);
                }

                iterator++;
                if (iterator % LIMIT == 0) {
                    System.out.print("\n");
                }
            }
        }
    }

    /**
     * initialise the board with the chosen number of animal instances.
     *
     * @param numberOfAnimalsToCreate, set the number of animals which will be created for each species.
     */
    public void placeAnimals(int numberOfAnimalsToCreate) {
        int numberOfAnimalsAlreadyCreated = 0;
        Lion newLion;
        Giraffe newGiraffe;
        Crocodile newCrocodile;
        Hippopotamus newHippopotamus;

        for (int x = 0; x < SAFEZONESIZE; x++) {
            for (int y = 0; y < SAFEZONESIZE; y++) { // those loops in order to browse the nine cases of the safe-zone.
                if (!this.getCaseAt(x, y).isBusy()) { // in order to avoid putting an element on the master
                    newLion = new Lion(); // at each new loop we add one animal of each species on the board.
                    newGiraffe = new Giraffe();
                    newCrocodile = new Crocodile();
                    newHippopotamus = new Hippopotamus();
                    this.getCaseAt(x, y).content = newLion;
                    newLion.setPositionX(x);
                    newLion.setPositionY(y);
                    this.getCaseAt(x, LIMIT - 1 - y).content = newGiraffe;
                    newGiraffe.setPositionX(x);
                    newGiraffe.setPositionY(LIMIT - 1 - y);
                    this.getCaseAt(LIMIT - 1 - x, LIMIT - 1 - y).content = newHippopotamus;
                    newHippopotamus.setPositionX(LIMIT - 1 - x);
                    newHippopotamus.setPositionY(LIMIT - 1 - y);
                    this.getCaseAt(LIMIT - 1 - x, y).content = newCrocodile;
                    newCrocodile.setPositionX(LIMIT - 1 - x);
                    newCrocodile.setPositionY(y);
                    numberOfAnimalsAlreadyCreated += 1;
                    if (numberOfAnimalsAlreadyCreated == numberOfAnimalsToCreate) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * Each turn, breeds move one by one and board is displayed
     */
    public void startGame() {
        ArrayList<String> turnOrderList = new ArrayList<>();
        turnOrderList.add("Lion");
        turnOrderList.add("Crocodile");
        turnOrderList.add("Hippopotamus");
        turnOrderList.add("Giraffe");

        while (true) {
            // Shuffle order
            Collections.shuffle(turnOrderList);
            for (String breed : turnOrderList) {
                if (Objects.equals(breed, "Lion")) {
                    this.makeASpeciesMove("Lion");
                }

                if (Objects.equals(breed, "Hippopotamus")) {
                    this.makeASpeciesMove("Hippopotamus");
                }

                if (Objects.equals(breed, "Giraffe")) {
                    this.makeASpeciesMove("Giraffe");
                }

                if (Objects.equals(breed, "Crocodile")) {
                    this.makeASpeciesMove("Crocodile");
                }
                // Display
                clearConsole();
                this.show();
                try {
                    Thread.sleep(SPEED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // End of turn
            this.manageHunger();
            // generate plants if eaten
            if (bushNumber <= NBUSHES) {
                generateBushes();
            }

            if (treeNumber <= NTREES) {
                generateTrees();
            }
            clearConsole();
            this.show();
            System.out.println("END OF TURN");
            try {
                Thread.sleep(SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
