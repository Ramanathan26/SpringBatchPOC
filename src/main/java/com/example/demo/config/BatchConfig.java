package com.example.demo.config;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.example.demo.model.Employee;
import com.example.demo.processor.EmployeeItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	    @Autowired
	    private JobBuilderFactory jobBuilderFactory;
	 
	    @Autowired
	    private StepBuilderFactory stepBuilderFactory;
	    
	    @Autowired
	    private DataSource dataSource;
	   
	    @Bean
	    public DataSource dataSource(){
	    	final DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    	  dataSource.setDriverClassName("org.h2.Driver");
	    	  dataSource.setUrl("jdbc:h2:~/test");
	    	  dataSource.setUsername("sa");
	    	  dataSource.setPassword("");
	    	  return dataSource;
	    }
	    
	    @Bean
	    public FlatFileItemReader<Employee> reader()
	    {
	     FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
	     reader.setResource(new ClassPathResource("employees.csv"));
	     reader.setLineMapper(new DefaultLineMapper<Employee>(){{
	     setLineTokenizer(new DelimitedLineTokenizer() {{
	     setNames(new String[] {"Name", "Age", "Salary", "Gender"});}});
	     setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
	     {
	     setTargetType(Employee.class);
	     }
	     }
	     );
	     }});
	     return reader;
	     }
	    
	    @Bean
	    public EmployeeItemProcessor processor()
	    {
	     return new EmployeeItemProcessor();
	    }
	    
	    @Bean
	    public JdbcBatchItemWriter<Employee> writer() {
	        JdbcBatchItemWriter<Employee> itemWriter = new JdbcBatchItemWriter<Employee>();
	        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
	        itemWriter.setSql("INSERT INTO Employees(Name, Age, Salary, Gender) VALUES (:Name, :Age, :Salary, :Gender)");
	        itemWriter.setDataSource(dataSource);
	        return itemWriter;
	    }
	     
	    @Bean
	    public Step step() {
	        return stepBuilderFactory
	                .get("step")
	                .<Employee, Employee>chunk(3)
	                .reader(reader())
	                .processor(processor())
	                .writer(writer())
	                .build();
	    }
	 
	    @Bean
	    public Job importUserJob() {
	     return jobBuilderFactory.get("importUserJob")
	       .incrementer(new RunIdIncrementer())
	       .flow(step())
	       .end()
	       .build();
	    }
	    
	}
