package jp.co.careritz.inmane.service.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;

/**
 * ログイン失敗処理ハンドラ.
 */
@Component
public class SecurityLoginFailureHandler implements AuthenticationFailureHandler {

  // ----------------------------------------------------------------------
  // インスタンス変数
  // ----------------------------------------------------------------------

  // ----------------------------------------------------------------------
  // インスタンスメソッド
  // ----------------------------------------------------------------------
  /*
   * 再読み込み時に認証エラーメッセージが表示され続けないよう、エラーメッセージをデフォルトのセッションではなくフラッシュセッションに設定します。
   */
  @Override
  public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res,
      AuthenticationException ex) throws IOException, ServletException {

    /* ---------------------------------------------------- */
    /* ローカル変数の宣言と初期化 */
    /* ---------------------------------------------------- */
    FlashMap flashMap = new FlashMap();
    FlashMapManager flashMapManager = new SessionFlashMapManager();

    /* ---------------------------------------------------- */
    /* 認証処理 */
    /* ---------------------------------------------------- */
    // 例外取得時
    if (ex != null) {
      // 認証エラーはフラッシュセッションにエラーメッセージを設定
      if (ex instanceof BadCredentialsException) {
        flashMap.put("loginFailureMessage", ex.getMessage());
        flashMapManager.saveOutputFlashMap(flashMap, req, res);
      } else {    // それ以外のエラーはシステムエラー画面に遷移
        throw ex;
      }
    }
    res.sendRedirect(req.getHeader("referer"));
  }
}
