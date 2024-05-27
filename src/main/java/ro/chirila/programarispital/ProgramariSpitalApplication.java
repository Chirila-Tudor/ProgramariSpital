package ro.chirila.programarispital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProgramariSpitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgramariSpitalApplication.class, args);
    }

}
