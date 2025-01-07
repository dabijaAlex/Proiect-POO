package org.poo.app;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.commerciants.Commerciant;

import java.util.HashMap;

public class CommerciantMap {
    @Getter @Setter
    private static HashMap<String, Commerciant> commerciantsMap;
}
