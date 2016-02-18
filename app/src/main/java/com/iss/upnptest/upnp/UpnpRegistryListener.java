package com.iss.upnptest.upnp;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import com.iss.upnptest.utils.LogUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-4-29
 */

public abstract class UpnpRegistryListener extends DefaultRegistryListener {

	public void remoteDeviceDiscoveryStarted(Registry registry,
			RemoteDevice device) {
		deviceAdded(device);
	}

	public void remoteDeviceDiscoveryFailed(Registry registry,
			RemoteDevice device, Exception ex) {
		LogUtil.d("", "Discovery failed of '"
				+ device.getDisplayString()
				+ "': "
				+ (ex != null ? ex.toString()
						: "Couldn't retrieve device/service descriptors"));
		deviceRemoved(device);
	}

	public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
		deviceAdded(device);
	}

	public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
		deviceAdded(device);
	}

	public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
		deviceRemoved(device);
	}

	public void localDeviceAdded(Registry registry, LocalDevice device) {
		deviceAdded(device);
	}

	public void localDeviceRemoved(Registry registry, LocalDevice device) {
		deviceRemoved(device);
	}

	/**
	 * 添加设备
	 * 
	 * @param device
	 */
	public abstract void deviceAdded(Device device);

	/**
	 * 删除设备
	 * 
	 * @param device
	 */
	public abstract void deviceRemoved(Device device);

}
