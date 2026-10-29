package org.fulfillrouter.namings.filterchain;

import java.util.List;

public class OrderPlanDemo {

    public static void main(String[] args) {
        List<OrderProcessingPlan> plans = List.of(new NormalOrderPlan(), new HighValueOrderPlan());

        for (Order order : List.of(new Order(500), new Order(25_000))) {
            OrderProcessingPlan plan = plans.stream()
                    .filter(p -> p.matches(order))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No plan for " + order));

            System.out.println(order + " -> " + plan.getClass().getSimpleName());
            plan.getSteps().forEach(step -> System.out.println("  - " + step.name()));
        }
    }
}



record Order(double amount) {}

record Step(String name) {}

interface OrderProcessingPlan {
    boolean matches(Order order);
    List<Step> getSteps();
}

class NormalOrderPlan implements OrderProcessingPlan {

    @Override
    public boolean matches(Order order) {
        return order.amount() < 10_000;
    }

    @Override
    public List<Step> getSteps() {
        return List.of(
                new Step("Check inventory"),
                new Step("Deduct inventory")
        );
    }
}


class HighValueOrderPlan implements OrderProcessingPlan {

    @Override
    public boolean matches(Order order) {
        return order.amount() >= 10_000;
    }

    @Override
    public List<Step> getSteps() {
        return List.of(
                new Step("Manual review"),
                new Step("Check inventory"),
                new Step("Deduct inventory")
        );
    }
}
