package com.marckregio.makunatlib.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;
import com.marckregio.makunatlib.BaseActivity;
import com.marckregio.makunatlib.Broadcast;
import com.marckregio.makunatlib.R;
import com.marckregio.makunatlib.inapp.IabBroadcastReceiver;
import com.marckregio.makunatlib.inapp.IabHelper;
import com.marckregio.makunatlib.inapp.IabResult;
import com.marckregio.makunatlib.inapp.InappRequest;
import com.marckregio.makunatlib.inapp.Inventory;
import com.marckregio.makunatlib.inapp.Purchase;
import com.marckregio.makunatlib.inapp.SkuDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by makregio on 22/05/2017.
 */

public abstract class InappActivity extends BaseActivity implements IabBroadcastReceiver.IabBroadcastListener {


    private static final int PURCHASE_REQUEST_CODE = 10001;
    private static final int SUBS_REQUEST_CODE = 10002;
    private static final int RESPONSE_OK = 0;
    private static final String RESPONSE = "response";
    private static final int RESPONSE_OWNED = 7;
    private static final int RESPONSE_DEVERROR = 5;

    public static final String TYPE = "type";
    public static final String JSON = "jsonResult";
    public static final String PRODUCT = "product";
    public static final String SUBS = "subs";
    public static final String SUBS_ORDERID = "orderId";
    public static final String SUBS_PRODUCTID = "productId";
    public static final String SUBS_STATE = "purchaseState";
    public static final String SUBS_AUTO = "autoRenewing";
    public static final String PRODUCT_SKUID = "productId";
    public static final String PRODUCT_SKUPRICE = "price";
    public static final String PRODUCT_SKUDESC = "description";

    public static String SAMPLE = "com.marckregio.apitester.producttest";
    public static String SUBS_WEEKLY = "";
    public static String SUBS_MONTHLY = "";
    public static String SUBS_3MONTHS = "";
    public static String SUBS_6MONTHS = "";
    public static String SUBS_YEARLY = "";
    public static String SUBS_SEASONAL = "";

    IInAppBillingService inAppBillingService;
    public IabHelper inAppHelper;
    public IabBroadcastReceiver inAppBroadcastReceiver;

    private ArrayList<String> skuList = new ArrayList<>();

    ServiceConnection inappServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            inAppBillingService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            inAppBillingService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, inappServiceConnection, Context.BIND_AUTO_CREATE);

    }

    public void initBilling(){
        inAppHelper = new IabHelper(this, getResources().getString(R.string.INAPP_KEY));

        inAppHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) return;
                if (inAppHelper == null) return;

                Log.v("INAPP", "Success");

                IntentFilter filter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(inAppBroadcastReceiver, filter);

            }
        });
    }

    public void initBilling(ArrayList<String> skuList){
        inAppHelper = new IabHelper(this, getResources().getString(R.string.INAPP_KEY));

        this.skuList.addAll(skuList);

        inAppHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) return;
                if (inAppHelper == null) return;

                Log.v("INAPP", "Success");

                IntentFilter filter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(inAppBroadcastReceiver, filter);

                queryInventory();

            }
        });
    }

    public void queryInventory(){
        if (inAppHelper == null) return;

        try {
            inAppHelper.queryInventoryAsync(true, skuList, QueryInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    public void buyItem(String sku){
        if (inAppHelper == null) return;

        try {
            inAppHelper.launchPurchaseFlow(this, sku, PURCHASE_REQUEST_CODE, PurchaseListener, getDeveloperPayload());
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    public void subscribeItem(String sku){
        if (inAppHelper == null) return;

        try {
            inAppHelper.launchSubscriptionPurchaseFlow(this, sku, SUBS_REQUEST_CODE, PurchaseListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    IabHelper.QueryInventoryFinishedListener QueryInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
            if (result.isFailure()) return; // NO PURCHASES

            JSONObject purchaseObject = new JSONObject();
            if (result.getResponse() == RESPONSE_OK) {
                for (String sku : skuList){
                    Purchase purchaseSubs = inv.getPurchase(sku);
                    boolean purchaseProd = inv.hasPurchase(sku);

                    if (purchaseSubs != null){
                        try {
                            purchaseObject.put(TYPE, purchaseSubs.getItemType());
                            purchaseObject.put(JSON, purchaseSubs.getOriginalJson());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (purchaseProd){
                        try {
                            purchaseObject.put(TYPE, PRODUCT);
                            JSONObject prodJson = new JSONObject();
                            prodJson.put(PRODUCT_SKUID, sku);
                            purchaseObject.put(JSON, prodJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    sendInAppBroadcast(true, purchaseObject);
                }


            }

        }
    };

    IabHelper.OnIabPurchaseFinishedListener PurchaseListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            if (result.isFailure()) {
                try {
                    sendInAppBroadcast(false, new JSONObject("{ " + RESPONSE + " : " + result.getResponse() + "}"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }

            JSONObject purchaseJson = new JSONObject();
            try {
                purchaseJson.put(TYPE, info.getItemType());
                purchaseJson.put(JSON, info.getOriginalJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendInAppBroadcast(true, purchaseJson);
        }

    };


    @Override
    public void receivedBroadcast(Context context, Intent intent) {
    }


    private void sendInAppBroadcast(boolean isSuccess, JSONObject jsonObject){

        Intent inappIntent = new Intent(IabBroadcastReceiver.ACTION);
        inappIntent.putExtra(Broadcast.REFRESH_KEY, isSuccess);
        inappIntent.putExtra(Broadcast.JSON_KEY, jsonObject.toString());
        sendBroadcast(inappIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (inAppHelper == null) return;

        if (!inAppHelper.handleActivityResult(requestCode, resultCode, data)){
            super.onActivityResult(requestCode, resultCode, data);
        } else {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (inAppBillingService != null) unbindService(inappServiceConnection);
        if (inAppBroadcastReceiver != null) unregisterReceiver(inAppBroadcastReceiver);
        if (inAppHelper != null){
            inAppHelper.disposeWhenFinished();
            inAppHelper = null;
        }
    }

    private String getDeveloperPayload(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);

        return random.toString();
    }


}
