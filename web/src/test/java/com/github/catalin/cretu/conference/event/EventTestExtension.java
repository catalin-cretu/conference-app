package com.github.catalin.cretu.conference.event;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.togglz.spring.boot.autoconfigure.TogglzAutoConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Import({ EventConfig.class, RepositoryConfig.class, TogglzAutoConfiguration.class })
@WebMvcTest(controllers = EventsController.class)
@ActiveProfiles({ "events-controller", "test" })
@SpringBootTest
@ExtendWith(SpringExtension.class)
@interface EventTestExtension {
    //no-op
}
