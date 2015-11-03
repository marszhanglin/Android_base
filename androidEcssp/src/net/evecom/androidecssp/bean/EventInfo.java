package net.evecom.androidecssp.bean;

import java.io.Serializable;

public class EventInfo  implements Serializable{
	
	/**
     * 描述
     */
    private static final long serialVersionUID = 2554459493557977955L;
    private String id;
	private String eventname;
	private String happendate;
	private String typename;
	private String gisx;
	private String gisy;
	private String areaname;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEventname() {
		return eventname;
	}
	public void setEventname(String eventname) {
		this.eventname = eventname;
	}
	public String getHappendate() {
		return happendate;
	}
	public void setHappendate(String happendate) {
		this.happendate = happendate;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getGisx() {
		return gisx;
	}
	public void setGisx(String gisx) {
		this.gisx = gisx;
	}
	public String getGisy() {
		return gisy;
	}
	public void setGisy(String gisy) {
		this.gisy = gisy;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	
}
/*{
	"happenaddress": "宁德市福鼎市秦屿镇备湾村",
	"eventarea": "350200",
	"reporterdate": null,
	"curstatus": "1",
	"happendate": "2015-05-18 17:13:45",
	"remark": null,
	"updatedt": null,
	"eventname": "宁德 核电站2号机组发生爆炸",
	"reporterdeptid": null,
	"createby": "超级管理员",
	"datatype": null,
	"id": "d44c4fea0fb048e9b5567488e182d199",
	"dienum": null,
	"eventreceiver": null,
	"createtime": "2015-05-19 17:14:14",
	"auditstatus": "1",
	"areaname": "厦门市",
	"takensteps": null,
	"belongunitname": null,
	"eventlever": "4",
	"attr2": null,
	"reporterdept": null,
	"attr1": null,
	"dataflg": null,
	"belongunitid": "3d5ded86e8f24a3a94e84a8c6137b7aa",
	"missnum": null,
	"updateby": "超级管理员",
	"receivedate": null,
	"eventlevel": null,
	"gisx": "120.171375273125",
	"receivedeptid": null,
	"reporterperson": "报告人",
	"gisy": "26.96499785875",
	"eventtype": "12000",
	"eventstatus": "2",
	"reportertel": "18920391092",
	"dataaqure": null,
	"hurtnum": null,
	"eventclass": null,
	"eventcontent": "宁德 核电站2号机组发生爆炸",
	"typename": "事故灾难",
	"eventannex": "0396b279a08f410684b01b5f4d8cb7e1,7e7d6945cd7844b7a04c1622804641fe,54cfaa0de6c54e0d9aff7dc10aba3c96"
}*/