package metrics;

import attacker_attribution.User;
import attacker_attribution.UserParser;
import causality.CausalModel;
import mef.faulttree.FaultTreeDefinition;
import parser.adtool.ADTNode;
import parser.adtool.ADTParser;

import java.io.File;
import java.util.Set;

public class MetricsEvaluation {
    private static final String USER_FILE_PATH = "src/test/resources/evaluation/users/";
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

    public static void main(String[] args) {
        Set<User> users2 = UserParser.parse(USERS_2);
        Set<User> users4 = UserParser.parse(USERS_4);
        Set<User> users8 = UserParser.parse(USERS_8);
    }
}
