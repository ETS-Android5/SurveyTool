package com.hkm.api;

import com.hkm.datamodel.JobTaskData;

import java.util.ArrayList;

/**
 * Created by Hesk on 5/6/2014.
 */
public class JobTaskDataWorker {
  /*  private JobTaskData jt;*/
    private ArrayList<JobTaskData> list;
/*

    public JobTaskDataWorker(JobTaskData c) {
        this.jt = c;
    }
*/

    public JobTaskDataWorker(ArrayList<JobTaskData> c) {
        this.list = c;
    }

    public JobTaskData findByJID(int JID) throws Exception {
        for (JobTaskData j : this.list) {
            if (j.getID() == JID) return j;
        }
        throw new Exception("JOB related ID is not found");
    }

}
