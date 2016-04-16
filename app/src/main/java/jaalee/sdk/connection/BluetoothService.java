package jaalee.sdk.connection;

import android.bluetooth.BluetoothGattCharacteristic;
/**
 * @author JAALEE, Inc
 * 
 * @Support dev@jaalee.com
 * @Sales: sales@jaalee.com
 * 
 * @see
 */

public abstract interface BluetoothService
{
  public abstract void update(BluetoothGattCharacteristic paramBluetoothGattCharacteristic);
  public abstract void onCharacteristicWrite(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, int state);
}
