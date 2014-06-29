package com.zeallat.testbluetoothscan;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		static final int REQUEST_ENABLE_BT = 1001;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter == null) {
			    // Device does not support Bluetooth
			}
			else
			{
				if (!mBluetoothAdapter.isEnabled()) {
				    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				}
			}
			
			
			
			
			return rootView;
		}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
		    // Check which request we're responding to
		    if (requestCode == REQUEST_ENABLE_BT) 
		    {
		        // Make sure the request was successful
		        if (resultCode == RESULT_OK) 
		        {
		        	Toast.makeText(getActivity(), R.string.arlert_bluetooth_enabled, Toast.LENGTH_SHORT).show();
		        	
		            // The user picked a contact.
		            // The Intent's data Uri identifies which contact was selected.

		            // Do something with the contact here (bigger example below)
		        }
		        else
		        {
		        	Toast.makeText(getActivity(), R.string.arlert_bluetooth_disabled, Toast.LENGTH_SHORT).show();
		        	
		        }
		    }
		}
		
		
	}

}
