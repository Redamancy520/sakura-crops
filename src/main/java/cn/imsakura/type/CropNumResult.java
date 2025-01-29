package cn.imsakura.type;

/** 农作物数量类型(RETURN)
 *  allCrops: 农作物总数量
 *  agedCrops: 成熟农作物数量
 */

public class CropNumResult {
    public CropNumResult(){}

    public int allCrops = 0;
    public int agedCrops = 0;

    public CropNumResult(int allCrops,int agedCrops){
        this.allCrops=allCrops;
        this.agedCrops=agedCrops;
    }

}
