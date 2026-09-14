package com.nikita.smartpantrymanager.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    // Comma-separated ingredient names, e.g. "egg,milk,flour"
    @NonNull
    @ColumnInfo(name = "required_ingredients")
    private String requiredIngredients;

    // Comma-separated quantities matching the order of requiredIngredients, e.g. "2,250,100"
    @NonNull
    @ColumnInfo(name = "required_quantities")
    private String requiredQuantities;

    @ColumnInfo(name = "preparation_steps")
    private String preparationSteps;

    public Recipe(@NonNull String name, @NonNull String requiredIngredients,
                  @NonNull String requiredQuantities, String preparationSteps) {
        this.name = name;
        this.requiredIngredients = requiredIngredients;
        this.requiredQuantities = requiredQuantities;
        this.preparationSteps = preparationSteps;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    @NonNull
    public String getRequiredIngredients() { return requiredIngredients; }
    public void setRequiredIngredients(@NonNull String requiredIngredients) { this.requiredIngredients = requiredIngredients; }

    @NonNull
    public String getRequiredQuantities() { return requiredQuantities; }
    public void setRequiredQuantities(@NonNull String requiredQuantities) { this.requiredQuantities = requiredQuantities; }

    public String getPreparationSteps() { return preparationSteps; }
    public void setPreparationSteps(String preparationSteps) { this.preparationSteps = preparationSteps; }
}
