package org.activiti.engine.impl.asyncexecutor;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cmd.LockExclusiveJobCmd;
import org.activiti.engine.impl.cmd.UpdateJobVersionCmd;
import org.activiti.engine.runtime.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyExecuteAsyncRunnable extends ExecuteAsyncRunnable
{
	private static Logger log = LoggerFactory.getLogger(MyExecuteAsyncRunnable.class);

	public MyExecuteAsyncRunnable(String jobId, ProcessEngineConfigurationImpl processEngineConfiguration)
	{
		super(jobId, processEngineConfiguration);
	}
	
	public MyExecuteAsyncRunnable(Job job, ProcessEngineConfigurationImpl processEngineConfiguration)
	{
		super(job, processEngineConfiguration);
	}
	

	protected boolean lockJobIfNeeded()
	{
		try
		{
			if (job.isExclusive())
			{
				processEngineConfiguration.getCommandExecutor().execute(new LockExclusiveJobCmd(job));
			}
			
			log.info("=========[lockJobIfNeeded] try customize upgradeJobVersion======" + job.getId());
			//============Custom Code==========
			processEngineConfiguration.getCommandExecutor().execute(new UpdateJobVersionCmd(job));
			//============Custom Code==========
		}
		catch (Throwable lockException)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Could not lock exclusive job. Unlocking job so it can be acquired again. Catched exception: "
						+ lockException.getMessage());
			}

			// Release the job again so it can be acquired later or by another node
			unacquireJob();

			return false;
		}

		return true;
	}

}