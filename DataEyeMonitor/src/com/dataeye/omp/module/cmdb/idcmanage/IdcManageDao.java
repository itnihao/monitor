package com.dataeye.omp.module.cmdb.idcmanage;


import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class IdcManageDao {

    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplate;


    //获取服务器机房机柜分布图
    public List<IdcVo> get(){

        final List<IdcVo> idcList = new ArrayList<>();

        try {
            //IDC
            String sql_idc = "select id,name from idc_list";
            jdbcTemplate.query(sql_idc, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    IdcVo idcVo = new IdcVo();
                    int idcId = rs.getInt(1);
                    String idcName = rs.getString(2);

                    //CABINET
                    String sql_cabinet = "select id,name from cabinet_list where idc_id = ?";
                    final List<CabinetVo> cabinetList = new ArrayList<>();
                    jdbcTemplate.query(sql_cabinet, new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet rs) throws SQLException {

                            int cabinetId = rs.getInt(1);
                            String cabinetName = rs.getString(2);

                            //server
                            String sql_server = "select a.host_name hostname,b.ip ip,a.id,a.sortFlag flag from server_list a left " +
                                    "join server_ip_list b on a.id=b.svr_id where b.type=0 and a.cabinet_id=? order by flag";
                            final List<HostNameAndIP> hostNameAndIPList = new ArrayList<>();
                            jdbcTemplate.query(sql_server, new RowCallbackHandler() {
                                @Override
                                public void processRow(ResultSet rs) throws SQLException {
                                    HostNameAndIP hostNameAndIP = new HostNameAndIP();
                                    hostNameAndIP.setIp(rs.getString(2));
                                    hostNameAndIP.setHostname(rs.getString(1));
                                    hostNameAndIP.setId(rs.getInt(3));
                                    hostNameAndIPList.add(hostNameAndIP);
                                }
                            }, cabinetId);

                            CabinetVo cabinetVo = new CabinetVo();
                            cabinetVo.setName(cabinetName);
                            cabinetVo.setList(hostNameAndIPList);
                            cabinetList.add(cabinetVo);
                        }
                    }, idcId);

                    idcVo.setName(idcName);
                    idcVo.setList(cabinetList);
                    idcList.add(idcVo);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < idcList.size(); i++) {
            IdcVo idc = idcList.get(i);
            List<CabinetVo> cabinetList = idc.getList();
            List<CabinetVo> removeList = new ArrayList<>();
            for (int j = 0; j < cabinetList.size(); j++) {
                CabinetVo cabinet = cabinetList.get(j);
                if (cabinet.getList().size() == 0) {
                    removeList.add(cabinet);
                }
            }
            for (CabinetVo vo : removeList) {
                cabinetList.remove(vo);
            }
        }

        return idcList;
    }

    /**
     * 服务器机柜排序
     * @param serverId 服务器id
     * @param sortFlag 上移 0  下移 1
     * @return
     */
    public int updateSortFlag(int serverId, int sortFlag){
        String sql_flag = "select sortFlag from server_list where id = ?";
        String sql_cabinet = "select cabinet_id from server_list where id = ?";
        String sql_maxFlag = "select max(sortFlag) from server_list where cabinet_id = ?";
        String sql_preOrNex = "select id from server_list where cabinet_id = ? and sortFlag = ?";
        String sql_update = "update server_list set sortFlag = ? where id = ?";
        int flag = 0;
        try {

            int currentFlag = jdbcTemplate.queryForInt(sql_flag, serverId);

            int cabinetId = jdbcTemplate.queryForInt(sql_cabinet, serverId);

            int maxFlag = jdbcTemplate.queryForInt(sql_maxFlag, cabinetId);

            System.out.println(" currentFlag " + currentFlag + "  cabinetid " + cabinetId + " maxflag " + maxFlag + "  serverid " + serverId);
            //上移
            if (sortFlag == 0) {
                //不能上移，已经是第一个
                if (currentFlag == 1) {
                    flag = -2;
                } else {
                    //get the previous server id
                    int prev = jdbcTemplate.queryForInt(sql_preOrNex, cabinetId, currentFlag - 1);

                    //update the previous server sortFlag to increment by one
                    jdbcTemplate.update(sql_update, currentFlag, prev);

                    //update the specified server sortFlag to decrement by one
                    jdbcTemplate.update(sql_update, currentFlag - 1, serverId);

                    flag = 2;
                }
            }

            //下移
            else if (sortFlag == 1) {
                //不能下移,已经是最后一个
                if (currentFlag == maxFlag) {
                    flag = -3;
                } else {
                    //get the next server id
                    int next = jdbcTemplate.queryForInt(sql_preOrNex, cabinetId, currentFlag + 1);

                    //update the current server sortFlag to increment by one
                    jdbcTemplate.update(sql_update, currentFlag + 1, serverId);

                    //update the next server sortFlag to decrement by one
                    jdbcTemplate.update(sql_update, currentFlag, next);

                    flag = 2;
                }
            }

            //输入的sortFlag异常
            else {
                flag = -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
