package tacos.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;
import tacos.Taco;

import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbc;
    private IngredientRepository ingredientRepository;

//    @Autowired
    public JdbcTacoRepository(final JdbcTemplate jdbc, final IngredientRepository ingredientRepository) {
        this.jdbc = jdbc;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for(String ingredientId : taco.getIngredients()){

            saveIngredientToTaco(ingredientRepository.findById(ingredientId), tacoId);
        }
        return taco;
    }

    private long saveTacoInfo(final Taco taco) {
        taco.setCreatedAt(new Date());
        LocalDateTime localDateTime = LocalDateTime.now();
        PreparedStatementCreator psc =
                new PreparedStatementCreatorFactory(
                        "insert into TACO (name, createdAt) values ( ?,? )",
                        Types.VARCHAR, Types.TIMESTAMP
                ).newPreparedStatementCreator(Arrays.asList(taco.getName(), taco.getCreatedAt().getTime()));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(final Ingredient ingredient, final long tacoId) {
        jdbc.update(
                "insert into TACO_INGREDIENTS(taco, ingredient)" +
                        "values ( ?,? )",
                tacoId, ingredient.getId());

    }
}
