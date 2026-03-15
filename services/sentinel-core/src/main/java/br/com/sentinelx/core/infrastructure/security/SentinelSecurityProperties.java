package br.com.sentinelx.core.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sentinel.security")
public class SentinelSecurityProperties {

    private final Agent agent = new Agent();
    private final Admin admin = new Admin();

    public Agent getAgent() {
        return agent;
    }

    public Admin getAdmin() {
        return admin;
    }

    public static class Agent {
        private String apiKeyHeader = "X-Agent-Api-Key";
        private String apiKey = "change-me-agent-key";

        public String getApiKeyHeader() {
            return apiKeyHeader;
        }

        public void setApiKeyHeader(String apiKeyHeader) {
            this.apiKeyHeader = apiKeyHeader;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }

    public static class Admin {
        private String username = "admin";
        private String password = "change-me-admin-password";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}