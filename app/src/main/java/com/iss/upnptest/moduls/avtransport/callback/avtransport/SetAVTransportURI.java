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
 * @version 1.0.0 2015-4-23
 */

public abstract class SetAVTransportURI extends UpnpActionCallback {

	private static String TAG = SetAVTransportURI.class.getSimpleName();

	public SetAVTransportURI(UnsignedIntegerFourBytes instanceId,
			Service service, String uri, String metadata) {
		super(new ActionInvocation(service.getAction("SetAVTransportURI")));
		try {
			setInput("InstanceID", instanceId);
			setInput("CurrentURI", uri);
			setInput("CurrentURIMetaData", metadata);
		} catch (InvalidValueException e) {
			LogUtil.e(TAG, e);
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		onSuccess("Set AVTransport URI successful");
	}

	public abstract void onSuccess(String msg);

}
