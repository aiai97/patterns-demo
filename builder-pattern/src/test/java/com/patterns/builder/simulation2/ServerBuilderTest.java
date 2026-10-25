package com.patterns.builder.simulation2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServerBuilderTest {

    @Test
    void buildUsesSensibleDefaults() {
        ServerBuilder server = ServerBuilder.builder().build();

        assertThat(server.getHost()).isEqualTo("localhost");
        assertThat(server.getPort()).isEqualTo(8080);
        assertThat(server.getHttp2Config()).isNull();
        assertThat(server.getHandlers()).isEmpty();
        assertThat(server.isShutdownHook()).isFalse();
        assertThat(server.getResponseCompleteListeners()).isEmpty();
    }

    @Test
    void builderConfiguresAllServerOptionsFluently() {
        Http2Config http2Config = new Http2Config();
        RequestHandler handler = (request, response) -> true;
        Listener listener = () -> { };

        ServerBuilder server = ServerBuilder.builder()
                .host("api.example.test")
                .port(9443)
                .http2Config(http2Config)
                .addHandler(handler)
                .shutdownHook(true)
                .addResponseCompleteListener(listener)
                .build();

        assertThat(server.getHost()).isEqualTo("api.example.test");
        assertThat(server.getPort()).isEqualTo(9443);
        assertThat(server.getHttp2Config()).isSameAs(http2Config);
        assertThat(server.getHandlers()).containsExactly(handler);
        assertThat(server.isShutdownHook()).isTrue();
        assertThat(server.getResponseCompleteListeners()).containsExactly(listener);
    }

    @Test
    void collectionSettersCopyInputAndBuiltCollectionsAreImmutable() {
        RequestHandler handler = (request, response) -> true;
        Listener listener = () -> { };
        List<RequestHandler> handlers = new ArrayList<>(List.of(handler));
        List<Listener> listeners = new ArrayList<>(List.of(listener));

        ServerBuilder server = ServerBuilder.builder()
                .handlers(handlers)
                .responseCompleteListeners(listeners)
                .build();
        handlers.clear();
        listeners.clear();

        assertThat(server.getHandlers()).containsExactly(handler);
        assertThat(server.getResponseCompleteListeners()).containsExactly(listener);
        assertThatThrownBy(() -> server.getHandlers().clear())
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> server.getResponseCompleteListeners().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void portMustBeWithinTcpPortRange() {
        assertThatThrownBy(() -> ServerBuilder.builder().port(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("port");
        assertThatThrownBy(() -> ServerBuilder.builder().port(65536))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("port");
    }

    @Test
    void hostMustNotBeNullOrBlank() {
        assertThatThrownBy(() -> ServerBuilder.builder().host(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ServerBuilder.builder().host("  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("host");
    }

    @Test
    void nullHandlersAndListenersAreRejected() {
        assertThatThrownBy(() -> ServerBuilder.builder().addHandler(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ServerBuilder.builder().addResponseCompleteListener(null))
                .isInstanceOf(NullPointerException.class);
    }
}
