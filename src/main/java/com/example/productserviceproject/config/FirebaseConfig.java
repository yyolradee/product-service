package com.example.productserviceproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
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

    // Getters and setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPrivateKeyId() {
        return privateKeyId;
    }

    public void setPrivateKeyId(String privateKeyId) {
        this.privateKeyId = privateKeyId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAuthUri() {
        return authUri;
    }

    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public String getAuthProviderX509CertUrl() {
        return authProviderX509CertUrl;
    }

    public void setAuthProviderX509CertUrl(String authProviderX509CertUrl) {
        this.authProviderX509CertUrl = authProviderX509CertUrl;
    }

    public String getClientX509CertUrl() {
        return clientX509CertUrl;
    }

    public void setClientX509CertUrl(String clientX509CertUrl) {
        this.clientX509CertUrl = clientX509CertUrl;
    }

    public String getUniverseDomain() {
        return universeDomain;
    }

    public void setUniverseDomain(String universeDomain) {
        this.universeDomain = universeDomain;
    }

    @Override
    public String toString() {
        return "FirebaseConfig{" +
                "type='" + type + '\'' +
                ", projectId='" + projectId + '\'' +
                ", privateKeyId='" + privateKeyId + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", clientId='" + clientId + '\'' +
                ", authUri='" + authUri + '\'' +
                ", tokenUri='" + tokenUri + '\'' +
                ", authProviderX509CertUrl='" + authProviderX509CertUrl + '\'' +
                ", clientX509CertUrl='" + clientX509CertUrl + '\'' +
                ", universeDomain='" + universeDomain + '\'' +
                '}';
    }
}

