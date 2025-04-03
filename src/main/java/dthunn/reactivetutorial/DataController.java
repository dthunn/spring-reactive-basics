package dthunn.reactivetutorial;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Map;

@RestController
public class DataController {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public DataController(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @PostMapping("/customer/create")
    public Mono<Customer> createCustomer(@RequestBody Customer customer) {
        return reactiveMongoTemplate.save(customer);
    }

    @GetMapping("/customer/find-by-id")
    public Mono<Customer> findCustomerById(@RequestParam("customerId") String customerId) {
        return getCustomerById(customerId);
    }

    @PostMapping("/order/create")
    public Mono<Order> createOrder(@RequestBody Order order) {
        return reactiveMongoTemplate.save(order);
    }


    @GetMapping("/sales/summary")
    public Mono<Map<String, Double>> calculateSummary() {
        return reactiveMongoTemplate.findAll(Customer.class)
                .flatMap(customer -> Mono.zip(Mono.just(customer), calculateOrderSum(customer.getId())))
                .collectMap(tuple2 -> tuple2.getT1().getName(), Tuple2::getT2);
    }

    private Mono<Double> calculateOrderSum(String customerId) {
        Criteria criteria = Criteria.where("customerId").is(customerId);
        return reactiveMongoTemplate.find(Query.query(criteria), Order.class)
                .map(Order::getTotal)
                .reduce(0d, Double::sum);
    }

    private Mono<Customer> getCustomerById(String customerId) {
        Criteria criteria = Criteria.where("id").is(customerId);
        Query query = Query.query(criteria);
        return reactiveMongoTemplate.findOne(query, Customer.class)
                .log();
    }
}
