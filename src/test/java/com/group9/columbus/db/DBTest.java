package com.group9.columbus.db;

import org.springframework.data.annotation.Id;

public class DBTest {

	 class TestDocument {

	    @Id
	    public String id;

	    public String key1;
	    public String key2;

	   
	    public TestDocument(){
	    }
	    
	    public TestDocument(String id, String key1, String key2) {
			this.id = id;
			this.key1 = key1;
			this.key2 = key2;
		}

		@Override
	    public String toString() {
	        return String.format(
	                "Object successfully instantiated[id=%s, key1='%s', key2='%s']",
	                id, key1, key2);
	    }

	}
}
