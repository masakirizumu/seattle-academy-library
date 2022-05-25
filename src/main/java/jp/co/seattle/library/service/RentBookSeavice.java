package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RentBookSeavice {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 借りる書籍を登録する
	 *
	 * @param bookId 書籍ID
	 */
	public void rentBook(int bookId) {
		String sql = "insert into rent (bookid,rent_date) select " + bookId
				+ ",now() where NOT EXISTS (select bookid from rent where bookid=" + bookId + ")";
		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍をカウントする
	 *
	 * @param bookId 書籍ID
	 * @return
	 */
	public int countBook() {

		String sql = "SELECT COUNT (id) FROM rent";
		int countBook = jdbcTemplate.queryForObject(sql, int.class);
		return countBook;
	}

	/**
	 * 書籍返却する
	 *
	 * @param bookId 書籍ID
	 * 
	 * @return
	 * 
	 */
	// public void returnBook(int bookId) {
	// String sql = "DELETE FROM rent WHERE bookid = " + bookId;
	// .update(sql);

	// }

	/**
	 * 書籍情報を取得
	 *
	 * @param bookId 書籍ID
	 */
	public int delete2Book(int bookId) {

		// JSPに渡すデータを設定する
		try {
			String sql = "SELECT bookid FROM rent where bookid =" + bookId;
			int returnId = jdbcTemplate.queryForObject(sql, int.class);
			return returnId;

		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * 新規書籍の貸出時
	 * 指定したbookIdがrentテーブルに存在するかの情報を取得
	 * 
	 * @param bookId
	 * @return historyNull
	 * @return 0
	 */
	public int hisNull(int bookId) {
		try {
			String sql = "select bookid from rent where bookid = " + bookId;
			int historyNull = jdbcTemplate.queryForObject(sql, int.class);
			return historyNull;
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 指定しているbookIdの貸出日をカウント数を取得
	 * rent_dateのカウント数を基準にして
	 * 貸出と返却をする
	 * 
	 * @param bookId
	 * @return rentDateNull
	 * @return 0
	 */
	public int rentNull(int bookId) {
		try {
			String sql = "SELECT COUNT (rent_date) FROM rent where bookid="+bookId;
			int rentDateNull = jdbcTemplate.queryForObject(sql, int.class);
			return rentDateNull;
		} catch (Exception e) {
			return 0;
		}
	}
}
