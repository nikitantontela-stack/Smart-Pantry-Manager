package com.nikita.smartpantrymanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PantryDAO {

    @Insert
    void insert(PantryItem item);

    @Update
    void update(PantryItem item);

    @Delete
    void delete(PantryItem item);

    @Query("SELECT * FROM pantry_items ORDER BY name ASC")
    LiveData<List<PantryItem>> getAllPantryItems();

    // Non-LiveData version, needed for the strict-matching logic since I can't observe LiveData outside the UI thread easily
    @Query("SELECT * FROM pantry_items ORDER BY name ASC")
    List<PantryItem> getAllPantryItemsSync();

    // Recipe queries below

    @Insert
    void insertRecipe(Recipe recipe);

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipes")
    List<Recipe> getAllRecipesSync();

    // Used on first app launch to check if I still need to seed the recipe list
    @Query("SELECT COUNT(*) FROM recipes")
    int getRecipeCount();
}
