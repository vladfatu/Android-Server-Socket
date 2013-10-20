package com.example.socketserver.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.util.Log;

public class NetworkUtils {
	
	public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    //if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress() ) {
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress()) ) {
                        String ipAddr = inetAddress.getHostAddress();
                        return ipAddr;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.d("", ex.toString());
        }
        return null;
    }   

}
