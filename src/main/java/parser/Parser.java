package parser;

import mef.faulttree.FaultTreeDefinition;
import org.apache.commons.io.FilenameUtils;
import parser.adtool.ADTParser;
import parser.emfta.EMFTAParser;

import java.io.File;

public abstract class Parser {
    public static FaultTreeDefinition handle(File file) {
        FaultTreeDefinition faultTreeDefinition = null;
        // get the file extension: .<filetype>
        String extension = FilenameUtils.getExtension(file.getName());

        // depending on the file type set the correct parser
        if (extension.equals("emfta")) {
            Parser parser = new EMFTAParser();
            faultTreeDefinition = parser.toMEF(file);
        } else if (extension.equals("adt")) {
            Parser parser = new ADTParser();
            faultTreeDefinition = parser.toMEF(file);
        }

        return faultTreeDefinition;
    }

    public abstract FaultTreeDefinition toMEF(File file);
}
