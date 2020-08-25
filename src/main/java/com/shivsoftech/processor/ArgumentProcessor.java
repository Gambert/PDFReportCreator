package com.shivsoftech.processor;

import com.shivsoftech.core.Parameters;
import com.shivsoftech.util.Constants;
import com.shivsoftech.util.Utility;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

import static com.shivsoftech.util.Constants.*;

public class ArgumentProcessor<E> extends AbstractProcessor {

    private static Logger LOG = LoggerFactory.getLogger(ArgumentProcessor.class);

    private HelpFormatter formatter = new HelpFormatter();
    private CommandLine commandLine = null;

    List<Options> listOptions = null;
    Options options = null;

    public ArgumentProcessor() {
        initialize();
    }

    private void initialize() {
        listOptions = new ArrayList<>();
        listOptions.add(new Options().addOptionGroup(getConfigFileOptions()));
        listOptions.add(new Options().addOptionGroup(getCommandLineOptions()));
        listOptions.add(new Options().addOptionGroup(getHelpOption()));

        options = new Options();

        getConfigFileOptions()
                .getOptions()
                .forEach(option -> options.addOption(option));
        getCommandLineOptions()
                .getOptions()
                .forEach(option -> options.addOption(option));
        getHelpOption()
                .getOptions()
                .forEach(option -> options.addOption(option));
    }

    private void print(Options options) {
        System.out.println();
        formatter.printHelp(LAUNCHER, options);
    }

    private OptionGroup getConfigFileOptions() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup
                .addOption(OptionBuilder.hasArg(true).withArgName("Configuration file").withLongOpt("config").create(SWITCH_INPUT_CONF_FILE))
                //.addOption(OptionBuilder.hasArg(true).withArgName("Output PDF file").withLongOpt("output").create(SWITCH_OUTPUT_MERGED_FILE))
        ;
        return optionGroup;
    }

    private OptionGroup getCommandLineOptions() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup
                .addOption(OptionBuilder.hasArg(true).withArgName("Front page PDF file").withLongOpt("front").create(SWITCH_INPUT_FRONT_PAGE))
                .addOption(OptionBuilder.hasArg(true).withArgName("Main PDF file").withLongOpt("main").create(SWITCH_INPUT_MAIN_FILE))
                .addOption(OptionBuilder.hasArg(true).withArgName("Last page PDF file").withLongOpt("last").create(SWITCH_INPUT_LAST_PAGE))
                .addOption(OptionBuilder.hasArg(true).withArgName("Output PDF file").withLongOpt("output").create(SWITCH_OUTPUT_MERGED_FILE))
        ;
        return optionGroup;
    }

    private OptionGroup getHelpOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup
                .addOption(OptionBuilder.hasArg(false).withArgName("Prints help").withLongOpt("help").create(SWITCH_HELP))
        ;
        return optionGroup;
    }

    private void printUsage() {
        listOptions
                .stream()
                .forEach(this::print);

        System.out.println();

        System.exit(0);
    }

    private void loadCommandArguments() {
        CommandLineParser parser = new PosixParser();
        try {

            this.commandLine = parser.parse(options, (String[]) context.get(COMMAND_ARGS));

        } catch (ParseException ex) {
            LOG.error(ex.getMessage()+ ", displaying help.");
            printUsage();
        }
    }

    private boolean hasHelpCommand() {
        return commandLine.hasOption(SWITCH_HELP);
    }

    private boolean hasConfigFileCommand() {
        return commandLine.hasOption(SWITCH_INPUT_CONF_FILE);
    }

    private boolean hasCommandLineArgsCommand() {
        return commandLine.hasOption(SWITCH_INPUT_FRONT_PAGE) && commandLine.hasOption(SWITCH_INPUT_LAST_PAGE) &&
                commandLine.hasOption(SWITCH_INPUT_MAIN_FILE) && commandLine.hasOption(SWITCH_OUTPUT_MERGED_FILE);
    }

    private boolean isValidArguments() {

        LOG.info("Validating arguments.");
        boolean isHelpCommand = hasHelpCommand();
        boolean isConfigFileCommand = hasConfigFileCommand();
        boolean isCommandLineArgsCommand = hasCommandLineArgsCommand();

        // LOG.info("isHelpCommand            :: " + isHelpCommand);
        // LOG.info("isConfigFileCommand      :: " + isConfigFileCommand);
        // LOG.info("isCommandLineArgsCommand :: " + isCommandLineArgsCommand);

        return isHelpCommand || isConfigFileCommand || isCommandLineArgsCommand;
    }

    private void processArguments() {

        List<Parameters> list = null;

        if (hasHelpCommand()) {

            LOG.info("Display help.");
            printUsage();
        } else if (hasCommandLineArgsCommand()) {

            LOG.info("Processing command line options.");
            Parameters parameters = new Parameters();
            parameters.setFront(commandLine.getOptionValue(SWITCH_INPUT_FRONT_PAGE));
            parameters.setLast(commandLine.getOptionValue(SWITCH_INPUT_LAST_PAGE));
            parameters.setMain(commandLine.getOptionValue(SWITCH_INPUT_MAIN_FILE));
            parameters.setOutput(commandLine.getOptionValue(SWITCH_OUTPUT_MERGED_FILE));

            list = new ArrayList<>();
            list.add(parameters);

        } else if (hasConfigFileCommand()) {
            LOG.info("Processing config file options.");

            String configFile = commandLine.getOptionValue(SWITCH_INPUT_CONF_FILE);
            // TODO Validate if file exists
            if (Utility.isFileExist(configFile)) {

                list = readConfigFile(configFile);

            } else {
                LOG.error("Unable to find config file ("+configFile+")");
                return;
            }
        }

        if (!Utility.isEmpty(list)) {
            context.set(Constants.MERGE_PARAMS_LIST, list);
        }
    }

    private List<Parameters> readConfigFile(String configFile) {
        List<Parameters> list = null;
        Properties props = new Properties();

        try {
            FileInputStream inputStream = new FileInputStream(new File(configFile));
            props.load(inputStream);
            inputStream.close();
        } catch (Exception ex) {
            LOG.error("Error while reading a config file");
            return null;
        }

        int count = Integer.parseInt(props.getProperty(PROP_CONFIG_COUNT));
        list = new ArrayList<>();
        for (int index=1; index<=count; index++) {

            Parameters parameters = new Parameters();
            parameters.setFront(props.getProperty(PROP_CONFIG_PREFIX + index + PROP_CONFIG_SUFFIX_PREPEND));
            parameters.setLast(props.getProperty(PROP_CONFIG_PREFIX + index + PROP_CONFIG_SUFFIX_APPEND));
            parameters.setMain(props.getProperty(PROP_CONFIG_PREFIX + index + PROP_CONFIG_SUFFIX_CONTENT));
            parameters.setOutput(props.getProperty(PROP_CONFIG_PREFIX + index + PROP_CONFIG_SUFFIX_OUTPUT));

            list.add(parameters);
        }

        LOG.info("Config file processed");
        return list;
    }

    @Override
    public void doProcess() {

        // TODO Load arguments
        loadCommandArguments();

        // TODO Validate arguments
        boolean isValidArguments = false;
        if (isValidArguments()) {

            processArguments();

        } else {
            LOG.error("Missing one or more arguments, displaying help.");
            printUsage();
        }

        return;
    }
}
