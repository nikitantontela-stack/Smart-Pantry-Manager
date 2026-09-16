package com.nikita.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikita.smartpantrymanager.adapter.PantryAdapter;
import com.nikita.smartpantrymanager.data.AppDatabase;
import com.nikita.smartpantrymanager.data.PantryItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements PantryAdapter.OnItemActionListener {

    private AppDatabase db;
    private PantryAdapter adapter;
    private TextView textEmptyPantry;

    // I put this here to run database writes off the main thread, since Room won't let me block the UI thread
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPantry);
        textEmptyPantry = findViewById(R.id.textEmptyPantry);
        FloatingActionButton fabAddItem = findViewById(R.id.fabAddItem);

        adapter = new PantryAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // I used LiveData here so the list refreshes itself automatically whenever the pantry_items table changes
        db.pantryDAO().getAllPantryItems().observe(this, this::updatePantryList);

        fabAddItem.setOnClickListener(v -> {
            // I put this placeholder here until I build the Add/Edit screen
            Toast.makeText(this, "Add item screen coming next", Toast.LENGTH_SHORT).show();
        });
    }

    private void updatePantryList(List<PantryItem> items) {
        adapter.setPantryItems(items);
        textEmptyPantry.setVisibility(items.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @Override
    public void onEditClick(PantryItem item) {
        // I put this placeholder here until I build the Add/Edit screen
        Toast.makeText(this, "Edit: " + item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        executorService.execute(() -> db.pantryDAO().delete(item));
        Toast.makeText(this, "Deleted " + item.getName(), Toast.LENGTH_SHORT).show();
    }
}