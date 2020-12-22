package tacos.kitchen;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import tacos.Order;

@Component
@Slf4j
public class KitchenUI {

  public void displayOrder(Order order) {
    // TODO: Trzeba rozbudować ten kod, aby jego działanie nie polegało wyłączenie na zapisaniu taco.
    //       Informacje powinny być wyświetlane w pewnym interfejsie użytkownika.
    log.info("RECEIVED ORDER:  " + order);
  }
  
}
