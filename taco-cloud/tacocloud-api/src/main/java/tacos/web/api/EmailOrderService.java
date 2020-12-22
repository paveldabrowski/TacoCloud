package tacos.web.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import tacos.Ingredient;
import tacos.Order;
import tacos.PaymentMethod;
import tacos.Taco;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.PaymentMethodRepository;
import tacos.data.UserRepository;
import tacos.web.api.EmailOrder.EmailTaco;

@Service
public class EmailOrderService {

  private UserRepository userRepo;
  private IngredientRepository ingredientRepo;
  private PaymentMethodRepository paymentMethodRepo;

  public EmailOrderService(UserRepository userRepo, IngredientRepository ingredientRepo,
      PaymentMethodRepository paymentMethodRepo) {
    this.userRepo = userRepo;
    this.ingredientRepo = ingredientRepo;
    this.paymentMethodRepo = paymentMethodRepo;
  }

  public Order convertEmailOrderToDomainOrder(EmailOrder emailOrder) {
    // TODO: Prawdopodobnie należy obsłużyć sytuację, w której adres e-mail nie pasuje podnamu użytkownikowi
    //       lub jeśli użytkownik nie podał przynajmniej jednej metody płatności.
    User user = userRepo.findByEmail(emailOrder.getEmail());
    PaymentMethod paymentMethod = paymentMethodRepo.findByUserId(user.getId());

    Order order = new Order();
    order.setUser(user);
    order.setCcNumber(paymentMethod.getCcNumber());
    order.setCcCVV(paymentMethod.getCcCVV());
    order.setCcExpiration(paymentMethod.getCcExpiration());
    order.setDeliveryName(user.getFullname());
    order.setDeliveryStreet(user.getStreet());
    order.setDeliveryCity(user.getCity());
    order.setDeliveryState(user.getState());
    order.setDeliveryZip(user.getZip());
    order.setPlacedAt(new Date());

    // TODO: Obsługa sytuacji, w której dany składnik nie został dopasowany
    List<EmailTaco> emailTacos = emailOrder.getTacos();
    for (EmailTaco emailTaco : emailTacos) {
      Taco taco = new Taco();
      taco.setName(emailTaco.getName());
      List<String> ingredientIds = emailTaco.getIngredients();
      List<Ingredient> ingredients = new ArrayList<>();
      for (String ingredientId : ingredientIds) {
        Optional<Ingredient> optionalIngredient = ingredientRepo.findById(ingredientId);
        if (optionalIngredient.isPresent()) {
          ingredients.add(optionalIngredient.get());
        }
      }
      taco.setIngredients(ingredients);
      order.addDesign(taco);
    }

    return order;
  }

}
