package com.dataeye.omp.module.cmdb.device;
import com.google.gson.annotations.Expose;

/**
 * 字典值
 * @auther wendy
 * @since 2015/12/27 13:48
 */
public class DicValue {
    @Expose
    private int id;

    @Expose
    private int itemId;

    @Expose
    private String label;

    @Expose
    private int value;

    public DicValue(){
    	
    }
    
    public DicValue(int value, String label) {
    	this.value = value;
    	this.label = label;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public DicValue(String label, int value) {
        this.label = label;
        this.value = value;
    }

}
