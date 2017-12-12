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
package org.wso2.testgrid.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class to handle {@link String} related operations.
 *
 * @since 1.0.0
 */
public class StringUtil {

    private static final Log log = LogFactory.getLog(StringUtil.class);

    /**
     * Returns whether the given string is a null or empty.
     *
     * @param string string to check whether null or empty
     * @return returns {@code true} if the string is null or empty, {@code false} otherwise
     */
    public static boolean isStringNullOrEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * Returns a string concatenating the given strings.
     *
     * @param objects list of objects to concatenate as strings
     * @return concatenated string
     */
    public static String concatStrings(Object... objects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : objects) {
            stringBuilder.append(object); // Null is handled by the append method.
        }
        return stringBuilder.toString();
    }
}
