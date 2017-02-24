package com.dataeye.omp.module.monitor.server;

import java.util.Comparator;

import com.qq.jutil.string.StringUtil;

/**
 * 服务器监控 比较器
 * @author chenfanglin
 * @date 2016年1月13日 上午11:10:13
 */
public class MachineComparator {
	
	/**
	 * 默认根据id降序【排序
	 */
	public static final Comparator<Machine> DEFAULT_ID_DESC = new Comparator<Machine>() {
		public int compare(Machine o1, Machine o2) {
			if (o1.getServerID() - o2.getServerID() > 0) {
                return -1;
            }
            if (o1.getServerID() - o2.getServerID() < 0) {
                return 1;
            }
			return 0;
		}
	};
	
	/**
	 * 点主机名降序
	 */
	public static final Comparator<Machine> CLICK_HOSTNONE_DESC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (o1.getHostName().compareTo(o2.getHostName()) > 0) {
                return -1;
            }
            if (o1.getHostName().compareTo(o2.getHostName()) < 0) {
                return 1;
            }
			return 0;
		}
	};
	
	/**
	 * 点主机名升序
	 */
	public static final Comparator<Machine> CLICK_HOSTNAME_ASC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (o1.getHostName().compareTo(o2.getHostName()) < 0) {
				return -1;
			}
			if (o1.getHostName().compareTo(o2.getHostName()) > 0) {
				return 1;
			}
			return 0;
		}
	};

	/**
	 * 点IP降序
	 */
//	public static final Comparator<Machine> CLICK_IP_DESC = new Comparator<Machine>() {
//		@Override
//		public int compare(Machine o1, Machine o2) {
//			if (o1.getIp().compareTo(o2.getIp()) > 0) {
//                return -1;
//            }
//            if (o1.getIp().compareTo(o2.getIp()) < 0) {
//                return 1;
//            }
//			return 0;
//		}
//	};
	
	/**
	 * 点IP升序
	 */
