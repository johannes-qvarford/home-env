package net.qvarford.homeenvmcp.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class GitUtils {
  private final Path workingDirectory;
  private final CommandExecutor commandExecutor;

  public GitUtils(CommandExecutor commandExecutor) {
    this.commandExecutor = commandExecutor;
    this.workingDirectory = Paths.get(System.getProperty("user.dir"));
  }

  public GitUtils(ProcessRunner processRunner) {
    this((CommandExecutor) processRunner);
  }

  public ProcessResult reset() throws IOException, InterruptedException {
    return commandExecutor.run(
        Arrays.asList("git", "reset"), workingDirectory, Duration.ofSeconds(60));
  }

  public ProcessResult addAll() throws IOException, InterruptedException {
    return commandExecutor.run(
        Arrays.asList("git", "add", "."), workingDirectory, Duration.ofSeconds(60));
  }

  public ProcessResult addFiles(List<String> files) throws IOException, InterruptedException {
    List<String> command = new java.util.ArrayList<>(Arrays.asList("git", "add"));
    command.addAll(files);
    return commandExecutor.run(command, workingDirectory, Duration.ofSeconds(60));
  }

  public ProcessResult commit(String message) throws IOException, InterruptedException {
    return commit(message, false);
  }

  public ProcessResult commit(String message, boolean amend)
      throws IOException, InterruptedException {
    List<String> command = new java.util.ArrayList<>(Arrays.asList("git", "commit", "-m", message));
    if (amend) {
      command.add("--amend");
    }
    return commandExecutor.run(command, workingDirectory, Duration.ofSeconds(60));
  }
}
