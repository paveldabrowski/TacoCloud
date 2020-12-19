package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tacos.Order;
import tacos.Taco;
import tacos.data.OrderRepository;
import tacos.data.TacoRepository;

import javax.swing.text.FieldView;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping(path = "design", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class DesignTacoController {
    private TacoRepository tacoRepo;
    private OrderRepository orderRepo;

    @Autowired
    EntityLinks entityLinks;

    public DesignTacoController(final TacoRepository tacoRepo, final OrderRepository orderRepo){
        this.tacoRepo = tacoRepo;
        this.orderRepo = orderRepo;
    }

//    @GetMapping("/recent")
//    public Resources<Resource<Taco>> recentTacos(){
//        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
//
//        List<Taco> tacos = tacoRepo.findAll(page).getContent();
//        Resources<Resource<Taco>> recentResources = Resources.wrap(tacos);
//        recentResources.add(new Link("http://localhost:8080/design/recent", "recents"));
//        return recentResources;
//    }
//    @GetMapping("/recent")
//    public Resources<Resource<Taco>> recentTacos(){
//        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
//
//        List<Taco> tacos = tacoRepo.findAll(page).getContent();
//        Resources<Resource<Taco>> recentResources = Resources.wrap(tacos);
//        recentResources.add(ControllerLinkBuilder.linkTo(DesignTacoController.class).slash("recent").withRel("recents"));
//        return recentResources;
//    }

    @GetMapping("/recent")
    public Resources<Resource<Taco>> recentTacos(){
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());

        List<Taco> tacos = tacoRepo.findAll(page).getContent();
        Resources<Resource<Taco>> recentResources = Resources.wrap(tacos);
        recentResources.add(linkTo(methodOn(DesignTacoController.class).recentTacos()).withRel("recents"));
        return recentResources;
    }



    @GetMapping("/{id}")
    public ResponseEntity<Taco> findById(@PathVariable("id") Long id){
        Optional<Taco> optionalTaco = tacoRepo.findById(id);
        if(optionalTaco.isPresent())
            return new ResponseEntity<>(optionalTaco.get(), HttpStatus.OK);

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody Taco taco){
        return tacoRepo.save(taco);
    }

}
