package com.jqvarford.homeenvmcp;

import com.jqvarford.homeenvmcp.tools.GitAddAndCommitTool;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;

import java.time.Duration;

public class McpApplication {
    public static void main(String[] args) throws InterruptedException {
        McpSyncServer syncServer = McpServer.sync(new StdioServerTransportProvider())
            .serverInfo("my-server", "1.0.0")
            .capabilities(ServerCapabilities.builder()
                .resources(false, true)
                .tools(true)
                .prompts(true)
                .logging()
                .completions()
                .build())
            .build();
        GitAddAndCommitTool tool = new GitAddAndCommitTool();

        syncServer.addTool(tool.specification());

        while (true) {
            Thread.sleep(Duration.ofSeconds(1));
        }
    }
}