package com.kodbook.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kodbook.entities.Post;
import com.kodbook.services.PostService;

@Controller
public class PostController 
{
	@Autowired
	PostService pserv;
	
	@PostMapping("/createpost")
	public String creaePost(@RequestParam("caption") String caption,@RequestParam("photo") MultipartFile photo,Model model)
	{
		Post post=new Post();
		post.setCaption(caption);
		try {
			post.setPhoto(photo.getBytes());
		}
		catch(IOException e)
		{
			e.printStackTrace(); 
		}
		pserv.createPost(post);
		List<Post> allposts=pserv.fetchAllPosts();
		model.addAttribute("allposts", allposts);
		return "home";
		
	}
	@GetMapping("/opennewsfeed")
	public String showPosts(Model model)
	{
		List<Post> allposts=pserv.fetchAllPosts();
		model.addAttribute("allposts", allposts);
		return "home";
	}
	
	@PostMapping("/likepost")
	public String likePost(@RequestParam Long id, Model model) {
		Post post=pserv.getPost(id);
		post.setLikes(post.getLikes() + 1);
		pserv.updatePost(post);
		
		
		List<Post> allposts = pserv.fetchAllPosts();
		model.addAttribute("allposts", allposts);
		return "home";
	}
	
	@PostMapping("/addcomment")
	public String addComment(@RequestParam Long id,@RequestParam String comment, Model model) {
		Post post= pserv.getPost(id);
		List<String> comments = post.getComments();
		//if the fetched comments is null then it will create an object to resolve the null pointer exception
		if(comments == null) {
			comments = new ArrayList<String>();
		}
		comments.add(comment);
		post.setComments(comments);
		pserv.updatePost(post);
		
		List<Post> allposts = pserv.fetchAllPosts();
		model.addAttribute("allposts", allposts);
		return "home";
	}
}
