package slotgames.shihzamanapp.com;
import android.app.Application;





public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();



    private static MyApplication mInstance;

    private double mGlobalVarValue;

    public double getGlobalVarValue() {
        return mGlobalVarValue;
    }

    public void setGlobalVarValue(double str) {
        mGlobalVarValue = str;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }




}