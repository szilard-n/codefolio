package com.codefolio.util;

import com.codefolio.entity.Project;
import com.codefolio.entity.Task;
import com.codefolio.entity.User;
import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@UtilityClass
public class Constants {

    public static final User USER_1 = User.builder()
            .id(UUID.fromString("00000000-2000-0000-0000-000000000000"))
            .username("testUser")
            .password("$2a$12$Cb47wYaqUmGWzHFiLSCdfeLPf81hjKnf.0nlyAzhgbTleO/oY9d1y")
            .email("testUser@gmail.com")
            .build();

    public static final User USER_2 = User.builder()
            .id(UUID.fromString("00000000-3000-0000-0000-000000000000"))
            .username("secondUser")
            .password("$2a$12$Cb47wYaqUmGWzHFiLSCdfeLPf81hjKnf.0nlyAzhgbTleO/oY9d1y")
            .email("secondUser@gmail.com")
            .build();

    public static final User USER_3 = User.builder()
            .id(UUID.fromString("00000000-4000-0000-0000-000000000000"))
            .username("thirdUser")
            .password("$2a$12$Cb47wYaqUmGWzHFiLSCdfeLPf81hjKnf.0nlyAzhgbTleO/oY9d1y")
            .email("thirdUser@gmail.com")
            .build();

    public static final Project PROJECT_1 = Project.builder()
            .id(UUID.fromString("00000000-2000-0000-0000-000000000000"))
            .category("BACKEND")
            .title("Platform for projects")
            .description("App where people can post their side project ideas")
            .createdBy(USER_1.getId())
            .createdAt(Timestamp.from(Instant.now()))
            .updatedAt(Timestamp.from(Instant.now()))
            .build();

    public static final Project PROJECT_2 = Project.builder()
            .id(UUID.fromString("00000000-3000-0000-0000-000000000000"))
            .category("UI/UX")
            .title("Shop for books")
            .description("Create the design for a shop for books")
            .createdBy(USER_2.getId())
            .createdAt(Timestamp.from(Instant.now()))
            .updatedAt(Timestamp.from(Instant.now()))
            .build();
}
