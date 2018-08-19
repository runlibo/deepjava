package study.com.java8.function;

import com.google.common.base.Strings;
import study.com.java8.lambda.Person;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by runlibo.li on 2018/1/22.
 *
 * @author runlibo.li
 */
public class Predicates {

    public static void main(String[] args) {
        Predicate<String> predicate = (s) -> s.length() > 0;
        System.out.println(predicate.test("foo"));
        System.out.println(predicate.negate().test("foo"));

        Predicate<Boolean> nonNull = Objects::nonNull;
        System.out.println(nonNull.test(false));

        Predicate<String> isEmpty = Strings::isNullOrEmpty;
        System.out.println(isEmpty.test(""));
        System.out.println(isEmpty.test(null));

        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        System.out.println(backToString.apply("123"));

        Function<Integer, Integer> backInteger = toInteger.compose(String::valueOf);
        System.out.println(backInteger.apply(123));

        Supplier<Person> personSupplier = Person::new;
        System.out.println(personSupplier.get());

        Consumer<Person> personConsumer = System.out::println;
        personConsumer.accept(new Person("Li", "Bo"));

    }

}
