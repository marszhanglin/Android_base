package net.evecom.androidecssp.bean;

import java.io.Serializable;

public class ProjectInfo implements Serializable{
	private String id;
	private String eventid;
	private String planid;
	private String projectname;
	private String createtime;
	private String projectcode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEventid() {
		return eventid;
	}
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}
	public String getPlanid() {
		return planid;
	}
	public void setPlanid(String planid) {
		this.planid = planid;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getProjectcode() {
		return projectcode;
	}
	public void setProjectcode(String projectcode) {
		this.projectcode = projectcode;
	}
	 
}


/*{
	"eventid": "d44c4fea0fb048e9b5567488e182d199",
	"projectname": "°²È«¾¯½ä",
	"createtime": "2015-05-25 16:44:36",
	"projectcode": "F42",
	"projectcontent": null,
	"remark": null,
	"dataflg": null,
	"createby": "a382eba16e9a41f9b7377ad760215357",
	"projectno": "1",
	"updateby": null,
	"planid": "1faa0daa10254580b40470e6e145694e",
	"id": "a9347db9fe2944f9b2a28c622ae15f0c",
	"state": null,
	"updatetime": null
}*/
 