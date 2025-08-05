package com.jqvarford.homeenvmcp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessRunner {
    
    public static class ProcessResult {
        private final int exitCode;
        private final String stdout;
        private final String stderr;

        public ProcessResult(int exitCode, String stdout, String stderr) {
            this.exitCode = exitCode;
            this.stdout = stdout;
            this.stderr = stderr;
        }

        public int getExitCode() { return exitCode; }
        public String getStdout() { return stdout; }
        public String getStderr() { return stderr; }
        public boolean isSuccess() { return exitCode == 0; }
    }

    public static ProcessResult run(List<String> command, Path workingDirectory) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workingDirectory.toFile());
        pb.redirectErrorStream(false);

        Process process = pb.start();
        
        StringBuilder stdout = new StringBuilder();
        StringBuilder stderr = new StringBuilder();

        Thread stdoutThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stdout.append(line).append("\n");
                }
            } catch (IOException e) {
                // Ignore
            }
        });

        Thread stderrThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stderr.append(line).append("\n");
                }
            } catch (IOException e) {
                // Ignore
            }
        });

        stdoutThread.start();
        stderrThread.start();

        boolean finished = process.waitFor(60, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            throw new IOException("Process timed out after 60 seconds");
        }

        stdoutThread.join();
        stderrThread.join();

        return new ProcessResult(process.exitValue(), stdout.toString().trim(), stderr.toString().trim());
    }

    public static String getEnvironmentVariable(String name) {
        return System.getenv(name);
    }
}