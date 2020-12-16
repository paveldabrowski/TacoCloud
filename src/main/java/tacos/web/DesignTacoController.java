package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
class DesignTacoController {
    private final IngredientRepository ingredientRepo;
    private TacoRepository designRepo;

    @Autowired
    DesignTacoController(final IngredientRepository ingredientRepo, final TacoRepository designRepo) {
        this.ingredientRepo = ingredientRepo;
        this.designRepo = designRepo;
    }

    @ModelAttribute(name = "order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco(){
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model){
       List<Ingredient> ingredients = new ArrayList<Ingredient>();
       ingredientRepo.findAll().forEach(ingredient -> ingredients.add(ingredient));

        Type[] types = Ingredient.Type.values();
        for(Type type: types){
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
        model.addAttribute("design", new Taco());
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order){
       if (errors.hasErrors()) {

           System.out.println("here");
           return "design";
       }

       Taco saved = designRepo.save(design);
       order.addDesign(saved);

       return "redirect:/orders/current";

    }

    private List<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}