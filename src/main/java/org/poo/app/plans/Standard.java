package org.poo.app.plans;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class Standard extends ServicePlan {
    private String name = "Standard";

    @Override
    public ServicePlan upgrade() {
        return new Silver();
    }
}
