package com.attask.jenkins;

import hudson.Extension;
import hudson.Launcher;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixRun;
import hudson.model.BuildListener;
import hudson.util.IOUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

/**
 * User: Joel Johnson
 * Date: 2/1/13
 * Time: 12:53 PM
 */
public class LogAggregator extends MatrixAggregator {
	private final Object $listenerLock = new Object();

	protected LogAggregator(MatrixBuild build, Launcher launcher, BuildListener listener) {
		super(build, launcher, listener);
	}

	@Override
	public boolean endRun(MatrixRun run) throws InterruptedException, IOException {
		Reader logReader = run.getLogReader();
		try {
			synchronized ($listenerLock) {
				PrintStream logger = listener.getLogger();

				String finishedLine = "|  Finished running " + run.getFullDisplayName() + ".  |";
				String repeat = repeat('-', finishedLine.length());

				logger.println();
				logger.println();
				logger.println();
				logger.println(repeat);
				logger.println(finishedLine);
				logger.println(repeat);

				IOUtils.copy(logReader, logger);

				logger.println(repeat);
				logger.println(repeat);
			}
		} finally {
			logReader.close();
		}
		return true;
	}

	private String repeat(char character, int times) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < times; i++) {
			sb.append(character);
		}
		return sb.toString();
	}
}
