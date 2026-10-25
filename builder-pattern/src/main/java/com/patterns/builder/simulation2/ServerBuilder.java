package com.patterns.builder.simulation2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Http2Config {
    private int maxConcurrentStreams;
    private int initialWindowSize;
    private int maxFrameSize;
    private int maxHeaderListSize;
}

interface Listener {
    void onResponseComplete();
}

public class ServerBuilder {
    private final String host;
    private final Http2Config http2Config;
    private final int port;
    private final List<RequestHandler> handlers;
    private final boolean shutdownHook;
    private final List<Listener> responseCompleteListeners;

    private ServerBuilder(Builder builder) {
        this.host = builder.host;
        this.http2Config = builder.http2Config;
        this.port = builder.port;
        this.handlers = List.copyOf(builder.handlers);
        this.shutdownHook = builder.shutdownHook;
        this.responseCompleteListeners = List.copyOf(builder.responseCompleteListeners);
    }

    /**
     * Starts building a server configuration with localhost and port 8080 defaults.
     */
    public static Builder builder() {
        return new Builder();
    }

    public String getHost() {
        return host;
    }

    public Http2Config getHttp2Config() {
        return http2Config;
    }

    public int getPort() {
        return port;
    }

    public List<RequestHandler> getHandlers() {
        return handlers;
    }

    public boolean isShutdownHook() {
        return shutdownHook;
    }

    public List<Listener> getResponseCompleteListeners() {
        return responseCompleteListeners;
    }

    /**
     * Fluent builder for assembling server settings before creating a configuration.
     */
    public static final class Builder {
        private String host = "localhost";
        private Http2Config http2Config;
        private int port = 8080;
        private final List<RequestHandler> handlers = new ArrayList<>();
        private boolean shutdownHook;
        private final List<Listener> responseCompleteListeners = new ArrayList<>();

        private Builder() {
        }

        public Builder host(String host) {
            Objects.requireNonNull(host, "host must not be null");
            if (host.isBlank()) {
                throw new IllegalArgumentException("host must not be blank");
            }
            this.host = host;
            return this;
        }

        public Builder http2Config(Http2Config http2Config) {
            this.http2Config = http2Config;
            return this;
        }

        public Builder port(int port) {
            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException("port must be between 1 and 65535");
            }
            this.port = port;
            return this;
        }

        public Builder addHandler(RequestHandler handler) {
            handlers.add(Objects.requireNonNull(handler, "handler must not be null"));
            return this;
        }

        public Builder handlers(List<? extends RequestHandler> handlers) {
            Objects.requireNonNull(handlers, "handlers must not be null");
            this.handlers.clear();
            handlers.forEach(handler ->
                    this.handlers.add(Objects.requireNonNull(handler, "handler must not be null")));
            return this;
        }

        public Builder shutdownHook(boolean shutdownHook) {
            this.shutdownHook = shutdownHook;
            return this;
        }

        public Builder addResponseCompleteListener(Listener listener) {
            responseCompleteListeners.add(
                    Objects.requireNonNull(listener, "listener must not be null"));
            return this;
        }

        public Builder responseCompleteListeners(List<? extends Listener> listeners) {
            Objects.requireNonNull(listeners, "listeners must not be null");
            responseCompleteListeners.clear();
            listeners.forEach(listener -> responseCompleteListeners.add(
                    Objects.requireNonNull(listener, "listener must not be null")));
            return this;
        }

        public ServerBuilder build() {
            return new ServerBuilder(this);
        }
    }
}
