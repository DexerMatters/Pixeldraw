package com.pixeldraw.dbrt.pixeldraw;

public class Alternate {
    private static boolean isRun=true;
    public static void start(Alternate alternate){
        if(isRun){
            alternate.runFirst();
            isRun=false;
            return;
        }
        else {
            alternate.runSec();
            isRun=true;
        }
    }
    public void runFirst(){}
    public void runSec(){}
}
