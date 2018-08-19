package study.com.java8.lambda;

/**
 * Created by runlibo.li on 2018/1/22.
 *
 * @author runlibo.li
 */
@FunctionalInterface
public interface PersonFactory<T extends Person> {

    T create(String firstName, String lastName);

    default String descrision() {
        return "default";
    }
}
