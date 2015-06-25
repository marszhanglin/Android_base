package net.evecom.androidecssp.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "SYS_DICT")
public class SysDictBean  {
	/** Ö÷¼ü */
	@Id(column = "Idkey")
	private String idkey;
	/** Öµ */
	private String value;

	public String getIdkey() {
		return idkey;
	}

	public void setIdkey(String idkey) {
		this.idkey = idkey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
