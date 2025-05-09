package edu.mci.snacktrack.model;

import jakarta.persistence.Entity;

@Entity
public class Calories {
    private int caloriesInKcal;
    private int  caloriesInKJoule;

    public Calories(int caloriesInKcal){
        setCaloriesInKcal(caloriesInKcal);
    }

    // getter & setter
    public int getCaloriesInKcal() {
        return caloriesInKcal;
    }

    public void setCaloriesInKcal(int caloriesInKcal) {
        this.caloriesInKcal = caloriesInKcal;
        this.caloriesInKJoule = (int) Math.round(caloriesInKcal * 4.184);
    }

    public int  getCaloriesInKJoule(){
        return caloriesInKJoule;
    }
}
