package com.jonah.portfolio.cli;

import info.picocli.CommandLine;

public final class DevopsAutomationCliApplication {

    private DevopsAutomationCliApplication() {
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new DependencyScannerCommand()).execute(args);
        System.exit(exitCode);
    }
}
