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

public abstract class Seek extends UpnpActionCallback {

	private static String TAG = Seek.class.getSimpleName();

	public Seek(UnsignedIntegerFourBytes instanceId, Service service,
			String seekTo) {
		super(new ActionInvocation(service.getAction("Seek")));
		try {
			setInput("InstanceID", instanceId);
			/**
			 * <allowedValueList> <allowedValue>ABS_COUNT</allowedValue>
			 * <allowedValue>TRACK_NR</allowedValue>
			 * <allowedValue>REL_TIME</allowedValue> </allowedValueList>
			 */
			setInput("Unit", "REL_TIME");
			setInput("Target", seekTo);
		} catch (InvalidValueException e) {
			LogUtil.e(TAG, e);
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		onSuccess("Seek successful");
	}

	public abstract void onSuccess(String msg);

}
