package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

@Controller // APIの入り口
public class BulkController {

	final static Logger logger = LoggerFactory.getLogger(BulkController.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private ThumbnailService thumbnailService;

	@RequestMapping(value = "/bulkRegist", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String login(Model model) {
		return "bulk";
	}

	@Transactional
	@RequestMapping(value = "/bulkRegist", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String bulk(Locale locale, Model model, @RequestParam("csvfile") MultipartFile file) {

		List<BookDetailsInfo> lines = new ArrayList<BookDetailsInfo>();
		List<String> Errorlines = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			String line;
			int i = 0;
			
			if (br.ready()) {
				model.addAttribute("nullFile","ファイルの中身がありません。");
			}
			while ((line = br.readLine()) != null) {

				final String[] split = line.split(",", -1);

				BookDetailsInfo bookInfo = new BookDetailsInfo();
				bookInfo.setTitle(split[0]);
				bookInfo.setAuthor(split[1]);
				bookInfo.setPublisher(split[2]);
				bookInfo.setPublishDate(split[3]);
				bookInfo.setIsbn(split[4]);
				

				boolean DetailCheck = (split[0].isEmpty() || split[1].isEmpty() || split[2].isEmpty()
						|| split[3].isEmpty());
				boolean PublishDateCheck = split[3].matches("^[0-9]{8}$");
				boolean IsbnCheck1 = split[4].matches("^[0-9]{10}$");
				boolean IsbnCheck2 = split[4].matches("^[0-9]{13}$");

				if (DetailCheck || !PublishDateCheck || !IsbnCheck1 && !IsbnCheck2) {

					i++;
					Errorlines.add(i + "行目でエラーが発生しました。");
				}
				lines.add(bookInfo);
			}
		} catch (IOException e) {
			throw new RuntimeException("ファイルが読み込めません。",e);

		}
		if (Errorlines.size() > 0) {
			model.addAttribute("countError", Errorlines);
			return "bulk";
		}	

		for (BookDetailsInfo bookInfo : lines) {
			 booksService.registBook(bookInfo);				
			}
		 	
	        model.addAttribute("bookList", booksService.getBookList());
	
	        return"home";
	}
}
