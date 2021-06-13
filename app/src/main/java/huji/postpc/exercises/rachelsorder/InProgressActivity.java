package huji.postpc.exercises.rachelsorder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

public class InProgressActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_progress);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        DataBase db = DataBase.getInstance();
        String myOrderId = sp.getString("myOrderID", "Order Didn't found");
        LiveData<Order> liveDataOfOrder = db.getLiveDataOfOrder(myOrderId);

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
                        moveToWaitingActivity();
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
