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
     *
     */
    private void initObjectives() {
        //delete this later, only for debugging at mimmis appartment.
        this.objectivesIncompelete.add(new Objective( 55.722167, 13.218062, R.drawable.image_lagenhet, "Mimmis lähenhet", "PANO_lagenhet.jpg"));

        //Manual addition of objectives to locate in Lund.
        this.objectivesIncompelete.add(new Objective(55.704553, 13.202201, R.drawable.image_botan, "Botaniska trädgården", "PANO_botan.jpg"));
        this.objectivesIncompelete.add(new Objective(55.717827, 13.212905, R.drawable.image_ideon, "Ideon Hotel", "PANO_ideon.jpg"));
        this.objectivesIncompelete.add(new Objective(55.710719, 13.209045, R.drawable.image_sjonsjon, "Sjön sjön", "PANO_sjonsjon.jpg" ));

        //Old test objectives for implementing early functionallity.
        //this.objectivesIncompelete.add(new Objective(20.00, 10.00, R.drawable.img_not_found, "testbild", "panorama_test.jpg"));
        //this.objectivesIncompelete.add(new Objective( 55.983431, 13.495272, R.drawable.uno, "MimmiTestLocation(DagsSjön)", "panorama_test.jpg"));
        //this.objectivesIncompelete.add(new Objective(59.33022, 18.0270402, R.drawable.kungsholmen2, "Kungsholmen", "kungsholmen.jpg"));
        //this.objectivesIncompelete.add(new Objective(59.3307, 18.0579, R.drawable.img_not_found, "Stockholm centralstation", "kungsholmen.jpg"));
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
            return new Objective(0, 0, R.drawable.img_not_found, "", "");
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

    public Objective get(int i){
        return objectivesIncompelete.get(i);
    }

    public int size(){
        return objectivesIncompelete.size();
    }

    public ArrayList<Objective> getIncompleteObjectives() {
        return objectivesIncompelete;
    }

}
