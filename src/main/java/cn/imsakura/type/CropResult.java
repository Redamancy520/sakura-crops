package cn.imsakura.type;

public class CropResult {
    public CropMonitor cropMonitor = null;
    public boolean flag = false;

    public CropResult(){

    }
    public CropResult(CropMonitor cm,boolean f){
        this.flag=f;
        this.cropMonitor=cm;
    }


}
