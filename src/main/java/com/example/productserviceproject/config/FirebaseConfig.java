package com.example.productserviceproject.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class FirebaseConfig {

    @Value("${firebase.config.type}")
    private String type;

    @Value("${firebase.config.project_id}")
    private String projectId;

    @Value("${firebase.config.private_key_id}")
    private String privateKeyId;

    @Value("${firebase.config.private_key}")
    private String privateKey;

    @Value("${firebase.config.client_email}")
    private String clientEmail;

    @Value("${firebase.config.client_id}")
    private String clientId;

    @Value("${firebase.config.auth_uri}")
    private String authUri;

    @Value("${firebase.config.token_uri}")
    private String tokenUri;

    @Value("${firebase.config.auth_provider_x509_cert_url}")
    private String authProviderX509CertUrl;

    @Value("${firebase.config.client_x509_cert_url}")
    private String clientX509CertUrl;

    @Value("${firebase.config.universe_domain}")
    private String universeDomain;

    public String toJson() {
        return "{" +
                "\"type\": \"" + type + "\"," +
                "\"project_id\": \"" + projectId + "\"," +
                "\"private_key_id\": \"" + privateKeyId + "\"," +
                "\"private_key\": \"" + privateKey.replace("\n", "\\n") + "\"," +  // Replace newline characters
                "\"client_email\": \"" + clientEmail + "\"," +
                "\"client_id\": \"" + clientId + "\"," +
                "\"auth_uri\": \"" + authUri + "\"," +
                "\"token_uri\": \"" + tokenUri + "\"," +
                "\"auth_provider_x509_cert_url\": \"" + authProviderX509CertUrl + "\"," +
                "\"client_x509_cert_url\": \"" + clientX509CertUrl + "\"," +
                "\"universe_domain\": \"" + universeDomain + "\"" +
                "}";
    }
}
