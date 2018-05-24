package cn.finder.wae.business.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaskPlan implements Serializable{

	
	private long id;
	
	private String name;
	
	private long showtableConfigId;
	
	private int status;
	
	private int times;
	
	private long duration;
	
	private Date startTime;
	
	private String processClass;
	
	private String params;
	
	private String remark;
	
	private int isUpdated;
	
	private int hasCompleteTimes=0;
	
	private int startWithServer=0;
	
	private int isThreadQueue=0;
	private boolean forceStoped=false;
	
	private Map<String,Object>  mapPara= new HashMap<String,Object>();
 
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getProcessClass() {
		return processClass;
	}

	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getShowtableConfigId() {
		return showtableConfigId;
	}

	public void setShowtableConfigId(long showtableConfigId) {
		this.showtableConfigId = showtableConfigId;
	}

	public int getIsUpdated() {
		return isUpdated;
	}

	public void setIsUpdated(int isUpdated) {
		this.isUpdated = isUpdated;
	}

	public Map<String, Object> getMapPara() {
		return mapPara;
	}
	
	
	public Object get(String key)
	{
		return mapPara.get(key);
	}
	public String getString(String key)
	{
		return (String)mapPara.get(key);
	}
	public long getLong(String key)
	{
		return Long.valueOf(getString(key));
	}
	
	public int getInt(String key)
	{
		return Integer.valueOf(getString(key));
	}

	public int getHasCompleteTimes() {
		return hasCompleteTimes;
	}

	public void setHasCompleteTimes(int hasCompleteTimes) {
		this.hasCompleteTimes = hasCompleteTimes;
	}

	public int getStartWithServer() {
		return startWithServer;
	}

	public void setStartWithServer(int startWithServer) {
		this.startWithServer = startWithServer;
	}

	public int getIsThreadQueue() {
		return isThreadQueue;
	}

	public void setIsThreadQueue(int isThreadQueue) {
		this.isThreadQueue = isThreadQueue;
	}

	public boolean isForceStoped() {
		return forceStoped;
	}

	public void setForceStoped(boolean forceStoped) {
		this.forceStoped = forceStoped;
	}

	
	
	
	
	
}
