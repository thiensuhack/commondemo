package com.orange.studio.bobo.xml;

import java.util.ArrayList;

import android.util.Log;

public class XMLGettersSetters {
	
	public String title;
	public String artist;
	public String country;
	public String comp;
	public String price;
	public String year;
	
private ArrayList<String> company = new ArrayList<String>();
    public ArrayList<String> getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company.add(company);
        Log.i("This is the company:", company);
    }
}