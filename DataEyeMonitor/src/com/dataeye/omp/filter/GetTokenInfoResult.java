package com.dataeye.omp.filter;

/**
 * 调用接口http://10.1.2.176:48080/ptlogin/innerapi/getTokenInfo.do?token=
 * 76381489e82052444e801212c6d128bd62cc587c378528fd的返回结果
 * 
 * @author ivantan
 *
 */
public class GetTokenInfoResult {
	/**
	 * {"statusCode":200,"content":{"UID":10012,"userID":"demo@qq.com",
	 * "userName":"Demo","companyName":"慧动-测试账号","tel":"","qq":"35725755",
	 * "createTime":1379060401,"createID":0,"status":2},"id":"1441173588402u"}
	 */
	private int statusCode;
	private TokenInfo content;
	private String id;

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setContent(TokenInfo content) {
		this.content = content;
	}

	public TokenInfo getContent() {
		return content;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "GetTokenInfoResult [statusCode=" + statusCode + ", content=" + content + ", id=" + id + "]";
	}

}
