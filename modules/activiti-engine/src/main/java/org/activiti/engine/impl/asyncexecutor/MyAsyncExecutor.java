package org.activiti.engine.impl.asyncexecutor;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.runtime.Job;

public class MyAsyncExecutor extends DefaultAsyncJobExecutor {
	public MyAsyncExecutor(ProcessEngineConfigurationImpl processEngineConfiguration) {
		super();
		
		this.setAsyncJobLockTimeInMillis(5*1000);//锁定5秒
		this.setResetExpiredJobsInterval(5*1000);
		this.setResetExpiredJobsRunnable(new MyResetExpiredJobsRunnable(this));
		processEngineConfiguration.setAsyncExecutorNumberOfRetries(1);
		this.setProcessEngineConfiguration(processEngineConfiguration);
	}
	
	protected Runnable createRunnableForJob(final Job job) {
		if (executeAsyncRunnableFactory == null) {
			return new MyExecuteAsyncRunnable(job, processEngineConfiguration);
		} else {
			return executeAsyncRunnableFactory.createExecuteAsyncRunnable(job, processEngineConfiguration);
		}
	}
	
}