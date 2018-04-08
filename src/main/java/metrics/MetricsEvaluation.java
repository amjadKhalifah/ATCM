package metrics;

import attacker_attribution.User;
import attacker_attribution.UserParser;
import causality.CausalModel;
import mef.faulttree.FaultTreeDefinition;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import parser.adtool.ADTNode;
import parser.adtool.ADTParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetricsEvaluation {
    private static final String USER_FILE_PATH = "src/test/resources/evaluation/users/";
    private static final String TREE_FILE_PATH = "src/test/resources/";
    private static final ADTParser ADT_PARSER = new ADTParser();

    public static final File USERS_2 = new File(USER_FILE_PATH + "2users.xml");
    public static final File USERS_3 = new File(USER_FILE_PATH + "3users.xml");
    public static final File USERS_4 = new File(USER_FILE_PATH + "4users.xml");
    public static final File USERS_8 = new File(USER_FILE_PATH + "8users.xml");

    private static File arsonists = new File(TREE_FILE_PATH + "evaluation/arsonists.xml");
    private static File billySuzy = new File(TREE_FILE_PATH + "evaluation/BillySuzy.xml");
    private static File becomeRootUser1 = new File(TREE_FILE_PATH + "evaluation/Become_Root_User_1.xml");
    private static File becomeRootUser2 = new File(TREE_FILE_PATH + "evaluation/Become_Root_User_2.xml");
    private static File copySensitiveInformation = new File(TREE_FILE_PATH + "evaluation/Copy_Sensitive_Information_to_USB.xml");
    private static File stealMasterKey = new File(TREE_FILE_PATH + "user_attribution/Steal_Master_Key.adt");
    private static File dummyTree2branches = new File(TREE_FILE_PATH + "evaluation/dummy_trees/tree_2branches.xml");
    private static File dummyTree4branches = new File(TREE_FILE_PATH + "evaluation/dummy_trees/tree_4branches.xml");
    private static File dummyTree8branches = new File(TREE_FILE_PATH + "evaluation/dummy_trees/tree_8branches.xml");
    private static File dummyTree16branches = new File(TREE_FILE_PATH + "evaluation/dummy_trees/tree_16branches.xml");
    private static File dummyTree48branches = new File(TREE_FILE_PATH + "evaluation/dummy_trees/tree_48branches.xml");

    private static final String CSV_FILE = System.getProperty("user.home") + "/Desktop/evaluation.csv";

    @Deprecated
    private static CausalModel generateCausalModel(File attackTree, Set<User> users, boolean preemption) {
        FaultTreeDefinition tree = ADT_PARSER.toMEF(attackTree, users);
        CausalModel causalModel = preemption ? CausalModel.fromMEF(tree, users) : CausalModel.fromMEF(tree);
        return causalModel;
    }

    private static CausalModel generateCausalModel(File attackTree, Set<User> users, boolean preemption,
                                                   int[] unfoldLevels) {
        FaultTreeDefinition tree = ADT_PARSER.toMEF(attackTree, users, unfoldLevels);
        CausalModel causalModel = preemption ? CausalModel.fromMEF(tree, users) : CausalModel.fromMEF(tree);
        return causalModel;
    }

    @Deprecated
    private static ADTNode generateAttackTree(File attackTree, Set<User> users) {
        ADTNode tree = ADT_PARSER.fromAD(attackTree);
        if (users != null) {
            tree.unfold(users);
        }

        return tree;
    }

    private static ADTNode generateAttackTree(File attackTree, Set<User> users, int[] unfoldLevels) {
        ADTNode tree = ADT_PARSER.fromAD(attackTree);
        if (users != null) {
            tree.unfold(users, unfoldLevels);
        }

        return tree;
    }

    private static List<Set<User>> getUserSets() {
        return Stream.of(USERS_2, USERS_3, USERS_4, USERS_8)
                .map(UserParser::parse)
                .collect(Collectors.toList());
    }

    private static List<File> getAttackTrees() {
        return Arrays.asList(arsonists, billySuzy, becomeRootUser1, becomeRootUser2, copySensitiveInformation,
                stealMasterKey, dummyTree2branches, dummyTree4branches, dummyTree8branches, dummyTree16branches,
                dummyTree48branches);
    }

    private static List<String> toCSVEntry(String type, String name, boolean attribution, boolean preemption,
                                            int users, Metrics metrics) {
        List<String> csvEntry = Arrays.asList(type, name, Boolean.toString(attribution), Boolean.toString(preemption),
                Integer.toString(users), Integer.toString(metrics.getNodes()), Integer.toString(metrics.getEdges()),
                Integer.toString(metrics.getLeafs()), Integer.toString(metrics.getAnds()),
                Integer.toString(metrics.getOrs()));
        return csvEntry;
    }

    private static List<String> toCSVEntry(String type, String name, boolean attribution, boolean preemption,
                                           int users, Metrics metrics, int[] unfoldLevels) {
        List<String> csvEntry = Arrays.asList(type, name, Boolean.toString(attribution), Boolean.toString(preemption),
                Integer.toString(users), Integer.toString(metrics.getNodes()), Integer.toString(metrics.getEdges()),
                Integer.toString(metrics.getLeafs()), Integer.toString(metrics.getAnds()),
                Integer.toString(metrics.getOrs()), Arrays.toString(unfoldLevels));
        return csvEntry;
    }

    private static List<Metrics> computeAndExportMetrics(File file, List<Set<User>> userSets, CSVPrinter csvPrinter) throws IOException {
        List<Metrics> metrics = new ArrayList<>();

        ADTNode attackTreeNoUserAttribution = generateAttackTree(file, null);
        Metrics attackTreeMetricsNoUserAttribution = new Metrics(attackTreeNoUserAttribution);
        csvPrinter.printRecord(toCSVEntry("AttackTree", file.getName(), false, false, 0,
                attackTreeMetricsNoUserAttribution));

        CausalModel causalModelNoUserAttribution = generateCausalModel(file, null, false);
        Metrics causalModelMetricsNoUserAttribution = new Metrics(causalModelNoUserAttribution);
        csvPrinter.printRecord(toCSVEntry("CausalModel", file.getName(), false, false, 0,
                causalModelMetricsNoUserAttribution));

        metrics.addAll(Arrays.asList(attackTreeMetricsNoUserAttribution, causalModelMetricsNoUserAttribution));

        for (Set<User> users : userSets) {
            ADTNode attackTree = generateAttackTree(file, users);
            Metrics attackTreeMetrics = new Metrics(attackTree);
            csvPrinter.printRecord(toCSVEntry("AttackTree", file.getName(), true, false, users.size(),
                    attackTreeMetrics));

            CausalModel causalModelWithoutPreemption = generateCausalModel(file, users, false);
            Metrics causalModelMetricsWithoutPreemption = new Metrics(causalModelWithoutPreemption);
            csvPrinter.printRecord(toCSVEntry("CausalModel", file.getName(), true, false, users.size(),
                    causalModelMetricsWithoutPreemption));

            CausalModel causalModelWitPreemption = generateCausalModel(file, users, true);
            Metrics causalModelMetricsWithPreemption = new Metrics(causalModelWitPreemption);
            csvPrinter.printRecord(toCSVEntry("CausalModel", file.getName(), true, true, users.size(),
                    causalModelMetricsWithPreemption));

            metrics.addAll(Arrays.asList(attackTreeMetrics, causalModelMetricsWithoutPreemption,
                    causalModelMetricsWithPreemption));
        }

        return metrics;
    }

    private static List<Metrics> computeAndExportMetricsNew(File file, List<Set<User>> userSets,
                                                            List<int[]> unfoldConfigs, CSVPrinter csvPrinter) throws IOException {
        List<Metrics> metrics = new ArrayList<>();

        ADTNode attackTreeNoUserAttribution = generateAttackTree(file, null, null);
        Metrics attackTreeMetricsNoUserAttribution = new Metrics(attackTreeNoUserAttribution);
        csvPrinter.printRecord(toCSVEntry("AttackTree", file.getName(), false, false, 0,
                attackTreeMetricsNoUserAttribution, null));

        CausalModel causalModelNoUserAttribution = generateCausalModel(file, null, false, null);
        Metrics causalModelMetricsNoUserAttribution = new Metrics(causalModelNoUserAttribution);
        csvPrinter.printRecord(toCSVEntry("CausalModel", file.getName(), false, false, 0,
                causalModelMetricsNoUserAttribution, null));

        metrics.addAll(Arrays.asList(attackTreeMetricsNoUserAttribution, causalModelMetricsNoUserAttribution));

        for (int[] unfoldLevels: unfoldConfigs) {
            for (Set<User> users : userSets) {
                ADTNode attackTree = generateAttackTree(file, users, unfoldLevels);
                Metrics attackTreeMetrics = new Metrics(attackTree);
                csvPrinter.printRecord(toCSVEntry("AttackTree", file.getName(), true, false, users.size(),
                        attackTreeMetrics, unfoldLevels));

                CausalModel causalModelWithoutPreemption = generateCausalModel(file, users, false, unfoldLevels);
                Metrics causalModelMetricsWithoutPreemption = new Metrics(causalModelWithoutPreemption);
                csvPrinter.printRecord(toCSVEntry("CausalModel", file.getName(), true, false, users.size(),
                        causalModelMetricsWithoutPreemption, unfoldLevels));

                // PREEMPTION ist not yet implemented with new unfold method
                /*CausalModel causalModelWitPreemption = generateCausalModel(file, users, true);
                Metrics causalModelMetricsWithPreemption = new Metrics(causalModelWitPreemption);
                csvPrinter.printRecord(toCSVEntry("CausalModel", file.getName(), true, true, users.size(),
                        causalModelMetricsWithPreemption));*/

                metrics.addAll(Arrays.asList(attackTreeMetrics, causalModelMetricsWithoutPreemption));
            }
        }

        return metrics;
    }

    public static void main(String[] args) {
        List<Set<User>> userSets = getUserSets();
        List<File> attackTrees = getAttackTrees();

        FileWriter writer = null;
        CSVPrinter csvPrinter = null;

        List<Metrics> metrics = new ArrayList<>();
        try {
            // clear file
            new PrintWriter(CSV_FILE).close();
            writer = new FileWriter(CSV_FILE, true);
            csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Type", "Name", "Attribution",
                    "Preemption", "Users", "Nodes", "Edges", "Leafs", "Ands", "Ors", "UnfoldLevels"));
            List<int[]> unfoldConfigsStealMasterKey = Arrays.asList(new int[] {}, new int[] {0,0}, new int[] {0,1},
                    new int[] {1,0}, new int[] {1,1});
            metrics.addAll(computeAndExportMetricsNew(stealMasterKey, userSets, unfoldConfigsStealMasterKey,
                    csvPrinter));
            // TODO artificial trees

            // uncomment to generate metrics for old unfold method
            /*csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("Type", "Name", "Attribution", "Preemption", "Users", "Nodes", "Edges", "Leafs",
                            "Ands", "Ors"));
            for (File attackTree : attackTrees) {
                metrics.addAll(computeAndExportMetrics(attackTree, userSets, csvPrinter));
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
                csvPrinter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
