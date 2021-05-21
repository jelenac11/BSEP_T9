package com.tim9.bolnica.util;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleBasedSystemUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(RuleBasedSystemUtil.class);

	public static void mavenCleanAndInstall() throws MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File("../drools-spring-kjar/pom.xml"));
        request.setGoals(Arrays.asList("clean", "install"));

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv("MAVEN_HOME")));
        InvocationResult result = invoker.execute(request);
        if (result.getExitCode() != 0) {
        	logger.info("Maven clean and install failed");
            System.out.println(result.getExecutionException().toString());
            System.out.println(result.getExitCode());
        } else {
        	logger.error("Successfully finished maven clean and install");
        }
    }
}
