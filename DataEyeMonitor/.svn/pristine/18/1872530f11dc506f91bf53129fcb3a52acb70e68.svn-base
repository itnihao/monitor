package com.dataeye.omp.module.monitor.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.common.PrivilegeControl.Scope;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.common.DEResponse;

/**
 * 对比图
 * @author chenfanglin
 * @date 2016年1月20日 上午10:56:03
 */
@Controller
public class ReportComparedModule {


	@Autowired
	private ReportComparedService reportComparedService;
	
	/**
	 * CPU 相关
	 * 10 CPU 总使用率对比图
	 * 11 CPU 1分钟负载
	 * 12 CPU 5分钟负载
	 * 13 CPU 15分钟负载
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 上午10:43:58
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getComparedCpuUsage.do")
	public DEResponse getComparedCpuUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE,Keys.STARTTIME,Keys.ENDTIME,Keys.FEATRUEID);
		DEResponse deResp = reportComparedService.getComparedCpuUsage(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getStarttime(),parameter.getEndtime(), parameter.getFeatrueID(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 网络相关
	 * 50 em2内网网卡出流量
	 * 51 em2内网网卡入流量
	 * 50 em1外网网卡出流量
	 * 51 em1 外网网卡入流量
	 * 52 em2内网网卡出包量
	 * 53 em2内网网卡入包量
	 * 52 em1外网网卡出包量
	 * 53 em1外网网卡入包量
	 * 54 被动打开TCP连接数
	 * 55TCP连接数
	 * 56UDP接收数据报
	 * 57UDP发送数据报
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 下午6:57:48
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getComparedNetworkUsage.do")
	public DEResponse getComparedNetworkUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE,Keys.STARTTIME,Keys.ENDTIME,Keys.FEATRUEID);
		DEResponse deResp = reportComparedService.getComparedNetworkUsage(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getStarttime(),parameter.getEndtime(), 
				parameter.getFeatrueID(), parameter.getObject(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 内存相关 内存使用率
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 下午4:56:49
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getComparedMemUsage.do")
	public DEResponse getComparedMemUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE,Keys.STARTTIME,Keys.ENDTIME,Keys.FEATRUEID);
		DEResponse deResp = reportComparedService.getComparedMemUsage(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getStarttime(),parameter.getEndtime(), parameter.getFeatrueID(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 内存相关
	 * 21 mem_used 内存使用率
	 * 22 mem_pri Private内存
	 * 23 mem_vir Virtual内存
	 * 24         Private+IPCS
	 * 25 mem_swap_used SWAP内存使用量
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 下午5:21:38
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getComparedMemUsageAmount.do")
	public DEResponse getComparedMemUsageAmount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE,Keys.STARTTIME,Keys.ENDTIME,Keys.FEATRUEID);
		DEResponse deResp = reportComparedService.getComparedMemUsageAmount(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getStarttime(),parameter.getEndtime(),
				parameter.getFeatrueID(), parameter.getObject(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 内存相关- SWAP内存使用率， 使用以下特性计算 SWAP使用大小/SWAP总大小
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 下午5:43:45
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getComparedMemSwapUsage.do")
	public DEResponse getComparedMemSwapUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE,Keys.STARTTIME,Keys.ENDTIME,Keys.FEATRUEID);
		DEResponse deResp = reportComparedService.getComparedMemSwapUsage(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getStarttime(),parameter.getEndtime(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * IO相关 -磁盘分区使用率 (分区总大小 -分区空闲大小)/分区总大小
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 下午7:26:17
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getComparedIOPartitionUsage.do")
	public DEResponse getComparedIOPartitionUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE,Keys.STARTTIME,Keys.ENDTIME);
		DEResponse deResp = reportComparedService.getComparedIOPartitionUsage(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getStarttime(),parameter.getEndtime(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * IO相关 -磁盘分区使用量 (分区总大小 -分区空闲大小)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 下午7:27:10
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getComparedIOPartitionAmount.do")
	public DEResponse getComparedIOPartitionAmount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE,Keys.STARTTIME,Keys.ENDTIME);
		DEResponse deResp = reportComparedService.getComparedIOPartitionAmount(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getStarttime(),parameter.getEndtime(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * IO相关 -
	 * featrueID=31&object=DISK IO IN
	 *featrueID=32&object=DISK IO OUT
	 *featrueID=33&object=SWAP SI
	 *featrueID=34&object=SWAP SO
	 *featrueID=35&object=’Svctm_time_max’
	 *featrueID=36&object=’Await_time_max’
	 *featrueID=37&object=’avgqu_sz_max’
	 *featrueID=38&object=’avgrq_sz’
	 *featrueID=39&object=’util_max’
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 下午7:31:24
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getComparedIOUsage.do")
	public DEResponse getComparedIOUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE,Keys.STARTTIME,Keys.ENDTIME,Keys.FEATRUEID);
		DEResponse deResp = reportComparedService.getComparedIOUsage(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getStarttime(),parameter.getEndtime(),
				parameter.getFeatrueID(),parameter.getObject(), parameter.getLang());
		return deResp;
	}
	
}
