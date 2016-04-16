package jaalee.sdk;

import java.util.List;
import jaalee.sdk.Beacon;

/**
 * Callback to be invoked when beacons are ranged.
 *
 */
public abstract interface RangingListener
{
  public abstract void onBeaconsDiscovered(Region paramRegion, List<Beacon> paramList);
}