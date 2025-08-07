package net.qvarford.homeenvmcp.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import io.modelcontextprotocol.spec.McpSchema;
import java.util.Arrays;
import java.util.List;
import net.qvarford.homeenvmcp.exceptions.ToolFailureException;
import net.qvarford.homeenvmcp.tools.GitAddAndCommit.Arguments;
import net.qvarford.homeenvmcp.util.CommandExecutor;
import net.qvarford.homeenvmcp.util.EnvironmentProvider;
import net.qvarford.homeenvmcp.util.GitUtils;
import net.qvarford.homeenvmcp.util.ProcessResult;
import net.qvarford.homeenvmcp.util.ToolHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GitAddAndCommitTest {

  @Mock private ToolHelper toolHelper;
  @Mock private CommandExecutor commandExecutor;
  @Mock private EnvironmentProvider environmentProvider;
  @Mock private GitUtils gitUtils;

  private GitAddAndCommit gitAddAndCommit;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    gitAddAndCommit =
        new GitAddAndCommit(toolHelper, commandExecutor, environmentProvider, gitUtils);
  }

  @Test
  void testName() {
    assertEquals("GitAddAndCommit", gitAddAndCommit.name());
  }

  @Test
  void testSchema() {
    assertEquals(Arguments.class, gitAddAndCommit.schema());
  }

  @Test
  void testDescription() {
    assertNotNull(gitAddAndCommit.description());
    assertTrue(gitAddAndCommit.description().contains("Commits specified files"));
  }

  @Test
  void testRunWithAllFlag() throws Exception {
    Arguments arguments = new Arguments(true, null, "Test commit message", null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    when(environmentProvider.getEnvironmentVariable("VERIFIER")).thenReturn(null);

    gitAddAndCommit.run(null, result, arguments);

    verify(toolHelper, times(3)).runCommand(anyString(), any());
  }

  @Test
  void testRunWithFiles() throws Exception {
    List<String> files = Arrays.asList("file1.txt", "file2.txt");
    Arguments arguments = new Arguments(null, files, "Test commit message", null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    when(environmentProvider.getEnvironmentVariable("VERIFIER")).thenReturn(null);

    gitAddAndCommit.run(null, result, arguments);

    verify(toolHelper, times(4)).runCommand(anyString(), any());
  }

  @Test
  void testRunWithAmendFlag() throws Exception {
    Arguments arguments = new Arguments(true, null, "Test commit message", true);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    when(environmentProvider.getEnvironmentVariable("VERIFIER")).thenReturn(null);

    gitAddAndCommit.run(null, result, arguments);

    verify(toolHelper, times(3)).runCommand(anyString(), any());
  }

  @Test
  void testRunWithVerifier() throws Exception {
    Arguments arguments = new Arguments(true, null, "Test commit message", null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    when(environmentProvider.getEnvironmentVariable("VERIFIER"))
        .thenReturn("echo 'verification passed'");

    gitAddAndCommit.run(null, result, arguments);

    ArgumentCaptor<ToolHelper.ExceptionThrowingSupplier<ProcessResult>> captor =
        ArgumentCaptor.forClass(ToolHelper.ExceptionThrowingSupplier.class);

    verify(toolHelper, times(4)).runCommand(anyString(), captor.capture());

    List<ToolHelper.ExceptionThrowingSupplier<ProcessResult>> suppliers = captor.getAllValues();
    assertEquals(4, suppliers.size());
  }

  @Test
  void testRunWithoutAllOrFiles() {
    Arguments arguments = new Arguments(null, null, "Test commit message", null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    when(environmentProvider.getEnvironmentVariable("VERIFIER")).thenReturn(null);

    assertThrows(
        ToolFailureException.class,
        () -> {
          gitAddAndCommit.run(null, result, arguments);
        });
  }

  @Test
  void testRunWithEmptyFilesList() {
    Arguments arguments = new Arguments(null, Arrays.asList(), "Test commit message", null);
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    when(environmentProvider.getEnvironmentVariable("VERIFIER")).thenReturn(null);

    assertThrows(
        ToolFailureException.class,
        () -> {
          gitAddAndCommit.run(null, result, arguments);
        });
  }
}
