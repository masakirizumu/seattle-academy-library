package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentBookSeavice;

@Controller //APIの入り口
public class ReturnBookController {
	
	 final static Logger logger = LoggerFactory.getLogger(ReturnBookController.class);
	    
	 @Autowired
	    private BooksService booksService;
	 
	 @Autowired
		private RentBookSeavice rentService;

	 /**
	     * 対象書籍を削除
	     * 
	     * @param locale ロケール情報
	     * @param bookId 書籍ID
	     * @param model モデル情報
	     * @return 遷移先画面名
	     */
	 @Transactional
	    @RequestMapping(value = "/returnBook", method = RequestMethod.POST,produces = "text/plain;charset=utf-8")
	    public String returnBook(Model model, Locale locale, @RequestParam("bookId") int bookId) {
	        
	//貸し出しエラー表示
		 int BforeCount = rentService.countBook();
	    		  
	        rentService.returnBook(bookId);
	        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
	        
	      int AfterCount = rentService.countBook();
			
			if(BforeCount==AfterCount){
				model.addAttribute("returnError","貸し出しされていません。"); 
			}

	        return "details";
	 }
}
