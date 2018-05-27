package com.swj.ics.RPCSimple;

/**
 * Created by swj on 2017/1/3.
 */
public class EchoServiceImpl implements IEchoService {
    @Override
    public String echo(String inputPara) {
       return inputPara!=null? inputPara+"--> echo from server complete! ":"echo implement";
    }
}


