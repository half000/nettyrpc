package com.half.nettyrpc.client;

import com.google.gson.Gson;
import com.half.nettyRpc.protobuf.MsgRequest;
import com.half.nettyRpc.protobuf.MsgResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.resources.LocaleData;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @description
 * 提供方法调用
 *
 *
 * @date Create in 2018-05-26 10:22
 **/
public class RpcUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(RpcUtils.class);
    private static long id=0;
     /**
      *
      * 提供给外部的RPC调用
      * @author wangwei
      * @date 2018-05-26 12:29
      * @param
      *     params 参数 json格式
      * @return
      *     返回结果 json格式
      */
    public static String exec(String params){
        return null;
    }

     /**
      * @author wangwei  
      * @date 2018-05-26 14:20
      * @param
      * @return   
      */ 
    public static String exec(String method, Map<String,Object> header, Map<String,Object> params){
        Map<String,Object> param=new HashMap<>();
        param.put("Method",method);
        param.put("header",header );
        param.put("params",params );
        Gson gson=new Gson();
        return exec(gson.toJson(param));
    }
     /**
      * @author wangwei  
      * @date 2018-05-26 18:07  
      * @param   
      * @return
      */ 
    public static String exec(RpcClient rpcClient,String method, String header, String params){
        try {
            MsgRequest.msgRequest.Builder builder= MsgRequest.msgRequest.newBuilder();
            builder.setMessageId(id++);
            builder.setMethodName("test1");
            builder.setHead("head");
            builder.setParams("name");
            MsgResponse.msgResponse response=rpcClient.invoke(builder.build());
            return response.toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RpcClient getClient(String ip,int port) throws InterruptedException {
        return RpcClient.getConnect(ip,port);
    }

    public static void main(String[] args) {


        RpcClient rpcClient= null;
        try {
            rpcClient=RpcUtils.getClient("127.0.0.1", 10100);
            long t1=System.currentTimeMillis();
            for(int i=0;i<100;i++) {
                String res = RpcUtils.exec(rpcClient, "method", "head", "params");
                logger.info(res);
            }
            long t2=System.currentTimeMillis();
            logger.info("t2-t1:"+ (t2-t1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(rpcClient!=null) {
                rpcClient.closeConnect();
            }
        }

    }
}
