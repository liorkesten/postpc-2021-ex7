package huji.postpc.exercises.rachelsorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String DEFAULT_ORDER_ID = "No-order-for-me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RachelSandwichApp app = RachelSandwichApp.getInstance();
        DataBase db = DataBase.getInstance();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String myOrderID = sp.getString("myOrderID", DEFAULT_ORDER_ID);
        if (myOrderID.equals(DEFAULT_ORDER_ID)) {
            moveToNewOrderActivity();
            return;
        }
        Task<DocumentSnapshot> documentSnapshotTask = db.getOrder(myOrderID);
        documentSnapshotTask.addOnSuccessListener(doc -> {
            if (doc == null) {
                moveToNewOrderActivity();
                return;
            }
            Order myOrder = doc.toObject(Order.class);
            if (myOrder == null) {
                moveToNewOrderActivity();
                return;
            }
            switch (myOrder.getStatus()) {
                case "waiting":
                    moveToWaitingActivity();
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
                default:
                    moveToNewOrderActivity();
                    break;
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