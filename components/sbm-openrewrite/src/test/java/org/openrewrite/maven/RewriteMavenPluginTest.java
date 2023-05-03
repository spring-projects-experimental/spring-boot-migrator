/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.maven;


import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

/**
 * Testing the OpenRewrite Maven Plugin to verify expectations about Markers.
 * https://maven.apache.org/plugin-testing/maven-plugin-testing-harness/getting-started/index.html
 *
 * @author Fabian Krüger
 */
public class RewriteMavenPluginTest extends AbstractMojoTestCase
{
    /** {@inheritDoc} */
    protected void setUp()
            throws Exception
    {
        // required
        super.setUp();

    }

    /** {@inheritDoc} */
    protected void tearDown()
            throws Exception
    {
        // required
        super.tearDown();

    }

    /**
     * @throws Exception if any
     */
    public void testSomething()
            throws Exception
    {
        File pom = getTestFile( "src/test/resources/rewrite-maven-plugin/pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

//
//        MyMojo myMojo = (MyMojo) lookupMojo( "touch", pom );
//        assertNotNull( myMojo );
//        myMojo.execute();
//
//        ...
    }
}

