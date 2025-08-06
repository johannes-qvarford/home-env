package com.jqvarford.homeenvmcp.tools;

import com.jqvarford.homeenvmcp.Tool;
import com.jqvarford.homeenvmcp.exceptions.ToolException;
import com.jqvarford.homeenvmcp.exceptions.ToolFailureException;
import com.jqvarford.homeenvmcp.util.GitUtils;
import com.jqvarford.homeenvmcp.util.ProcessRunner;
import com.jqvarford.homeenvmcp.util.ToolHelper;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult.Builder;
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

  public McpSchema.CallToolResult run(McpSyncServerExchange ignored, Arguments arguments)
      throws ToolException {
    Builder result = McpSchema.CallToolResult.builder();
    try {
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

      toolHelper.runCommand("Git Reset", gitUtils::reset);
      result.addTextContent("Unstaged remaining files");

      return result.build();
    } catch (Exception e) {
      result.addTextContent("Exception occurred: " + e);
      result.isError(true);
      return result.build();
    }
  }

  public record Arguments(Boolean all, List<String> files, @NotNull String message) {}

  /*
  public JsonNode getInputSchema() {

      ObjectNode schema = mapper.createObjectNode();
      schema.put("type", "object");

      ObjectNode properties = mapper.createObjectNode();

      ObjectNode messageProperty = mapper.createObjectNode();
      messageProperty.put("type", "string");
      messageProperty.put("description", "Commit message");
      properties.set("message", messageProperty);

      ObjectNode allProperty = mapper.createObjectNode();
      allProperty.put("type", "boolean");
      allProperty.put("description", "Stage all files (default: false)");
      allProperty.put("default", false);
      properties.set("all", allProperty);

      ObjectNode fileProperty = mapper.createObjectNode();
      fileProperty.put("type", "array");
      ObjectNode fileItems = mapper.createObjectNode();
      fileItems.put("type", "string");
      fileProperty.set("items", fileItems);
      fileProperty.put("description", "Specific files to stage and commit");
      properties.set("file", fileProperty);

      schema.set("properties", properties);

      ArrayNode required = mapper.createArrayNode();
      required.add("message");
      schema.set("required", required);

      return schema;
  }*/
}
