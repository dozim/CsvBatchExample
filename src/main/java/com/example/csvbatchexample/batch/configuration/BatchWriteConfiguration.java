package com.example.csvbatchexample.batch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.kohsuke.randname.RandomNameGenerator;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class BatchWriteConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    private final AtomicInteger limitLines = new AtomicInteger(0);

    @Bean
    public ItemReader<String> stringItemReader() {
        return () -> {
            Integer alter = limitLines.getAndIncrement();

            if (alter == 100) {
                return null;
            }

            return new RandomNameGenerator(randomInt()).next() + ";" + new RandomNameGenerator(randomInt()).next() + ";" + alter;
        };
    }

    private int randomInt() {
        return (int) (Math.random() * 100);
    }

    @Bean
    public FlatFileItemWriter<String> csvWriter() {
        return new FlatFileItemWriterBuilder<String>()
                .saveState(false)
                .lineAggregator(new DelimitedLineAggregator<>())
                .name("csvwriter")
                .build();
    }

    @Bean
    public Consumer<String> fileNameWriterConsumer(FlatFileItemWriter<String> csvWriter) {
        return fileName -> csvWriter.setResource(new FileSystemResource(fileName));
    }

    @Bean
    public Step writeStep() {
        return stepBuilderFactory.get("writeStep")
                .<String, String>chunk(10)
                .reader(this.stringItemReader())
                .writer(this.csvWriter())
                .allowStartIfComplete(true)
                .listener(getCounterCleanerOnFinishedListener())
                .build();
    }

    @Bean
    public Job writeJob(Step writeStep) {
        return jobBuilderFactory.get("writeJob")
                .incrementer(new RunIdIncrementer())
                .flow(writeStep)
                .end()
                .build();
    }

    private StepExecutionListener getCounterCleanerOnFinishedListener() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                // Do Nothing
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                log.info("setting generated lines to Zero");
                limitLines.set(0);
                return null;
            }
        };
    }
}
