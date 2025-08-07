package net.qvarford.homeenvmcp.tools;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import net.qvarford.homeenvmcp.Tool;
import net.qvarford.homeenvmcp.exceptions.ToolException;
import net.qvarford.homeenvmcp.exceptions.ToolFailureException;
import net.qvarford.homeenvmcp.util.CommandExecutor;
import net.qvarford.homeenvmcp.util.EnvironmentProvider;
import net.qvarford.homeenvmcp.util.GitUtils;
import net.qvarford.homeenvmcp.util.ProcessRunner;
import net.qvarford.homeenvmcp.util.ToolHelper;

public class GitAddAndCommit implements Tool<GitAddAndCommit.Arguments> {
  private final ToolHelper toolHelper;
  private final CommandExecutor commandExecutor;
  private final EnvironmentProvider environmentProvider;
  private final GitUtils gitUtils;

  public GitAddAndCommit() {
    ProcessRunner processRunner = new ProcessRunner();
    this.toolHelper = new ToolHelper();
    this.commandExecutor = processRunner;
    this.environmentProvider = processRunner;
    this.gitUtils = new GitUtils(processRunner);
  }

  public GitAddAndCommit(
      ToolHelper toolHelper,
      CommandExecutor commandExecutor,
      EnvironmentProvider environmentProvider,
      GitUtils gitUtils) {
    this.toolHelper = toolHelper;
    this.commandExecutor = commandExecutor;
    this.environmentProvider = environmentProvider;
    this.gitUtils = gitUtils;
  }

  public String name() {
    return "GitAddAndCommit";
  }

  @Override
  public Class<Arguments> schema() {
    return Arguments.class;
  }

  public String description() {
    return "Commits specified files with a message. Supports --all flag, individual files, and --amend option. Runs VERIFIER env variable if set.";
  }

  public McpSchema.CallToolResult run(
      McpSyncServerExchange ignored, McpSchema.CallToolResult.Builder result, Arguments arguments)
      throws ToolException {
    String message = arguments.message();
    boolean all = Boolean.TRUE.equals(arguments.all);
    List<String> files = arguments.files();
    boolean amend = Boolean.TRUE.equals(arguments.amend);

    String verifier = environmentProvider.getEnvironmentVariable("VERIFIER");
    if (verifier != null && !verifier.trim().isEmpty()) {
      result.addTextContent("Running VERIFIER: %s".formatted(verifier));
      toolHelper.runCommand(
          verifier,
          () ->
              commandExecutor.run(
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

    toolHelper.runCommand("Git Commit", () -> gitUtils.commit(message, amend));
    String commitType = amend ? "Amended commit" : "Committed";
    result.addTextContent("%s with message: %s".formatted(commitType, message));

    if (!all) {
      toolHelper.runCommand("Git Reset", gitUtils::reset);
      result.addTextContent("Unstaged remaining files");
    }

    return result.build();
  }

  public record Arguments(
      Boolean all, List<String> files, @NotNull String message, Boolean amend) {}
}
