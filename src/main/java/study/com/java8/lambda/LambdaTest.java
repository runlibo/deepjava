package study.com.java8.lambda;


/**
 * Created by runlibo.li on 2018/1/22.
 *
 * @author runlibo.li
 */
public class LambdaTest {

    public static void main(String[] args) {
        PersonFactory<Person> personPersonFactory = Person::new;
        Person person = personPersonFactory.create("Li", "Bo");
        System.out.println(person);
    }

}
