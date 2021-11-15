import com.threeman.web.WebApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/5/9 15:16
 */

@SpringBootTest(classes = WebApplication.class)
public class testBCrypt {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Test
    void testBCrypt() {
        System.out.println(passwordEncoder.encode("songjing"));
    }
}
