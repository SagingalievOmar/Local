package Local;

import org.springframework.boot.SpringApplication;

public class TestLocalApplication {

    public static void main(String[] args) {
        SpringApplication.from(LocalApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
