package com.jqvarford.homeenvmcp.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class GitUtils {
    private final Path workingDirectory;

    public GitUtils(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public GitUtils() {
        this.workingDirectory = Paths.get(System.getProperty("user.dir"));
    }

    public ProcessRunner.ProcessResult reset() throws IOException, InterruptedException {
        return ProcessRunner.run(Arrays.asList("git", "reset"), workingDirectory);
    }

    public ProcessRunner.ProcessResult addAll() throws IOException, InterruptedException {
        return ProcessRunner.run(Arrays.asList("git", "add", "."), workingDirectory);
    }

    public ProcessRunner.ProcessResult addFiles(List<String> files) throws IOException, InterruptedException {
        List<String> command = new java.util.ArrayList<>(Arrays.asList("git", "add"));
        command.addAll(files);
        return ProcessRunner.run(command, workingDirectory);
    }

    public ProcessRunner.ProcessResult commit(String message) throws IOException, InterruptedException {
        return ProcessRunner.run(Arrays.asList("git", "commit", "-m", message), workingDirectory);
    }

    public ProcessRunner.ProcessResult status() throws IOException, InterruptedException {
        return ProcessRunner.run(Arrays.asList("git", "status", "--porcelain"), workingDirectory);
    }
}