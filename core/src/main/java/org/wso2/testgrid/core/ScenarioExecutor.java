/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.testgrid.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.testgrid.automation.Test;
import org.wso2.testgrid.automation.TestAutomationException;
import org.wso2.testgrid.automation.reader.TestReader;
import org.wso2.testgrid.automation.reader.TestReaderFactory;
import org.wso2.testgrid.common.Deployment;
import org.wso2.testgrid.common.TestPlan;
import org.wso2.testgrid.common.TestScenario;
import org.wso2.testgrid.common.util.StringUtil;
import org.wso2.testgrid.core.exception.ScenarioExecutorException;
import org.wso2.testgrid.dao.TestGridDAOException;
import org.wso2.testgrid.dao.uow.TestScenarioUOW;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is mainly responsible for executing a TestScenario. This will invoke the TestAutomationEngine for
 * executing the tests available for a particular solution pattern.
 *
 * @since 1.0.0
 */
public class ScenarioExecutor {

    private static final Log log = LogFactory.getLog(ScenarioExecutor.class);

    /**
     * This method executes a given TestScenario.
     *
     * @param testScenario an instance of TestScenario in which the tests should be executed.
     * @param deployment   an instance of Deployment in which the tests should be executed against.
     * @param testPlan     test plan associated with the test scenario
     * @throws ScenarioExecutorException If something goes wrong while executing the TestScenario.
     */
    public void runScenario(TestScenario testScenario, Deployment deployment, TestPlan testPlan)
            throws ScenarioExecutorException {
        try {
            // Run test scenario.
            String homeDir = testPlan.getTestRepoDir();
            testScenario.setTestPlan(testPlan);
            testScenario.setStatus(TestScenario.Status.TEST_SCENARIO_RUNNING);
            testScenario = persistTestScenario(testScenario);

            log.info("Executing Tests for Solution Pattern : " + testScenario.getName());
            String testLocation = Paths.get(homeDir, testScenario.getName()).toAbsolutePath().toString();
            List<Test> tests = getTests(testScenario, testLocation);

            for (Test test : tests) {
                log.info(StringUtil.concatStrings("Executing ", test.getTestName(), " Test"));
                test.execute(testLocation, deployment);
                log.info("---------------------------------------");
            }

            // Test scenario completed.
            testScenario.setStatus(TestScenario.Status.TEST_SCENARIO_COMPLETED);
            persistTestScenario(testScenario);
        } catch (TestAutomationException e) {
            testScenario.setStatus(TestScenario.Status.TEST_SCENARIO_ERROR);
            persistTestScenario(testScenario);
            throw new ScenarioExecutorException(StringUtil
                    .concatStrings("Exception occurred while running the Tests for Solution Pattern '",
                            testScenario.getName(), "'"));
        }
    }

    /**
     * Persists the {@link TestScenario} instance.
     *
     * @param testScenario test scenario to be persisted
     * @return persisted {@link TestScenario} instance
     * @throws ScenarioExecutorException thrown when error on persisting the {@link TestScenario} instance
     */
    private TestScenario persistTestScenario(TestScenario testScenario) throws ScenarioExecutorException {
        try {
            TestScenarioUOW testScenarioUOW = new TestScenarioUOW();
            return testScenarioUOW.persistTestScenario(testScenario);
        } catch (TestGridDAOException e) {
            throw new ScenarioExecutorException(StringUtil
                    .concatStrings("Error occurred when persisting test scenario - ", testScenario), e);
        }
    }

    /**
     * This method goes through every type of test in the folder structure and returns a list of tests
     * with common test interface.
     *
     * @param testScenario test scenario to obtain tests from
     * @param testLocation location on which test files are
     * @return a list of {@link Test} instances
     * @throws ScenarioExecutorException thrown when an error on reading tests
     */
    private List<Test> getTests(TestScenario testScenario, String testLocation) throws ScenarioExecutorException {
        try {
            Path testLocationPath = Paths.get(testLocation);
            List<Test> testList = new ArrayList<>();

            if (Files.exists(testLocationPath)) {
                TestScenario.TestEngine testType = testScenario.getTestEngine();
                Optional<TestReader> testReader = TestReaderFactory.getTestReader(testType);

                if (testReader.isPresent()) {
                    List<Test> tests = testReader.get().readTests(testLocation, testScenario);
                    testList.addAll(tests);
                }
            }
            return testList;
        } catch (TestAutomationException e) {
            throw new ScenarioExecutorException("Error while reading tests for test scenario.", e);
        }
    }
}
