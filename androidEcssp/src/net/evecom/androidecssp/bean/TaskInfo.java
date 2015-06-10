package net.evecom.androidecssp.bean;

import java.io.Serializable;

public class TaskInfo implements Serializable{
	
	private String id;
	private String taskname;
	private String taskcontern;
	private String createtime;
	private String keyword;
	private String taskdept;
	private String taskdeptid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public String getTaskcontern() {
		return taskcontern;
	}
	public void setTaskcontern(String taskcontern) {
		this.taskcontern = taskcontern;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getTaskdept() {
		return taskdept;
	}
	public void setTaskdept(String taskdept) {
		this.taskdept = taskdept;
	}
	public String getTaskdeptid() {
		return taskdeptid;
	}
	public void setTaskdeptid(String taskdeptid) {
		this.taskdeptid = taskdeptid;
	} 
	
}
/*{
	"taskperson": "赵或",
	"eventid": "d44c4fea0fb048e9b5567488e182d199",
	"createtime": "2015-05-29 14:40:30",
	"restime": null,
	"taskname": "设置警戒点对警戒范围内进行戒严",
	"endtime": null,
	"remark": "备 注",
	"starttime": null,
	"deptpic": null,
	"taskcontern": "任务内容",
	"dataflg": null,
	"createby": "a382eba16e9a41f9b7377ad760215357",
	"updateby": null,
	"taskno": null,
	"planid": "1faa0daa10254580b40470e6e145694e",
	"taskdeptid": "b6239ac481ef48a891af572c70366026",
	"id": "3427290d13d64e52b92c76d373353d67",
	"state": "2",
	"taskpersonid": "dc75a101c7fe4487a5b7c35d5b7101e9",
	"taskeffi": "2015-05-29 14:40:12",
	"updatetime": "2015-06-05 11:31:18",
	"keyword": "设置警戒点3个",
	"projectid": "a9347db9fe2944f9b2a28c622ae15f0c",
	"taskdept": "市安监局"
}*/