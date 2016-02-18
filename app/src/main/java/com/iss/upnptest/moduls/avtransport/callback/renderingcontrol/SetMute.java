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

public abstract class SetMute extends UpnpActionCallback {

	private static String TAG = SetMute.class.getSimpleName();

	public SetMute(Service service, UnsignedIntegerFourBytes instanceId,
			boolean desiredMute) {
		super(new ActionInvocation(service.getAction("SetMute")));
		try {
			setInput("InstanceID", instanceId);
			setInput("Channel", "Master");
			setInput("DesiredMute", desiredMute);
		} catch (InvalidValueException e) {
			LogUtil.e(TAG, e);
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		onSuccess("SetMute successed");
	}

	public abstract void onSuccess(String msg);

}
