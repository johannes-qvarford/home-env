package com.jqvarford.homeenvmcp.tools;

// MCP SDK imports - these will need to be adjusted based on actual SDK structure
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jqvarford.homeenvmcp.util.GitUtils;
import com.jqvarford.homeenvmcp.util.ProcessRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GitAddAndCommitTool {
    private final ObjectMapper mapper = new ObjectMapper();
    private final GitUtils gitUtils = new GitUtils();
    
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

    public JsonNode execute(JsonNode params) throws Exception {
            String message = params.get("message").asText();
            boolean all = params.has("all") ? params.get("all").asBoolean() : false;
            List<String> files = new ArrayList<>();
            
            if (params.has("file")) {
                JsonNode fileArray = params.get("file");
                if (fileArray.isArray()) {
                    for (JsonNode fileNode : fileArray) {
                        files.add(fileNode.asText());
                    }
                }
            }

            StringBuilder result = new StringBuilder();

            // Check and run VERIFIER if it exists
            String verifier = ProcessRunner.getEnvironmentVariable("VERIFIER");
            if (verifier != null && !verifier.trim().isEmpty()) {
                result.append("Running VERIFIER: ").append(verifier).append("\n");
                ProcessRunner.ProcessResult verifierResult = ProcessRunner.run(
                    Arrays.asList("sh", "-c", verifier), 
                    java.nio.file.Paths.get(".")
                );
                
                if (!verifierResult.isSuccess()) {
                    throw new RuntimeException("VERIFIER failed with exit code " + verifierResult.getExitCode() + 
                        ": " + verifierResult.getStderr());
                }
                result.append("VERIFIER completed successfully\n");
            }

            // Unstage all currently staged files
            ProcessRunner.ProcessResult resetResult = gitUtils.reset();
            if (!resetResult.isSuccess()) {
                result.append("Warning: git reset failed: ").append(resetResult.getStderr()).append("\n");
            } else {
                result.append("Reset staging area\n");
            }

            // Stage files based on parameters
            if (all) {
                ProcessRunner.ProcessResult addResult = gitUtils.addAll();
                if (!addResult.isSuccess()) {
                    throw new RuntimeException("Failed to stage all files: " + addResult.getStderr());
                }
                result.append("Staged all files\n");
            } else if (!files.isEmpty()) {
                ProcessRunner.ProcessResult addResult = gitUtils.addFiles(files);
                if (!addResult.isSuccess()) {
                    throw new RuntimeException("Failed to stage files " + files + ": " + addResult.getStderr());
                }
                result.append("Staged files: ").append(String.join(", ", files)).append("\n");
            } else {
                throw new RuntimeException("Either --all=true or --file must be specified");
            }

            // Commit with message
            ProcessRunner.ProcessResult commitResult = gitUtils.commit(message);
            if (!commitResult.isSuccess()) {
                throw new RuntimeException("Failed to commit: " + commitResult.getStderr());
            }
            result.append("Committed with message: '").append(message).append("'\n");

            // Unstage any remaining staged files
            ProcessRunner.ProcessResult finalResetResult = gitUtils.reset();
            if (!finalResetResult.isSuccess()) {
                result.append("Warning: final git reset failed: ").append(finalResetResult.getStderr()).append("\n");
            } else {
                result.append("Unstaged remaining files\n");
            }

            return mapper.valueToTree(result.toString());
    }
}