package com.agarwalaman.addressbook.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Returns the Elastic Search client used to interact with the Elastic cloud server
 */
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
                    .put("request.headers.X-Found-Cluster", clusterId)
                    .put("xpack.security.user", "elastic:lABenlPStE0qyWx6x2kSrDPf")
                    .put("xpack.security.transport.ssl.verification_mode", "none")
                    .build();

            client = new PreBuiltXPackTransportClient(settings);
            client.addTransportAddress(new TransportAddress(InetAddress.getByName(hostname), 9343));

        } catch (UnknownHostException exception) {
            exception.printStackTrace();
        }
        return client;
    }


}