//	public static final Comparator<Machine> CLICK_IP_ASC = new Comparator<Machine>() {
//		@Override
//		public int compare(Machine o1, Machine o2) {
//			if (o1.getIp().compareTo(o2.getIp()) < 0) {
//				return -1;
//			}
//			if (o1.getIp().compareTo(o2.getIp()) > 0) {
//				return 1;
//			}
//			return 0;
//		}
//	};
	
	/**
	 * 点机房降序
	 */
	public static final Comparator<Machine> CLICK_MACHINEROOM_DESC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (o1.getMachineRoom().compareTo(o2.getMachineRoom()) > 0) {
                return -1;
            }
            if (o1.getMachineRoom().compareTo(o2.getMachineRoom()) < 0) {
                return 1;
            }
			return 0;
		}
	};
	
	/**
	 * 点机房升序
	 */
	public static final Comparator<Machine> CLICK_MACHINEROOM_ASC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (o1.getMachineRoom().compareTo(o2.getMachineRoom()) < 0) {
				return -1;
			}
			if (o1.getMachineRoom().compareTo(o2.getMachineRoom()) > 0) {
				return 1;
			}
			return 0;
		}
	};
	
	/**
	 * 点业务降序
	 */
	public static final Comparator<Machine> CLICK_BUSINESS_DESC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (StringUtil.isNotEmpty(o1.getBusiness()) &&
					StringUtil.isNotEmpty(o2.getBusiness())) {
				if (o1.getBusiness().compareTo(o2.getBusiness()) > 0) {
	                return -1;
	            }
	            if (o1.getBusiness().compareTo(o2.getBusiness()) < 0) {
	                return 1;
	            }
			}else if(StringUtil.isNotEmpty(o1.getBusiness())&&
					StringUtil.isEmpty(o2.getBusiness())){
				return -1;
			}else if(StringUtil.isEmpty(o1.getBusiness())&&
					StringUtil.isNotEmpty(o2.getBusiness())){
				return 1;
			} else {
				return -1;
			}
			return 0;
		}
	};
	
	/**
	 * 点业务升序
	 */
	public static final Comparator<Machine> CLICK_BUSINESS_ASC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (StringUtil.isNotEmpty(o1.getBusiness()) && StringUtil.isNotEmpty(o2.getBusiness())) {
				if (o1.getBusiness().compareTo(o2.getBusiness()) < 0) {
					return -1;
				}
				if (o1.getBusiness().compareTo(o2.getBusiness()) > 0) {
					return 1;
				}
			} else if (StringUtil.isNotEmpty(o1.getBusiness()) &&
					StringUtil.isEmpty(o2.getBusiness())) {
				return 1;
			} else if (StringUtil.isEmpty(o1.getBusiness()) &&
					StringUtil.isNotEmpty(o2.getBusiness())) {
				return -1;
			} else {
				return 1;
			}
			return 0;
		}
	};
	
	/**
	 * 点CPU使用率降序
	 */
	public static final Comparator<Machine> CLICK_CPUUSAGE_DESC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (StringUtil.isNotEmpty(o1.getCpuUsage()) && StringUtil.isNotEmpty(o2.getCpuUsage())) {
				String v1 = o1.getCpuUsage().substring(0, o1.getCpuUsage().length() - 1);
				String v2 = o2.getCpuUsage().substring(0, o2.getCpuUsage().length() - 1);

				if (Double.parseDouble(v1) > Double.parseDouble(v2)) {
					return -1;
				}
				if (Double.parseDouble(v1) < Double.parseDouble(v2)) {
					return 1;
				}
			} else {
				return -1;
			}
			return 0;
		}
	};
	
	/**
	 * 点cpu使用率升序
	 */
	public static final Comparator<Machine> CLICK_CPUUSAGE_ASC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (StringUtil.isNotEmpty(o1.getCpuUsage()) && StringUtil.isNotEmpty(o2.getCpuUsage())) {
				String v1 = o1.getCpuUsage().substring(0, o1.getCpuUsage().length() - 1);
				String v2 = o2.getCpuUsage().substring(0, o2.getCpuUsage().length() - 1);

				if (Double.parseDouble(v1) < Double.parseDouble(v2)) {
					return -1;
				}
				if (Double.parseDouble(v1) > Double.parseDouble(v2)) {
					return 1;
				}
			} else {
				return 1;
			}
			return 0;
		}
	};
	
	/**
	 * 点五分钟负载降序
	 */
	public static final Comparator<Machine> CLICK_FIVELOAD_DESC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (o1.getFiveLoad()>o2.getFiveLoad()) {
				return -1;
			}
			if (o1.getFiveLoad()<o2.getFiveLoad()) {
				return 1;
			}
			return 0;
		}
	};
	
	/**
	 * 点五分钟负载升序
	 */
	public static final Comparator<Machine> CLICK_FIVELOAD_ASC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (o1.getFiveLoad()<o2.getFiveLoad()) {
				return -1;
			}

				if (o1.getFiveLoad()>o2.getFiveLoad()) {
				return 1;
			}
			return 0;
		}
	};
	
	/**
	 * 点状态降序
	 */
	public static final Comparator<Machine> CLICK_STATUS_DESC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (o1.getStatus() - o2.getStatus() > 0) {
                return -1;
            }
            if (o1.getStatus() - o2.getStatus() < 0) {
                return 1;
            }
			return 0;
		}
	};
	
	/**
	 * 点状态升序
	 */
	public static final Comparator<Machine> CLICK_STATUS_ASC = new Comparator<Machine>() {
		@Override
		public int compare(Machine o1, Machine o2) {
			if (o1.getStatus() - o2.getStatus() < 0) {
				return -1;
			}
			if (o1.getStatus() - o2.getStatus() > 0) {
				return 1;
			}
			return 0;
		}
	};
}
