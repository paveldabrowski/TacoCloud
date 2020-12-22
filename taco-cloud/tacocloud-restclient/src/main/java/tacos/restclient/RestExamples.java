package tacos.restclient;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Taco;

@SpringBootConfiguration
@ComponentScan
@Slf4j
public class RestExamples {

  public static void main(String[] args) {
    SpringApplication.run(RestExamples.class, args);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
  
  @Bean
  public CommandLineRunner fetchIngredients(TacoCloudClient tacoCloudClient) {
    return args -> {
      log.info("----------------------- GET -------------------------");
      log.info("POBIERANIE SKŁADNIKA WEDŁUG IDE");
      log.info("Składnik:  " + tacoCloudClient.getIngredientById("CHED"));
      log.info("POBIERANIE WSZYSTKICH SKŁADNIKÓW");
      List<Ingredient> ingredients = tacoCloudClient.getAllIngredients();
      log.info("Wszystkie składniki:");
      for (Ingredient ingredient : ingredients) {
        log.info("   - " + ingredient);
      }
    };
  }
  
  @Bean
  public CommandLineRunner putAnIngredient(TacoCloudClient tacoCloudClient) {
    return args -> {
      log.info("----------------------- PUT -------------------------");
      Ingredient before = tacoCloudClient.getIngredientById("LETC");
      log.info("PRZED:  " + before);
      tacoCloudClient.updateIngredient(new Ingredient("LETC", "kawałki sałaty", Ingredient.Type.VEGGIES));
      Ingredient after = tacoCloudClient.getIngredientById("LETC");
      log.info("PO:  " + after);
    };
  }
  
  @Bean
  public CommandLineRunner addAnIngredient(TacoCloudClient tacoCloudClient) {
    return args -> {
      log.info("----------------------- POST -------------------------");
      Ingredient chix = new Ingredient("CHIX", "kawałki kurczaka", Ingredient.Type.PROTEIN);
      Ingredient chixAfter = tacoCloudClient.createIngredient(chix);
      log.info("AFTER=1:  " + chixAfter);
      Ingredient beefFajita = new Ingredient("BFFJ", "fajita z wołowiny", Ingredient.Type.PROTEIN);
      URI uri = tacoCloudClient.createIngredient2(beefFajita);
      log.info("AFTER-2:  " + uri);      
      Ingredient shrimp = new Ingredient("SHMP", "krewetki", Ingredient.Type.PROTEIN);
      Ingredient shrimpAfter = tacoCloudClient.createIngredient3(shrimp);
      log.info("AFTER-3:  " + shrimpAfter);      
    };
  }

  
  @Bean
  public CommandLineRunner deleteAnIngredient(TacoCloudClient tacoCloudClient) {
    return args -> {
      log.info("----------------------- DELETE -------------------------");
      Ingredient before = tacoCloudClient.getIngredientById("CHIX");
      log.info("PRZED:  " + before);
      tacoCloudClient.deleteIngredient(before);
      Ingredient after = tacoCloudClient.getIngredientById("CHIX");
      log.info("PO:  " + after);
      before = tacoCloudClient.getIngredientById("BFFJ");
      log.info("PRZED:  " + before);
      tacoCloudClient.deleteIngredient(before);
      after = tacoCloudClient.getIngredientById("BFFJ");
      log.info("PO:  " + after);
      before = tacoCloudClient.getIngredientById("SHMP");
      log.info("PRZED:  " + before);
      tacoCloudClient.deleteIngredient(before);
      after = tacoCloudClient.getIngredientById("SHMP");
      log.info("PO:  " + after);
    };
  }
  
  //
  // Przykłady użycia Traverson
  //
  
  @Bean
  public Traverson traverson() {
    Traverson traverson = new Traverson(
        URI.create("http://localhost:8080/api"), MediaTypes.HAL_JSON);
    return traverson;
  }
  
  @Bean
  public CommandLineRunner traversonGetIngredients(TacoCloudClient tacoCloudClient) {
    return args -> {
      Iterable<Ingredient> ingredients = tacoCloudClient.getAllIngredientsWithTraverson();
      log.info("----------------------- POBRANIE SKŁADNIKÓW ZA POMOCĄ TRAVERSON -------------------------");
      for (Ingredient ingredient : ingredients) {
        log.info("   -  " + ingredient);
      }
    };
  }
  
  @Bean
  public CommandLineRunner traversonSaveIngredient(TacoCloudClient tacoCloudClient) {
    return args -> {
      Ingredient pico = tacoCloudClient.addIngredient(
          new Ingredient("PICO", "Pico de Gallo", Ingredient.Type.SAUCE));
      List<Ingredient> allIngredients = tacoCloudClient.getAllIngredients();
      log.info("----------------------- WSZYSTKIE SKŁADNIKI PO ZAPISANIU PICO -------------------------");
      for (Ingredient ingredient : allIngredients) {
        log.info("   -  " + ingredient);
      }
      tacoCloudClient.deleteIngredient(pico);
    };
  }

}
