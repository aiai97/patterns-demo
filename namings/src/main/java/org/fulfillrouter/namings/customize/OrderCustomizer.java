package org.fulfillrouter.namings.customize;


import java.util.function.Consumer;

class OrderConfigurer {

    private boolean checkInventory = true;
    private boolean chargePayment = true;

    public OrderConfigurer checkInventory(boolean enabled) {
        this.checkInventory = enabled;
        return this;
    }

    public OrderConfigurer chargePayment(boolean enabled) {
        this.chargePayment = enabled;
        return this;
    }

    public boolean isCheckInventory() {
        return checkInventory;
    }

    public boolean isChargePayment() {
        return chargePayment;
    }
}

class OrderService {

    public OrderService configure(Consumer<OrderConfigurer> customizer) {

        OrderConfigurer configurer = new OrderConfigurer();

        customizer.accept(configurer);

        System.out.println("Check inventory: "
                + configurer.isCheckInventory());
        System.out.println("Charge payment: "
                + configurer.isChargePayment());

        return this;
    }
}


public class OrderCustomizer {
    public static void main(String[] args) {
        OrderService service = new OrderService();

        service.configure(configurer -> configurer
                .checkInventory(true)
                .chargePayment(false));
    }
}
