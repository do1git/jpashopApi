package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = getMember();

        Book book = getBook("jjppaa jjang", 10000, 10);

        int orderCnt = 3;

        //when
        Long order_id = orderService.order(member.getId(), book.getId(), orderCnt);

        //then
        Order foundOrder = orderRepository.findOne(order_id);
        Assert.assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, foundOrder.getStatus());
        Assert.assertEquals("주문 종류 수 일치", 1, foundOrder.getOrderItems().size());
        Assert.assertEquals("주문 가격 = 가격 * 수량", 30000, foundOrder.getTotalPrice());
        Assert.assertEquals("주문 수량만큼 재고가 줄어야 한다.", 7, book.getStockQuantity());
    }

    private Book getBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member getMember() {
        Member member = new Member();
        member.setName("dudu");
        member.setAddress(new Address("Seoul", "hangang-daero", "06000"));
        em.persist(member);
        return member;
    }

    @Test(expected = NotEnoughStockException.class)
    public void 예외_상품재고초과주문() throws Exception {
        //given
        Member member = getMember();
        Book item = getBook("jjppaa jjang", 10000, 10);

        int orderCnt = 11;

        //when
        orderService.order(member.getId(), item.getId(), orderCnt);

        //then
        // 혹시나 걸러지지 않으면 경고표시하자
        fail("제고 수량 예외가 떠야하는데...");
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = getMember();
        Book book = getBook("lalla", 10000, 10);

        int orderCnt = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCnt);

        //when
        Assert.assertEquals("물품감소", 8, book.getStockQuantity());
        orderService.cancelOrder(orderId);

        //then
        Order check_order = orderRepository.findOne(orderId);

        Assert.assertEquals("주문상태 취소여야함", OrderStatus.CANCEL,check_order.getStatus());
        Assert.assertEquals("물품갯수 원복", 10, book.getStockQuantity());


    }
}