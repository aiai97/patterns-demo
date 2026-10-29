package org.fulfillrouter.namings.customize;

import java.util.function.Consumer;

class FormLoginConfigurer {

    private String loginPage = "/login";
    private String usernameParameter = "username";
    private String passwordParameter = "password";

    public FormLoginConfigurer loginPage(String path) {
        this.loginPage = path;
        return this;
    }

    public FormLoginConfigurer usernameParameter(String name) {
        this.usernameParameter = name;
        return this;
    }

    public FormLoginConfigurer passwordParameter(String name) {
        this.passwordParameter = name;
        return this;
    }

    public void printConfig() {
        System.out.println("login in which page：" + loginPage);
        System.out.println("username field：" + usernameParameter);
        System.out.println("password field：" + passwordParameter);
    }
}

class HttpSecurity {

    public HttpSecurity formLogin(
            Consumer<FormLoginConfigurer> customizer) {

        FormLoginConfigurer configurer =
                new FormLoginConfigurer();

        customizer.accept(configurer);

        configurer.printConfig();


        return this;
    }
}

public class SecurityDemo {
    public static void main(String[] args) {
        HttpSecurity http = new HttpSecurity();

        http.formLogin(form -> form
                .loginPage("/my-login")
                .usernameParameter("email")
                .passwordParameter("password")
        );
    }
}
