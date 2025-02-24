package ru.otus.hw.commands;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class BatchCommands {

    private final JobLauncher jobLauncher;

    private final Job migrationJob;

    public BatchCommands(JobLauncher jobLauncher, Job migrationJob) {
        this.jobLauncher = jobLauncher;
        this.migrationJob = migrationJob;
    }

    @ShellMethod(key = "sm", value = "Start migration MongoDB")
    public String startMigration() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution execution = jobLauncher.run(migrationJob, jobParameters);
            return "Start migration. Status: " + execution.getStatus();
        } catch (Exception e) {
            return "Migration error: " + e.getMessage();
        }
    }
}
