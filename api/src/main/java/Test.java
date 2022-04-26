import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/4/25 14:49
 */
public class Test {

    public static void main(String[] args) {

        List<Integer> primes = Arrays.asList(2, 3,5,7);
        int factor = 2;
        primes.forEach((element)->{
            System.out.println(factor*element);
        });
        List<Object> objects = new ArrayList<>(Collections.singletonList("jas"));
        List<Object> collect = objects.stream().filter("js"::equals).collect(Collectors.toList());
        System.out.println(collect);
    }
}
