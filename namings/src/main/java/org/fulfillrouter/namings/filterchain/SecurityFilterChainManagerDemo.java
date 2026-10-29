package org.fulfillrouter.namings.filterchain;

import java.util.List;



record Request(String path) {}

interface Filter {
    void doFilter(Request request);
}

class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(Request request) {
        System.out.println("filter auth: " + request.path());
    }
}

class LoggingFilter implements Filter {
    @Override
    public void doFilter(Request request) {
        System.out.println("filter log: " + request.path());
    }
}

class AdminPermissionFilter implements Filter {
    @Override
    public void doFilter(Request request) {
        System.out.println("filter admin permission: " + request.path());
    }
}

interface SecurityFilterChain {
    boolean matches(Request request);
    List<Filter> getFilters();
}

class NormalSecurityFilterChain implements SecurityFilterChain {
    private final List<Filter> filters = List.of(
            new LoggingFilter(),
            new AuthenticationFilter()
    );

    @Override
    public boolean matches(Request request) {
        return !request.path().startsWith("/admin");
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}

class AdminSecurityFilterChain implements SecurityFilterChain {
    private final List<Filter> filters = List.of(
            new LoggingFilter(),
            new AuthenticationFilter(),
            new AdminPermissionFilter()
    );

    @Override
    public boolean matches(Request request) {
        return request.path().startsWith("/admin");
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}

class SecurityFilterChainManager {
    private final List<SecurityFilterChain> chains;

    SecurityFilterChainManager(List<SecurityFilterChain> chains) {
        this.chains = chains;
    }

    public void handle(Request request) {
        SecurityFilterChain selected = chains.stream()
                .filter(chain -> chain.matches(request))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("No matching security filter chain found for " + request.path())
                );

        for (Filter filter : selected.getFilters()) {
            filter.doFilter(request);
        }
    }
}

public class SecurityFilterChainManagerDemo {
    public static void main(String[] args) {
        var manager = new SecurityFilterChainManager(List.of(
                new AdminSecurityFilterChain(),
                new NormalSecurityFilterChain()
        ));

        manager.handle(new Request("/admin/users"));
        System.out.println("---");
        manager.handle(new Request("/orders"));
    }
}
