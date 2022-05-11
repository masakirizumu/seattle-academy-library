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

@Controller // APIの入り口
public class RentBookController {
	final static Logger logger = LoggerFactory.getLogger(EditBooksController.class);

	@Autowired
	private BooksService booksService;
	@Autowired
	private RentBookSeavice rentService;

	@Transactional
	@RequestMapping(value = "/rentBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	// RequestParamでname属性を取得
	public String rentBook(Model model, Locale locale, @RequestParam("bookId") int bookId) {

		int count1 = rentService.countBook();

		rentService.rentBook(bookId);
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		int count2 = rentService.countBook();

		if (count1 == count2) {
			model.addAttribute("countError", "貸し出し済みです。");

		}

		return "details";
	}

}