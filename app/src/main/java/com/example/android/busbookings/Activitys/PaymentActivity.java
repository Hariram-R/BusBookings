package com.example.android.busbookings.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.android.busbookings.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener{

    static String TAG = PaymentActivity.class.getSimpleName();

    String email,desc,key;
    int amt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        email = getIntent().getStringExtra("Email");
        key = getIntent().getStringExtra("BookingKey");
        amt = getIntent().getIntExtra("Amount",0) * 100;

        startPayment();
    }

    public void startPayment() {
        /*
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        /*
         * Set your logo here
         */
        checkout.setImage(R.mipmap.ic_launcher);
        /*
         * Reference to current activity
         */
        final Activity activity = this;
        /*
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            /*
             * Merchant Name
             * eg: Rentomojo || HasGeek etc.
             */
            options.put("name", "Bus Booking");
            /*
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Bus ticket");
            options.put("currency", "INR");

            /*
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            options.put("amount", amt);

            checkout.open(activity, options);
        }
        catch(Exception e)
        {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Intent launchMain = new Intent(PaymentActivity.this,MainActivity.class);
        launchMain.putExtra("Email",email);
        launchMain.putExtra("BookingKey",key);
        startActivity(launchMain);
        finish();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this,"Payment failed!",Toast.LENGTH_SHORT).show();
        Intent launchMain = new Intent(PaymentActivity.this,MainActivity.class);
        launchMain.putExtra("Email",email);
        launchMain.putExtra("BookingKey",key);
        startActivity(launchMain);
        finish();
    }
}
