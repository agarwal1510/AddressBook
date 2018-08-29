package com.agarwalaman.addressbook.elasticsearch;

import org.elasticsearch.client.Client;


import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.shield.ShieldPlugin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticSearchClient {

    public static Client client() {
        TransportClient client = null;
        String clusterId = "0234927193704267b3345ce93b7a65b0";
        String region = "us-east-1";
        String hostname = clusterId + "." + region + ".aws.found.io";
        try {

            Settings settings = Settings.builder()
                    .put("transport.ping_schedule", "5s")
                    .put("client.transport.sniff", false)
                    .put("transport.tcp.compress", true)
                    .put("cluster.name", clusterId)
                    .put("xpack.security.transport.ssl.enabled", true)
                    //.put("action.bulk.compress", false)
                    //.put("shield.transport.ssl", true)
                    .put("request.headers.X-Found-Cluster", clusterId)
                    //.put("shield.user", "elastic:lABenlPStE0qyWx6x2kSrDPf")
                    .put("xpack.security.user", "elastic:lABenlPStE0qyWx6x2kSrDPf")
                    .put("xpack.security.transport.ssl.verification_mode", "none")
                    .build();

//            Settings settings = Settings.builder()
//                    .put("client.transport.nodes_sampler_interval", "5s")
//                    .put("client.transport.sniff", false)
//                    .put("transport.tcp.compress", true)
//                    .put("cluster.name", clusterName)
//                    .put("xpack.security.transport.ssl.enabled", enableSsl)
//                    .put("request.headers.X-Found-Cluster", "${cluster.name}")
//                    .put("xpack.security.user", System.getProperty("xpack.security.user"))
//                    .put("xpack.security.transport.ssl.verification_mode", insecure ? "none" : "full")
//                    .build();

            // Instantiate a TransportClient and add the cluster to the list of addresses to connect to.
            // Only port 9343 (SSL-encrypted) is currently supported. The use of x-pack security features is required.
            client = new PreBuiltXPackTransportClient(settings);
            client.addTransportAddress(new TransportAddress(InetAddress.getByName(hostname), 9343));

//            Client client = PreXTransportClient.builder()
//                    .addPlugin(ShieldPlugin.class)
//                    .settings(settings)
//                    .build()
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostname), 9343));
//            client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName(hostname), 9243));
        } catch (UnknownHostException exception) {
            exception.printStackTrace();
        }
        return client;
    }


}
