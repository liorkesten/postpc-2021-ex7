package huji.postpc.exercises.rachelsorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.google.android.material.slider.Slider;

public class NewOrderActivity extends AppCompatActivity {
    DataBase _db;
    SharedPreferences _sp;
    LiveData<Order> _orderLiveData;
    Order _myOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        _db = DataBase.getInstance();
        _sp = PreferenceManager.getDefaultSharedPreferences(this);
        ImageButton addNewOrderButton = findViewById(R.id.addNewOrderButton);
        CheckBox humus = findViewById(R.id.humusCheckBox);
        CheckBox tahini = findViewById(R.id.tahiniCheckBox);
        Slider pickles = findViewById(R.id.picklesSlider);
        EditText customerName = findViewById(R.id.customerNameEditText);
        EditText comment = findViewById(R.id.commentEditText);
        _myOrder = new Order();
        _orderLiveData = _db.getLiveDataOfOrder(_myOrder.getId());
        _orderLiveData.observe(this, order -> {
            if (order == null) {
                Log.d("NewOrderActivity", "onCreate: order deleted from db.");
                //TODO Handle
            } else {
                Log.d("NewOrderActivity", String.format("onCreate: order %s fetched from live data.", order.getId()));
                _sp.edit().putString("myOrderID", order.getId()).apply();
                Intent intent = new Intent(this, WaitingOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        customerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addNewOrderButton.setEnabled(s.toString().trim().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addNewOrderButton.setEnabled(false);
        addNewOrderButton.setOnClickListener(v -> {
            Log.d("NewOrderActivity", "AddNewOrder Button was clicked");
            _myOrder.setTahini(tahini.isChecked());
            _myOrder.setHummus(humus.isChecked());
            _myOrder.setCustomerName(customerName.getText().toString());
            _myOrder.setComment(comment.getText().toString());
            _myOrder.setPickles((int) pickles.getValue());
            _myOrder.setStatus(OrderStatus.WAITING.toString());
            _db.addOrder(_myOrder);
            addNewOrderButton.setEnabled(false);
        });
    }
}
