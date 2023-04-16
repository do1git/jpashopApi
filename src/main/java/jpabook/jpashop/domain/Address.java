package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter //getter 만 만든이유는 값타입은 immutable해야함.  생성할때만 할당하자
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address(){}
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
