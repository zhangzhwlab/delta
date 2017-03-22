/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.ac.big.circos.util;
import com.sun.jersey.api.client.Client;

import com.sun.jersey.api.client.ClientResponse;

/**
 * 
 * @author sweeter
 */
public class BIGWebServiceClientFactory {
    private static Client client =null;

    public static  Client getClient(){
        if(client==null){
            client = Client.create();
        }
        return client;
    }

}
