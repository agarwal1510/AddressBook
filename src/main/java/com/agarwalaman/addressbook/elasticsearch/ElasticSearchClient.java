package com.agarwalaman.addressbook.elasticsearch;

import org.elasticsearch.client.Client;


import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticSearchClient {

    public static Client client() {
        Client client = null;
        String clusterId = "0234927193704267b3345ce93b7a65b0";
        String region = "us-east-1";

        try {

            Settings settings = Settings.builder()
                    .put("transport.ping_schedule", "5s")
                    .put("cluster.name", clusterId)
                    .put("action.bulk.compress", false)
                    .put("shield.transport.ssl", true)
                    .put("request.headers.X-Found-Cluster", clusterId)
                    .put("shield.user", "elastic:lABenlPStE0qyWx6x2kSrDPf")
                    .build();

            String hostname = clusterId + "." + region + ".aws.found.io";
            client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName(hostname), 9343));
        } catch (UnknownHostException exception) {
            exception.printStackTrace();
        }
        return client;
    }


}
