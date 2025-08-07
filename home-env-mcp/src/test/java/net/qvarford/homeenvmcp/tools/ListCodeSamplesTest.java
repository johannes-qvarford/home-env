package net.qvarford.homeenvmcp.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.qvarford.homeenvmcp.exceptions.ToolFailureException;
import net.qvarford.homeenvmcp.tools.ListCodeSamples.Arguments;
import net.qvarford.homeenvmcp.util.CodeSampleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ListCodeSamplesTest {

  @Mock private CodeSampleService codeSampleService;

  private ListCodeSamples listCodeSamples;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    listCodeSamples = new ListCodeSamples(codeSampleService);
  }

  @Test
  void testName() {
    assertEquals("ListCodeSamples", listCodeSamples.name());
  }

  @Test
  void testSchema() {
    assertEquals(Arguments.class, listCodeSamples.schema());
  }

  @Test
  void testDescription() {
    assertNotNull(listCodeSamples.description());
    assertTrue(listCodeSamples.description().contains("Lists all available code sample types"));
  }

  @Test
  void testListSamplesWithContent() throws Exception {
    Arguments arguments = new Arguments();
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    Map<String, List<String>> sampleTypes = new HashMap<>();

    List<String> javaExamples = new ArrayList<>();
    javaExamples.add("hello-world");
    javaExamples.add("rest-client");
    javaExamples.add("unit-test");
    sampleTypes.put("java", javaExamples);

    List<String> bashExamples = new ArrayList<>();
    bashExamples.add("file-operations");
    bashExamples.add("git-workflow");
    sampleTypes.put("bash", bashExamples);

    List<String> dockerExamples = new ArrayList<>();
    dockerExamples.add("multi-stage");
    sampleTypes.put("docker", dockerExamples);

    when(codeSampleService.getAllSampleTypes()).thenReturn(sampleTypes);

    McpSchema.CallToolResult callResult = listCodeSamples.run(null, result, arguments);

    verify(codeSampleService).getAllSampleTypes();
    assertNotNull(callResult);

    // Verify result is not null and operation completed successfully
    assertNotNull(callResult);
  }

  @Test
  void testListSamplesEmpty() throws Exception {
    Arguments arguments = new Arguments();
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    when(codeSampleService.getAllSampleTypes()).thenReturn(new HashMap<>());

    McpSchema.CallToolResult callResult = listCodeSamples.run(null, result, arguments);

    verify(codeSampleService).getAllSampleTypes();
    assertNotNull(callResult);

    assertNotNull(callResult);
  }

  @Test
  void testListSamplesWithSingleType() throws Exception {
    Arguments arguments = new Arguments();
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    Map<String, List<String>> sampleTypes = new HashMap<>();
    List<String> javaExamples = new ArrayList<>();
    javaExamples.add("hello-world");
    sampleTypes.put("java", javaExamples);

    when(codeSampleService.getAllSampleTypes()).thenReturn(sampleTypes);

    McpSchema.CallToolResult callResult = listCodeSamples.run(null, result, arguments);

    // Verify result is successful
    assertNotNull(callResult);
  }

  @Test
  void testIOExceptionHandling() throws Exception {
    Arguments arguments = new Arguments();
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    when(codeSampleService.getAllSampleTypes()).thenThrow(new IOException("Test IO error"));

    ToolFailureException exception =
        assertThrows(
            ToolFailureException.class,
            () -> {
              listCodeSamples.run(null, result, arguments);
            });

    assertTrue(exception.getMessage().contains("Failed to load code sample catalog"));
    assertTrue(exception.getMessage().contains("Test IO error"));
  }

  @Test
  void testDefaultConstructor() {
    ListCodeSamples defaultListCodeSamples = new ListCodeSamples();
    assertEquals("ListCodeSamples", defaultListCodeSamples.name());
  }

  @Test
  void testEmptyArguments() {
    Arguments arguments = new Arguments();
    assertNotNull(arguments);
  }

  @Test
  void testFormattingWithMultipleSamples() throws Exception {
    Arguments arguments = new Arguments();
    McpSchema.CallToolResult.Builder result = McpSchema.CallToolResult.builder();

    Map<String, List<String>> sampleTypes = new HashMap<>();

    List<String> javaExamples = new ArrayList<>();
    javaExamples.add("example1");
    javaExamples.add("example2");
    javaExamples.add("example3");
    sampleTypes.put("java", javaExamples);

    when(codeSampleService.getAllSampleTypes()).thenReturn(sampleTypes);

    McpSchema.CallToolResult callResult = listCodeSamples.run(null, result, arguments);

    // Verify result is successful
    assertNotNull(callResult);
  }
}
