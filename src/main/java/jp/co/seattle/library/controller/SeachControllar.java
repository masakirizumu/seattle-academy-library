package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

@Controller //APIの入り口
public class SeachControllar {
	 final static Logger logger = LoggerFactory.getLogger(SeachControllar.class);
	 
	 @Autowired
	    private BooksService booksService;

	 /**
	  * 検索結果をホーム画面へ
	  * @param locale
	  * @param title
	  * @param model
	  * @return ホーム画面
	  */
	 @RequestMapping(value = "/seach", method = RequestMethod.POST) //value＝actionで指定したパラメータ
	    //RequestParamでname属性を取得
	    public String tranjitionSeach(Locale locale,@RequestParam("title") String title,Model model) {
		
		model.addAttribute("bookList", booksService.seachBook(title));
	        return "home";
	 }
	 
	
}
