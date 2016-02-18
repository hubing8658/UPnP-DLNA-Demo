package com.iss.upnptest.moduls.contentbrowse.bll;

import org.fourthline.cling.model.action.ActionException;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ErrorCode;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;
import android.os.Handler;
import android.os.Message;
import com.iss.upnptest.moduls.avtransport.bll.MediaControlBiz;
import com.iss.upnptest.moduls.contentbrowse.callback.ContentBrowseActionCallback;
import com.iss.upnptest.moduls.contentbrowse.entity.ContentItem;
import com.iss.upnptest.upnp.UpnpServiceBiz;
import com.iss.upnptest.upnp.constants.HandlerWhat;
import com.iss.upnptest.utils.FiletypeUtil;
import com.iss.upnptest.utils.LogUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-5-4
 */

public class ContentBrowseBiz {

	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;

	private static String TAG = MediaControlBiz.class.getSimpleName();

	private UpnpServiceBiz upnpBiz;
	private Handler handler;

	public ContentBrowseBiz(Handler handler) {
		upnpBiz = UpnpServiceBiz.newInstance();
		this.handler = handler;
	}

	/**
	 * 获取根目录下内容
	 * @param device
	 */
	public void getRootContent(Device device) {
		Service service = device.findService(new UDAServiceType("ContentDirectory", 1));
		Container rootContainer = new Container();
		rootContainer.setId("0");
		if (service != null) {
			rootContainer.setTitle("Content Directory on "
					+ service.getDevice());
			execute(service, rootContainer);
		}
	}

	public void getContent(ContentItem ct) {
		execute(ct.getService(), ct.getContainer());
	}
	
	private void execute(final Service service, Container container) {
		upnpBiz.execute(new ContentBrowseActionCallback(service, container) {
			@Override
			public void received(ActionInvocation actionInvocation,
					DIDLContent didl) {
				LogUtil.d(TAG,
						"Received browse action DIDL descriptor, creating tree nodes");
				try {
					handler.sendEmptyMessage(HandlerWhat.CLEAR_ALL);
					for (Container container : didl.getContainers()) {
						ContentItem ct = new ContentItem(container, service);
						sendMsg(ct);
					}
					for (Item item : didl.getItems()) {
						String contentFormat = item.getFirstResource().getProtocolInfo().getContentFormat();
						ContentItem ct = new ContentItem(item, service, FiletypeUtil
								.getFiletype(contentFormat));
						sendMsg(ct);
					}
				} catch (Exception e) {
					LogUtil.e(TAG, "Creating DIDL tree nodes failed: " + e);
					actionInvocation.setFailure(new ActionException(
							ErrorCode.ACTION_FAILED,
							"Can't create list childs: " + e, e));
					failure(actionInvocation, null);
				}
			}

			private void sendMsg(ContentItem ct) {
				Message msg = Message.obtain(handler);
				msg.what = HandlerWhat.ADD;
				msg.arg1 = SUCCESS;
				msg.obj = ct;
				msg.sendToTarget();
			}
			
			@Override
			public void failure(ActionInvocation invocation,
					UpnpResponse operation, String defaultMsg) {
				LogUtil.d(TAG, "failure:" + defaultMsg);
			}
		});
	}
	
}
