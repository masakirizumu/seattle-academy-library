package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.service.BooksService;

@Controller
public class HistoryController {
	final static Logger logger = LoggerFactory.getLogger(HistoryController.class);
	
	@Autowired
	private BooksService booksService;
	
	/**
	 * 書籍登録
	 * @param model モデル
	 * @return　一括登録画面
	 */
	@RequestMapping(value = "/historyBook", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String transitionHis(Locale locale,			
			Model model) {
		model.addAttribute("hisList", booksService.getHistoryList());
		return "history";
	}
	
}

	

