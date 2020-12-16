package tacos.data;

import tacos.Order;
import tacos.Taco;

public interface OrderRepository {
    Order save(Order order);
}
