/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet.james137137.lolnettwitchaddonbc;

import net.md_5.bungee.config.Configuration;

/**
 *
 * @author James
 */
public class Config {

    //Your Twitter App's Consumer Key

    public static String consumerKey;

    //Your Twitter App's Consumer Secret
    public static String consumerSecret;

    //Your Twitter Access Token
    public static String accessToken;

    //Your Twitter Access Token Secret
    public static String accessTokenSecret;

    public Config(Configuration config) {
        consumerKey = config.getString("consumerKey");
        consumerSecret = config.getString("consumerSecret");
        accessToken = config.getString("accessToken");
        accessTokenSecret = config.getString("accessTokenSecret");
    }

}
