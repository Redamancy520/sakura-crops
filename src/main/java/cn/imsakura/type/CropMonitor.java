package cn.imsakura.type;

import net.minecraft.util.math.Position;

/**
 * Json Data Class
 */
public class CropMonitor {
    private String name = "null";
    private String x1 = "-1";
    private String y1 = "-1";
    private String z1 = "-1";
    private String x2 = "1";
    private String y2 = "1";
    private String z2 = "1";

    public CropMonitor(String name){
        this.name=name;
    }

    public CropMonitor(String name, MyPosition p1, MyPosition p2){
        this.name=name;
        this.x1=p1.x;
        this.y1=p1.y;
        this.z1=p1.z;
        this.x2=p2.x;
        this.y2=p2.y;
        this.z2=p2.z;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setX1(String x1) {
        this.x1 = x1;
    }
    public String getX1() {
        return x1;
    }

    public void setY1(String y1) {
        this.y1 = y1;
    }
    public String getY1() {
        return y1;
    }

    public void setZ1(String z1) {
        this.z1 = z1;
    }
    public String getZ1() {
        return z1;
    }

    public void setX2(String x2) {
        this.x2 = x2;
    }
    public String getX2() {
        return x2;
    }

    public void setY2(String y2) {
        this.y2 = y2;
    }
    public String getY2() {
        return y2;
    }

    public void setZ2(String z2) {
        this.z2 = z2;
    }
    public String getZ2() {
        return z2;
    }

}
