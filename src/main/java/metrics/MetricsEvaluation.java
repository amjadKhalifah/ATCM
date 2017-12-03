package metrics;

import attacker_attribution.User;
import attacker_attribution.UserParser;
import causality.CausalModel;
import mef.faulttree.FaultTreeDefinition;
import parser.adtool.ADTNode;
import parser.adtool.ADTParser;

import java.io.File;
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
    public static final File USERS_4 = new File(USER_FILE_PATH + "4users.xml");
    public static final File USERS_8 = new File(USER_FILE_PATH + "8users.xml");

    private static CausalModel generateCausalModel(File attackTree, Set<User> users, boolean preemption) {
        FaultTreeDefinition tree = ADT_PARSER.toMEF(attackTree, users);
        CausalModel causalModel = preemption ? CausalModel.fromMEF(tree, users) : CausalModel.fromMEF(tree);
        return causalModel;
    }

    private static ADTNode generateAttackTree(File attackTree, Set<User> users) {
        ADTNode tree = ADT_PARSER.fromAD(attackTree);
        if (users != null) {
            tree.unfold(users);
        }

        return tree;
    }

    private static List<Set<User>> getUserSets() {
        return Stream.of(USERS_2, USERS_4, USERS_8)
                .map(UserParser::parse)
                .collect(Collectors.toList());
    }

    private static List<File> getAttackTrees() {
        File becomeRootUser1 = new File(TREE_FILE_PATH + "evaluation/Become_Root_User_1.xml");
        File becomeRootUser2 = new File(TREE_FILE_PATH + "evaluation/Become_Root_User_2.xml");
        File copySensitiveInformation = new File(TREE_FILE_PATH + "evaluation/Copy_Sensitive_Information_to_USB.xml");
        File stealMasterKey = new File(TREE_FILE_PATH + "user_attribution/Steal_Master_Key.adt");
        return Arrays.asList(becomeRootUser1, becomeRootUser2, copySensitiveInformation, stealMasterKey);
    }

    private static List<Metrics> computeAndPrintMetrics(File file, List<Set<User>> userSets) {
        List<Metrics> metrics = new ArrayList<>();

        ADTNode attackTreeNoUserAttribution = generateAttackTree(file, null);
        Metrics attackTreeMetricsNoUserAttribution = new Metrics(attackTreeNoUserAttribution);
        System.out.println("[AttackTree;'" + file.getName() + "';attribution=false] " + attackTreeMetricsNoUserAttribution);
        CausalModel causalModelNoUserAttribution = generateCausalModel(file, null, false);
        Metrics causalModelMetricsNoUserAttribution = new Metrics(causalModelNoUserAttribution);
        System.out.println("[CausalModel;'" + file.getName() + "'; attribution=false; preemption=false] " + causalModelMetricsNoUserAttribution);

        metrics.addAll(Arrays.asList(attackTreeMetricsNoUserAttribution, causalModelMetricsNoUserAttribution));

        for (Set<User> users : userSets) {
            ADTNode attackTree = generateAttackTree(file, users);
            Metrics attackTreeMetrics = new Metrics(attackTree);
            System.out.println("[AttackTree;'" + file.getName() + "'; attribution=true; users=" +users.size()+"] " +
                    attackTreeMetrics);

            CausalModel causalModelWithoutPreemption = generateCausalModel(file, users, false);
            Metrics causalModelMetricsWithoutPreemption = new Metrics(causalModelWithoutPreemption);
            System.out.println("[CausalModel;'" + file.getName() + "'; attribution=true; preemption=false; users="
                    +users.size()+"] " + causalModelMetricsWithoutPreemption);
            CausalModel causalModelWitPreemption = generateCausalModel(file, users, true);
            Metrics causalModelMetricsWithPreemption = new Metrics(causalModelWitPreemption);
            System.out.println("[CausalModel;'" + file.getName() + "'; attribution=true; preemption=true; users="
                    +users.size()+"] " + causalModelMetricsWithPreemption);

            metrics.addAll(Arrays.asList(attackTreeMetrics, causalModelMetricsWithoutPreemption,
                    causalModelMetricsWithPreemption));
        }
        return metrics;
    }

    public static void main(String[] args) {
        List<Set<User>> userSets = getUserSets();
        List<File> attackTrees = getAttackTrees();

        attackTrees.forEach(f -> computeAndPrintMetrics(f, userSets));
    }
}
