package com.kristjanvingel.runnerz.run;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController
public class RunController {

  private final RunRepository runRepository;

  public RunController(RunRepository runRepository) {
    this.runRepository = runRepository;
  }

  // GET
  @GetMapping("/api/runs")
  public List<Run> get() {
    return runRepository.findAllRuns();
  }

  @GetMapping("/api/runs/{id}")
  public Run getById(@PathVariable Integer id) {
    Optional<Run> run = runRepository.findRunById(id);

    if (run.isEmpty()) {
      throw new RunNotFoundException();
    }
    return run.get();
  }

  // POST
  @PostMapping("/api/runs")
  @ResponseStatus(HttpStatus.CREATED)
  void create(@Valid @RequestBody Run run) {
    this.runRepository.addRun(run);
  }

  // UPDATE
  @PutMapping("/api/runs/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void update(@Valid @RequestBody Run run, @PathVariable Integer id) {
    this.runRepository.updateRun(run, id);
  }

  // DELETE
  @DeleteMapping("/api/runs/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(@PathVariable Integer id) {
    this.runRepository.deleteRun(id);
  }

}
