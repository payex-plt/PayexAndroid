package io.payex.android;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.HashMap;

import io.payex.android.util.ConnectivityReceiver;
import io.payex.android.util.PayexAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vince on 11/28/2016.
 */

public class MyApp extends Application {
    private static final String TAG = MyApp.class.getSimpleName();

    private static MyApp mInstance;

    static Bank[] banks;
    static Bank bank;

    static String bin;
    static String mid;
    static String merchant;

    public static String getMID() {
        return mid;
    }

    public static void setMID(String mid) {
        MyApp.mid = mid;
    }

    public static String getBIN() {
        return bin;
    }

    public static void setBIN(String bin) {
        MyApp.bin = bin;
    }

    private static String currency = "RM";

    public static String getCurrency() {
        return currency;
    }

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://payexterminals.payex.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    static public PayexAPI payexAPI = retrofit.create(PayexAPI.class);

    public HashMap<String, Merchant> merchants = new HashMap<String, Merchant>();

    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        banks = new Bank[] {
                new Bank("1111", "Chase Manhattan Bank", "enquiry@chase.com", "+1-800-555-7134", "+1-800-555-9876"),
                new Bank("2222", "Maybank", "maybank@gmail.com", "+2222222221", "+2222222222"),
                new Bank("3333", "CIMB Bank", "cimb@gmail.com", "+3333333331", "+3333333332"),
                new Bank("429313", "AmBank (M) Berhad", "info@ambankgroup.com.my", "(+60)3 2132 6532 (Helpdesk)", "(+60)2-5653 0964 (Auth Centre)"),
                new Bank("5555", "Hong Leong Bank Berhad", "support@hlbb.hongleong.com.my", "03-2773 0100 (Office Hours)", "03-7956 8166 (24 Hours)")
        };


        bank = banks[0];
        Log.d(TAG, "default bank -> " + bank.getName());

        merchants.put("TEST00000930362", new Merchant("TEST00000930362", "void", "payex456", "77BEA9B8", ""));
        merchants.put("TEST00000936302", new Merchant("TEST00000936302", "void", "payex456", "3842528D", ""));

//        merchants.put("00000930362", new Merchant("00000930362", "void", "payex123", "02BD15CD", ""));
//        merchants.put("00000936302", new Merchant("00000936302", "void", "payex123", "50267BDF", ""));

//        // GET DEVICE ID
//        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//
//        // GET IMEI NUMBER
//        TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceIMEI = tManager.getDeviceId();
//
//        Log.d(TAG, "device id -> " + deviceId);
//        Log.d(TAG, "device imei -> " + deviceIMEI);
    }

    public static Bank findBank(String bin) {
        Bank bank = null;

        for(int i = 0; i < banks.length; i++) {
            if (banks[i].getBin().equals(bin)) {
                bank = banks[i];
                break;
            }
        }

        return bank;
    }

    public static Bank getBank() {
        return bank;
    }
    public static void setBank(Bank bank) {
        MyApp.bank = bank;
    }

}
