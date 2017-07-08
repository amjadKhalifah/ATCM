package main;


import causality.CausalModel;
import graph.GraphBuilder;
import mef.faulttree.FaultTreeDefinition;
import org.apache.commons.cli.*;
import parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ArgsHandler {
    public static void handle(String[] args) {
        // get command line options based on which the command line will be parsed
        Options options = getOptions();
        // instantiate new command line parser
        CommandLineParser parser = new DefaultParser();

        try {
            // parse command line
            CommandLine line = parser.parse(options, args);
            // get all command line arguments as list
            // NOTE: these are the arguments and NOT the options!
            List<String> cliArgs =  line.getArgList();

            // check if a file has been passed; we assume that the one and only argument is a file
            if (cliArgs.size() < 1)
                throw new ParseException("To be converted file not specified.");
            // create file object from first passed argument
            File f = new File(cliArgs.get(0));

            // throw exception, if file does not exist or file is a directory
            if (!f.exists() || f.isDirectory())
                throw new ParseException("Specified File '" + f.getPath() + "' does not exist or is directory.");

            // convert passed file to Open-PSA fault tree def
            FaultTreeDefinition faultTreeDefinition = Parser.handle(f);
            // if result is null, something went wrong
            if (faultTreeDefinition == null) {
                System.err.println("Error during parsing of file '" + f.getName() + "'.");
            }

            // convert fault tree representation to causal model
            CausalModel causalModel = CausalModel.fromMEF(faultTreeDefinition);

            // convert causal model to report and print it
            String report = causalModel.toReport();
            System.out.println(report);

            // if option 'e' (i.e. -e <path>) exists, the user wants to export the causal model graph as .dot file
            if (line.hasOption('e')) {
                // get file path specified with option e
                String filePath = line.getOptionValue('e');
                try {
                    GraphBuilder.export(causalModel, filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // define command line options
    private static Options getOptions() {
        Options options = new Options();

        Option graphExport = Option.builder("e").hasArg().build();
        graphExport.setDescription("path and filename of exported .dot-file containing the causal graph");
        graphExport.setArgName("file");

        options.addOption(graphExport);

        return options;
    }
}
