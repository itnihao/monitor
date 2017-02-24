import com.dataeye.omp.report.bean.Feature;
import com.dataeye.omp.report.utils.DateUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wendy
 * @since 2016/1/9 14:12
 */
public class TestServerReport {
    static Gson gson = new Gson();
    public static void main(String[] args) throws IOException {

        Timer timer = new Timer();

        List<Feature> params = new ArrayList<>();
        Feature param = new Feature();
        param.setFid(123);
        param.setObject("cpu0");
        param.setClient("10.1.2.37");
        // param.setValue(100);
        param.setTime(DateUtils.currentTime());

        params.add(param);
        params.add(param);
        final String content= gson.toJson(params);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    ClientUtil.sendReport("http://localhost:8388/report/post",content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },1000,2000);
    }

}
