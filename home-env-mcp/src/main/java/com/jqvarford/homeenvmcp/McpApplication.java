package com.jqvarford.homeenvmcp;

import com.jqvarford.homeenvmcp.tools.GitAddAndCommitTool;

import java.io.IOException;

public class McpApplication {
    public static void main(String[] args) {
        McpServerWrapper server = new McpServerWrapper();
        
        // Register available tools
        GitAddAndCommitTool gitTool = new GitAddAndCommitTool();
        server.registerTool(gitTool.getName(), gitTool.getDescription());
        
        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Error starting MCP server: " + e.getMessage());
            System.exit(1);
        }
    }
}