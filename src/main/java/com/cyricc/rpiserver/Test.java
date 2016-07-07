package com.cyricc.rpiserver;

import java.nio.charset.Charset;

/**
 * Created by luej on 7/3/16.
 */
public class Test {
    public static void main (String[] args) {
        System.out.println(Charset.availableCharsets().keySet().toString());
    }
}
