package com.tim9.bolnica.util;

import java.util.ArrayList;

public class BlackListIpAddresses {
	
	public static ArrayList<String> blackList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
            add("128.128.199.200");
            add("13.48.123.212");
            add("14.54.11.127");
            add("22.54.160.93");
            add("43.220.201.254");
        }
    };


}
