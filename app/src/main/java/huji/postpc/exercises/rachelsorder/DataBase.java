package huji.postpc.exercises.rachelsorder;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    private static DataBase singleton;
    private FirebaseFirestore fireStore;
    private final Map<String, Order> orders = new HashMap<>();
    private final MutableLiveData<List<Order>> mutableLiveData = new MutableLiveData<>();

    private DataBase() {
        mutableLiveData.setValue(new ArrayList<>());
        fireStore = FirebaseFirestore.getInstance();
        // Add listener!
        fireStore.collection("orders").addSnapshotListener((value, err) -> {
            if (err != null) {
                Log.e(DataBase.class.toString(), "Fail to extacrt data base. err: " + err);
            } else if (value == null) {
                //TODO Delete everything
            } else {
                List<DocumentSnapshot> documentSnapshots = value.getDocuments();
                orders.clear();
                documentSnapshots.forEach(doc -> orders.put(doc.getId(), doc.toObject(Order.class)));
                mutableLiveData.setValue(new ArrayList<>(orders.values()));
                Log.d(DataBase.class.toString(), "All orders extracted successfully");
            }
        });
    }

    public LiveData<List<Order>> getLiveDataOrders() {
        return mutableLiveData;
    }

    public LiveData<Order> getLiveDataOfOrder(String orderID) {
//        if (!orders.containsKey(orderID)) {
//            Log.d(DataBase.class.toString(), String.format("User asked for order (%s)that is not existed in the db.", orderID));
//            return null;
//        }
        MutableLiveData<Order> mutableLiveDataOfOrder = new MutableLiveData<>(orders.get(orderID));
        fireStore.collection("orders").document(orderID).addSnapshotListener((value, err) -> {
            if (err != null) {
                Log.e(DataBase.class.toString(), "Fail to extract " + orderID + " Order from data base due to the err: " + err);
            } else if (value == null) {
                //TODO Delete this .....
            } else {
                Order newOrder = value.toObject(Order.class);
                if (newOrder == null) {
                    //TODO
                    Log.e(DataBase.class.toString(), "Fail to extract " + orderID);
                }
                mutableLiveDataOfOrder.setValue(newOrder);
                Log.d(DataBase.class.toString(), "Order extracted successfully");
            }
        });
        return mutableLiveDataOfOrder;
    }

    public Task<DocumentSnapshot> getOrder(String orderID) {
        return fireStore.collection("orders").document(orderID).get();
//        MutableLiveData<Order> mutableLiveDataOfOrder = new MutableLiveData<>(orders.get(orderID));
//        .addOnSuccessListener(value -> {
//            if (value == null) {
//                //TODO Delete this .....
//                return;
//            }
//            Order newOrder = value.toObject(Order.class);
//            if (newOrder == null) {
//                //TODO
//                Log.e(DataBase.class.toString(), "Fail to extract " + orderID);
//                return;
//            }
//            mutableLiveDataOfOrder.setValue(newOrder);
//            Log.d(DataBase.class.toString(), "Order extracted successfully");
//        });
//        return mutableLiveDataOfOrder;
    }

    public void deleteOrder(String orderID) {
        if (!orders.containsKey(orderID)) {
            Log.d(DataBase.class.toString(), String.format("Order: %s is not exist", orderID));
        }
        orders.remove(orderID);
        fireStore.collection("orders").document(orderID).delete();
    }

    public void addOrder(Order order) {
        if (order == null) {
            Log.d(DataBase.class.toString(), "Can't add null order");
        }
        orders.put(order.getId(), order);
        fireStore.collection("orders").document(order.getId()).set(order);
    }

    public void editOrder(Order editedOrder) {
        if (editedOrder.getStatus().equals("done")) {
            deleteOrder(editedOrder.getId());
            return;
        }
        addOrder(editedOrder);
    }

    public static DataBase getInstance() {
        if (singleton == null) {
            singleton = new DataBase();
        }
        return singleton;
    }
}
