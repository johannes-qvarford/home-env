package net.qvarford.homeenvmcp.tools;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.qvarford.homeenvmcp.Tool;
import net.qvarford.homeenvmcp.exceptions.ToolException;
import net.qvarford.homeenvmcp.util.ProcessRunner;
import net.qvarford.homeenvmcp.util.ToolHelper;

public class RunIntegrationTests implements Tool<RunIntegrationTests.Arguments> {
  private final ToolHelper toolHelper = new ToolHelper();
  private final ProcessRunner processRunner = new ProcessRunner();

  public String name() {
    return "RunIntegrationTests";
  }

  @Override
  public Class<Arguments> schema() {
    return Arguments.class;
  }

  public String description() {
    return "RunIntegrationTests runs all Cucumber tests. You can specify an optional feature file and an optional scenario name to filter what to execute.";
  }

  public McpSchema.CallToolResult run(
      McpSyncServerExchange ignored, McpSchema.CallToolResult.Builder result, Arguments arguments)
      throws ToolException {
    String featureFile = arguments.featureFile();
    List<String> command = buildCommand(arguments, featureFile);

    result.addTextContent(
        "Running Cucumber integration tests with command: %s".formatted(String.join(" ", command)));

    toolHelper.runCommand(
        "Cucumber Integration Tests",
        () -> processRunner.run(command, java.nio.file.Paths.get("."), Duration.ofMinutes(15)));

    result.addTextContent("Integration tests completed successfully");
    return result.build();
  }

  private static List<String> buildCommand(Arguments arguments, String featureFile) {
    String scenarioName = arguments.scenarioName();

    List<String> command = new ArrayList<>(Arrays.asList("mvn", "test"));

    command.add("-Dtest=**/*IT");

    if (featureFile != null && !featureFile.trim().isEmpty()) {
      String featurePath = featureFile.trim();
      if (!featurePath.endsWith(".feature")) {
        featurePath += ".feature";
      }
      command.add("-Dcucumber.features=" + featurePath);
    }

    if (scenarioName != null && !scenarioName.trim().isEmpty()) {
      command.add("-Dcucumber.filter.name=" + scenarioName.trim());
    }
    return command;
  }

  public record Arguments(String featureFile, String scenarioName) {}
}
