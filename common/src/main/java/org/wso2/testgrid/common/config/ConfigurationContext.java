/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.testgrid.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.testgrid.common.TestGridConstants;
import org.wso2.testgrid.common.util.StringUtil;
import org.wso2.testgrid.common.util.TestGridUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Implementation of configuration context which will contain functions relates with property file.
 * Example: Retrieve property value by property key.
 */
public class ConfigurationContext {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationContext.class);
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            Path configPath = TestGridUtil.getConfigFilePath();
            try (InputStream inputStream = Files.newInputStream(configPath)) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            logger.error(StringUtil.concatStrings(
                    "Error while trying to read ", TestGridConstants.TESTGRID_CONFIG_FILE));
        }
    }

    /**
     * Retrieve property from the property file.
     * @param property Property key as in the property file.
     * @return Property value read from property file.
     */
    public static String getProperty(Enum property) {
        return properties.getProperty(property.toString());
    }

    /**
     * Defines the testgrid configuration properties.
     */
    public enum ConfigurationProperties {

        /**
         * Database URL propertyName
         */
        DB_URL("DB_URL"),

        /**
         * Database user propertyName
         */
        DB_USER("DB_USER"),

        /**
         * Property for assword of database user
         */
        DB_USER_PASS("DB_USER_PASS"),

        /**
         * Jenkins host propertyName
         */
        JENKINS_HOST("JENKINS_HOST"),

        /**
         * Property for Jenkins user authorization key
         */
        JENKINS_USER_AUTH_KEY("JENKINS_USER_AUTH_KEY"),

        /**
         * SSO login URL propertyName
         */
        SSO_LOGIN_URL("SSO_LOGIN_URL"),

        /**
         * Property to check if SSO is enabled
         */
        ENABLE_SSO("ENABLE_SSO"),

        /**
         * AWS Region of TestGrid deployment
         */
        AWS_REGION_NAME("AWS_REGION_NAME"),

        /**
         * AWS S3 Bucket used to upload TestGrid artifacts
         */
        AWS_S3_BUCKET_NAME("AWS_S3_BUCKET_NAME"),

        /**
         * WUM Username of TestGrid deployment
         */
        WUM_USERNAME("WUM_USERNAME"),

        /**
         * WUM user password of TestGrid deployment
         */
        WUM_PASSWORD("WUM_PASSWORD"),

        /**
         * Deployment Tinkerer endpoint of TestGrid deployment.
         */
        DEPLOYMENT_TINKERER_EP("DEPLOYMENT_TINKERER_EP"),

        /**
         * Deployment Tinkerer username of TestGrid deployment.
         */
        DEPLOYMENT_TINKERER_USERNAME("DEPLOYMENT_TINKERER_USERNAME"),

        /**
         * Deployment Tinkerer password of TestGrid deployment.
         */
        DEPLOYMENT_TINKERER_PASSWORD("DEPLOYMENT_TINKERER_PASSWORD"),

        /**
         * Base path of deployment Tinkerer rest api
         */
        DEPLOYMENT_TINKERER_REST_BASE_PATH("DEPLOYMENT_TINKERER_REST_BASE_PATH"),

        /**
         * Host-name of TestGrid deployment.
         */
        TESTGRID_HOST("TESTGRID_HOST"),

        /**
         * Waits for tack deletion.
         */
        WAIT_FOR_STACK_DELETION("WAIT_FOR_STACK_DELETION");

        private String propertyName;

        /**
         * Sets the name of the property.
         *
         * @param propertyName name of the property
         */
        ConfigurationProperties(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public String toString() {
            return this.propertyName;
        }
    }
}
