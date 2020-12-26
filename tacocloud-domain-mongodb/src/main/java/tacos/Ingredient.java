package tacos;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
//@RequiredArgsConstructor
//@NoArgsConstructor(access=AccessLevel.PUBLIC, force=true)
@Document
public class Ingredient {
  
  @Id
  private String id;
  private String name;
  private Type type;

  public Ingredient(){

  }


 public Ingredient(final String id, final String name, final Type type) {
    this.id = id;
    this.name = name;
    this.type = type;
  }

  public static enum Type {
    WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
  }

}
