/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet.james137137.lolnettwitchaddonbc;

import java.util.ArrayList;
import java.util.List;
import static nz.co.lolnet.james137137.lolnettwitchaddonbc.Config.accessToken;
import static nz.co.lolnet.james137137.lolnettwitchaddonbc.Config.accessTokenSecret;
import static nz.co.lolnet.james137137.lolnettwitchaddonbc.Config.consumerKey;
import static nz.co.lolnet.james137137.lolnettwitchaddonbc.Config.consumerSecret;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 *
 * @author James
 */
public class TwitterAPI {

    private static Twitter twitter;
    

    public TwitterAPI() {
        //Instantiate a re-usable and thread-safe factory
        TwitterFactory twitterFactory = new TwitterFactory();

        //Instantiate a new Twitter instance
        twitter = twitterFactory.getInstance();

        //setup OAuth Consumer Credentials
        twitter.setOAuthConsumer(consumerKey, consumerSecret);

        //setup OAuth Access Token
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
    }

    public List<Status> getLatestStatus() {
        List<Status> twitchTweets = new ArrayList<>();
        try {
            Query query = new Query("lolnetNZ twitch.tv");
            QueryResult result;
            result = twitter.search(query);

            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                if (tweet.getUser().getId() == 495479479 && tweet.getSource().contains("http://www.twitch.tv")) {
                    twitchTweets.add(tweet);
                }
            }
        } catch (TwitterException te) {
            System.out.println("Failed to search tweets: " + te.getMessage());
        }
        return twitchTweets;
    }

}
