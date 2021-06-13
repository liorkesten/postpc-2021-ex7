package huji.postpc.exercises.rachelsorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

public class ReadyOrderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        DataBase db = DataBase.getInstance();
        String myOrderId = sp.getString("myOrderID", "Order Didn't found");
        LiveData<Order> liveDataOfOrder = db.getLiveDataOfOrder(myOrderId);
        Order myOrder = liveDataOfOrder.getValue();

        Button button = findViewById(R.id.giveMeMySandwichButton);
        button.setOnClickListener(v -> {
            if (myOrder != null) {
                myOrder.setStatus("done");
                db.editOrder(myOrder);
                Intent intent = new Intent(this, NewOrderActivity.class);
                sp.edit().remove("myOrderID").apply();
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, NewOrderActivity.class);
                sp.edit().remove("myOrderID").apply();
                startActivity(intent);
            }
        });
    }
}
