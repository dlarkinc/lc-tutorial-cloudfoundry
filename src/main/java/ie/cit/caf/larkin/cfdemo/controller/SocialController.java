package ie.cit.caf.larkin.cfdemo.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/social")
public class SocialController {

	@Inject
	private Twitter twitter;

	@Inject
    private ConnectionRepository connectionRepository;
	
	@Inject
	private Facebook facebook;
	
    @RequestMapping(value="/twitter/followers", method=RequestMethod.GET)
    public String helloTwitter(Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }

        model.addAttribute(twitter.userOperations().getUserProfile());
        CursoredList<TwitterProfile> followers = twitter.friendOperations().getFriends();
        model.addAttribute("followers", followers);
        return "social/twitterFollowers";
    }

    @RequestMapping(value="/facebook/posts", method=RequestMethod.GET)
    public String helloFacebook(Model model) {
        if (!facebook.isAuthorized()) {
            return "redirect:/connect/facebook";
        }

        FacebookProfile profile = facebook.userOperations().getUserProfile();
        model.addAttribute(profile);
        
        PagedList<Post> feed = facebook.feedOperations().getFeed();
        model.addAttribute(feed);
        
        List<String> friends = facebook.friendOperations().getFriendIds();
        model.addAttribute(friends);

        facebook.feedOperations().updateStatus("I'm trying out Spring Social!");
        
        return "social/facebookPosts";
    }
	
}
