package huji.postpc.exercises.rachelsorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.google.android.material.slider.Slider;

public class WaitingOrderActivity extends AppCompatActivity {

    private ImageButton _deleteButton;
    private ImageButton _editButton;
    private ImageButton _revertButton;
    private ImageButton _saveChangesButton;
    private CheckBox _humus;
    private CheckBox _tahini;
    private Slider _pickles;
    private EditText _customerName;
    private EditText _comment;
    private DataBase _db;
    private Order _myOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        _db = DataBase.getInstance();
        String myOrderId = sp.getString("myOrderID", "Order Didn't found");
        LiveData<Order> liveDataOfOrder = _db.getLiveDataOfOrder(myOrderId);
        _myOrder = liveDataOfOrder.getValue();

        liveDataOfOrder.observe(this, order -> {
            if (order == null) {
                Log.d("WaitingOrderActivity", "onCreate: order deleted from db.");
                Intent intent = new Intent(this, NewOrderActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.d("WaitingOrderActivity", String.format("onCreate: order %s fetched from live data.", order.getId()));
                switch (order.getStatus()) {
                    case "waiting":
                        _myOrder = order;
                        updateUIToDataOfMyOrder();
                        break;
                    case "in-progress":
                        moveToInProgressActivity();
                        break;
                    case "ready":
                        moveToReadyActivity();
                        break;
                    case "done":
                        moveToDoneActivity();
                        break;
                }
            }
        });

        initViewsAndButtons();
        changeMode(false);
        if (_myOrder != null) {
            updateUIToDataOfMyOrder();
        }
        setUpListeners(myOrderId);
    }

    private void setUpListeners(String myOrderId) {
        _editButton.setOnClickListener(v -> {
            changeMode(true);
        });

        _deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Order")
                    .setMessage("Are you sure you want to delete this order?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Continue with delete operation
                        _db.deleteOrder(myOrderId);
                        Log.d("WaitingOrderActivity", "Delete Button - Delete item was clicked");
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        _revertButton.setOnClickListener(v -> {
            Log.d("WaitingOrderActivity", "Revert Button was clicked");
            if (_myOrder == null) {
                Log.e("WaitingOrderActivity", "Revert - my order is null??");
                return;
            }
            updateUIToDataOfMyOrder();
            changeMode(false);
        });

        _saveChangesButton.setOnClickListener(v -> {
            Log.d("WaitingOrderActivity", "Save Button was clicked");
            if (_myOrder == null) {
                Log.e("WaitingOrderActivity", "Save - my order is null??");
                return;
            }
            updateUiToDataOfViews();
            changeMode(false);
        });
    }

    private void initViewsAndButtons() {
        _deleteButton = findViewById(R.id.deleteOrderButton);
        _editButton = findViewById(R.id.editOrderButton);
        _revertButton = findViewById(R.id.revertChangesButton);
        _saveChangesButton = findViewById(R.id.saveChangesButton);
        _humus = findViewById(R.id.humusCheckBox);
        _tahini = findViewById(R.id.tahiniCheckBox);
        _pickles = findViewById(R.id.picklesSlider);
        _customerName = findViewById(R.id.customerNameEditText);
        _comment = findViewById(R.id.commentEditText);

        _customerName.setEnabled(false);
    }

    private void updateUiToDataOfViews() {
        _myOrder.setTahini(_tahini.isChecked());
        _myOrder.setHummus(_humus.isChecked());
        _myOrder.setCustomerName(_customerName.getText().toString());
        _myOrder.setComment(_comment.getText().toString());
        _myOrder.setPickles((int) _pickles.getValue());
        _myOrder.setStatus(OrderStatus.WAITING.toString());
        _db.editOrder(_myOrder);
    }

    private void updateUIToDataOfMyOrder() {
        _humus.setChecked(_myOrder.isHummus());
        _tahini.setChecked(_myOrder.isTahini());
        _pickles.setValue(_myOrder.getPickles());
        _customerName.setText(_myOrder.getCustomerName());
        _comment.setText(_myOrder.getComment());
    }

    private void changeMode(boolean changeToEditMode) {
        int firstButtons = (changeToEditMode) ? View.INVISIBLE : View.VISIBLE;
        int secondButtons = (changeToEditMode) ? View.VISIBLE : View.INVISIBLE;
        _deleteButton.setVisibility(firstButtons);
        _editButton.setVisibility(firstButtons);
        _revertButton.setVisibility(secondButtons);
        _saveChangesButton.setVisibility(secondButtons);
        _humus.setEnabled(changeToEditMode);
        _tahini.setEnabled(changeToEditMode);
        _pickles.setEnabled(changeToEditMode);
        _comment.setEnabled(changeToEditMode);
    }


    private void moveToNewOrderActivity() {
        Intent intent = new Intent(this, NewOrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToDoneActivity() {
        moveToNewOrderActivity();
    }

    private void moveToReadyActivity() {
        Intent intent = new Intent(this, ReadyOrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToInProgressActivity() {
        Intent intent = new Intent(this, InProgressActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToWaitingActivity() {
        Intent intent = new Intent(this, WaitingOrderActivity.class);
        startActivity(intent);
        finish();
    }
}
