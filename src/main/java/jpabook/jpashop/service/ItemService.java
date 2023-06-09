package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long item_id) {
        return itemRepository.findOne(item_id);
    }

    @Transactional
    public void updatedItem(Long itemId, int price, String name, int stockQuantity) {
        Item foundItem = itemRepository.findOne(itemId);
        foundItem.change(name, price, stockQuantity);
//        foundItem.setName(name); // setter쓰지말자
//        foundItem.setPrice(price);
//        foundItem.setStockQuantity(stockQuantity);


    }
}
