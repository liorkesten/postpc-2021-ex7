package huji.postpc.exercises.rachelsorder;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class RachelSandwichApp extends Application {
    private static RachelSandwichApp singleton;

    public static RachelSandwichApp getInstance(){
        if (singleton == null){
            singleton = new RachelSandwichApp();
        }
        return singleton;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(RachelSandwichApp.getInstance());

    }
}
