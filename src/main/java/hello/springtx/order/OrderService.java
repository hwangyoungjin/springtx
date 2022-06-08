package hello.springtx.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void order(Order order) throws NotEnoughMoneyException {
        log.info("order 호출");
        orderRepository.save(order);

        log.info("결제 프로세스 진입");
        if(order.getUsername().equals("예외")){
            log.info("시스템 예외 발생");
            //runtime 예외는 rollback 된다.
            throw new RuntimeException();
        } else if(order.getUsername().equals("잔고부족")){
            log.info("잔고부족 비즈니스 예외 발생");
            order.setPayStatus("대기");
            //checked 예외는 commit 된다.
            throw new NotEnoughMoneyException("잔고가 부족합니다.");
        } else { //정상
            log.info("정상 승인");
            order.setPayStatus("완료");
        }
        log.info("결제 프로세스 종료");
    }
}
