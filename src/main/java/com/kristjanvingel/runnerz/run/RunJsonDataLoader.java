package com.kristjanvingel.runnerz.run;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class RunJsonDataLoader implements CommandLineRunner {
  private static final Logger logger = LoggerFactory.getLogger(RunJsonDataLoader.class);
  private final RunRepository runRepository;
  private final ObjectMapper objectMapper;

  public RunJsonDataLoader(RunRepository runRepository, ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.runRepository = runRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    if (runRepository.countRuns() == 0) {
      try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/runs.json")) {
        Runs allRuns = objectMapper.readValue(inputStream, Runs.class);
        logger.info("Reading {} runs from JSON data and saving to in-memory collection.", allRuns.runs().size());
        runRepository.saveAllRuns(allRuns.runs());

      } catch (IOException e) {
        throw new RuntimeException("Failed to read JSON data", e);
      }
    } else {
      logger.info("Not loading Runs from JSON data because the collection contains data.");
    }
  }
}
