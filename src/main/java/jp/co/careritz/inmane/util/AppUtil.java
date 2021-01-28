package jp.co.careritz.inmane.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

/**
 * アプリケーション共通ユーティリティ.
 */
public class AppUtil {

  public static final String SQLLOG = "SQL: %s ";



  private AppUtil() {

  }

  /**
   * 検証対象文字列に値が入力されているかどうかを検証する.
   *
   * @param input 検証対象文字列
   * @return
   *         <dl>
   *         <dt>true</dt>
   *         <dd>検証対象文字列がnullまたは空文字の場合</dd>
   *         <dt>false</dt>
   *         <dd>検証対象文字列が存在する場合</dd>
   *         </dl>
   */
  public static boolean isEmptyStr(CharSequence input) {
    return StringUtils.isEmpty(input);
  }

  /**
   * 検証対象文字列に値が入力されているかどうかを検証する.
   *
   * @param input 検証対象文字列
   * @return
   *         <dl>
   *         <dt>true</dt>
   *         <dd>検証対象文字列が存在する場合</dd>
   *         <dt>false</dt>
   *         <dd>検証対象文字列がnullまたは空文字の場合</dd>
   *         </dl>
   */
  public static boolean isNotEmptyStr(CharSequence input) {
    return !isEmptyStr(input);
  }

  /**
   * 日付型オブジェクトを引数で指定した日付フォーマットの日付文字列に変換します.
   *
   * @param input 変換対象タイムスタンプ型オブジェクト
   * @param format 日付フォーマット
   * @return 日付文字列<br>
   *         ※引数未入力の場合はnullを返却する
   */
  public static <T extends Date> String convDateToStr(T input, String format) {

    // 未入力検証処理
    if (input == null || isEmptyStr(format)) {
      return null;
    }

    return convTimestampToStr(new Timestamp(input.getTime()), format);
  }

  /**
   * タイムスタンプ型オブジェクトを引数で指定した日付フォーマットの日付文字列に変換します.
   *
   * @param input 変換対象タイムスタンプ型オブジェクト
   * @param format 日付フォーマット
   * @return 日付文字列<br>
   *         ※引数未入力の場合はnullを返却する
   */
  public static String convTimestampToStr(Timestamp input, String format) {

    // 未入力検証処理
    if (input == null || isEmptyStr(format)) {
      return null;
    }

    // 日付日付フォーマットの取得
    SimpleDateFormat df = new SimpleDateFormat(format, Locale.JAPAN);

    Timestamp date = new Timestamp(input.getTime());

    // 指定日付フォーマットに変換
    return df.format(date);
  }


  /**
   * sqlログの表示.
   *
   * @param sqlBuilder SQL文
   * @param log ロガー
   */
  public static void throwLogInfo(StringBuilder sqlBuilder, Logger log) {
    if (log.isInfoEnabled()) {
      log.info(String.format(SQLLOG, sqlBuilder));
    }
  }

}
