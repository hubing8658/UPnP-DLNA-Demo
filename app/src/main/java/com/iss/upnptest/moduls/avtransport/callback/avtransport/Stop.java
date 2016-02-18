package com.iss.upnptest.moduls.avtransport.callback.avtransport;

import java.util.Map;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;

import com.iss.upnptest.upnp.UpnpActionCallback;
import com.iss.upnptest.utils.LogUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public abstract class Stop extends UpnpActionCallback {

	private static String TAG = Stop.class.getSimpleName();

	public Stop(UnsignedIntegerFourBytes instanceId, Service service) {
		super(new ActionInvocation(service.getAction("Stop")));
		try {
			setInput("InstanceID", instanceId);
		} catch (InvalidValueException e) {
			LogUtil.e(TAG, e);
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		onSuccess("Stop play successful");
	}

	public abstract void onSuccess(String msg);

}
