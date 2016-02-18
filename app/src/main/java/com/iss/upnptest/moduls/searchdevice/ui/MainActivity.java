package com.iss.upnptest.moduls.searchdevice.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iss.upnptest.R;
import com.iss.upnptest.adapter.GeneralAdapter;
import com.iss.upnptest.app.MyApplication;
import com.iss.upnptest.moduls.contentbrowse.ui.BrowserContentActivity;
import com.iss.upnptest.moduls.searchdevice.entity.DeviceDisplay;
import com.iss.upnptest.moduls.searchdevice.listener.TestRegistryListener;
import com.iss.upnptest.server.medialserver.bll.MediaContentBiz;
import com.iss.upnptest.server.medialserver.server.MediaServer;
import com.iss.upnptest.upnp.UpnpServiceBiz;
import com.iss.upnptest.upnp.constants.HandlerWhat;
import com.iss.upnptest.utils.IPUtil;
import com.iss.upnptest.utils.LogUtil;

import org.fourthline.cling.transport.Router;
import org.fourthline.cling.transport.RouterException;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    protected GeneralAdapter<DeviceDisplay> adapter;
    protected Handler handler;
    private ListView lvDevices;
    private TestRegistryListener rListener;
    private UpnpServiceBiz upnpServiceBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                DeviceDisplay dd = (DeviceDisplay) msg.obj;
                switch (msg.what) {
                    case HandlerWhat.ADD:
                        int p = adapter.getPosition(dd);
                        if (adapter.getPosition(dd) >= 0) {
                            // Device already in the list, re-set new value at same
                            // position
                            adapter.remove(dd);
                            adapter.insert(dd, p);
                        } else {
                            adapter.add(dd);
                        }
                        break;
                    case HandlerWhat.REMOVE:
                        adapter.remove(dd);
                        break;
                }
            }
        };
        initView();
        init();
        setListener();
    }

    private void init() {
        upnpServiceBiz = UpnpServiceBiz.newInstance();
        rListener = new TestRegistryListener(handler);
        upnpServiceBiz.addListener(rListener);

        // 初始化ContentDirectory服务
        try {
            // 启动ContentDirectory服务
            InetAddress localAddress = IPUtil.getLocalIpAddress(this);
            MediaServer mediaServer = new MediaServer(localAddress);
            upnpServiceBiz.addDevice(mediaServer.getDevice());

            MediaContentBiz mediaContentBiz = new MediaContentBiz();
            mediaContentBiz.prepareMediaServer(this, mediaServer.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(TAG, "Creating ContentDirectory device failed");
        }
    }

    private void initView() {
        // 设置显示ToolBar
        Toolbar tbTitle = (Toolbar) findViewById(R.id.tb_title);
        setSupportActionBar(tbTitle);

        lvDevices = (ListView) findViewById(R.id.lv_devices);
        adapter = new GeneralAdapter<DeviceDisplay>(this, android.R.layout.simple_list_item_1, null) {

            @Override
            public void convert(GeneralAdapter.ViewHolder holder, DeviceDisplay item, int position) {
                String text = item.toString();
                holder.setText(android.R.id.text1, text);
            }
        };
        lvDevices.setAdapter(adapter);
    }

    private void setListener() {
        lvDevices.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                DeviceDisplay dd = adapter.getItem(position);
                ((MyApplication) getApplication()).setDeviceDisplay(dd);
                Intent intent = null;
                if (dd.getDevice().isFullyHydrated()) {
                    intent = new Intent(MainActivity.this,
                            BrowserContentActivity.class);
                } else {
                    showDeviceDetails(dd);
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
        lvDevices.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                DeviceDisplay deviceDisplay = adapter.getItem(position);
                return showDeviceDetails(deviceDisplay);
            }
        });
        rListener = new TestRegistryListener(handler);
    }

    /**
     * 显示设备详细信息
     *
     * @param deviceDisplay
     * @return
     * @author hubing
     */
    protected boolean showDeviceDetails(DeviceDisplay deviceDisplay) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(R.string.device_details);
        dialog.setMessage(deviceDisplay.getDetailsMsg());
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialog.show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(12);
        return dialog.isShowing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (upnpServiceBiz != null) {
            upnpServiceBiz.removeListener(rListener);
            upnpServiceBiz = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                searchDevices();
                break;
            // case R.id.network_switch:
            // switchRouter();
            // break;
            case R.id.log_switch:
                switchLogging();
                break;
        }
        return false;
    }

    /**
     * 查找upnp设备
     */
    protected void searchDevices() {
        if (upnpServiceBiz != null) {
            // 清空list中数据
            adapter.clear();
            Toast.makeText(this, R.string.searching_LAN, Toast.LENGTH_SHORT).show();
            upnpServiceBiz.removeAllRemoteDevices();
            upnpServiceBiz.search();
        }
    }

    /**
     * 打开/关闭路由
     */
    protected void switchRouter() {
        if (upnpServiceBiz == null) {
            return;
        }
        Router router = upnpServiceBiz.getRouter();
        try {
            if (router.isEnabled()) {
                Toast.makeText(this, R.string.disabling_router, Toast.LENGTH_SHORT).show();
                router.disable();
            } else {
                Toast.makeText(this, R.string.enabling_router, Toast.LENGTH_SHORT).show();
                router.enable();
            }
        } catch (RouterException e) {
            Toast.makeText(this, getText(R.string.error_switching_router), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * 打开/关闭调试log
     */
    protected void switchLogging() {
        Logger logger = Logger.getLogger("org.fourthline.cling");
        Level level = logger.getLevel();
        if (level != null && !level.equals(Level.INFO)) {
            Toast.makeText(this, R.string.disabling_debug_logging, Toast.LENGTH_SHORT).show();
            logger.setLevel(Level.INFO);
        } else {
            Toast.makeText(this, R.string.enabling_debug_logging, Toast.LENGTH_SHORT).show();
            logger.setLevel(Level.FINEST);
        }
    }

}
