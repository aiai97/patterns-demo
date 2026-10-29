package org.fulfillrouter.namings.customize;

import java.util.function.Consumer;

class NotificationConfigurer {
    private boolean emailEnabled = true;
    private boolean smsEnabled = false;

    public NotificationConfigurer email(boolean enabled) {
        this.emailEnabled = enabled;
        return this;
    }

    public NotificationConfigurer sms(boolean enabled) {
        this.smsEnabled = enabled;
        return this;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public boolean isSmsEnabled() {
        return smsEnabled;
    }
}

class NotificationService {
    public void configure1(
            NotificationConfigurer configurer) {

        System.out.println("Email: " + configurer.isEmailEnabled());
        System.out.println("SMS: " + configurer.isSmsEnabled());
    }

    public void configure(
            Consumer<NotificationConfigurer> customizer) {

        NotificationConfigurer configurer =
                new NotificationConfigurer();

        customizer.accept(configurer);

        System.out.println("Email: " + configurer.isEmailEnabled());
        System.out.println("SMS: " + configurer.isSmsEnabled());
    }
}

public class NotificationCustomer{
    public static void main(String[] args) {
        NotificationService service = new NotificationService();

        service.configure(configurer -> configurer
                .email(false)
                .sms(true));
    }
}
