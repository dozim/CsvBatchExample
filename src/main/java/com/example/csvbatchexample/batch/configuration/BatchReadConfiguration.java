package com.example.csvbatchexample.batch.configuration;

import com.example.csvbatchexample.user.persistence.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@EnableBatchProcessing
@Configuration
public class BatchReadConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<User> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<User>()
                .saveState(false)
                .delimited()
                .delimiter(";")
                .names(new String[]{"vorname", "nachname", "alter"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(User.class);
                }})
                .build();
    }

    @Bean
    public Consumer<String> fileNameReaderConsumer(FlatFileItemReader<User> flatFileItemReader) {
        return fileName -> flatFileItemReader.setResource(new FileSystemResource(fileName));
    }

    @Bean
    public ItemProcessor<User, List<String>> stringListItemProcessor() {
        return item -> List.of(item.toString());
    }

    @Bean
    public ItemWriter<List<String>> stringItemWriter() {
        return items -> items.forEach(strings -> strings.forEach(log::info));
    }

    @Bean
    public Step readStep() {
        return stepBuilderFactory.get("readStep")
                .<User, List<String>>chunk(10)
                .reader(flatFileItemReader())
                .processor(stringListItemProcessor())
                .writer(stringItemWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job readJob(Step readStep) {
        return jobBuilderFactory.get("readJob")
                .incrementer(new RunIdIncrementer())
                .flow(readStep)
                .end()
                .build();
    }
}
