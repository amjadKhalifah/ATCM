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
        // get command line options
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();

        try {
            // parse command line
            CommandLine line = parser.parse(options, args);

            List<String> cliArgs =  line.getArgList();

            // check if a file has been passed
            if (cliArgs.size() < 1)
                throw new ParseException("To be converted file not specified.");

            File f = new File(cliArgs.get(0));

            if (!f.exists() || f.isDirectory())
                throw new ParseException("Specified File '" + f.getPath() + "' does not exist or is directory.");

            // convert passed file to Open-PSA fault tree def
            FaultTreeDefinition faultTreeDefinition = Parser.handle(f);
            if (faultTreeDefinition == null) {
                System.err.println("Error during parsing of file '" + f.getName() + "'.");
            }

            // convert fault tree def to causal model
            CausalModel causalModel = CausalModel.fromMEF(faultTreeDefinition);

            // convert causal model to report
            String report = causalModel.toReport();
            System.out.println(report);


            if (line.hasOption('e')) {
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
