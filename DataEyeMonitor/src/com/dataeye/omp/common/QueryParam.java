package com.dataeye.omp.common;

import java.util.List;

import com.dataeye.exception.ClientException;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.utils.ReflectUtils;
import com.dataeye.utils.ValidateUtils;

/**
 * 查询参数
 * @author chenfanglin
 * @date 2016年1月6日 下午2:20:54
 */
public class QueryParam {
	
	//开始日期
	private String startDate;
	
	// 结束日期
	private String endDate;
	
	// 页数
	private int pageID;
	
	// 每页记录数
	private int pageSize;
	
	// 排序字段名称
	private String sortName;
	
	// 排序方法 desc  asc
	private String order;
	
	// 查询关键字
	private String keyValue;

	/**
     * <pre>
     * 检查必填参数是否为非法值,如果为字符串验证是否为空，如果为数字验证是否为invalid_number
     *
     * @param parameterNames
     * @throws ClientException
     * @throws ServerException
     * @author Ivan<br>
     * @date 2015年4月1日 下午4:38:17 <br>
     */
    public void checkParameter(String... parameterNames) throws ClientException, ServerException {
        for (String attr : parameterNames) {
            Class<?> clz = ReflectUtils.getFieldType(QueryParam.class, attr);
            Object object = ReflectUtils.getFieldValue(this, attr);
            if (object == null) {
                ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
            }
            if (clz == String.class) {// 是String类型，调用get方法，得到的值是否为空
                String value = (String) object;
                if (ValidateUtils.isEmpty(value)) {
                    ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
                }
                // 对时间格式多做一个匹配
                if (Keys.STARTDATE.equals(attr) || Keys.ENDDATE.equals(attr)) {
                    if (!value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR_TIME);
                    }
                }
            } else if (clz == Integer.class || clz == int.class) {
                if (!ValidateUtils.isValidNumber((Integer) object)) {
                    ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
                }
            } else if (clz == Long.class || clz == long.class) {
                if (!ValidateUtils.isValidNumber((Long) object)) {
                    ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
                }
            } else if (clz == List.class) {
                List l = (List) object;
                if (ValidateUtils.isEmptyList(l)) {
                    ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
                }
            } else {
                ExceptionHandler.throwClassException(StatusCode.CLASS_ERROR,
                        "unsupport class name in checkParameter:" + clz.getName());
            }
        }
    }
    
	/**
	 * @return the pageID
	 */
	public int getPageID() {
		return pageID;
	}

	/**
	 * @param pageID the pageID to set
	 */
	public void setPageID(int pageID) {
		this.pageID = pageID;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the sortName
	 */
	public String getSortName() {
		return sortName;
	}

	/**
	 * @param sortName the sortName to set
	 */
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * @return the keyValue
	 */
	public String getKeyValue() {
		return keyValue;
	}

	/**
	 * @param keyValue the keyValue to set
	 */ 
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
