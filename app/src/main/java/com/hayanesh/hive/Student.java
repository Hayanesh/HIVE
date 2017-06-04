package com.hayanesh.hive;

/**
 * Created by Hayanesh on 26-May-17.
 */

public class Student {
    public int no;
    public String name;
    public String id;
    public boolean present;
    public boolean isSelected;
    public Student()
    {

    }
    public Student(int no,String name,String id,boolean present,boolean isSelected)
    {
        this.no = no;
        this.name = name;
        this.id = id;
        this.present = present;
        this.isSelected = isSelected;
    }

    public void setNo(int no)
    {
        this.no = no;
    }
    public void setSelected(boolean selected)
    {
        this.isSelected = selected;
    }
    public boolean getSelected()
    {
        return this.isSelected;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public void setPresent(boolean present)
    {
        this.present = present;
    }
    public int getNo()
    {
        return  this.no;
    }
    public String getName()
    {
        return this.name;
    }
    public String getId()
    {
        return this.id;
    }
    public boolean getPresent()
    {
        return this.present;
    }

}
