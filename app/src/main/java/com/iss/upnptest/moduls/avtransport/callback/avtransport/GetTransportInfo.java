package com.iss.upnptest.moduls.avtransport.callback.avtransport;

import java.util.Map;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.model.TransportInfo;

import com.iss.upnptest.upnp.UpnpActionCallback;
import com.iss.upnptest.utils.LogUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public abstract class GetTransportInfo extends UpnpActionCallback {

	private static String TAG = GetTransportInfo.class.getSimpleName();

	public GetTransportInfo(UnsignedIntegerFourBytes instanceId, Service service) {
		super(new ActionInvocation(service.getAction("GetTransportInfo")));
		try {
			setInput("InstanceID", instanceId);
		} catch (InvalidValueException e) {
			LogUtil.e(TAG, e);
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		TransportInfo transportInfo = new TransportInfo(
				invocation.getOutputMap());
		onSuccess(transportInfo);
	}

	public abstract void onSuccess(TransportInfo transportInfo);

}
