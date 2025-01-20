package org.poo.app;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.commerciants.ClothesCommerciant;
import org.poo.app.commerciants.Commerciant;
import org.poo.app.commerciants.FoodCommerciant;
import org.poo.app.commerciants.TechCommerciant;
import org.poo.fileio.CommerciantInput;
import org.poo.fileio.ObjectInput;

import java.util.HashMap;

public class CommerciantMap {
    @Getter @Setter
    private static HashMap<String, Commerciant> commerciantsMap;

    public CommerciantMap(ObjectInput input) {
        HashMap<String, Commerciant> commerciants = new HashMap<>();
        CommerciantMap.setCommerciantsMap(commerciants);
        for (CommerciantInput commerciantInput: input.getCommerciants()) {
            String commerciantIban = commerciantInput.getAccount();
            if (commerciantInput.getType().equals("Food")) {
                commerciants.put(commerciantIban, new FoodCommerciant(commerciantInput));
            }
            if (commerciantInput.getType().equals("Clothes")) {
                commerciants.put(commerciantIban, new ClothesCommerciant(commerciantInput));
            }
            if (commerciantInput.getType().equals("Tech")) {
                commerciants.put(commerciantIban, new TechCommerciant(commerciantInput));
            }

            //  put the name of the commerciant also
            commerciants.put(commerciantInput.getCommerciant(), commerciants.get(commerciantIban));
            Integer idInt = new Integer(commerciantInput.getId());
            commerciants.put(idInt.toString(), commerciants.get(commerciantIban));
        }
    }
}
