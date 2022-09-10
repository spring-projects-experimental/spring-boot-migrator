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
package org.springframework.sbm.shell2;

import org.springframework.sbm.shell2.client.api.ScanResult;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

/**
 * @author Fabian Krüger
 */
@Component
public class ScanProgressRenderer extends AbstractShellComponent {
    public void renderResult(ScanResult scanResult) {
        System.out.println("Scan finished");
    }

    public void renderUpdate(ScanProgressUpdate scanProgressUpdate) {
        System.out.println("Scan update");
    }

    public void startScan(Path path) {
        System.out.println("start scan " + path);
    }
}
