package net.qvarford.homeenvmcp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessRunner {
  public ProcessResult run(List<String> command, Path workingDirectory, Duration timeout)
      throws IOException, InterruptedException {
    System.err.printf("Running command '%s' in %s for %s%n", command, workingDirectory, timeout);

    ProcessBuilder pb = new ProcessBuilder(command);
    pb.directory(workingDirectory.toFile());
    pb.redirectErrorStream(false);

    Process process = pb.start();

    StringBuilder stdout = new StringBuilder();
    StringBuilder stderr = new StringBuilder();

    Thread stdoutThread =
        new Thread(
            () -> {
              try (BufferedReader reader =
                  new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                  stdout.append(line).append("\n");
                }
              } catch (IOException e) {
                // Ignore this
              }
            });

    Thread stderrThread =
        new Thread(
            () -> {
              try (BufferedReader reader =
                  new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
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

    boolean finished = process.waitFor(timeout.toSeconds(), TimeUnit.SECONDS);
    if (!finished) {
      process.destroyForcibly();
      throw new IOException("Process timed out after %s seconds".formatted(timeout.toSeconds()));
    }

    stdoutThread.join();
    stderrThread.join();

    return new ProcessResult(
        process.exitValue(), stdout.toString().trim(), stderr.toString().trim());
  }

  public String getEnvironmentVariable(String name) {
    return System.getenv(name);
  }
}
