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
package org.jupyterkernel.kernel;

import org.apache.commons.cli.*;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.HashMap;

/**
 *
 * @author kay schluehr
 */
public class Main extends Thread {
    public static final String PARAM_KERNEL_NAME= "kernelName";
    public static final String PARAM_CONN_FILE_PATH= "connectionFilePath";

    public static boolean _DEBUG_ = false;

    private static final java.util.Map<String, String> getArgs(String[] args)  {
        Options options = new Options();
        options.addOption("f", true, "connection file path");
        options.addOption("k", true, "kernel name");
        CommandLineParser parser = new PosixParser();
        java.util.Map params = new HashMap<String, String>();
        try {
            CommandLine cmd = parser.parse(options, args);
            params.put(PARAM_CONN_FILE_PATH, cmd.getOptionValue("f"));
            params.put(PARAM_KERNEL_NAME, cmd.getOptionValue("k"));
        } catch (ParseException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException(e);
        }
        return params;
    }

    public static java.util.Map<String, String> createArgsMap(String[] args) throws FileNotFoundException,
            InvalidKeyException,
            IOException {
        java.util.Map<String, String> newArgs = null;
        if (args==null || args.length == 0) {
            // Used for debugging the kernel.
            // Start this application in your IDE first.
            Session._DEBUG_ = true;
            ZContext ctx = new ZContext();
            Socket channel = ctx.createSocket(ZMQ.REP);
            channel.bind("tcp://127.0.0.1:2222");
            byte[] msg = channel.recv();
            String sArgs = new String(msg, StandardCharsets.UTF_8);
            newArgs = getArgs(sArgs.split(" "));
            channel.send("ok");
        } else {
           newArgs =getArgs(args);
        }
        return newArgs;
    }

    public static void main(String[] args) throws
            InvalidKeyException,
            IOException {
        java.util.Map<String, String> newArgs = createArgsMap(args);
        Session session = new Session(newArgs.get(PARAM_KERNEL_NAME), newArgs.get(PARAM_CONN_FILE_PATH));
        session.start();
        try {
            session.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
