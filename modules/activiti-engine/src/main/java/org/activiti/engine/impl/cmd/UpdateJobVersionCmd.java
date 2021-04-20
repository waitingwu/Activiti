package org.activiti.engine.impl.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateJobVersionCmd implements Command<Object>, Serializable {

    private static final long serialVersionUID = 1L;
  
    private static Logger log = LoggerFactory.getLogger(UpdateJobVersionCmd.class);
  
    protected Job job;
  
    public UpdateJobVersionCmd(Job job) {
      this.job = job;
    }
  
    public Object execute(CommandContext commandContext) {
  
      if (job == null) {
        throw new ActivitiIllegalArgumentException("job is null");
      }
  
      String jobId = job.getId();

      if (log.isDebugEnabled()) {
        log.debug("Executing update version job {}", jobId);
      }
  
      if (jobId != null) {
        commandContext.getJobEntityManager().updateJobVersion(jobId);
      }
  
      return null;
    }
  }
