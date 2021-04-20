package org.activiti.engine.impl.asyncexecutor;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ActivitiOptimisticLockingException;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyResetExpiredJobsRunnable extends ResetExpiredJobsRunnable
{
	private static Logger log = LoggerFactory.getLogger(MyResetExpiredJobsRunnable.class);
	
	public MyResetExpiredJobsRunnable(AsyncExecutor asyncExecutor)
	{
		super(asyncExecutor);
	}
	
	
	public synchronized void run()
	{
		log.info("{} starting to reset expired jobs");
		Thread.currentThread().setName("activiti-reset-expired-jobs");

		while (!isInterrupted)
		{

			try
			{
				//=================Custom Code===================
				List<JobEntity> expiredJobs = asyncExecutor.getProcessEngineConfiguration().getCommandExecutor()
            		.execute(new FindMyExpiredJobsCmd(asyncExecutor.getResetExpiredJobsPageSize()));
        
				List<String> expiredJobIds = new ArrayList<String>(expiredJobs.size());
				for (JobEntity expiredJob : expiredJobs) {
					expiredJobIds.add(expiredJob.getId());
				}
				//=================Custom Code===================

				if (expiredJobIds.size() > 0)
				{
					for(String str : expiredJobIds) {
						log.info("got expired jobs: " + str);
					}

					asyncExecutor.getProcessEngineConfiguration().getCommandExecutor()
						.execute(new ResetExpiredJobsCmd(expiredJobIds));
				}

			}
			catch (Throwable e)
			{
				if (e instanceof ActivitiOptimisticLockingException)
				{
					log.debug("Optmistic lock exception while resetting locked jobs", e);
				}
				else
				{
					log.error("exception during resetting expired jobs", e.getMessage(), e);
				}
			}

			// Sleep
			try
			{

				synchronized (MONITOR)
				{
					if (!isInterrupted)
					{
						isWaiting.set(true);
						MONITOR.wait(asyncExecutor.getResetExpiredJobsInterval());
					}
				}

			}
			catch (InterruptedException e)
			{
				if (log.isDebugEnabled())
				{
					log.debug("async reset expired jobs wait interrupted");
				}
			}
			finally
			{
				isWaiting.set(false);
			}

		}

		log.info("{} stopped resetting expired jobs");
	}
}