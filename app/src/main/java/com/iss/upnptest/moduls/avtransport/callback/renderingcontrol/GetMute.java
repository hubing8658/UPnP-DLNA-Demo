package com.iss.upnptest.moduls.avtransport.callback.renderingcontrol;

import java.util.Map;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import com.iss.upnptest.upnp.UpnpActionCallback;
import com.iss.upnptest.utils.LogUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-4-29
 */

public abstract class GetMute extends UpnpActionCallback {

	private static String TAG = GetMute.class.getSimpleName();

	public GetMute(Service service, UnsignedIntegerFourBytes instanceId) {
		super(new ActionInvocation(service.getAction("GetMute")));
		try {
			setInput("InstanceID", instanceId);
			setInput("Channel", "Master");
		} catch (InvalidValueException e) {
			LogUtil.e(TAG, e);
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		onSuccess((Boolean) result.get("CurrentMute"));
	}

	public abstract void onSuccess(boolean currentMute);

}
