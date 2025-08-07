package net.qvarford.homeenvmcp.tools;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.qvarford.homeenvmcp.Tool;
import net.qvarford.homeenvmcp.exceptions.ToolException;
import net.qvarford.homeenvmcp.util.CommandExecutor;
import net.qvarford.homeenvmcp.util.ProcessRunner;
import net.qvarford.homeenvmcp.util.ToolHelper;

public class RunUnitTests implements Tool<RunUnitTests.Arguments> {
  private final ToolHelper toolHelper;
  private final CommandExecutor commandExecutor;

  public RunUnitTests() {
    ProcessRunner processRunner = new ProcessRunner();
    this.toolHelper = new ToolHelper();
    this.commandExecutor = processRunner;
  }

  public RunUnitTests(ToolHelper toolHelper, CommandExecutor commandExecutor) {
    this.toolHelper = toolHelper;
    this.commandExecutor = commandExecutor;
  }

  public String name() {
    return "RunUnitTests";
  }

  @Override
  public Class<Arguments> schema() {
    return Arguments.class;
  }

  public String description() {
    return "Runs all tests in a certain package. Assume we are using Maven, and JUnit5. RunUnitTests also accepts an optional class name, and an optional method, to filter down what test(s) to execute.";
  }

  public McpSchema.CallToolResult run(
      McpSyncServerExchange ignored, McpSchema.CallToolResult.Builder result, Arguments arguments)
      throws ToolException {
    String packageName = arguments.packageName();
    String className = arguments.className();
    String methodName = arguments.methodName();

    List<String> command = new ArrayList<>(Arrays.asList("mvn", "test"));

    String testFilter = buildTestFilter(packageName, className, methodName);
    if (testFilter != null) {
      command.add("-Dtest=" + testFilter);
    }

    result.addTextContent(
        "Running Maven unit tests with command: %s".formatted(String.join(" ", command)));

    toolHelper.runCommand(
        "Maven Unit Tests",
        () -> commandExecutor.run(command, java.nio.file.Paths.get("."), Duration.ofMinutes(10)));

    result.addTextContent("Unit tests completed successfully");
    return result.build();
  }

  private String buildTestFilter(String packageName, String className, String methodName) {
    StringBuilder filter = new StringBuilder();

    if (packageName != null && !packageName.trim().isEmpty()) {
      filter.append(packageName.trim());
      if (!packageName.endsWith("**")) {
        filter.append(".**");
      }
    }

    if (className != null && !className.trim().isEmpty()) {
      if (!filter.isEmpty()) {
        filter.setLength(0);
      }
      if (packageName != null && !packageName.trim().isEmpty()) {
        filter.append(packageName.trim());
        if (packageName.endsWith("**")) {
          filter.setLength(filter.length() - 2);
        }
        if (!packageName.endsWith(".")) {
          filter.append(".");
        }
      }
      filter.append(className.trim());
    }

    if (methodName != null && !methodName.trim().isEmpty()) {
      filter.append("#").append(methodName.trim());
    }

    return !filter.isEmpty() ? filter.toString() : null;
  }

  public record Arguments(String packageName, String className, String methodName) {}
}
