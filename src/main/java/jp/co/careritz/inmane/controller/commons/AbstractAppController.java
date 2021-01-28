package jp.co.careritz.inmane.controller.commons;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jp.co.careritz.inmane.constant.AppConst;
import jp.co.careritz.inmane.model.security.SecurityUserModel;


/**
 * アプリケーション基底コントローラ.
 */
@Controller
public abstract class AbstractAppController {

  // --------------------------------------------------------------------------
  // 共通定義
  // --------------------------------------------------------------------------
  /**
   * コントローラ共通処理.
   *
   * @param req HTTPサーブレットリクエスト
   * @param model モデル
   */
  @ModelAttribute
  public void initModel(HttpServletRequest req, Model model) {
    // セッションからユーザ情報を取得
    SecurityUserModel user = getSessionUser();
    String userName = user != null ? user.getUserName() : "anonymousUser";
    String userRole = user != null ? user.getRoleName() : "NONE";

    // ログインユーザ名
    model.addAttribute("sessionUserName", userName);
    model.addAttribute("sessionUserRole", userRole);
  }


  /**
   * ログインユーザ情報をセッションから取得します.
   *
   * @return ログインユーザモデル.
   */
  public SecurityUserModel getSessionUser() {

    Object sessionData = null;

    if (SecurityContextHolder.getContext() != null
        && SecurityContextHolder.getContext().getAuthentication() != null) {
      sessionData = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      if ("anonymousUser".equals(sessionData)) {
        return null;
      }

    }
    return (SecurityUserModel) sessionData;
  }

  // --------------------------------------------------------------------------
  // 同期レスポンス関連メソッド
  // --------------------------------------------------------------------------
  /**
   * 同期処理正常終了メッセージを設定します.
   *
   * @param attributes リダイレクトトリビュート
   * @param message 終了メッセージ
   * @return 正常終了レスポンスデータ
   */
  protected void setCompleteMessageSuccess(RedirectAttributes attributes, String message) {
    attributes.addFlashAttribute(AppConst.APP_COMPLETE_MESSAGE_ID_SUCCESS, message);
  }

  /**
   * 同期処理警告終了メッセージを設定します.
   *
   * @param attributes リダイレクトトリビュート
   * @param message 終了メッセージ
   */
  protected void setCompleteMessageWarning(RedirectAttributes attributes, String message) {
    attributes.addFlashAttribute(AppConst.APP_COMPLETE_MESSAGE_ID_WARNING, message);
  }

  /**
   * 同期処理例外終了メッセージを設定します.
   *
   * @param attributes リダイレクトトリビュート
   * @param message 終了メッセージ
   */
  protected void setCompleteMessageFailure(RedirectAttributes attributes, String message) {
    attributes.addFlashAttribute(AppConst.APP_COMPLETE_MESSAGE_ID_FAILURE, message);
  }

}
