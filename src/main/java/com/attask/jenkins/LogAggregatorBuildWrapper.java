package com.attask.jenkins;

import hudson.Extension;
import hudson.Launcher;
import hudson.matrix.MatrixAggregatable;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixProject;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

/**
 * User: Joel Johnson
 * Date: 2/1/13
 * Time: 1:37 PM
 */
public class LogAggregatorBuildWrapper extends BuildWrapper implements MatrixAggregatable {
	@DataBoundConstructor
	public LogAggregatorBuildWrapper() {

	}

	@Override
	public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
		return new Environment() {
			@Override
			public boolean tearDown(AbstractBuild build, BuildListener listener) throws IOException, InterruptedException {
				return super.tearDown(build, listener);
			}
		};
	}

	@Override
	public MatrixAggregator createAggregator(MatrixBuild build, Launcher launcher, BuildListener listener) {
		return new LogAggregator(build, launcher, listener);
	}

	@Extension
	public static class DescriptorImpl extends BuildWrapperDescriptor {
		@Override
		public boolean isApplicable(AbstractProject<?, ?> item) {
			return item instanceof MatrixProject;
		}

		@Override
		public String getDisplayName() {
			return "Aggregate Logs";
		}
	}
}
