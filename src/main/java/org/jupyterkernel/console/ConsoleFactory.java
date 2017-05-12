/*
 * Copyright 2016 kay schluehr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jupyterkernel.console;

/**
 *
 * @author kay schluehr
 */

import javax.script.ScriptEngine;

public class ConsoleFactory {

    public static InteractiveConsole createConsole(String name)
    {
        switch(name)
        {
            case "python":
                return new JythonConsole();
            case "clojure":
                return new ClojureConsole();
            case "nashorn":
                return new NashornConsole("nashorn");
            default:
                return new InteractiveConsole(name);
        }
    }    
}
