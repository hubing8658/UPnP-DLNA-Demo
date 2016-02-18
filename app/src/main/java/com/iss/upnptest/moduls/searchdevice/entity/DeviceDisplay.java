package com.iss.upnptest.moduls.searchdevice.entity;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;

/**
 * @author hubing
 * @version 1.0.0 2015-4-15
 */

public class DeviceDisplay {

	private Device device;

	public DeviceDisplay(Device device) {
		this.device = device;
	}

	public Device getDevice() {
		return this.device;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DeviceDisplay that = (DeviceDisplay) o;
		return device.equals(that.device);
	}

	@Override
	public int hashCode() {
		return device.hashCode();
	}

	@Override
	public String toString() {
		String name = device.getDetails() != null && device.getDetails().getFriendlyName() != null
				? device.getDetails().getFriendlyName() : device.getDisplayString();
		return device.isFullyHydrated() ? name : name + " *";
	}

	public String getDetailsMsg() {
		StringBuilder sb = new StringBuilder();
		if (device.isFullyHydrated()) {
			sb.append(device.getDisplayString());
			sb.append("\n\n");
//			sb.append(device.getIdentity().getUdn()).append("\n");
//			sb.append("max-age:" + device.getIdentity().getMaxAgeSeconds()).append("\n");
			for (Service srv : device.getServices()) {
				sb.append(srv.getServiceType()).append("\n");
			}
		} else {
			sb.append("正在查找设备详情,请稍后.");
		}
		return sb.toString();
	}

}
