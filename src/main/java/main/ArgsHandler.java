package main;


import attacker_attribution.User;
import attacker_attribution.UserParser;
import causality.CausalModel;
import graph.GraphBuilder;
import mef.faulttree.FaultTreeDefinition;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

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

            Set<User> users = null;
            if (line.hasOption("u")) {
                File usersFile = new File(line.getOptionValue("u"));
                users = UserParser.parse(usersFile);
            }

            // convert passed file to Open-PSA fault tree def
            FaultTreeDefinition faultTreeDefinition = Parser.handle(f, users);
            // if result is null, something went wrong
            if (faultTreeDefinition == null) {
                System.err.println("Error during parsing of file '" + f.getName() + "'.");
            }

            // convert fault tree representation to causal model
            CausalModel causalModel;
            if (users != null)
                causalModel = CausalModel.fromMEF(faultTreeDefinition, users);
            else
                causalModel = CausalModel.fromMEF(faultTreeDefinition);



            // convert causal model to report and print it
            String report = causalModel.toReport();
            System.out.println(report);

            // if option 'e' (i.e. -e <path>) exists, the user wants to export the causal model and the report
            if (line.hasOption('e')) {
                // get file path specified with option e
                String path = line.getOptionValue('e');
                File dir = new File(path);
                if (!dir.isDirectory())
                    throw new ParseException("Cannot export output. The specified export path is not a directory or " +
                            "does not exist.");
                try {
                    File graphFile = new File(dir.getAbsolutePath() + "/"+f.getName()+"_causal_graph.dot");
                    // export the graph
                    GraphBuilder.export(causalModel, graphFile.getAbsolutePath());
                    // export report
                    FileUtils.write(new File(dir.getAbsolutePath() + "/"+f.getName()+"_report.txt"), report, "UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            HelpFormatter helpFormatter = new HelpFormatter();
            String header = "Extract causal model from ATTACK or FAULT TREE.\n";
            String footer = "Supported modelling tools: EMFTA, ADTool";
            helpFormatter.printHelp("extractr [ATTACK/FAULT TREE]", header, options, footer, true);
        }
    }

    // define command line options
    private static Options getOptions() {
        Options options = new Options();

        Option graphExport = Option.builder("e").hasArg().build();
        graphExport.setDescription("path to export directory");
        graphExport.setArgName("file");

        Option unfold = Option.builder("u").hasArg().build();
        unfold.setDescription("unfold attack tree using passed user file");
        unfold.setArgName("users");

        options.addOption(graphExport);
        options.addOption(unfold);

        return options;
    }
}
