package tacos.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * <p>Obsługa treści wiadomości e-mail wraz z zamówieniem taco...</p>
 *  <li> Adres e-mail zamawiającego jest adresem e-mail wysyłającego</li>
 *  <li> Tematem wiadomości *musi* być "ZAMÓWIENIE TACO", ponieważ w przeciwnym razie wiadomość zostanie zignorowana</li>
 *  <li> Każdy wiersz wiadomości rozpoczyna się od nazwy przygotowanego taco,
 *    a następnie znajduje się rozdzielona przecinkami lista składników.</li>
 *    
 * <p>Nazwy składników zostają dopasowane do zbioru za pomocą algorytmu LevenshteinDistance.
 * Przykładowo "wołowina" zostanie dopasowana do "mielona wołowina" i mapowana na "GRBF", natomiast "kukurydziana"
 * będzie dopasowana do "mąka kukurydziana" i mapowana na "COTO".</p>
 * 
 * <p>Oto przykładowa wiadomość e-mail:</p>
 * 
 * <code>
 * Corn Carnitas: kukurydziana, kawałki mięsa, sałata, pomidory, cheddar<br/>
 * Veggielicious: pszenna, pomidory, sałata, pikantny sos pomidorowy
 * </code>
 * 
 * <p>To oznacza zamówienie dwóch taco o nazwach "Corn Carnitas" i "Veggielicious".
 * Składniki to {COTO, CARN, LETC, TMTO, CHED} i {FLTO,TMTO,LETC,SLSA}.</p>
 */
@Component
public class EmailToOrderTransformer
     extends AbstractMailMessageTransformer<Order> {
  
  private static final String SUBJECT_KEYWORDS = "ZAMÓWIENIE TACO";

  @Override
  protected AbstractIntegrationMessageBuilder<Order> 
                doTransform(Message mailMessage) throws Exception {
    Order tacoOrder = processPayload(mailMessage);
    return MessageBuilder.withPayload(tacoOrder);
  }
  
  private Order processPayload(Message mailMessage) {
    try {
      String subject = mailMessage.getSubject();
      if (subject.toUpperCase().contains(SUBJECT_KEYWORDS)) {
        String email = 
              ((InternetAddress) mailMessage.getFrom()[0]).getAddress();
        String content = mailMessage.getContent().toString();
        return parseEmailToOrder(email, content);
      }
    } catch (MessagingException e) {
    } catch (IOException e) {}
    return null;
  }

  private Order parseEmailToOrder(String email, String content) {
    Order order = new Order(email);
    String[] lines = content.split("\\r?\\n");
    for (String line : lines) {
      if (line.trim().length() > 0 && line.contains(":")) {
        String[] lineSplit = line.split(":");
        String tacoName = lineSplit[0].trim();
        String ingredients = lineSplit[1].trim();
        String[] ingredientsSplit = ingredients.split(",");
        List<String> ingredientCodes = new ArrayList<>();
        for (String ingredientName : ingredientsSplit) {
          String code = lookupIngredientCode(ingredientName.trim());
          if (code != null) {
            ingredientCodes.add(code);
          }
        }
        
        Taco taco = new Taco(tacoName);
        taco.setIngredients(ingredientCodes);
        order.addTaco(taco);
      }
    }
    return order;
  }
  
  private String lookupIngredientCode(String ingredientName) {
    for (Ingredient ingredient : ALL_INGREDIENTS) {
      String ucIngredientName = ingredientName.toUpperCase();
      if (LevenshteinDistance.getDefaultInstance().apply(ucIngredientName, ingredient.getName()) < 3 ||
          ucIngredientName.contains(ingredient.getName()) ||
          ingredient.getName().contains(ucIngredientName)) {
        return ingredient.getCode();
      }
    }
    return null;
  }
  
  private static Ingredient[] ALL_INGREDIENTS = new Ingredient[] {
      new Ingredient("FLTO", "pszenna"),
      new Ingredient("COTO", "kukurydziana"),
      new Ingredient("GRBF", "mielona wołowina"),
      new Ingredient("CARN", "kawałki mięsa"),
      new Ingredient("TMTO", "pomidory"),
      new Ingredient("LETC", "sałata"),
      new Ingredient("CHED", "cheddar"),
      new Ingredient("JACK", "Monterrey Jack"),
      new Ingredient("SLSA", "pikantny sos pomidorowy"),
      new Ingredient("SRCR", "śmietana")
  };
}
  