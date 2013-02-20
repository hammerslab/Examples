package com.hammerslab.examplegps;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements LocationListener {
	private LocationManager mLocationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// GPS
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// 位置情報の更新を受け取るように設定
		// 衛星を使用
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // プロバイダ
				0, // 通知のための最小時間間隔
				0, // 通知のための最小距離間隔
				this); // 位置情報リスナー
		// 携帯基地局も使用
		mLocationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, // プロバイダ
				0, // 通知のための最小時間間隔
				0, // 通知のための最小距離間隔
				this); // 位置情報リスナー
	}

	@Override
	protected void onResume() {
		if (mLocationManager != null) {
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,
					// LocationManager.NETWORK_PROVIDER,
					0, 0, this);
		}

		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(this);
		}

		super.onPause();
	}

	@Override
	public void onLocationChanged(Location location) {
		String s = null;
		s = "Latitude:" + String.valueOf(location.getLatitude()); // 緯度
		s = s + " Longitude:" + String.valueOf(location.getLongitude()); // 経度
		s = s + " Accuracy:" + String.valueOf(location.getAccuracy()); // 精度
		s = s + " Altitude:" + String.valueOf(location.getAltitude()); // 標高
		s = s + " Time:" + String.valueOf(location.getTime()); // 時間
		s = s + " Speed:" + String.valueOf(location.getSpeed()); // 速度
		s = s + " Bearing:" + String.valueOf(location.getBearing()); // 方位
		s = s
				+ " Address:"
				+ this.point2address(location.getLatitude(),
						location.getLongitude());
		Log.d("GPS", s);
	}

	final String tag = "ReverseGeocode";

	// 座標を住所のStringへ変換
	private String point2address(double latitude, double longitude) {

		String string = new String();

		// geocoedrの実体化
		Log.d(tag, "Start point2adress");
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());

		// 端末で選択されているLocale　Locale.getDefault()

		try {
			List<Address> list_address = geocoder.getFromLocation(latitude,
					longitude, 5); // 引数末尾は返す検索結果数

			// ジオコーディングに成功したらStringへ
			if (!list_address.isEmpty()) {

				Address address = list_address.get(0);
				StringBuffer strbuf = new StringBuffer();

				// adressをStringへ
				String buf;
				for (int i = 0; (buf = address.getAddressLine(i)) != null; i++) {
					Log.d(tag, "loop no." + i);
					strbuf.append(i + " " + buf
							+ "\n");
				}

				string = strbuf.toString();

			}

			// 失敗（Listが空だったら）
			else {
				Log.d(tag, "Fail Geocoding");
			}

		} catch (Exception e) {

			string = "Nothing";
		}
		Log.d(tag, string);
		return string;
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case LocationProvider.AVAILABLE:
			Log.d("Status", "AVAILABLE");
			break;
		case LocationProvider.OUT_OF_SERVICE:
			Log.d("Status", "OUT_OF_SERVICE");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Log.d("Status", "TEMPORARILY_UNAVAILABLE");
			break;
		}
	}
}
