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

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MyBenchmark {

    @State(Scope.Benchmark)
    public static class MyState {
        ADTParser adtParser;
        File users;
        File stealMasterKeyXML;

        @Setup(Level.Invocation)
        public void doSetup() {
            String projectRoot = System.getProperty("user.dir") + "/";

            adtParser = new ADTParser();
            users = new File(projectRoot + "../src/test/resources/user_attribution/users.xml");
            stealMasterKeyXML = new File(projectRoot + "../src/test/resources/user_attribution/Steal_Master_Key.adt");
            System.out.println("Do Setup");
        }
    }

    @Benchmark
    @Warmup(iterations = 3, time = 10, timeUnit = MILLISECONDS)
    @Measurement(iterations = 3, time = 10, timeUnit = MILLISECONDS)
    @BenchmarkMode(Mode.All)
    public void testMethod(MyState state) {
        Set<User> users = UserParser.parse(state.users);
        FaultTreeDefinition stealMasterKeyMEF = state.adtParser.toMEF(state.stealMasterKeyXML, users);
        CausalModel.fromMEF(stealMasterKeyMEF, users);
    }

}
