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

public abstract class GetVolume extends UpnpActionCallback {

	private static String TAG = GetVolume.class.getSimpleName();
	
	public GetVolume(Service service, UnsignedIntegerFourBytes instanceId) {
		super(new ActionInvocation(service.getAction("GetVolume")));
		try {
			setInput("InstanceID", instanceId);
			setInput("Channel", "Master");
		} catch (InvalidValueException e) {
			LogUtil.e(TAG, e);
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		onSuccess(Integer.parseInt(result.get("CurrentVolume").toString()));
	}

	public abstract void onSuccess(int currentVolume);
	
}

