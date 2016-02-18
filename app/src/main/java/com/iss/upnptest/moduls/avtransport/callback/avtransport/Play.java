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

public abstract class Play extends UpnpActionCallback {

	private static String TAG = Play.class.getSimpleName();

	public Play(UnsignedIntegerFourBytes instanceId, Service service) {
		super(new ActionInvocation(service.getAction("Play")));
		try {
			setInput("InstanceID", instanceId);
			setInput("Speed", "1");
		} catch (InvalidValueException e) {
			LogUtil.e(TAG, e);
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		onSuccess("Play successful");
	}

	public abstract void onSuccess(String msg);

}
