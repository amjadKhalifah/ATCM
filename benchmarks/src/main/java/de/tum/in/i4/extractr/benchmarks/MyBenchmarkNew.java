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
import parser.adtool.ADTParser;

import java.io.File;
import java.util.Set;

public class MyBenchmarkNew {
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

        File stealMasterKeyXML = new File(PROJECT_ROOT +
                "../src/test/resources/user_attribution/Steal_Master_Key.adt");
        File dummyTree2branches8levels = new File(PROJECT_ROOT +
                "../src/test/resources/evaluation/dummy_trees/tree_2branches_8levels.xml");
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkStealMasterKey_2Users_UnfoldAtTop(MyState state) {
        complete(state.stealMasterKeyXML, state.users2, state.adtParser, new int[]{});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkStealMasterKey_2Users_Unfold00(MyState state) {
        complete(state.stealMasterKeyXML, state.users2, state.adtParser, new int[]{0, 0});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkStealMasterKey_2Users_Unfold11(MyState state) {
        complete(state.stealMasterKeyXML, state.users2, state.adtParser, new int[]{1, 1});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkStealMasterKey_8Users_UnfoldAtTop(MyState state) {
        complete(state.stealMasterKeyXML, state.users8, state.adtParser, new int[]{});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkStealMasterKey_8Users_Unfold00(MyState state) {
        complete(state.stealMasterKeyXML, state.users8, state.adtParser, new int[]{0, 0});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkStealMasterKey_8Users_Unfold11(MyState state) {
        complete(state.stealMasterKeyXML, state.users8, state.adtParser, new int[]{1, 1});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree2Branches8Levels_2Users_UnfoldAtTop(MyState state) {
        complete(state.dummyTree2branches8levels, state.users2, state.adtParser, new int[]{});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree2Branches8Levels_2Users_Unfold00(MyState state) {
        complete(state.dummyTree2branches8levels, state.users2, state.adtParser, new int[]{0, 0});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree2Branches8Levels_2Users_Unfold22(MyState state) {
        complete(state.dummyTree2branches8levels, state.users2, state.adtParser, new int[]{2, 2});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree2Branches8Levels_2Users_Unfold55(MyState state) {
        complete(state.dummyTree2branches8levels, state.users2, state.adtParser, new int[]{5, 5});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree2Branches8Levels_8Users_UnfoldAtTtop(MyState state) {
        complete(state.dummyTree2branches8levels, state.users8, state.adtParser, new int[]{});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree2Branches8Levels_8Users_Unfold00(MyState state) {
        complete(state.dummyTree2branches8levels, state.users8, state.adtParser, new int[]{0, 0});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree2Branches8Levels_8Users_Unfold22(MyState state) {
        complete(state.dummyTree2branches8levels, state.users8, state.adtParser, new int[]{2, 2});
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    @Measurement(iterations = ITERATIONS)
    @Fork(FORKS)
    @BenchmarkMode(Mode.All)
    public void benchmarkDummyTree2Branches8Levels_8Users_Unfold55(MyState state) {
        complete(state.dummyTree2branches8levels, state.users8, state.adtParser, new int[]{5, 5});
    }

    private void complete(File attackTree, Set<User> users, ADTParser adtParser, int[] unfoldLevels) {
        FaultTreeDefinition tree = adtParser.toMEF(attackTree, users, unfoldLevels);
        // CURRENTLY NO PREEMPTION!
        CausalModel causalModel = CausalModel.fromMEF(tree);
    }

}
