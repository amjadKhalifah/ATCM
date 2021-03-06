/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.tum.in.i4.extractr.benchmarks;

import attacker_attribution.User;
import attacker_attribution.UserParser;
import causality.CausalModel;
import mef.faulttree.FaultTreeDefinition;
import org.openjdk.jmh.annotations.*;
import parser.adtool.ADTNode;
import parser.adtool.ADTParser;

import java.io.File;
import java.util.Set;

public class MyBenchmark {
    private static final int FORKS = 1;
    private static final int WARMUP_ITERATIONS = 0;
    private static final int ITERATIONS = 1;

    @State(Scope.Benchmark)
    public static class MyState {
        final String PROJECT_ROOT = System.getProperty("user.dir") + "/";
        final ADTParser adtParser = new ADTParser();

        Set<User> users2 = UserParser.parse(new File(PROJECT_ROOT + "../src/test/resources/evaluation/users/2users.xml"));
        Set<User> users4 = UserParser.parse(new File(PROJECT_ROOT + "../src/test/resources/evaluation/users/4users.xml"));
        Set<User> users8 = UserParser.parse(new File(PROJECT_ROOT + "../src/test/resources/evaluation/users/8users.xml"));

        File arsonistsXML = new File(PROJECT_ROOT +
                "../src/test/resources/evaluation/arsonists.xml");
        File billySuzyXML = new File(PROJECT_ROOT +
                "../src/test/resources/evaluation/BillySuzy.xml");
        File stealMasterKeyXML = new File(PROJECT_ROOT +
                "../src/test/resources/user_attribution/Steal_Master_Key.adt");
        File becomeRootUser1XML = new File(PROJECT_ROOT +
                "../src/test/resources/evaluation/Become_Root_User_1.xml");
        File becomeRootUser2XML = new File(PROJECT_ROOT +
                "../src/test/resources/evaluation/Become_Root_User_2.xml");
        File dummyTree16 = new File(PROJECT_ROOT +
                "../src/test/resources/evaluation/dummy_trees/tree_16branches.xml");
        File dummyTree48 = new File(PROJECT_ROOT +
                "../src/test/resources/evaluation/dummy_trees/tree_48branches.xml");
        ADTNode stealMasterKeyAttackTree;

        // will be executed for every benchmark method call
        @Setup(Level.Invocation)
        public void doSetupInvocation() {
            stealMasterKeyAttackTree = adtParser.fromAD(stealMasterKeyXML);
        }
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkArsonists_2Users(MyState state) {
        complete(state.arsonistsXML, state.users2, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkBillySuzy_2Users(MyState state) {
        complete(state.billySuzyXML, state.users2, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkStealMasterKey_2Users(MyState state) {
        complete(state.stealMasterKeyXML, state.users2, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkStealMasterKey_4Users(MyState state) {
        complete(state.stealMasterKeyXML, state.users4, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkStealMasterKey_8Users(MyState state) {
        complete(state.stealMasterKeyXML, state.users8, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkBecomeRootUser1_2Users(MyState state) {
        complete(state.becomeRootUser1XML, state.users2, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkBecomeRootUser1_4Users(MyState state) {
        complete(state.becomeRootUser1XML, state.users4, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkBecomeRootUser1_8Users(MyState state) {
        complete(state.becomeRootUser1XML, state.users8, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkBecomeRootUser2_2Users(MyState state) {
        complete(state.becomeRootUser2XML, state.users2, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkBecomeRootUser2_4Users(MyState state) {
        complete(state.becomeRootUser2XML, state.users4, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkBecomeRootUser2_8Users(MyState state) {
        complete(state.becomeRootUser2XML, state.users8, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree16Branches_2Users(MyState state) {
        complete(state.dummyTree16, state.users2, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree16Branches_4Users(MyState state) {
        complete(state.dummyTree16, state.users4, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree16Branches_8Users(MyState state) {
        complete(state.dummyTree16, state.users8, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree48Branches_2Users(MyState state) {
        complete(state.dummyTree48, state.users2, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree48Branches_4Users(MyState state) {
        complete(state.dummyTree48, state.users4, state.adtParser);
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree48Branches_8Users(MyState state) {
        complete(state.dummyTree48, state.users8, state.adtParser);
    }

    private void complete(File attackTree, Set<User> users, ADTParser adtParser) {
        FaultTreeDefinition tree = adtParser.toMEF(attackTree, users);
        CausalModel causalModel = CausalModel.fromMEF(tree, users);
    }

    /**
     * Helper method for unfolding an attack tree
     * @param attackTree
     * @param users
     */
    private void unfold(ADTNode attackTree, Set<User> users) {
        attackTree.unfold(users);
    }

}
