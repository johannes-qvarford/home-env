package net.qvarford.homeenvmcp.util;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockCommandExecutor implements CommandExecutor {
  private final Map<List<String>, ProcessResult> commandResults = new HashMap<>();
  private ProcessResult defaultResult = new ProcessResult(0, "", "");

  public void setCommandResult(List<String> command, ProcessResult result) {
    commandResults.put(command, result);
  }

  public void setDefaultResult(ProcessResult result) {
    this.defaultResult = result;
  }

  @Override
  public ProcessResult run(List<String> command, Path workingDirectory, Duration timeout)
      throws IOException, InterruptedException {
    ProcessResult result = commandResults.get(command);
    return result != null ? result : defaultResult;
  }
}
