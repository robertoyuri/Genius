package br.com.genius;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

public class GeniusActivity extends Activity {

	private Button button_green;
	private Button button_yellow;
	private Button button_red;
	private Button button_blue;
	private Button button_send;
	private static final String DEVICE_ADDRESS = "00:11:09:01:05:96";
	private ArduinoReceiver arduinoReceiver = new ArduinoReceiver();
	private String result = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		button_green = (Button) findViewById(R.id.button_green);
		button_yellow = (Button) findViewById(R.id.button_yellow);
		button_red = (Button) findViewById(R.id.button_red);
		button_blue = (Button) findViewById(R.id.button_blue);
		button_send = (Button) findViewById(R.id.button_send);

	}

	@Override
	protected void onStart() {
		super.onStart();

		registerReceiver(arduinoReceiver, new IntentFilter(
				AmarinoIntent.ACTION_RECEIVED));
		Amarino.connect(this, DEVICE_ADDRESS);

		// ClickListener green button
		button_green.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Amarino.sendDataToArduino(GeniusActivity.this, DEVICE_ADDRESS,
						'A', 3);
				
				result += '3';

			}
		});

		// ClickListener yellow button
		button_yellow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Amarino.sendDataToArduino(GeniusActivity.this, DEVICE_ADDRESS,
						'A', 2);
				result += '2';

			}
		});

		// ClickListener red button
		button_red.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Amarino.sendDataToArduino(GeniusActivity.this, DEVICE_ADDRESS,
						'A', 1);
				
				result += '1';

			}
		});

		// ClickListener blue button
		button_blue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Amarino.sendDataToArduino(GeniusActivity.this, DEVICE_ADDRESS,
						'A', 0);
				
				result += '0';

			}
		});

		button_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Amarino.sendDataToArduino(GeniusActivity.this, DEVICE_ADDRESS,
						'B', result);

				result = "";

			}
		});

	}

	public class ArduinoReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String data = null;

			// the device address from which the data was sent, we don't need it
			// here but to demonstrate how you retrieve it
			final String address = intent
					.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);

			// the type of data which is added to the intent
			final int dataType = intent.getIntExtra(
					AmarinoIntent.EXTRA_DATA_TYPE, -1);

			// we only expect String data though, but it is better to check if
			// really string was sent
			// later Amarino will support differnt data types, so far data comes
			// always as string and
			// you have to parse the data to the type you have sent from
			// Arduino, like it is shown below
			if (dataType == AmarinoIntent.STRING_EXTRA) {
				data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);

				if (data != null) {
					try {
						// tvTextoArduino.setText(data);
						Toast.makeText(GeniusActivity.this, data,
								Toast.LENGTH_SHORT).show();
					} catch (NumberFormatException e) { /*
														 * oh data was not an
														 * integer
														 */
					}
				}
			}
		}
	}

}
