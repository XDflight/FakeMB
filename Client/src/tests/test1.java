package tests;

import server.structs.AccountData;

public class test1 {
    public static void main(String[] args) {
//        AccountData data= (AccountData) ReflectHelper.classInstance("server.structs.AccountData");
//        System.out.println(data);
        AccountData dataEntry=new AccountData();
        dataEntry.userName="Jameres86";
        AccountData dataClone= (AccountData) dataEntry.makeClone();
        dataEntry.userName="Jameres357";
        System.out.println(dataEntry);
        System.out.println(dataClone);
    }
}
