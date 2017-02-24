package com.dataeye.common;


/**
 * 成员信息
 * @author chenfanglin
 * @date 2015年9月24日 下午2:59:52
 */
public class EmployeeInfo {

	// 邮箱账号
	private String Alias;
	
	// 姓名
	private String Name;
	
	// 性别 （1.男，2女）
	private int Gender;
	
	// 别名列表，用逗号分隔
	private String SlaveList;
	
	// 职位
	private String Position;
	
	// 联系电话
	private String Tel;
	
	// 手机
	private String Mobile;
	
	// 编号
	private String ExtId;
	
	// 部门列表
	private Department PartyList;
	
	//成员状态：1=启用，2=禁用
	private int OpenType;
	
	private int Status;

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return Alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		Alias = alias;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * @return the gender
	 */
	public int getGender() {
		return Gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(int gender) {
		Gender = gender;
	}

	/**
	 * @return the slaveList
	 */
	public String getSlaveList() {
		return SlaveList;
	}

	/**
	 * @param slaveList the slaveList to set
	 */
	public void setSlaveList(String slaveList) {
		SlaveList = slaveList;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return Position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		Position = position;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return Tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		Tel = tel;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return Mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	/**
	 * @return the extId
	 */
	public String getExtId() {
		return ExtId;
	}

	/**
	 * @param extId the extId to set
	 */
	public void setExtId(String extId) {
		ExtId = extId;
	}

	/**
	 * @return the partyList
	 */
	public Department getPartyList() {
		return PartyList;
	}

	/**
	 * @param partyList the partyList to set
	 */
	public void setPartyList(Department partyList) {
		PartyList = partyList;
	}

	/**
	 * @return the openType
	 */
	public int getOpenType() {
		return OpenType;
	}

	/**
	 * @param openType the openType to set
	 */
	public void setOpenType(int openType) {
		OpenType = openType;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return Status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		Status = status;
	}
	
}
