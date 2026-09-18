package com.nikita.smartpantrymanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nikita.smartpantrymanager.data.AppDatabase;
import com.nikita.smartpantrymanager.data.PantryItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class AddEditItemActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "extra_item_id";

    private EditText editTextItemName;
    private EditText editTextItemQuantity;
    private EditText editTextItemUnit;
    private EditText editTextExpiryDate;

    private AppDatabase db;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private int itemId = -1;

    // I use this pattern to check the expiry date is genuinely yyyy-MM-dd before saving
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        db = AppDatabase.getDatabase(getApplicationContext());

        editTextItemName = findViewById(R.id.editTextItemName);
        editTextItemQuantity = findViewById(R.id.editTextItemQuantity);
        editTextItemUnit = findViewById(R.id.editTextItemUnit);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        Button buttonSaveItem = findViewById(R.id.buttonSaveItem);

        itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);

        if (itemId != -1) {
            setTitle("Edit Ingredient");
            loadExistingItem();
        } else {
            setTitle("Add Ingredient");
        }

        buttonSaveItem.setOnClickListener(v -> validateAndSave());
    }

    private void loadExistingItem() {
        executorService.execute(() -> {
            PantryItem item = db.pantryDAO().getPantryItemById(itemId);
            if (item != null) {
                runOnUiThread(() -> {
                    editTextItemName.setText(item.getName());
                    editTextItemQuantity.setText(String.valueOf(item.getQuantity()));
                    editTextItemUnit.setText(item.getUnit());
                    editTextExpiryDate.setText(item.getExpiryDate());
                });
            }
        });
    }

    private void validateAndSave() {
        String name = editTextItemName.getText().toString().trim();
        String quantityText = editTextItemQuantity.getText().toString().trim();
        String unit = editTextItemUnit.getText().toString().trim();
        String expiryDate = editTextExpiryDate.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextItemName.setError("Ingredient name is required");
            return;
        }

        if (TextUtils.isEmpty(quantityText)) {
            editTextItemQuantity.setError("Quantity is required");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
            if (quantity <= 0) {
                editTextItemQuantity.setError("Quantity must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            editTextItemQuantity.setError("Enter a valid number");
            return;
        }

        if (!TextUtils.isEmpty(expiryDate) && !DATE_PATTERN.matcher(expiryDate).matches()) {
            editTextExpiryDate.setError("Use format yyyy-MM-dd");
            return;
        }

        String finalExpiryDate = TextUtils.isEmpty(expiryDate) ? null : expiryDate;

        if (itemId == -1) {
            PantryItem newItem = new PantryItem(name, quantity, unit, finalExpiryDate);
            executorService.execute(() -> db.pantryDAO().insert(newItem));
            Toast.makeText(this, "Added " + name, Toast.LENGTH_SHORT).show();
        } else {
            executorService.execute(() -> {
                PantryItem existingItem = db.pantryDAO().getPantryItemById(itemId);
                if (existingItem != null) {
                    existingItem.setName(name);
                    existingItem.setQuantity(quantity);
                    existingItem.setUnit(unit);
                    existingItem.setExpiryDate(finalExpiryDate);
                    db.pantryDAO().update(existingItem);
                }
            });
            Toast.makeText(this, "Updated " + name, Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
