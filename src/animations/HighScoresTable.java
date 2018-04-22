package animations;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import animations.ScoreInfo;

/**
 * The class of the highScoresTable.
 * @author Barak Talmor
 */
public class HighScoresTable implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<ScoreInfo> hSTable;
    private int maxSize;

    /**
     * Create an empty high-scores table with the specified size.
     * @param size - of the list table
     */
    public HighScoresTable(int size) {
        this.hSTable = new ArrayList<>();
        this.maxSize = size;
    }

    /**
     * Create an empty high-scores table and size default.
     */
    public HighScoresTable() {
        this.hSTable = new ArrayList<>();
        this.maxSize = 5;
    }

    /**
     * Add a high-score.
     * @param score - scoreInfo object
     */
    public void add(ScoreInfo score) {
        boolean wasInsertion = false;
        if (this.hSTable.isEmpty()) {
            this.hSTable.add(score);
        } else {
            int i;
            for (i = 0; i < this.hSTable.size(); i++) {
                if (this.hSTable.get(i).getScore() < score.getScore()) {
                    wasInsertion = true;
                    this.hSTable.add(i, score);
                    break;
                }
            }
            if (i < this.maxSize && !wasInsertion) {
                this.hSTable.add(i, score);
            }
            // Checking if the new score exceeded the limit of the list
            if (this.hSTable.size() > this.maxSize) {
                this.hSTable.remove(this.maxSize);
            }
        }
    }

    /**
     * The method returns the current table size.
     * @return size
     */
    public int size() {
        return this.maxSize;
    }

    /**
     * Return the current high scores.
     * The list is already sorted such that the highest scores come first.
     * @return list of high scores
     */
    public List<ScoreInfo> getHighScores() {
        List<ScoreInfo> hS = new ArrayList<>();
        hS.addAll(this.hSTable);
        return hS;
    }

    /**
     * return the rank of the current score: where will it be on the list if
     * added?
     * @param score for the high score table
     * @return rank
     */
    public int getRank(int score) {
        if (this.hSTable.isEmpty()) {
            return 1;
        }
        int i;
        for (i = 0; i < this.hSTable.size(); i++) {
            if (this.hSTable.get(i).getScore() < score && i != this.maxSize) {
                return i + 1;
            }
        }
        if (i < this.maxSize) {
            return i + 1;
        } else {
            return this.maxSize + 1;
        }
    }

    /**
     * The method clears the table.
     */
    public void clear() {
        this.hSTable.clear();
    }

    /**
     * Load table data from file.
     * @param filename - to read from
     * @throws IOException - problem with IO
     */
    public void load(File filename) throws IOException {
        this.clear();
        ObjectInputStream oIS = null;
        HighScoresTable hST = null;
        try {
            oIS = new ObjectInputStream(new FileInputStream(filename));
            hST = (HighScoresTable) oIS.readObject();
            this.hSTable = hST.getHighScores();
            this.maxSize = hST.size();
        } catch (FileNotFoundException e) {
            System.err.println("File was not found");
            return;
        } catch (ClassNotFoundException e) {
            System.err.println("Class was not found");
            return;
        } finally {
            if (oIS != null) {
                oIS.close();
            }
        }
    }

    /**
     * Save table data to the specified file.
     * @param filename - to save on
     * @throws IOException - problem with IO
     */
    public void save(File filename) throws IOException {
        ObjectOutputStream oIS = null;
        try {
            oIS = new ObjectOutputStream(new FileOutputStream(filename));
            oIS.writeObject(this);
        } catch (FileNotFoundException e) {
            System.err.println("File was not found");
            return;
        } catch (IOException e) {
            System.err.println("Couldn't save file");
            return;
        } finally {
            if (oIS != null) {
                oIS.close();
            }
        }
    }

    /**
     * Read a table from file and return it.
     * @param filename - to read from
     * @return HighScoresTable
     */
    public static HighScoresTable loadFromFile(File filename) {
        HighScoresTable hST = new HighScoresTable(5);
        if (!filename.exists()) {
            System.out.println("File doesn't exist");
            return hST;
        }
        try {
            hST.load(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hST;
    }
}
