package net.qvarford.homeenvmcp.util;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public interface CommandExecutor {
  ProcessResult run(List<String> command, Path workingDirectory, Duration timeout)
      throws IOException, InterruptedException;
}
