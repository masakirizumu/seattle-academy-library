package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.dto.HistoryInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;
import jp.co.seattle.library.rowMapper.HistoryInfoRowMapper;

/**
 * 書籍サービス
 * 
 * booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍リストを取得する
	 *
	 * @return 書籍リスト
	 */
	public List<BookInfo> getBookList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"SELECT id,title,author,publisher,publish_date,isbn,explanation,thumbnail_url FROM books order by title ASC",
				new BookInfoRowMapper());
		return getedBookList;

	}

	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */
	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT * ,CASE WHEN rent_date isnull THEN '貸し出し可' ELSE '貸し出し中' END AS status FROM books LEFT OUTER JOIN rent ON books.id = rent.bookid where books.id ="+ bookId;
		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

		return bookDetailsInfo;
	}
	
	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 * 貸出履歴で使用する
	 *
	 * @param bookId 書籍ID
	 * @return historyInfo
	 */
	public HistoryInfo getHisInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT * FROM books where id="+ bookId;
		HistoryInfo historyInfo = jdbcTemplate.queryForObject(sql, new HistoryInfoRowMapper());

		return historyInfo;
	}
	
	/**
	 * 書籍を登録する
	 *
	 * @param bookInfo 書籍情報
	 */
	public void registBook(BookDetailsInfo bookInfo) {

		String sql = "INSERT INTO books (title, author,publisher,thumbnail_name,publish_date,isbn,explanation,thumbnail_url,reg_date,upd_date) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getThumbnailName() + "','" + bookInfo.getPublishDate() + "','" + bookInfo.getIsbn() + "','"
				+ bookInfo.getExplanation() + "','" + bookInfo.getThumbnailUrl() + "'," + "now()," + "now())";
		jdbcTemplate.update(sql);

	}

	/**
	 * 書籍を削除する
	 *
	 * @param bookId 書籍ID
	 */
	public void deleteBook(int bookId) {

		String sql = "delete from books where id =" + bookId;

		jdbcTemplate.update(sql);
	}
	/**
	 * 書籍を削除する
	 *
	 * @param bookId 書籍ID
	 */
	public void deleteRent(int bookId) {

		String sql = "delete from rent where bookid =" + bookId;

		jdbcTemplate.update(sql);
	}
	/**
	 * 最新の書籍IDを取得する
	 *
	 * @return maxId
	 */
	public int maxId() {
		String sql = "SELECT Max(id) FROM books";
		int maxId = jdbcTemplate.queryForObject(sql, int.class);
		return maxId;
	}

	/**
	 * 書籍を編集する
	 * @param bookInfo
	 */
	public void editBook(BookDetailsInfo bookInfo) {


		String sql = "UPDATE books SET title ='"+ bookInfo.getTitle()
		+ "',author ='"+ bookInfo.getAuthor()
		+"',publisher ='"+ bookInfo.getPublisher()
		+"',thumbnail_name ='"+ bookInfo.getThumbnailName()
		+"',publish_date ='"+ bookInfo.getPublishDate()																		 
		+"',isbn ='"+ bookInfo.getIsbn() 
		+"',explanation ='"+ bookInfo.getExplanation() 
		+"',thumbnail_url ='"+ bookInfo.getThumbnailUrl() 
		+"',upd_date = now() WHERE Id ="+ bookInfo.getBookId();
		jdbcTemplate.update(sql);
	}

	
	/**
	 * 部分一致での検索
	 * @param title
	 * @return getedBookList
	 */
	public List<BookInfo> seachBook(String title) {
		
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"SELECT id,title,author,publisher,publish_date,isbn,explanation,thumbnail_url FROM books WHERE title LIKE'%"+title+"%'order by title ASC",
				new BookInfoRowMapper());
		return getedBookList;
	}
	/**
	 * 完全一致での検索
	 * @param title
	 * @return getedBookList
	 */
	public List<BookInfo> allSeachBook(String title) {
		
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"SELECT * FROM books WHERE title ='"+title+"'order by title ASC",
				new BookInfoRowMapper());
		return getedBookList;
	}
	
	
	/**
	 * 履歴用の書籍リストを取得する
	 *
	 * @return getedHisList
	 */
	public List<HistoryInfo> getHistoryList() {
		// TODO 取得したい情報を取得するようにSQLを修正
		
		List<HistoryInfo> getedHisList = jdbcTemplate.query(
				"select books.id,books.title,rent.rent_date,rent.return_date FROM books LEFT OUTER JOIN rent ON books.id = rent.bookid",
				new HistoryInfoRowMapper());
		return getedHisList;

	}
	/**
	 *貸出時
	 *貸出日に現在日時を、返却日を空欄をそれぞれ更新する
	 *
	 * @param bookId
	 */
	public void hisRent(int bookId) {

		String sql ="UPDATE rent set rent_date=now(),return_date=null WHERE bookid="+bookId;
		jdbcTemplate.update(sql);
	}
	
	/**
	 * 返却時
	 * 返却日に現在日時を、貸出日に空欄をそれぞれ更新する
	 * 
	 * @param bookId
	 */
	public void hisReturn(int bookId) {

		String sql ="UPDATE rent set return_date=now(),rent_date=null WHERE bookid="+bookId;
		jdbcTemplate.update(sql);
	}

}



