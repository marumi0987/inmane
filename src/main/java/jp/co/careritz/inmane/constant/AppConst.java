package jp.co.careritz.inmane.constant;

/**
 * アプリケーション共通定数定義.
 */
public class AppConst {

  private AppConst() {

  }


  /** 空文字. */
  public static final String EMPTY = "";

  /** 処理完了メッセージID 正常. */
  public static final String APP_COMPLETE_MESSAGE_ID_SUCCESS = "appCompleteMessageSuccess";
  /** 処理完了メッセージID 警告. */
  public static final String APP_COMPLETE_MESSAGE_ID_WARNING = "appCompleteMessageWarning";
  /** 処理完了メッセージID 例外. */
  public static final String APP_COMPLETE_MESSAGE_ID_FAILURE = "appCompleteMessageFailure";

  /** 処理成功時メッセージ表示用. */
  public static final String OK = "ok.app.complete";
  /** 処理エラー時メッセージ表示. */
  public static final String FATAL = "error.app.fatal";
  /** 既存のユーザ登録メッセージ表示. */
  public static final String DEPLICATED = "error.app.user.deplicated";

  public static final String INVALID = "error.app.invalid.request";

  /** 404にリダイレクト. */
  public static final String REDIRECT_404 = "redirect:/error/404";

  /* ---------------------------------------------------- */
  /* ユーザ情報関連 */
  /* ---------------------------------------------------- */
  /** ロール名_一般ユーザ. */
  public static final String ROLE_NAME_USER = "USER";
  /** ロール名_管理者ユーザ. */
  public static final String ROLE_NAME_ADMIN = "ADMIN";
  /** ロール名_全て（検索用）. */
  public static final String ROLE_NAME_ALL = "ALL";



  /* ---------------------------------------------------- */
  /* フォーマット */
  /* ---------------------------------------------------- */

  /** 日付フォーマット(更新日時、登録日時を表示する際に使用する標準的なパターン). */
  public static final String DATE_PATTERN_STD = "yyyy/MM/dd HH:mm:ss";

}
