package net.qvarford.homeenvmcp.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Service for managing and retrieving code samples from resources. Provides functionality to list
 * available samples and retrieve sample content.
 */
public class CodeSampleService {
  private static final String SAMPLES_BASE_PATH = "/code-samples";
  private final Map<String, Map<String, String>> samplesCache = new HashMap<>();
  private final Map<String, List<String>> typesCache = new HashMap<>();
  private boolean cacheInitialized = false;

  /**
   * Get all available code sample types and their examples.
   *
   * @return Map of type -> list of example names
   */
  public Map<String, List<String>> getAllSampleTypes() throws IOException {
    initializeCache();
    return new HashMap<>(typesCache);
  }

  /**
   * Get a specific code sample by type and example name.
   *
   * @param type The sample type (e.g., "java", "bash")
   * @param example The example name within the type
   * @return Optional containing the sample content, or empty if not found
   */
  public Optional<String> getSample(String type, String example) throws IOException {
    initializeCache();

    Map<String, String> typeMap = samplesCache.get(type);
    if (typeMap == null) {
      return Optional.empty();
    }

    return Optional.ofNullable(typeMap.get(example));
  }

  /**
   * Get all code samples for a specific type.
   *
   * @param type The sample type
   * @return Map of example name -> sample content
   */
  public Map<String, String> getAllSamplesForType(String type) throws IOException {
    initializeCache();

    Map<String, String> typeMap = samplesCache.get(type);
    if (typeMap == null) {
      return new HashMap<>();
    }

    return new HashMap<>(typeMap);
  }

  /** Initialize the cache by scanning the resources directory. */
  private void initializeCache() throws IOException {
    if (cacheInitialized) {
      return;
    }

    try {
      URL resourceUrl = CodeSampleService.class.getResource(SAMPLES_BASE_PATH);
      if (resourceUrl == null) {
        // Resources directory doesn't exist yet, initialize empty cache
        cacheInitialized = true;
        return;
      }

      Path samplesPath = Paths.get(resourceUrl.toURI());

      try (Stream<Path> typeDirs = Files.list(samplesPath)) {
        typeDirs.filter(Files::isDirectory).forEach(this::loadSamplesForType);
      }

      cacheInitialized = true;
    } catch (URISyntaxException e) {
      throw new IOException("Failed to resolve samples path", e);
    }
  }

  /** Load all samples for a specific type directory. */
  private void loadSamplesForType(Path typeDir) {
    String typeName = typeDir.getFileName().toString();
    Map<String, String> sampleMap = new HashMap<>();
    List<String> exampleNames = new ArrayList<>();

    try (Stream<Path> sampleFiles = Files.list(typeDir)) {
      sampleFiles
          .filter(Files::isRegularFile)
          .forEach(
              sampleFile -> {
                String fileName = sampleFile.getFileName().toString();
                String exampleName = removeFileExtension(fileName);
                exampleNames.add(exampleName);

                try {
                  String content = loadSampleContent(typeName + "/" + fileName);
                  sampleMap.put(exampleName, content);
                } catch (IOException e) {
                  // Log error but continue processing other files
                  System.err.println(
                      "Failed to load sample: " + sampleFile + " - " + e.getMessage());
                }
              });
    } catch (IOException e) {
      System.err.println(
          "Failed to list samples in directory: " + typeDir + " - " + e.getMessage());
      return;
    }

    if (!sampleMap.isEmpty()) {
      samplesCache.put(typeName, sampleMap);
      typesCache.put(typeName, exampleNames);
    }
  }

  /** Load sample content from resources. */
  private String loadSampleContent(String relativePath) throws IOException {
    String fullPath = SAMPLES_BASE_PATH + "/" + relativePath;
    try (InputStream is = CodeSampleService.class.getResourceAsStream(fullPath)) {
      if (is == null) {
        throw new IOException("Sample not found: " + fullPath);
      }
      return new String(is.readAllBytes());
    }
  }

  /** Remove file extension from filename. */
  private String removeFileExtension(String fileName) {
    int lastDot = fileName.lastIndexOf('.');
    if (lastDot > 0) {
      return fileName.substring(0, lastDot);
    }
    return fileName;
  }
}
