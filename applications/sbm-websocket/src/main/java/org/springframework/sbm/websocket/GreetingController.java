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

package org.springframework.sbm.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

  @MessageMapping("/scan")
  @SendTo("/topic/applicableRecipes")
  public String greeting(String message) throws Exception {
    Thread.sleep(1000); // simulated delay
    System.out.println("scanning " + message);
    return "applicableRceipes: " + "some list of recipes"; //new Greeting("Hello, " + HtmlUtils.htmlEscape(message) + "!");
  }

  @SubscribeMapping("/topic/hello")
  public String sendOneTimeMessage() {
    return "server one-time message via the application";
  }

}