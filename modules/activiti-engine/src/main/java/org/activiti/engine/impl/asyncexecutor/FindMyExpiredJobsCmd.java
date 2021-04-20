package org.activiti.engine.impl.asyncexecutor;

import java.util.List;

import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.JobEntity;

public class FindMyExpiredJobsCmd implements Command<List<JobEntity>> {
  
    protected int pageSize;
    
    public FindMyExpiredJobsCmd(int pageSize) {
      this.pageSize = pageSize;
    }
    
    @Override
    public List<JobEntity> execute(CommandContext commandContext) {
      return commandContext.getJobEntityManager().findMyExpiredJobs(new Page(0, pageSize));
    }
  
  }
  