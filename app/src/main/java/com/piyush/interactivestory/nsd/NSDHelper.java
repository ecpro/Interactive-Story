package com.piyush.interactivestory.nsd;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;

public class NSDHelper {

    private static final String TAG = NSDHelper.class.getSimpleName();

    public NsdManager nsdManager;
    public NsdManager.DiscoveryListener discoveryListener;
    public NsdManager.RegistrationListener registrationListener;
    public NsdManager.ResolveListener resolveListener;
    public Context context;
    public NsdServiceInfo nsdServiceInfo;

    public String serviceName = "Piyush";
    public static final String serviceType = "_http._tcp";
    public int port;

    public NSDHelper(Context context) {
        this.context = context;
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        setPort();
    }

    public void registerService() {
        NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
        nsdServiceInfo.setServiceName(this.serviceName);
        nsdServiceInfo.setPort(this.port);
        nsdServiceInfo.setServiceType(serviceType);
        initializeRegistrationListener();
        Log.v(TAG, Build.MANUFACTURER + " registering service: " + port);
        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, this.registrationListener);
    }

    public void discoverServices() {
        stopDiscovery();  // Cancel any existing discovery request
        initializeResolveListener();
        initializeDiscoveryListener();
        nsdManager.discoverServices(
                serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    public void setPort() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            int localPort = serverSocket.getLocalPort();
            this.port = localPort;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeRegistrationListener() {
        this.registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
                Log.v(TAG, "Failed to register the service : " + serviceName);
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
                Log.v(TAG, "Failed to unregister the service : " + serviceName);
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
                serviceName = nsdServiceInfo.getServiceName();
                Toast.makeText(context, "Registered " + serviceName + " service", Toast.LENGTH_LONG).show();
                Log.v(TAG, "Service registered on port: " + nsdServiceInfo.getPort() + " and service name is : " + serviceName);
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {
                Toast.makeText(context, "Unregistered " + nsdServiceInfo.getServiceName() + " service", Toast.LENGTH_LONG).show();
                Log.v(TAG, "Successfully unregistered service : " + serviceName);
            }
        };

    }

    public void initializeResolveListener() {
            this.resolveListener = new NsdManager.ResolveListener() {
                @Override
                public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i) {
                    Log.e(TAG, "Resolve failed" + i);
                }

                @Override
                public void onServiceResolved(NsdServiceInfo serviceInfo) {
                    Log.v(TAG, "Resolve Succeeded. " + serviceInfo);
                    if (serviceInfo.getServiceName().equals(nsdServiceInfo)) {
                        Log.d(TAG, "Same IP.");
                        return;
                    }
                    nsdServiceInfo = serviceInfo;
                }
            };
        }

        public void initializeDiscoveryListener() {
            this.discoveryListener = new NsdManager.DiscoveryListener() {
                @Override
                public void onStartDiscoveryFailed(String s, int i) {
                    Log.e(TAG, "Discovery failed: Error code:" + s);
                }

                @Override
                public void onStopDiscoveryFailed(String s, int i) {
                   Log.e(TAG, "Discovery failed: Error code:" + i);
                }

                @Override
                public void onDiscoveryStarted(String s) {
                    Log.d(TAG, "Service discovery started");
                }

                @Override
                public void onDiscoveryStopped(String s) {
                    Log.i(TAG, "Discovery stopped: " + serviceType);
                }

                @Override
                public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
                    Log.d(TAG, "Service discovery success" + nsdServiceInfo);
                    String serviceType = nsdServiceInfo.getServiceType();
                    Log.d(TAG, "Service discovery success: " + nsdServiceInfo.getServiceName());
                    nsdManager.resolveService(nsdServiceInfo, resolveListener);
                }

                @Override
                public void onServiceLost(NsdServiceInfo serviceInfo) {
                    Log.e(TAG, "service lost" + serviceInfo);
                    if (nsdServiceInfo == serviceInfo) {
                        nsdServiceInfo = null;
                    }
                }
            };
        }

    public void initializeServerSocket() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            int localPort = serverSocket.getLocalPort();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to find a available port", e);
        }
    }

    public void tearDown() {
            this.nsdManager.unregisterService(this.registrationListener);
    }

    public void stopDiscovery() {
        if (discoveryListener != null) {
            try {
                nsdManager.stopServiceDiscovery(discoveryListener);
            } finally {
            }
            discoveryListener = null;
        }
    }

}
