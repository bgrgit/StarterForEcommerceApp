package com.cashkarodemoapp.utillities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkCheck {

	private Context myContext;

	public NetworkCheck(Context context) {
		this.myContext = context;
	}

	public boolean isConnectingToInternet() {
		boolean status = false;
		try {
			NetworkInfo aWIFI, aMobile;
			ConnectivityManager myConnectivityManager = (ConnectivityManager) myContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			myConnectivityManager = (ConnectivityManager) myContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			aWIFI = myConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			aMobile = myConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            status = aWIFI.isConnected() || aMobile.isConnected();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return status;
	}

}
