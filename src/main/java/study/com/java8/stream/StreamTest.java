package study.com.java8.stream;

import com.google.common.collect.Lists;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by runlibo.li on 2018/1/22.
 *
 * @author runlibo.li
 */
public class StreamTest {

    public static List<String> getList() {
        return Lists.newArrayList("aaa1", "aaa2", "bbb1", "ccc1", "ddd1", "ddd2");
    }

    public static void testCreate() {
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4);
        Stream<String> stringStream = Stream.of("stream");

        Stream.generate(Math::random).limit(10).forEach(System.out::println);

        Stream.iterate(1, item -> item + 1).limit(10).forEach(System.out::println);
    }

    public static void testFilter() {
        StreamTest.getList().stream().filter(s -> s.contains("1")).forEach(System.out::println);
        System.out.println();

        StreamTest.getList().stream().filter(s -> s.contains("1")).sorted(Comparator.reverseOrder()).forEach(System.out::println);
        System.out.println();

        StreamTest.getList().stream().map(String::toUpperCase).forEach(System.out::println);
        System.out.println();
    }


    public static void testMatch() {
        System.out.println(StreamTest.getList().stream().anyMatch(s -> s.startsWith("1")));
        System.out.println(StreamTest.getList().stream().allMatch(s -> s.startsWith("1")));
        System.out.println(StreamTest.getList().stream().noneMatch(s -> s.startsWith("1")));
        System.out.println(StreamTest.getList().stream().count());
    }

    public static void main(String[] args) throws FileNotFoundException {
        //testFilter();
        //testMatch();
    }
}
