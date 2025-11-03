package com.jonah.portfolio.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class DependencyScannerCommandTest {

    @Test
    void shouldDetectSnapshotDependencies() throws IOException {
        Path pom = Files.createTempFile("test", "pom.xml");
        Files.writeString(pom, """
                <project xmlns=\"http://maven.apache.org/POM/4.0.0\"
                         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
                         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">
                  <modelVersion>4.0.0</modelVersion>
                  <groupId>com.example</groupId>
                  <artifactId>demo</artifactId>
                  <version>1.0.0</version>
                  <dependencies>
                    <dependency>
                      <groupId>com.acme</groupId>
                      <artifactId>sample</artifactId>
                      <version>1.0-SNAPSHOT</version>
                    </dependency>
                  </dependencies>
                </project>
                """);

        DependencyScannerCommand command = new DependencyScannerCommand();
        new picocli.CommandLine(command).parseArgs(pom.toString());

        int exitCode = command.call();
        assertThat(exitCode).isEqualTo(1);
    }

    @Test
    void shouldPassWhenAllDependenciesPinned() throws IOException {
        Path pom = Files.createTempFile("test", "pom.xml");
        Files.writeString(pom, """
                <project xmlns=\"http://maven.apache.org/POM/4.0.0\"
                         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
                         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">
                  <modelVersion>4.0.0</modelVersion>
                  <groupId>com.example</groupId>
                  <artifactId>demo</artifactId>
                  <version>1.0.0</version>
                  <dependencies>
                    <dependency>
                      <groupId>com.acme</groupId>
                      <artifactId>sample</artifactId>
                      <version>1.2.3</version>
                    </dependency>
                  </dependencies>
                </project>
                """);

        DependencyScannerCommand command = new DependencyScannerCommand();
        new picocli.CommandLine(command).parseArgs(pom.toString());

        int exitCode = command.call();
        assertThat(exitCode).isZero();
    }
}
