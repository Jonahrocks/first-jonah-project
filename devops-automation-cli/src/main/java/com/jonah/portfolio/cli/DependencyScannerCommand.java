package com.jonah.portfolio.cli;

import info.picocli.CommandLine;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

@CommandLine.Command(name = "scan", description = "Scan Maven projects for unstable dependency versions.")
public class DependencyScannerCommand implements Callable<Integer> {

    @CommandLine.Parameters(arity = "1..*", paramLabel = "POM", description = "One or more pom.xml files to scan")
    private List<Path> poms;

    @Override
    public Integer call() {
        List<Finding> findings = poms.stream()
                .flatMap(path -> analyzePom(path).stream())
                .collect(Collectors.toList());

        if (findings.isEmpty()) {
            CommandLine.Help.Ansi.AUTO.out().println("No issues detected.\n");
            return 0;
        }

        findings.forEach(finding -> CommandLine.Help.Ansi.AUTO.out().println(finding.message()));
        CommandLine.Help.Ansi.AUTO.out().println("Total issues: " + findings.size());
        return 1;
    }

    private List<Finding> analyzePom(Path path) {
        List<Finding> findings = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(path)) {
            MavenXpp3Reader pomReader = new MavenXpp3Reader();
            Model model = pomReader.read(reader);
            for (Dependency dependency : model.getDependencies()) {
                String version = dependency.getVersion();
                if (version == null || version.isBlank()) {
                    findings.add(new Finding(path, dependency, "Missing explicit version"));
                } else if (version.endsWith("-SNAPSHOT")) {
                    findings.add(new Finding(path, dependency, "Snapshot dependency should be replaced with a release version"));
                } else if (version.toLowerCase().contains("latest") || version.toLowerCase().contains("release")) {
                    findings.add(new Finding(path, dependency, "Dynamic version '" + version + "' detected. Pin to a specific release."));
                }
            }
        } catch (IOException | XmlPullParserException e) {
            throw new IllegalStateException("Failed to read pom " + path, e);
        }
        return findings;
    }

    record Finding(Path pom, Dependency dependency, String problem) {
        String message() {
            return "[" + pom + "] " + dependency.getGroupId() + ":" + dependency.getArtifactId() + " -> " + problem;
        }
    }
}
