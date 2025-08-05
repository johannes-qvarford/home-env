package com.jqvarford.homeenvmcp.tools;

// MCP SDK imports - these will need to be adjusted based on actual SDK structure
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jqvarford.homeenvmcp.util.GitUtils;
import com.jqvarford.homeenvmcp.util.ProcessRunner;

import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult.Builder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GitAddAndCommitTool {
    private final ObjectMapper mapper = new ObjectMapper();
    private final GitUtils gitUtils = new GitUtils();

    public SyncToolSpecification specification() {
        return new SyncToolSpecification(
            new McpSchema.Tool(
                "GitAddAndCommit",
                "Commits specified files with a message. Supports --all flag or individual files. Runs VERIFIER env variable if set.",
                getInputSchema().toPrettyString()),
            this::execute);
    }

    @SuppressWarnings("unchecked")
    public McpSchema.CallToolResult execute(McpSyncServerExchange exchange, Map<String, Object> params) {
        Builder result = McpSchema.CallToolResult.builder();
        try {
            String message = params.get("message").toString();
            boolean all = params.containsKey("all") && (boolean)params.get("all");
            List<String> files = (List<String>)params.get("file");

            // Check and run VERIFIER if it exists
            String verifier = ProcessRunner.getEnvironmentVariable("VERIFIER");
            if (verifier != null && !verifier.trim().isEmpty()) {
            result.addTextContent("Running VERIFIER: %s\n".formatted(verifier));
            ProcessRunner.ProcessResult verifierResult = ProcessRunner.run(
                Arrays.asList("sh", "-c", verifier), 
                java.nio.file.Paths.get(".")
            );
            
            if (!verifierResult.isSuccess()) {
                result.addTextContent("VERIFIER failed with exit code %d: %s".formatted(verifierResult.getExitCode(), verifierResult.getStderr()));
                result.isError(true);
                return result.build();
            }
            result.addTextContent("VERIFIER completed successfully\n");
            }

            // Unstage all currently staged files
            ProcessRunner.ProcessResult resetResult = gitUtils.reset();
            if (!resetResult.isSuccess()) {
            result.addTextContent("Warning: git reset failed: %s\n".formatted(resetResult.getStderr()));
            } else {
            result.addTextContent("Reset staging area\n");
            }

            // Stage files based on parameters
            if (all) {
            ProcessRunner.ProcessResult addResult = gitUtils.addAll();
            if (!addResult.isSuccess()) {
                result.addTextContent("Failed to stage all files: " + addResult.getStderr());
                result.isError(true);
                return result.build();
            }
            result.addTextContent("Staged all files\n");
            } else if (files != null && !files.isEmpty()) {
            ProcessRunner.ProcessResult addResult = gitUtils.addFiles(files);
            if (!addResult.isSuccess()) {
                result.addTextContent("Failed to stage files " + files + ": " + addResult.getStderr());
                result.isError(true);
                return result.build();
            }
            result.addTextContent("Staged files: %s\n".formatted(String.join(", ", files)));
            } else {
            result.addTextContent("Either --all=true or --file must be specified");
            result.isError(true);
            return result.build();
            }

            // Commit with message
            ProcessRunner.ProcessResult commitResult = gitUtils.commit(message);
            if (!commitResult.isSuccess()) {
            result.addTextContent("Failed to commit: " + commitResult.getStderr());
            result.isError(true);
            return result.build();
            }
            result.addTextContent("Committed with message: %s\n".formatted(message));

            // Unstage any remaining staged files
            ProcessRunner.ProcessResult finalResetResult = gitUtils.reset();
            if (!finalResetResult.isSuccess()) {
            result.addTextContent("Warning: final git reset failed: %s\n".formatted(finalResetResult.getStderr()));
            } else {
            result.addTextContent("Unstaged remaining files\n");
            }

            return result.build();
        } catch (Exception e) {
            result.addTextContent("Exception occurred: " + e);
            result.isError(true);
            return result.build();
        }
    }
    
    public String getName() {
        return "GitAddAndCommit";
    }
    
    public String getDescription() {
        return "Commits specified files with a message. Supports --all flag or individual files. Runs VERIFIER env variable if set.";
    }

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
    }

    
}