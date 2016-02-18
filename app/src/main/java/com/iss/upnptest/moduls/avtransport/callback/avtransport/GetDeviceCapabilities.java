package com.iss.upnptest.moduls.avtransport.callback.avtransport;

import java.util.Map;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.model.DeviceCapabilities;

import com.iss.upnptest.upnp.UpnpActionCallback;
import com.iss.upnptest.utils.LogUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public abstract class GetDeviceCapabilities extends UpnpActionCallback {

	private static String TAG = GetDeviceCapabilities.class.getSimpleName();

	public GetDeviceCapabilities(UnsignedIntegerFourBytes instanceId,
			Service service) {
		super(new ActionInvocation(service.getAction("GetDeviceCapabilities")));
		try {
			setInput("InstanceID", instanceId);
		} catch (InvalidValueException e) {
			LogUtil.e(TAG, e);
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		DeviceCapabilities caps = new DeviceCapabilities(
				invocation.getOutputMap());
		onSuccess(caps);
	}

	public abstract void onSuccess(DeviceCapabilities caps);

}
