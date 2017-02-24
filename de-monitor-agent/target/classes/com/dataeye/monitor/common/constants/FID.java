package com.dataeye.monitor.common.constants;

/**
 * 服务器特性键值对
 * Created by wendy on 2016/7/4.
 */
public enum FID {

    cpu("cpu", 10),
    load_average_one("cpu", 11),
    load_average_five("cpu", 12),
    load_average_fifteen("cpu", 13),
    mem_proc("mem_proc", 20),
    mem_used("mem_used", 21),
    mem_pri("mem_pri", 22),
    mpri_ipcs("mpri_ipcs", 24),
    mem_vir("mem_vir", 23),
    mem_swap_used("mem_swap_used", 25),
    mem_swap_total("mem_swap_total", 26),
    mem_res_total("mem_res_total", 27),

    pgpgin("pgpgin", 31),
    pgpgout("pgpgout", 32),
    pswpin("pswpin",33),
    pswpout("pswpout", 34),
    svctm("svctm", 35),
    await("await", 36),
    avgqu_sz_max("aveq", 37),
    avgrq_sz("avgrq_sz", 38),
    util_max("util", 39),
    partition_free("partition_used", 40),
    partition_total("partition_total", 41),

    net_byte_in("net_byte_in", 50),
    net_byte_out("net_byte_out", 51),
    net_pack_in("net_pack_in", 52),
    net_pack_out("net_pack_out", 53),

    idcNetIn("idcNetIn", 60),
    idcNetOut("idcNetOut", 61),

    tcppassiveopens("passiveopens", 54),
    tcp_conn("TIME_ESTA", 55),
    udp_in("indatagrams", 56),
    udp_out("outdatagrams", 57),

    time_wait("TIME_WAIT", 58),
    ping("PING", 59),

    hard_proc("hard_proc", 70),
    hard_mem("hard_mem", 71),
    hard_temp("hard_temp", 72),
    hard_disk("hard_disk", 73),
    hard_pwr("hard_pwr", 74),
    hard_batt("hard_batt", 75),
    hard_nics("hard_nics", 76),
    hard_fans("hard_fans",77),
    mysql_open_files("open_files",80),
    mysql_open_files_limit("open_files_limit",81),
    mysql_conn("mysql_conn",82),
    mysql_lock_num("lock_num",83),
    mysql_lock_time("lock_time",84);

    private int value;
    private String name;

    // 构造方法
    private FID(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
