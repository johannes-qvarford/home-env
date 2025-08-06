package com.jqvarford.homeenvmcp.tools;

import com.jqvarford.homeenvmcp.Tool;
import com.jqvarford.homeenvmcp.exceptions.ToolException;
import com.jqvarford.homeenvmcp.exceptions.ToolFailureException;
import com.jqvarford.homeenvmcp.util.GitUtils;
import com.jqvarford.homeenvmcp.util.ProcessRunner;
import com.jqvarford.homeenvmcp.util.ToolHelper;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class GitAddAndCommit implements Tool<GitAddAndCommit.Arguments> {
  private final ToolHelper toolHelper = new ToolHelper();
  private final ProcessRunner processRunner = new ProcessRunner();
  private final GitUtils gitUtils = new GitUtils(processRunner);

  public String name() {
    return "GitAddAndCommit";
  }

  @Override
  public Class<Arguments> schema() {
    return Arguments.class;
  }

  public String description() {
    return "Commits specified files with a message. Supports --all flag or individual files. Runs VERIFIER env variable if set.";
  }

  public McpSchema.CallToolResult run(
      McpSyncServerExchange ignored, McpSchema.CallToolResult.Builder result, Arguments arguments)
      throws ToolException {
    String message = arguments.message();
    boolean all = Boolean.TRUE.equals(arguments.all);
    List<String> files = arguments.files();

    String verifier = processRunner.getEnvironmentVariable("VERIFIER");
    if (verifier != null && !verifier.trim().isEmpty()) {
      result.addTextContent("Running VERIFIER: %s".formatted(verifier));
      toolHelper.runCommand(
          verifier,
          () ->
              processRunner.run(
                  Arrays.asList("sh", "-c", verifier),
                  java.nio.file.Paths.get("."),
                  Duration.ofSeconds(60)));
      result.addTextContent("VERIFIER completed successfully");
    }

    // Unstage all currently staged files
    toolHelper.runCommand("Git Reset", gitUtils::reset);
    result.addTextContent("Reset staging area");

    // Stage files based on parameters
    if (all) {
      toolHelper.runCommand("Git Add All", gitUtils::addAll);
      result.addTextContent("Staged all files");
    } else if (files != null && !files.isEmpty()) {
      toolHelper.runCommand("Git Add Files", () -> gitUtils.addFiles(files));
      result.addTextContent("Staged files: %s".formatted(String.join(", ", files)));
    } else {
      throw new ToolFailureException("Either --all=true or --file must be specified");
    }

    toolHelper.runCommand("Git Commit", () -> gitUtils.commit(message));
    result.addTextContent("Committed with message: %s".formatted(message));

    if (!all) {
      toolHelper.runCommand("Git Reset", gitUtils::reset);
      result.addTextContent("Unstaged remaining files");
    }

    return result.build();
  }

  public record Arguments(Boolean all, List<String> files, @NotNull String message) {}
}
