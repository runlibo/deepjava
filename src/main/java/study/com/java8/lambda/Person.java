package study.com.java8.lambda;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by runlibo.li on 2018/1/22.
 *
 * @author runlibo.li
 */
@Getter
@Setter
@ToString
public class Person {
    String firstName;

    String lastName;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

