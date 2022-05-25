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

	/**
	 * 書籍を借りる
	 * 
	 * @param locale ロケール情報
	 * @param bookId 書籍ID
	 * @param model  モデル情報
	 * @return 遷移先画面名
	 */
	@Transactional
	@RequestMapping(value = "/rentBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	// RequestParamでname属性を取得
	public String rentBook(Model model, Locale locale, @RequestParam("bookId") int bookId) {
		
		//書籍の貸出
		
		int historyNull = rentService.hisNull(bookId);
		
		int rentDate = rentService.rentNull(bookId);
		
		//書籍の新規貸し出し
		if (historyNull == 0) {
			rentService.rentBook(bookId);
		} else {
			//既存書籍を貸出の更新をする場合
			if (rentDate == 0) {
				booksService.hisRent(bookId);
			} else {
				//貸出エラー表示
				model.addAttribute("countError", "貸し出し済みです。");
			}
		}
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}

}