package com.sip.ams.controllers;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sip.ams.entity.Article;
import com.sip.ams.entity.Provider;
import com.sip.ams.repository.ArticleRepository;
import com.sip.ams.repository.ProviderRepository;

@Controller
@RequestMapping("/article/")
public class ArticleController {
	
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploadsArticle";
	
	private final ArticleRepository articleRepository;
	private final ProviderRepository providerRepository;
    @Autowired
    public ArticleController(ArticleRepository articleRepository, ProviderRepository providerRepository) {
        this.articleRepository = articleRepository;
        this.providerRepository = providerRepository;
    }
    
    @GetMapping("list")
    public String listArticles(Model model) {
    	//model.addAttribute("articles", null);
    	List<Article> la= (List<Article>) articleRepository.findAll();
    	if(la.size()==0) la =null;
        model.addAttribute("articles", la);
        return "article/listArticles";
    }
    
    @GetMapping("add")
    public String showAddArticleForm(Article article, Model model) {
    	
    	model.addAttribute("providers", providerRepository.findAll());
    	//model.addAttribute("article", new Article());
        return "article/addArticle";
    }
    
    @PostMapping("add")
    //@ResponseBody
    public String addArticle(@Valid Article article, BindingResult result, @RequestParam(name = "providerId", required = false) Long p,
    		@RequestParam("files") MultipartFile[] files) {
    	
    	Provider provider = providerRepository.findById(p)
                .orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + p));
    	article.setProvider(provider);
    	

        //pour creer un nom inique de l'image
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        /// part upload
        StringBuilder fileName = new StringBuilder();
        MultipartFile file = files[0];
    	Path fileNameAndPath = Paths.get(uploadDirectory, timestamp.getTime()+file.getOriginalFilename());

        fileName.append(timestamp.getTime()+""+file.getOriginalFilename());


		  try {
			Files.write(fileNameAndPath, file.getBytes());
              //System.out.println(fileNameAndPath+"- deux -"+ file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		 article.setPicture(fileName.toString());
    	 articleRepository.save(article);
    	 return "redirect:list";
    	
    	//return article.getLabel() + " " +article.getPrice() + " " + p.toString();
    }

    /**
     * deleteArticle: allow to delete the article by id
     * @param id the id of the article
     * @param picture picture of the article(to delete the picture of the article from the local source)
     * @return the list of the articles available on the database
     */
    @GetMapping("delete")
    public String deleteArticle(@PathParam("id") long id, @PathParam("picture") String picture) {
        Article artice = articleRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("Invalid article Id:" + id));

        //delete the local image which is deleted from the database
        Path localFileName = Paths.get(uploadDirectory, picture);
        File fileToRemove =localFileName.toFile();
        fileToRemove.delete();

        //delete from database
        articleRepository.delete(artice);

        //model.addAttribute("articles", articleRepository.findAll());

        //return "article/listArticles";
        return "redirect:list";
    }
    
    @GetMapping("edit/{id}")
    public String showArticleFormToUpdate(@PathVariable("id") long id, Model model) {
    	Article article = articleRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid article Id:" + id));
    	
        model.addAttribute("article", article);
        model.addAttribute("providers", providerRepository.findAll());
        model.addAttribute("idProvider", article.getProvider().getId());
        
        return "article/updateArticle";
    }
    @PostMapping("edit/{id}")
    public String updateArticle(@PathVariable("id") long id, @Valid Article article, BindingResult result,
        Model model, @RequestParam(name = "providerId", required = false) Long p) {
        if (result.hasErrors()) {
        	article.setId(id);
            return "article/updateArticle";
        }
        
        Provider provider = providerRepository.findById(p)
                .orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + p));
    	article.setProvider(provider);
    	
        articleRepository.save(article);
        model.addAttribute("articles", articleRepository.findAll());
        return "article/listArticles";
    }
    
    @GetMapping("show/{id}")
    public String showArticleDetails(@PathVariable("id") long id, Model model) {
    	Article article = articleRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid article Id:" + id));
    	
        model.addAttribute("article", article);
        
        return "article/showArticle";
    }

}
