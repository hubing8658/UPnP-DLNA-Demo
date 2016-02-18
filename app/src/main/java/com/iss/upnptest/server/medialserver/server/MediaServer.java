package com.iss.upnptest.server.medialserver.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import com.iss.upnptest.server.medialserver.service.ContentDirectoryService;
import com.iss.upnptest.utils.LogUtil;

/**
 * @author hubing
 * @version 1.0.0 2015-5-8
 */

public class MediaServer {
	
	private UDN udn = new UDN(UUID.nameUUIDFromBytes("GNaP-MediaServer".getBytes()));
	private LocalDevice localDevice;

	private final static String deviceType = "MediaServer";
	private final static int version = 1;
	private final static String TAG = MediaServer.class.getSimpleName();;
	private final static int port = 8196;
	private static InetAddress localAddress;

	public MediaServer(InetAddress localAddress) throws ValidationException {
		DeviceType type = new UDADeviceType(deviceType, version);

		DeviceDetails details = new DeviceDetails(android.os.Build.MODEL,
				new ManufacturerDetails(android.os.Build.MANUFACTURER),
				new ModelDetails("GNaP", "GNaP MediaServer for Android", "v1"));

		LocalService service = new AnnotationLocalServiceBinder()
				.read(ContentDirectoryService.class);

		service.setManager(new DefaultServiceManager<ContentDirectoryService>(
				service, ContentDirectoryService.class));

		localDevice = new LocalDevice(new DeviceIdentity(udn), type, details,
				service);
		this.localAddress = localAddress;

		LogUtil.v(TAG, "MediaServer device created: ");
		LogUtil.v(TAG, "friendly name: " + details.getFriendlyName());
		LogUtil.v(TAG, "manufacturer: "
				+ details.getManufacturerDetails().getManufacturer());
		LogUtil.v(TAG, "model: " + details.getModelDetails().getModelName());

		// start http server
		try {
			new HttpServer(port);
		} catch (IOException ioe) {
			System.err.println("Couldn't start server:\n" + ioe);
			System.exit(-1);
		}

		LogUtil.v(TAG, "Started Http Server on port " + port);
	}

	public LocalDevice getDevice() {
		return localDevice;
	}

	public String getAddress() {
		return localAddress.getHostAddress() + ":" + port;
	}
}
