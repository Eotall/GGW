package com.example.geoguesserwalker;

import java.util.ArrayList;
import java.util.Random;

public class ObjectiveTracker {
    private ArrayList<Objective> objectivesIncompelete;

    /**
     * Class that contains all the available objectives
     */
    public ObjectiveTracker(){
        this.objectivesIncompelete = new ArrayList<Objective>();
        initObjectives();
    }

    /**
     * Populates the ArrayList with objectives
     * TODO populate objectives through database(mock will suffice for the porpuse of our project IMO //Martin)
     */
    private void initObjectives() {
        this.objectivesIncompelete.add(new Objective(55.710725, 13.209021, R.drawable.sjonsjon, "Sjön sjön" ));
        this.objectivesIncompelete.add(new Objective(20.00, 10.00, R.drawable.img_not_found, "testbild"));
        this.objectivesIncompelete.add(new Objective( 63.4010181, 13.0769338, R.drawable.uno, "en till testbild"));
    }

    /**
     *
     * @return A random unused location
     */
    public Objective nextObjective(){
        try{
            return objectivesIncompelete.remove(new Random().nextInt(objectivesIncompelete.size()));
        }catch (IndexOutOfBoundsException e){
            System.out.println("No more objectives in ");
            return new Objective(0, 0, R.drawable.img_not_found, "");
        }
    }

    /**
     *
     * @return True if there are more unused locations
     */
    public boolean hasNextObjective(){
        return objectivesIncompelete.size() > 0;
    }

    /**
     * Resets the used locations
     */
    public void reset(){
        objectivesIncompelete.clear();
        initObjectives();
    }

    public ArrayList<Objective> getIncompleteObjectives() {
        return objectivesIncompelete;
    }

}
