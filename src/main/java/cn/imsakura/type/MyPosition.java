package cn.imsakura.type;

public class MyPosition {

    public MyPosition(int x,int y,int z){
        this.x=String.valueOf(x);
        this.y=String.valueOf(y);
        this.z=String.valueOf(z);
    }
    public MyPosition(String x,String y,String z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public String x,y,z;

    public MyPosition() {

    }
}
