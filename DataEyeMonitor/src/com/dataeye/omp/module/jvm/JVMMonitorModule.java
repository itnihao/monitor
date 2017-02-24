package com.dataeye.omp.module.jvm;

import com.dataeye.common.CachedObjects;
import com.dataeye.common.PrivilegeControl;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.utils.HttpClientUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by wendy on 2016/6/3.
 */
@Controller
public class JVMMonitorModule {

    @Autowired
    private JvmMonitorService jvmMonitorService;

    private static final int port = 38888;


    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/jvm/getJvmServerList.do")
    public Object getJvmServerList(HttpServletRequest req,
                                   HttpServletResponse rsp) throws Exception {
        DEContext context = (DEContext) req.getAttribute("CTX");
        DEParameter param = context.getDeParameter();
        return jvmMonitorService.getJvmMonitoService(
                param.getPageID(), param.getPageSize(), param.getOrder(),
                param.getOrderBy(), param.getSearchKey());

    }

    /**
     * 获取每台机器的进程信息
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/jvm/getJpsInfoByServer.do")
    public Object getJpsInfoByServer(HttpServletRequest req,
                                   HttpServletResponse rsp) throws Exception {
        DEContext context = (DEContext) req.getAttribute("CTX");
        DEParameter param = context.getDeParameter();
        String ip = param.getIp();
        String url = "http://" + ip + ":" + port + "/jvm/cmds";
        String result = HttpClientUtil.get(url);

        List<JpsInfo> jpsInfoList = CachedObjects.GSON.fromJson(result,
                new TypeToken<List<JpsInfo>>() {}.getType());
        return jpsInfoList;
    }

    /**
     * 获取命令结果
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/jvm/getGreysCmdInfo.do")
    public Object getGreysCmdInfo(HttpServletRequest req,
                                  HttpServletResponse rsp) throws Exception {

        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        String ip = param.getIp();
        int pid = param.getPid();
        String cmd = param.getCmd();

        String url = "http://" + ip + ":" + port + "/greys/cmds";

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pid", String.valueOf(pid));

        hashMap.put("cmd", cmd);

        String result = HttpClientUtil.post(url, hashMap);
        return result;
    }


    public static void main(String[] args) throws UnsupportedEncodingException, ServerException {
        String ip = "10.1.2.197";
        String port = "38888";
        String url = "http://" + ip + ":" + port + "/greys/cmds";

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pid", String.valueOf(29623));

        hashMap.put("cmd", "sc *");

        String result = HttpClientUtil.post(url, hashMap);
        System.out.println(result);
    }


}
