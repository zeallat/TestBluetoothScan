package com.zeallat.testbluetoothscan;

import java.util.Set;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
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
		TextView textview_pairedDevice_list;
		TextView textview_discoverDevice_list;
		
		
		Button button_paired_device_query;
		Button button_discover_new_device;
		
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			textview_pairedDevice_list = (TextView)rootView.findViewById(R.id.textview_paired_device_list_data);
			textview_discoverDevice_list = (TextView)rootView.findViewById(R.id.textview_discover_device_list_data);
			
			
			button_discover_new_device = (Button)rootView.findViewById(R.id.button_discovery_new_device);
			button_paired_device_query = (Button)rootView.findViewById(R.id.button_paired_device_query);
			
			
			//ArrayAdapter<BluetoothDevice> mArrayAdapter = new ArrayAdapter<BluetoothDevice>(getActivity(), 1);
			
			
			//블루투스 어답터 생성
			final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			
			if (mBluetoothAdapter == null) {
			    // Device does not support Bluetooth
			}
			else
			{
				//디바이스가 블루투스 지원할경우, 블루투스의 활성화 여부에따라 블루투스 활성화 알림창 띄우기
				if (!mBluetoothAdapter.isEnabled()) {
				    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				}
			}
			
			
			
			
			
			button_paired_device_query.setOnClickListener(
					new View.OnClickListener()
					{
						
						@Override
						public void onClick(View v) 
						{
							// TODO Auto-generated method stub
							
							//페어링 된 디바이스 쿼리
							
							resetTextviewPairedList();
							Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
							
							//if there are paired devices
							if(pairedDevices.size() > 0)
							{
								//Loop through paired devices
								for(BluetoothDevice tempDevice : pairedDevices)
								{
									//Add the name and address to an arrayadapter
									
									textview_pairedDevice_list.append("Name : "+tempDevice.getName()+"\n");
									textview_pairedDevice_list.append("Address : "+tempDevice.getAddress()+"\n");
									textview_pairedDevice_list.append("Type : "+tempDevice.getType()+"\n");
									textview_pairedDevice_list.append("BondState : "+tempDevice.getBondState()+"\n");
									textview_pairedDevice_list.append("Uuids : "+tempDevice.getUuids().toString()+"\n\n");
									
								}
							}
						}
					});
			
			
			
			button_discover_new_device.setOnClickListener(
					new View.OnClickListener()
					{
						
						@Override
						public void onClick(View v) 
						{
							// TODO Auto-generated method stub
							
							resetTextviewDiscoverList();
							
							mBluetoothAdapter.startDiscovery();
							
							
						}
					});
			
			final BroadcastReceiver mReceiver = new BroadcastReceiver()
			{

				@Override
				public void onReceive(Context context,
						Intent intent) 
				{
					// TODO Auto-generated method stub
					String action = intent.getAction();
					
					//When discovery finds a device
					if(BluetoothDevice.ACTION_FOUND.equals(action))
					{
						/*
						 * 블루투스 기기를 찾아내었을때
						 */
						//Get the BluetoothDevice object from the Intent
						BluetoothDevice tempDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						
						//Add the name and address to list
						textview_discoverDevice_list.append("Name : "+tempDevice.getName()+"\n");
						textview_discoverDevice_list.append("Address : "+tempDevice.getAddress()+"\n");
						textview_discoverDevice_list.append("Type : "+tempDevice.getType()+"\n");
						textview_discoverDevice_list.append("BondState : "+tempDevice.getBondState()+"\n");
						//textview_discoverDevice_list.append("Uuids : "+tempDevice.getUuids().toString()+"\n\n");
						
						mBluetoothAdapter.cancelDiscovery();
					}
					else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
					{
						/*
						 * 블루투스 기기 검색작업이 종료되었을때
						 */
						
					}
				}
				
			};
			
			
			//인텐트 필터 생성후 리시버를 등록한다.
			IntentFilter deviceFoundfilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			IntentFilter discoveryFinishedFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			
			
			getActivity().registerReceiver(mReceiver, deviceFoundfilter);
			getActivity().registerReceiver(mReceiver, discoveryFinishedFilter);
			
			
			
			
			
			return rootView;
		}
		
		
		public void resetTextviewPairedList()
		{
			textview_pairedDevice_list.setText("");
		}
		
		public void resetTextviewDiscoverList()
		{
			//textview_discoverDevice_list.setText("");
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
		        else if(resultCode == RESULT_CANCELED)
		        {
		        	Toast.makeText(getActivity(), R.string.arlert_bluetooth_disabled, Toast.LENGTH_SHORT).show();
		        	
		        }
		    }
		}
		
		
	}

}
