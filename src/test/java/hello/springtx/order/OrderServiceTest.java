package hello.springtx.order;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.h2.engine.Right;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderServiceTest {
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void complete() throws NotEnoughMoneyException {
        //given
        Order order = new Order();
        order.setUsername("정상");

        //when
        orderService.order(order);

        //then
        Optional<Order> order1 = orderRepository.findById(order.getId());
        assertThat(order1.get().getPayStatus().equals("완료")).isTrue();
    }

    @Test
    void runtimeException() throws NotEnoughMoneyException {
        //given
        Order order = new Order();
        order.setUsername("예외");

        //when
        assertThatThrownBy(()-> orderService.order(order))
                .isInstanceOf(RuntimeException.class);

        //then
        Optional<Order> order1 = orderRepository.findById(order.getId());
        assertThat(order1.isEmpty()).isTrue();
    }

    /**
     * 비지니스 Exception
     * @throws NotEnoughMoneyException
     */
    @Test
    void checkedException(){
        //given
        Order order = new Order();
        order.setUsername("잔고부족");

        //when
        try {
            orderService.order(order);
        } catch (NotEnoughMoneyException e) {
            log.info("클라이언트에서 고객에게 잔고부족을 알리고 다른 계좌로 입금하도록 안내");
        }

        //then
        Optional<Order> order1 = orderRepository.findById(order.getId());
        assertThat(order1.get().getPayStatus().equals("대기")).isTrue();
    }
}