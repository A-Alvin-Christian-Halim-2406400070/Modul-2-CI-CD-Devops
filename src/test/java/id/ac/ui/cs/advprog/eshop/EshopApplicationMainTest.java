package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class EshopApplicationMainTest {

    @Test
    void mainDelegatesToSpringApplicationRun() {
        String[] args = {"--spring.profiles.active=test", "--server.port=9090"};

        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            EshopApplication.main(args);

            springApplicationMock.verify(() -> SpringApplication.run(EshopApplication.class, args));
        }
    }
}
