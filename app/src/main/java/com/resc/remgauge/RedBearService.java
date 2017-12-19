package com.resc.remgauge;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RedBearService extends Service {

	//static final String TAG = RedBearService.class.getName();
    static final String TAG = "REDBEAR";
	SensorCorrect sensorCorrect = null;
	public static final UUID RBL_SERVICE = UUID
			.fromString("713D0000-503E-4C75-BA94-3148F18D941E");

	public static final UUID RBL_DEVICE_RX_UUID = UUID
			.fromString("713D0002-503E-4C75-BA94-3148F18D941E");

	public static final UUID RBL_DEVICE_TX_UUID = UUID
			.fromString("713D0003-503E-4C75-BA94-3148F18D941E");

	public static final UUID CCC = UUID
			.fromString("00002902-0000-1000-8000-00805f9b34fb");

	public static final UUID SERIAL_NUMBER_STRING = UUID
			.fromString("00002A25-0000-1000-8000-00805f9b34fb");

    private static HashMap<String, String> attributes = new HashMap<String, String>();
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String BLE_SHIELD_TX = "713d0003-503e-4c75-ba94-3148f18d941e";
    public static String BLE_SHIELD_RX = "713d0002-503e-4c75-ba94-3148f18d941e";
    public static String BLE_SHIELD_SERVICE = "713d0000-503e-4c75-ba94-3148f18d941e";

    public final static UUID UUID_BLE_SHIELD_TX = UUID
            .fromString(BLE_SHIELD_TX);
    public final static UUID UUID_BLE_SHIELD_RX = UUID
            .fromString(BLE_SHIELD_RX);
    public final static UUID UUID_BLE_SHIELD_SERVICE = UUID
            .fromString(BLE_SHIELD_SERVICE);
	private BluetoothAdapter mBtAdapter = null;

	public BluetoothGatt mBluetoothGatt = null;
    private BluetoothDevice ourDevice = null;
	private IRedBearServiceEventListener mIRedBearServiceEventListener;

	HashMap<String, BluetoothDevice> mDevices = null;

	private BluetoothGattCharacteristic txCharc = null;
    BluetoothGattService rblService;
    Consumer gConsumer = null;
    public BluetoothGattCharacteristic charRX = null;
    int myAfr = 88;
	public void startScanDevice() {
		if (mDevices != null) {
			mDevices.clear();
		} else {
			mDevices = new HashMap<String, BluetoothDevice>();
		}

		startScanDevices();
	}

	public void stopScanDevice() {
		stopScanDevices();
	}

	public void setListener(IRedBearServiceEventListener mListener) {
		mIRedBearServiceEventListener = mListener;
	}

	public boolean isBLEDevice(String address) {
		BluetoothDevice mBluetoothDevice = mDevices.get(address);
		if (mBluetoothDevice != null) {
			return isBLEDevice(address);
		}
		return false;
	}

	public void connectDevice(String address, boolean autoconnect) {
		BluetoothDevice mBluetoothDevice = mDevices.get(address);
		if (mBluetoothDevice != null) {
			connect(mBluetoothDevice, autoconnect);
		}

	}

	public void disconnectDevice(String address) {
		BluetoothDevice mBluetoothDevice = mDevices.get(address);
		if (mBluetoothDevice != null) {
			disconnect(mBluetoothDevice);
		}
	}

	public void readRssi(String deviceAddress) {
		readDeviceRssi(deviceAddress);
	}

	public void writeValue(String deviceAddress, char[] data) {
		if (txCharc != null) {
			String value = new String(data);

			if (txCharc.setValue(value)) {
                if ( mBluetoothGatt != null ) {
				if (!mBluetoothGatt.writeCharacteristic(txCharc)) {
					Log.e(TAG, "Error: writeCharacteristic!");
				}
			} else {
				Log.e(TAG, "Error: setValue!");
			}
            }
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public class LocalBinder extends Binder {
		public RedBearService getService() {
			return RedBearService.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public void onCreate() {
		super.onCreate();
        Log.d(TAG, "onCreate");
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBtAdapter = bluetoothManager.getAdapter();

		if (mBtAdapter == null)
			return;

		if (mDevices == null) {
			mDevices = new HashMap<String, BluetoothDevice>();
		}
		Log.d(TAG, "onCreate");
        startScanDevices();


        gConsumer = new Consumer();
		sensorCorrect = new SensorCorrect();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d(TAG, "onStartCommand");
		return 0;
	}
	public boolean isBLEDevice(BluetoothDevice device) {
		if (mBluetoothGatt != null) {
			return true;
		} else {
			return false;
		}
	}

	private void startScanDevices() {
		if (mBtAdapter == null)
			return;

		mBtAdapter.startLeScan(mLeScanCallback);
	}

	protected void stopScanDevices() {
		if (mBtAdapter == null)
			return;

		mBtAdapter.stopLeScan(mLeScanCallback);
	}

	protected void readDeviceRssi(String address) {
		BluetoothDevice mDevice = mDevices.get(address);
		if (mDevice != null) {
			readDeviceRssi(mDevice);
		}
	}

	protected void readDeviceRssi(BluetoothDevice device) {
		if (mBluetoothGatt != null) {
			mBluetoothGatt.readRemoteRssi();
		}
	}

    protected void readDeviceChar(BluetoothDevice device) {
        //mBluetoothGatt.
    }
	protected void connect(BluetoothDevice device, boolean autoconnect) {
		mBluetoothGatt = device.connectGatt(this, autoconnect, mGattCallback);
	}

	protected void disconnect(BluetoothDevice device) {

		mBluetoothGatt.disconnect();
		mBluetoothGatt.close();
	}

    void readRR() {
       // if ( charRX != null ) {
           // BluetoothGattDescriptor desc = charRX.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
           //mBluetoothGatt.readDescriptor(desc);
          char buf[] = { 'V' };
          writeValue(ourDevice.getAddress(),buf);
           // mBluetoothGatt.readCharacteristic(
           //if (mBluetoothGatt.readCharacteristic(charRX) ) {
                //Log.d(TAG,"readCHA OKOKOKOKOKOK");
          // }

           // if ( mBluetoothGatt.readDescriptor() ) {
               // Log.d(TAG,"CHAR RX ok");
           // }
       // }
    }
	@Override
	public void onDestroy() {

        Log.d(TAG,"READBEAR onDestroy()");
		if (mBluetoothGatt == null)
			return;
        ;
		mBluetoothGatt.close();
		mBluetoothGatt = null;

		super.onDestroy();
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback;

    {
        mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi,
                                 byte[] scanRecord) {
                Log.d(TAG, "onScanResult (device : " + device.getName() + ")");
                String name = device.getName();
                if (name != null && name.matches("BlendMicro") || name != null && name.matches("Resc")) {
                    Log.d(TAG, "found blen device... connecting...");
                    connect(device, true);

                    ourDevice = device;
                    stopScanDevices();
                    //SysUtils.sleepMs(10);
                    for ( int i = 0; i < 10; i++ ) {
                    //    readDeviceRssi(device);
                    }
                   // connect(device, false);
                    ParcelUuid[] puid = device.getUuids();
                   if ( puid != null && puid.length > 0 ) {
                        Log.d(TAG,"Got uuids for device " + device.getAddress());
                    }

                    //BluetoothGattCharacteristic cc = rblService2.getCharacteristic(RBL_DEVICE_RX_UUID);


                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                           // device.readCharacteristic(characteristicRx);
                          //  mBluetoothGatt.readCharacteristic(characteristicRx);
                            while (true) {
                                readDeviceRssi(device);

                                SysUtils.sleepMs(500);
                              readRR() ;
                                //myAfr = myAfr + 10;
                                //gConsumer.setAfr();
                                //mBluetoothGatt.readCharacteristic(cc);

                            }
                        }
                    };
                    new Thread(runnable).start();
                  //  addDevice(device);

                }
                /*
                if (mIRedBearServiceEventListener != null) {
                    Log.d(TAG, "mIScanDeviceListener (device : " + device.getName()
                            + ")");
                    addDevice(device);
                    mIRedBearServiceEventListener.onDeviceFound(
                            device.getAddress(), device.getName(), rssi,
                            device.getBondState(), scanRecord, device.getUuids());
                }
                */
            }
        };
    }

	private float c2f(float c) {
		return (c *9.0f / 5.0f) + 32.0f;
	}

	private float convertToFahr(int low, int high) {
		int v = (high << 8) + low;
		Log.d("RED","raw = " + v);
		gConsumer.setAnalogChan(v,0);
		float tmpv = (((v * 5.0f) / 1024.0f) - 0.50f) *100.0f;
		float tempF = (tmpv * 9.0f / 5.0f) + 32.0f;

		return tempF;
	}

	private float convertToVolt(int low, int high ) {
		int v = (high << 8) + low;
		//R2 = (1 / (vIn/vOut) -1) * R1
		float vin = 4.95f;
		float countsPerVolt = 1024.0f / vin ;
		float vout = v / countsPerVolt;
		float r2 = (1.0f / ((vin/vout) - 1.0f)) * 683.0f;
		//return vout;
		float temp = sensorCorrect.convert(r2);
		Log.v("RED","v:" + v + " vout:" + vout + " r2:" + r2 + "TEMP: " + temp);
		return r2;
	}

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {

			Log.d(TAG, "onCharacteristicChanged ( characteristic : "
					+ characteristic + ")");

			int i = 0;
			Integer temp = characteristic.getIntValue(
					BluetoothGattCharacteristic.FORMAT_UINT8, i++);
			ArrayList<Integer> values = new ArrayList<Integer>();
			while (temp != null) {
			//	Log.e(TAG, "temp: " + temp);
				values.add(temp);
				temp = characteristic.getIntValue(
						BluetoothGattCharacteristic.FORMAT_UINT8, i++);
			}

			int[] received = new int[i];
			i = 0;
			for (Integer integer : values) {
				received[i++] = integer.intValue();
			}

			//if (mIRedBearServiceEventListener != null) {

				//mIRedBearServiceEventListener.onDeviceReadValue(received);
			//}

			float fahren = convertToFahr(received[1],received[2]);
			float v = convertToVolt(received[1],received[2]);
			float tempC = sensorCorrect.convert(v);
			gConsumer.setOt(c2f(tempC));
			//DataLogger.appendLog(c2f(tempC) + "," + v);
			try {
				DataLogger.appendLogMax(" , " + c2f(tempC)+ " , " + v + " , " + Consumer.getMph());
			} catch (IOException e) {
				e.printStackTrace();
			}
			//gConsumer.setMph(received[1]);
            gConsumer.setAmb(v);
		}


		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt,characteristic,status);
            Log.d(TAG, "onCharRead");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.d(TAG, "onCharacteristicRead ( characteristic :"
                        + characteristic + " ,status, : " + status + ")");
			}
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(TAG, "onCharWrite");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.d(TAG, "onCharacteristicWrite ( characteristic :"
						+ characteristic + " ,status : " + status + ")");
			}
		};


		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			BluetoothDevice device = gatt.getDevice();

			Log.d(TAG, "onConnectionStateChange (device : " + device
					+ ", status : " + status + " , newState :  " + newState
					+ ")");

			if (mIRedBearServiceEventListener != null) {
				mIRedBearServiceEventListener.onDeviceConnectStateChange(
						device.getAddress(), newState);
			}

			if (newState == BluetoothProfile.STATE_CONNECTED) {
				mBluetoothGatt.discoverServices();
				readDeviceRssi(device);
			}
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt,
				BluetoothGattDescriptor device, int status) {
			Log.d(TAG, "onDescriptorRead (device : " + device + " , status :  "
					+ status + ")");
			super.onDescriptorRead(gatt, device, status);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor arg0, int status) {
			Log.d(TAG, "onDescriptorWrite (arg0 : " + arg0 + " , status :  "
					+ status + ")");
			super.onDescriptorWrite(gatt, arg0, status);
		}

		@Override
		public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
			Log.d(TAG, "onReliableWriteCompleted (gatt : " + status
					+ " , status :  " + status + ")");
			super.onReliableWriteCompleted(gatt, status);
		}

		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			BluetoothDevice device = gatt.getDevice();

			Log.d(TAG, "onReadRemoteRssi -7777 (device : " + device + " , rssi :  "
                    + rssi + " , status :  " + status + ")");

            gConsumer.setAfr(rssi);
			if (mIRedBearServiceEventListener != null) {
				mIRedBearServiceEventListener.onDeviceRssiUpdate(
                        device.getAddress(), rssi, status);
			}

		};

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			rblService = mBluetoothGatt.getService(RBL_SERVICE);

			if (rblService == null) {
				Log.e(TAG, "RBL service not found!");
				return;
			}

			List<BluetoothGattCharacteristic> Characteristic = rblService
					.getCharacteristics();

			for (BluetoothGattCharacteristic a : Characteristic) {
				Log.d(TAG, " Characteristic =  uuid : " + a.getUuid() + "");
               // writeCharacterNotif(a.getUuid(),true);
			}

            writeCharacterNotif(UUID_BLE_SHIELD_RX,true);
            txCharc = rblService.getCharacteristic(RBL_DEVICE_TX_UUID);
            if (txCharc == null) {
                Log.e(TAG, "RBL RX Characteristic not found!");
                return;
            }
           // Tst.showLong("Connected to remog",getApplicationContext());
        //    writeCharacterNotif(UUID_BLE_SHIELD_SERVICE, true);
          //  writeCharacterNotif(UUID_BLE_SHIELD_TX,true);
