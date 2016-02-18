package com.iss.upnptest.moduls.avtransport.callback.renderingcontrol;

import java.util.Map;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;

import com.iss.upnptest.upnp.UpnpActionCallback;
import com.iss.upnptest.utils.LogUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-4-29
 */

public abstract class SetVolume extends UpnpActionCallback {

	private static String TAG = SetVolume.class.getSimpleName();

	/**
	 * 
	 * @param service
	 * @param instanceId
	 * @param volume Allowed value range: <minimum>0</minimum>-<maximum>100</maximum>,step: 1
	 */
	public SetVolume(Service service, UnsignedIntegerFourBytes instanceId,
			long volume) {
		super(new ActionInvocation(service.getAction("SetVolume")));
		try {
			setInput("InstanceID", instanceId);
			setInput("Channel", "Master");
			setInput("DesiredVolume", new UnsignedIntegerTwoBytes(volume));
		} catch (InvalidValueException e) {
			LogUtil.e(TAG, e);
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		onSuccess("SetVolume successed");
	}

	public abstract void onSuccess(String msg);

}
