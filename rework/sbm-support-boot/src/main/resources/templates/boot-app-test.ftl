<#if packageName?has_content>
package ${packageName};

</#if>
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ${className} {

    @Test
    void contextLoads() {
    }

}
