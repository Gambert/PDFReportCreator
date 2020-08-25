package com.shivsoftech;

import com.shivsoftech.core.Parameters;
import com.shivsoftech.processor.AbstractProcessor;
import com.shivsoftech.processor.ArgumentProcessor;
import com.shivsoftech.processor.MergeProcessor;
import com.shivsoftech.processor.Processor;
import com.shivsoftech.util.Constants;
import com.shivsoftech.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.boot.Banner.Mode.OFF;

@SpringBootApplication
public class Launcher extends AbstractProcessor<Optional> implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(Launcher.class);
    private List<Processor> processes = null;

    public static void main(String[] args) {
        //LOG.info("Starting the application.");

        SpringApplication application = new SpringApplication(Launcher.class);
        //application.setBannerMode(OFF);
        application.run(args);
        //LOG.info("Application ended.");
    }

    @Override
    public void run(String... args) {

        LOG.info("Application execution started.");

        context.set(Constants.COMMAND_ARGS, args);

        initilize();
        doProcess();

        LOG.info("Application execution finished.");
    }

    private void initilize() {
        processes = new ArrayList<>();

        processes.add(new ArgumentProcessor());
        processes.add(new MergeProcessor());
    }

    @Override
    public void doProcess() {

        if (Utility.isEmpty(processes))
            return;

        processes
                .stream()
                .forEach(Processor::doProcess);
//        // TODO Process the arguments
//        new ArgumentProcessor<>().doProcess();
//
//        // TODO Process the merging of files
//        new MergeProcessor<>().doProcess();

        return;
    }
}
