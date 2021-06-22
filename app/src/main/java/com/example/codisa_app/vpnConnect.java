package com.example.codisa_app;


import android.content.pm.PackageManager;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;

import java.io.IOException;

public class vpnConnect  extends android.net.VpnService {


    VpnService.Builder builder = new VpnService.Builder();

    // Create a local TUN interface using predetermined addresses. In your app,
    // you typically use values returned from the VPN gateway during handshaking.
    ParcelFileDescriptor localTunnel = builder
            .addAddress("192.168.2.2", 24)
            .addRoute("0.0.0.0", 0)
            .addDnsServer("192.168.1.1")
            .establish();
}