/*
            BluetoothGattCharacteristic characteristicRx = rblService.getCharacteristic(UUID_BLE_SHIELD_RX);
            //if ( enableNotification(true,characteristicRx)) {
             //   Log.d(TAG,"RBL enable notification() ok for UUID_BLE_SHIELD_RX");
           // }

			BluetoothGattCharacteristic rxCharc = rblService
					.getCharacteristic(RBL_DEVICE_RX_UUID);
			if (rxCharc == null) {
				Log.e(TAG, "RBL RX Characteristic not found!");
				return;
			}
            if (enableNotification(true,rxCharc) ) {
                Log.d(TAG,"RBL enable noticicaiton () ok for RBL_DEVICE_RX_UID");
            }


			txCharc = rblService.getCharacteristic(RBL_DEVICE_TX_UUID);
			if (txCharc == null) {
				Log.e(TAG, "RBL RX Characteristic not found!");
				return;
			}

			if ( enableNotification(true, txCharc) ) {
                Log.d(TAG,"RBL enableNotification() ok");
            }
            */
/*
			if (mIRedBearServiceEventListener != null)
				mIRedBearServiceEventListener.onDeviceCharacteristicFound();
				*/
		}

	};

	public boolean enableNotification(boolean enable,
			BluetoothGattCharacteristic characteristic) {
		if (mBluetoothGatt == null) {
			return false;
		}
		if (!mBluetoothGatt.setCharacteristicNotification(characteristic,
				enable)) {
			return false;
		}

		BluetoothGattDescriptor clientConfig = characteristic
				.getDescriptor(CCC);
		if (clientConfig == null) {
			return false;
		}

		if (enable) {
			Log.i(TAG, "enable notification");
			clientConfig
					.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		} else {
			Log.i(TAG, "disable notification");
			clientConfig
					.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
		}

		return mBluetoothGatt.writeDescriptor(clientConfig);
	}

	void addDevice(BluetoothDevice mDevice) {
		String address = mDevice.getAddress();

		mDevices.put(address, mDevice);
	}
    public void setCharacteristicNotification(
            BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
        }

        Log.w(TAG,"THIS IS THE ONE THAT IS BEING USED");
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        if (UUID_BLE_SHIELD_RX.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic
                    .getDescriptor(UUID
                            .fromString(CLIENT_CHARACTERISTIC_CONFIG));
			descriptor
                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public boolean writeCharacterNotif(UUID whichUid, boolean enabled ) {
        boolean isOk = false;
        BluetoothGattCharacteristic btgch = rblService.getCharacteristic(whichUid);
        mBluetoothGatt.setCharacteristicNotification(btgch, enabled);
        BluetoothGattDescriptor desc = btgch.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
        desc.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE: new byte[] { 0x00, 0x00});
        //desc.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE: new byte[] { 0x00, 0x00});
        isOk= mBluetoothGatt.writeDescriptor(desc);
        if (isOk ) {
           // mBluetoothGatt.readCharacteristic(btgch);

            Log.d(TAG,"writeCharacteristic for UUUID : " + whichUid.toString() + " SUCCESS");
            charRX = btgch;
        }
        return isOk;
    }

}
