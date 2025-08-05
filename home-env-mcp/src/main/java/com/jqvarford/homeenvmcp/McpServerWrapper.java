package com.jqvarford.homeenvmcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jqvarford.homeenvmcp.tools.GitAddAndCommitTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class McpServerWrapper {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, GitAddAndCommitTool> tools = new HashMap<>();
    
    public void registerTool(String name, String description) {
        if ("GitAddAndCommit".equals(name)) {
            tools.put(name, new GitAddAndCommitTool());
        }
    }
    
    public void start() throws IOException {
        System.err.println("Starting MCP Server (Simplified Implementation)");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;

        while ((line = reader.readLine()) != null) {
            try {
                JsonNode request = mapper.readTree(line);
                JsonNode response = handleRequest(request);
                if (response != null) {
                    System.out.println(mapper.writeValueAsString(response));
                    System.out.flush();
                }
            } catch (Exception e) {
                System.err.println("Error processing request: " + e.getMessage());
            }
        }
    }
    
    private JsonNode handleRequest(JsonNode request) throws Exception {
        String method = request.has("method") ? request.get("method").asText() : "";
        Object id = request.has("id") ? request.get("id") : null;
        
        switch (method) {
            case "initialize":
                return createInitializeResponse(id);
            case "tools/list":
                return createToolsListResponse(id);
            case "tools/call":
                return handleToolCall(request, id);
            default:
                return createErrorResponse(id, -32601, "Method not found: " + method);
        }
    }
    
    private JsonNode createInitializeResponse(Object id) {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        if (id != null) response.set("id", mapper.valueToTree(id));
        
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", "2024-11-05");
        
        ObjectNode capabilities = mapper.createObjectNode();
        ObjectNode tools = mapper.createObjectNode();
        tools.put("listChanged", false);
        capabilities.set("tools", tools);
        result.set("capabilities", capabilities);
        
        ObjectNode serverInfo = mapper.createObjectNode();
        serverInfo.put("name", "home-env-mcp");
        serverInfo.put("version", "1.0.0");
        result.set("serverInfo", serverInfo);
        
        response.set("result", result);
        return response;
    }
    
    private JsonNode createToolsListResponse(Object id) {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        if (id != null) response.set("id", mapper.valueToTree(id));
        
        ObjectNode result = mapper.createObjectNode();
        var toolsArray = mapper.createArrayNode();
        
        for (Map.Entry<String, GitAddAndCommitTool> entry : tools.entrySet()) {
            GitAddAndCommitTool tool = entry.getValue();
            ObjectNode toolInfo = mapper.createObjectNode();
            toolInfo.put("name", tool.getName());
            toolInfo.put("description", tool.getDescription());
            toolInfo.set("inputSchema", tool.getInputSchema());
            toolsArray.add(toolInfo);
        }
        
        result.set("tools", toolsArray);
        response.set("result", result);
        return response;
    }
    
    private JsonNode handleToolCall(JsonNode request, Object id) throws Exception {
        JsonNode params = request.get("params");
        String toolName = params.get("name").asText();
        JsonNode arguments = params.get("arguments");
        
        GitAddAndCommitTool tool = tools.get(toolName);
        if (tool == null) {
            return createErrorResponse(id, -32602, "Tool not found: " + toolName);
        }
        
        try {
            JsonNode result = tool.execute(arguments);
            
            ObjectNode response = mapper.createObjectNode();
            response.put("jsonrpc", "2.0");
            if (id != null) response.set("id", mapper.valueToTree(id));
            
            ObjectNode responseResult = mapper.createObjectNode();
            var content = mapper.createArrayNode();
            ObjectNode textContent = mapper.createObjectNode();
            textContent.put("type", "text");
            textContent.put("text", result.asText());
            content.add(textContent);
            responseResult.set("content", content);
            
            response.set("result", responseResult);
            return response;
        } catch (Exception e) {
            return createErrorResponse(id, -32603, "Tool execution error: " + e.getMessage());
        }
    }
    
    private JsonNode createErrorResponse(Object id, int code, String message) {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        if (id != null) response.set("id", mapper.valueToTree(id));
        
        ObjectNode error = mapper.createObjectNode();
        error.put("code", code);
        error.put("message", message);
        response.set("error", error);
        
        return response;
    }
}