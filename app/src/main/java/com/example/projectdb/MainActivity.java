package com.example.projectdb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener{

    // ALl elements variables
    EditText editTextName, editTextAge, editTextId;
    TextView resultText;
    SwitchCompat swIsActive;
    Button btnAdd, btnDisplayAll, btnUpdate;
    ListView customerList;
    dbHelper dbHelper;
    Customer customer;

    Customer customerClicked;
    ArrayAdapter<Customer> customerArrayAdapter;
    List<Customer> allCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextId = findViewById(R.id.editTextId);
        btnAdd = findViewById(R.id.btnAdd);
        btnDisplayAll = findViewById(R.id.btnDisplayAll);
        btnUpdate = findViewById(R.id.btnUpdate);
        swIsActive = findViewById(R.id.activeCustomer);
        customerList = findViewById(R.id.cxList);

        btnAdd.setOnClickListener(this);
        btnDisplayAll.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        customerList.setOnItemLongClickListener(this);
        customerList.setOnItemLongClickListener(this);


        dbHelper = new dbHelper(MainActivity.this);
    }

    @Override
    public void onClick(View view) {
        int btnId = view.getId();
        switch (btnId) {
            case R.id.btnAdd:
                add();
                break;
            case R.id.btnDisplayAll:
                displayAll();
                break;
            case R.id.btnUpdate:
                updateCustomer();
                break;
        }


    }


    private void add() {
        try {
            customer = new Customer(-1, editTextName.getText().toString(), Integer.parseInt(editTextAge.getText().toString()),swIsActive.isChecked());
            Toast.makeText(this, "Successsfully added", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e) {
            Toast.makeText(this, "Empty form", Toast.LENGTH_SHORT).show();
            customer = new Customer(-1, "Error", 0, false);
        }

        boolean success = dbHelper.addCustomer(customer);
        displayAll();
//        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

    }

    private void displayAll() {
        allCustomers = dbHelper.getAll();
        customerArrayAdapter = new ArrayAdapter<Customer>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,allCustomers);
        customerList.setAdapter(customerArrayAdapter);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("You want to delete?");

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close

            customerClicked = allCustomers.get(i);

            dbHelper.deleteCustomer(customerClicked);
            displayAll();
//        //ShowInputDialog();
            Toast.makeText(this, " Customer Deleted "+customerClicked.toString(), Toast.LENGTH_SHORT).show();

        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();

        return false;
    }

    public void updateCustomer() {

        try {
            if( !editTextId.getText().toString().isEmpty() && !editTextName.getText().toString().isEmpty() && !editTextAge.getText().toString().isEmpty()) {
                boolean result = dbHelper.updateCustomer(Integer.parseInt(editTextId.getText().toString()), editTextName.getText().toString(), Integer.parseInt(editTextAge.getText().toString()), swIsActive.isChecked());
                if (result) {
                    editTextId.setText("");
                    editTextName.setText("");
                    editTextAge.setText("");
                Toast.makeText(this, "Updated Successfully ", Toast.LENGTH_SHORT).show();
                    displayAll();
                } else {
                Toast.makeText(this, "No Records Found! ", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Empty feilds!.", Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e) {

            Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show();

        }

    }
}