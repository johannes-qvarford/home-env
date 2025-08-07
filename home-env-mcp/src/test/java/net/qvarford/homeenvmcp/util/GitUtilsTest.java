package net.qvarford.homeenvmcp.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GitUtilsTest {

  @Mock private CommandExecutor commandExecutor;

  private GitUtils gitUtils;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    gitUtils = new GitUtils(commandExecutor);
  }

  @Test
  void testReset() throws Exception {
    ProcessResult expectedResult = new ProcessResult(0, "success", "");
    when(commandExecutor.run(any(), any(), any())).thenReturn(expectedResult);

    ProcessResult result = gitUtils.reset();

    assertEquals(expectedResult, result);

    ArgumentCaptor<List<String>> commandCaptor = ArgumentCaptor.forClass(List.class);
    verify(commandExecutor)
        .run(
            commandCaptor.capture(),
            eq(Paths.get(System.getProperty("user.dir"))),
            eq(Duration.ofSeconds(60)));

    List<String> command = commandCaptor.getValue();
    assertEquals(Arrays.asList("git", "reset"), command);
  }

  @Test
  void testAddAll() throws Exception {
    ProcessResult expectedResult = new ProcessResult(0, "success", "");
    when(commandExecutor.run(any(), any(), any())).thenReturn(expectedResult);

    ProcessResult result = gitUtils.addAll();

    assertEquals(expectedResult, result);

    ArgumentCaptor<List<String>> commandCaptor = ArgumentCaptor.forClass(List.class);
    verify(commandExecutor)
        .run(
            commandCaptor.capture(),
            eq(Paths.get(System.getProperty("user.dir"))),
            eq(Duration.ofSeconds(60)));

    List<String> command = commandCaptor.getValue();
    assertEquals(Arrays.asList("git", "add", "."), command);
  }

  @Test
  void testAddFiles() throws Exception {
    List<String> files = Arrays.asList("file1.txt", "file2.txt", "file3.java");
    ProcessResult expectedResult = new ProcessResult(0, "success", "");
    when(commandExecutor.run(any(), any(), any())).thenReturn(expectedResult);

    ProcessResult result = gitUtils.addFiles(files);

    assertEquals(expectedResult, result);

    ArgumentCaptor<List<String>> commandCaptor = ArgumentCaptor.forClass(List.class);
    verify(commandExecutor)
        .run(
            commandCaptor.capture(),
            eq(Paths.get(System.getProperty("user.dir"))),
            eq(Duration.ofSeconds(60)));

    List<String> command = commandCaptor.getValue();
    List<String> expectedCommand =
        Arrays.asList("git", "add", "file1.txt", "file2.txt", "file3.java");
    assertEquals(expectedCommand, command);
  }

  @Test
  void testCommitWithMessage() throws Exception {
    String message = "Test commit message";
    ProcessResult expectedResult = new ProcessResult(0, "success", "");
    when(commandExecutor.run(any(), any(), any())).thenReturn(expectedResult);

    ProcessResult result = gitUtils.commit(message);

    assertEquals(expectedResult, result);

    ArgumentCaptor<List<String>> commandCaptor = ArgumentCaptor.forClass(List.class);
    verify(commandExecutor)
        .run(
            commandCaptor.capture(),
            eq(Paths.get(System.getProperty("user.dir"))),
            eq(Duration.ofSeconds(60)));

    List<String> command = commandCaptor.getValue();
    assertEquals(Arrays.asList("git", "commit", "-m", message), command);
  }

  @Test
  void testCommitWithMessageAndAmend() throws Exception {
    String message = "Test commit message";
    ProcessResult expectedResult = new ProcessResult(0, "success", "");
    when(commandExecutor.run(any(), any(), any())).thenReturn(expectedResult);

    ProcessResult result = gitUtils.commit(message, true);

    assertEquals(expectedResult, result);

    ArgumentCaptor<List<String>> commandCaptor = ArgumentCaptor.forClass(List.class);
    verify(commandExecutor)
        .run(
            commandCaptor.capture(),
            eq(Paths.get(System.getProperty("user.dir"))),
            eq(Duration.ofSeconds(60)));

    List<String> command = commandCaptor.getValue();
    assertEquals(Arrays.asList("git", "commit", "-m", message, "--amend"), command);
  }

  @Test
  void testCommitWithMessageAndNoAmend() throws Exception {
    String message = "Test commit message";
    ProcessResult expectedResult = new ProcessResult(0, "success", "");
    when(commandExecutor.run(any(), any(), any())).thenReturn(expectedResult);

    ProcessResult result = gitUtils.commit(message, false);

    assertEquals(expectedResult, result);

    ArgumentCaptor<List<String>> commandCaptor = ArgumentCaptor.forClass(List.class);
    verify(commandExecutor)
        .run(
            commandCaptor.capture(),
            eq(Paths.get(System.getProperty("user.dir"))),
            eq(Duration.ofSeconds(60)));

    List<String> command = commandCaptor.getValue();
    assertEquals(Arrays.asList("git", "commit", "-m", message), command);
  }

  @Test
  void testBackwardCompatibilityConstructor() {
    ProcessRunner processRunner = new ProcessRunner();
    GitUtils gitUtilsWithProcessRunner = new GitUtils(processRunner);

    assertNotNull(gitUtilsWithProcessRunner);
  }

  @Test
  void testWorkingDirectoryIsSetCorrectly() throws Exception {
    ProcessResult expectedResult = new ProcessResult(0, "success", "");
    when(commandExecutor.run(any(), any(), any())).thenReturn(expectedResult);

    gitUtils.reset();

    verify(commandExecutor).run(any(), eq(Paths.get(System.getProperty("user.dir"))), any());
  }

  @Test
  void testTimeoutIsSetCorrectly() throws Exception {
    ProcessResult expectedResult = new ProcessResult(0, "success", "");
    when(commandExecutor.run(any(), any(), any())).thenReturn(expectedResult);

    gitUtils.reset();

    verify(commandExecutor).run(any(), any(), eq(Duration.ofSeconds(60)));
  }
}
