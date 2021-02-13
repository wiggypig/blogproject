package com.tts.techtalentblog.controller;

import com.tts.techtalentblog.model.BlogPost;
import com.tts.techtalentblog.repo.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class BlogPostController {
    //@Autowired allows us to implement dependency injection
    //dependency injection allows us to give certain objects the dependencies that they need
    @Autowired
    private BlogPostRepository blogPostRepository;

    private static Iterable<BlogPost> posts = new ArrayList<>();
    private BlogPost blogPost;

    //GetMapping annotation specifies the route to this URI, in this case
    @GetMapping(value="/")
    public String index(BlogPost blogPost, Model model){
        //because we are utilizing thymeleaf
        //our output will be generated in a template
        //returning a reference to that template
        //will allow us to show the data that we want
        //model.addAttribute("posts", posts);
//        posts.removeAll(posts);
//        for (BlogPost post : blogPostRepository.findAll()){
//            posts.add(post);
//        }
        posts = blogPostRepository.findAll();
        model.addAttribute("posts", posts);
        return "blogpost/index";
    }
    //method to view new blog posts we have created
    //will allow us to show blog posts
    @GetMapping(value = "/blogpost/new")
    public String newBlog (BlogPost blogPost) {
        return "blogpost/new";
    }

    //this is where we are mapping our post requests in our project
    @PostMapping(value="/blogpost/new")
    public String addNewBlogPost(BlogPost blogPost, Model model){
        //blogPostRepository.save(blogPost);
        blogPostRepository.save(new BlogPost(blogPost.getTitle(),blogPost.getAuthor(),blogPost.getBlogEntry()));
        //blogPost from our parameter is the object that we're getting from
        //the thymeleaf form, we can simply save it in our repository
        model.addAttribute( "title", blogPost.getTitle());
        model.addAttribute( "author", blogPost.getAuthor());
        model.addAttribute( "blogEntry", blogPost.getBlogEntry());
        return "blogpost/result";
    }

    @RequestMapping(value = "/blogpost/{id}", method = RequestMethod.DELETE)
    public String deletePostWithId(@PathVariable Long id, BlogPost blogPost, Model model) {
        //crud repository method
        blogPostRepository.deleteById(id);
        Iterable<BlogPost> posts = blogPostRepository.findAll();
        model.addAttribute("posts",posts);
        return "blogpost/index";
    }

    @RequestMapping(value = "/blogpost/{id}", method = RequestMethod.GET)
    public String editPostWithId(@PathVariable Long id, BlogPost blogPost, Model model){
        Optional<BlogPost> post = blogPostRepository.findById(id);
        if(post.isPresent()){
            BlogPost actualPost = post.get();
            model.addAttribute("blogPost", actualPost);
        }
        return "blogpost/edit";
    }

    @RequestMapping(value = "/blogpost/update/{id}")
    public String updateExistingPost(@PathVariable Long id, BlogPost blogPost, Model model) {
        Optional<BlogPost> post = blogPostRepository.findById(id);
        if (post.isPresent()) {
            BlogPost actualPost = post.get();
            actualPost.setTitle(blogPost.getTitle());
            actualPost.setAuthor(blogPost.getAuthor());
            actualPost.setBlogEntry(blogPost.getBlogEntry());
            blogPostRepository.save(actualPost);
            model.addAttribute("blogPost", actualPost);
        }
        return "blogpost/result";
    }

    @RequestMapping(value = "blogposts/delete/{id}")
    public String deletePostById(@PathVariable Long id, BlogPost blogPost) {
        blogPostRepository.deleteById(id);
        return "blogpost/delete";
    }
}