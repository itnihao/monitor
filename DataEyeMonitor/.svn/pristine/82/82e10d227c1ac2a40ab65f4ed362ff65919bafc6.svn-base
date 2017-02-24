package com.dataeye.omp.module.monitor.server;

import java.util.List;

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
import com.dataeye.omp.common.ValueItem;

/**
 * 服务器监控 图表控制器
 * @author chenfanglin
 * @date 2016年1月14日 上午11:23:05
 */
@Controller
public class ReportModule {

	@Autowired
	private ReportService reportService;
	
	/**
	 * CPU相关- CPU 总利用率
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月14日 上午11:25:02
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getCpuTotalUsage.do")
	public DEResponse getCpuTotalUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getCpuTotalUsage(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * CPU 1/5/15分钟负载
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 上午11:26:38
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getCpuEveryMinUsage.do")
	public DEResponse getCpuEveryMinUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getCpuEveryMinUsage(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 各核心CPU使用率
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午3:28:47
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getCpuEveryCoreUsage.do")
	public DEResponse getCpuEveryCoreUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getCpuEveryCoreUsage(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 内存相关- 内存总使用率=已使用/总内存大小
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午3:01:11
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getMemTotalUsage.do")
	public DEResponse getMemTotalUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getMemTotalUsage(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 内存使用详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月14日 下午6:37:52
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getMemUseState.do")
	public DEResponse getMemUseState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getMemUseState(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * SWAP内存使用率  SWAP使用大小/SWAP总大小
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 上午10:32:35
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getSwapMemTotalUsage.do")
	public DEResponse getSwapMemTotalUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getSwapMemTotalUsage(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * SWAP内存使用量
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午4:30:26
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getSwapMemUseState.do")
	public DEResponse getSwapMemUseState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getSwapMemUseState(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 内网网卡出入流量
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午4:51:09
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getIntranetNICFlow.do")
	public DEResponse getIntranetNICFlow(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getIntranetNICFlow(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 内网网卡出入包量
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午4:55:58
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getIntranetNICPackageVolume.do")
	public DEResponse getIntranetNICPackageVolume(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getIntranetNICPackageVolume(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 外网网卡出入流量
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午7:35:11
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getExtranetNICFlow.do")
	public DEResponse getExtranetNICFlow(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getExtranetNICFlow(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 外网网卡出入包量
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午4:56:35
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getExtranetNICPackageVolume.do")
	public DEResponse getExtranetNICPackageVolume(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getExtranetNICPackageVolume(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 打开的Tcp连接数
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午5:42:43
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getTcpConn.do")
	public DEResponse getTcpConn(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getTcpConn(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 被动打开的Tcp连接数
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午5:42:43
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getPassiveTcpConn.do")
	public DEResponse getPassiveTcpConn(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getPassiveTcpConn(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * UDP收发数据报
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午4:59:12
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getUDPDatagram.do")
	public DEResponse getUDPDatagram(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getUDPDatagram(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 磁盘IO相关 - 磁盘分区使用率
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午6:15:20
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getIOTotalUsage.do")
	public DEResponse getIOTotalUsage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getIOTotalUsage(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 磁盘IO相关 - 磁盘IO使用量
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午6:21:51
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getIOUseState.do")
	public DEResponse getIOUseState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getIOUseState(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 磁盘IO相关 - 磁盘IO读写
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午6:24:35
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getDiskIOReadWrite.do")
	public DEResponse getDiskIOReadWrite(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getDiskIOReadWrite(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 磁盘IO相关 - SWAP 使用情况
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午6:25:15
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getIOSwapUseState.do")
	public DEResponse getIOSwapUseState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE);
		DEResponse deResp = reportService.getIOSwapUseState(parameter.getServerID(), parameter.getStartdate(), 
				parameter.getEnddate(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 35	Svctm_time_max
	 * 36	Await_time_max
	 * 37	avgqu_sz_max
	 * 38	avgrq_sz
	 * 39	util_max
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月21日 上午11:13:05
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getIOUseTrend.do")
	public DEResponse getIOUseTrend(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID,Keys.STARTDATE,Keys.ENDDATE,Keys.FEATRUEID);
		DEResponse deResp = reportService.getFeatureFrom35To39(parameter.getServerID(), parameter.getStartdate(),
				parameter.getEnddate(), parameter.getFeatrueID(), parameter.getLang());
		return deResp;
	}
	
	/**
	 * 硬件状态 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午6:26:40
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getHardWareStatus.do")
	public Object getHardWareStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.SERVERID);
		List<ValueItem> list = reportService.getHardWareStatus(parameter.getServerID(),parameter.getLang());
		return list;
	}
	
}






