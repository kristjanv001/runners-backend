package com.kristjanvingel.runnerz.run;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.JdbcClientAutoConfiguration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RunRepository {
  private static final Logger logger = LoggerFactory.getLogger(RunRepository.class);
  private final JdbcClient jdbcClient;

  public RunRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public List<Run> findAllRuns() {
    String sqlQuery = "select * from run";

    return this.jdbcClient
      .sql(sqlQuery)
      .query(Run.class)
      .list();
  }

  public Optional<Run> findRunById(Integer id) {
    String sqlQuery = "select id, title,started_on, completed_on, kilometers, location from run where id = :id";

    return this.jdbcClient
      .sql(sqlQuery)
      .param("id", id)
      .query(Run.class)
      .optional();
  }

  public void addRun(Run run) {
    String sqlQuery = "insert into run(id,title,started_on,completed_on,kilometers,location) values(?,?,?,?,?,?)";

    int updatedRun = jdbcClient
      .sql(sqlQuery)
      .params(List.of(run.id(),run.title(),run.startedOn(),run.completedOn(),run.kilometers(),run.location().toString()))
      .update();

    Assert.state(updatedRun == 1, "Failed to create run " + run.title());
  }

  public void updateRun(Run run, Integer id) {
    String sqlQuery = "update run set title = ?, started_on = ?, completed_on = ?, kilometers = ?, location = ? where id = ?";

    int updatedRun = jdbcClient
      .sql(sqlQuery)
      .params(List.of(run.title(),run.startedOn(),run.completedOn(),run.kilometers(),run.location().toString(), id))
      .update();

    Assert.state(updatedRun == 1, "Failed to update run " + run.title());
  }

  public void deleteRun(Integer id) {
    String sqlQuery = "delete from run where id = :id";
    var updated = jdbcClient.sql(sqlQuery)
      .param("id", id)
      .update();

    Assert.state(updated == 1, "Failed to delete run " + id);
  }

  public int countRuns() {
    return this.jdbcClient.sql("select * from run").query().listOfRows().size();
  }

  public void saveAllRuns(List<Run> runs) {
    runs.stream().forEach(this::addRun);
  }

  public List<Run> getByLocation(String location) {
    return jdbcClient.sql("select * from run where location = :location")
      .param("location", location)
      .query(Run.class)
      .list();
  }


}
