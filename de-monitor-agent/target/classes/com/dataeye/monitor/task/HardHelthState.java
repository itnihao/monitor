package com.dataeye.monitor.task;

import com.dataeye.monitor.common.constants.Commands;
import com.dataeye.monitor.common.constants.Constant;
import com.dataeye.monitor.common.constants.FID;
import com.dataeye.monitor.utils.ProcessUtil;

import java.math.BigDecimal;

/**
 * 硬件健康状态
 * Created by wendy on 2016/7/6.
 */
public class HardHelthState implements Runnable {

    private HardStat hardStat = new HardStat();
    private String result;
    @Override
    public void run() {

    }

    public void generateData(){
        Feature feature = new Feature();

        Feature hardProcFeature = new Feature(FID.hard_proc.getValue(),
                FID.hard_proc.getName(),
                new BigDecimal(hardStat.getHardProc()));
        feature.addFeature(hardProcFeature);

        Feature hardMemFeature = new Feature(FID.hard_mem.getValue(),
                FID.hard_mem.getName(),
                new BigDecimal(hardStat.getHardMem()));
        feature.addFeature(hardMemFeature);

        Feature hardTempFeature = new Feature(FID.hard_temp.getValue(),
                FID.hard_temp.getName(),
                new BigDecimal(hardStat.getHardTemp()));
        feature.addFeature(hardTempFeature);

        Feature hardDiskFeature = new Feature(FID.hard_disk.getValue(),
                FID.hard_disk.getName(),
                new BigDecimal(hardStat.getHardDisk())
        );
        feature.addFeature(hardDiskFeature);

        Feature hardPwrFeature = new Feature(FID.hard_pwr.getValue(),
                FID.hard_pwr.getName(),
                new BigDecimal(hardStat.getHardPwr())
        );
        feature.addFeature(hardPwrFeature);

        Feature hardBattFeature = new Feature(FID.hard_batt.getValue(),
                FID.hard_batt.getName(),
                new BigDecimal(hardStat.getHardBatt())
        );
        feature.addFeature(hardBattFeature);

        Feature hardNicsFeature = new Feature(FID.hard_nics.getValue(),
                FID.hard_nics.getName(),
                new BigDecimal(hardStat.getHardNics())
        );
        feature.addFeature(hardNicsFeature);

        Feature hardFansFeature = new Feature(FID.hard_fans.getValue(),
                FID.hard_fans.getName(),
                new BigDecimal(hardStat.getHardFans()));
        feature.addFeature(hardFansFeature);

        result = Constant.GSON_ONLY_EXPOSE.toJson(feature);
    }

    public void collectData() {
        hardStat.setHardProc(ProcessUtil.execute(Commands.hardProc).trim());
        hardStat.setHardMem(ProcessUtil.execute(Commands.hardMem).trim());
        hardStat.setHardTemp(ProcessUtil.execute(Commands.hardTemp).trim());
        hardStat.setHardDisk(ProcessUtil.execute(Commands.hardDisk).trim());

        hardStat.setHardPwr(ProcessUtil.execute(Commands.hardPwr).trim());
        hardStat.setHardBatt(ProcessUtil.execute(Commands.hardBatt).trim());
        hardStat.setHardNics(ProcessUtil.execute(Commands.hardNics).trim());
        hardStat.setHardFans(ProcessUtil.execute(Commands.hardFans).trim());
    }

    class HardStat {
        private String hardProc;
        private String hardMem;
        private String hardTemp;

        private String hardDisk;
        private String hardPwr;
        private String hardBatt;
        private String hardNics;
        private String hardFans;

        public String getHardProc() {
            return hardProc;
        }

        public void setHardProc(String hardProc) {
            this.hardProc = hardProc;
        }

        public String getHardMem() {
            return hardMem;
        }

        public void setHardMem(String hardMem) {
            this.hardMem = hardMem;
        }

        public String getHardTemp() {
            return hardTemp;
        }

        public void setHardTemp(String hardTemp) {
            this.hardTemp = hardTemp;
        }

        public String getHardDisk() {
            return hardDisk;
        }

        public void setHardDisk(String hardDisk) {
            this.hardDisk = hardDisk;
        }

        public String getHardPwr() {
            return hardPwr;
        }

        public void setHardPwr(String hardPwr) {
            this.hardPwr = hardPwr;
        }

        public String getHardBatt() {
            return hardBatt;
        }

        public void setHardBatt(String hardBatt) {
            this.hardBatt = hardBatt;
        }

        public String getHardNics() {
            return hardNics;
        }

        public void setHardNics(String hardNics) {
            this.hardNics = hardNics;
        }

        public String getHardFans() {
            return hardFans;
        }

        public void setHardFans(String hardFans) {
            this.hardFans = hardFans;
        }
    }

}
