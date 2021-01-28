package jp.co.careritz.inmane.controller.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.NestedServletException;

/**
 * アプリケーション共通エラー制御.
 */
@Controller
@RequestMapping("error")
public class AppErrorController implements ErrorController {

  // ----------------------------------------------------------------------
  // インスタンスメソッド
  // ----------------------------------------------------------------------
  /**
   * 初期読込処理.
   */
  @GetMapping
  public String index(HttpServletRequest request, HttpServletResponse response, Model model) {

    /* ---------------------------------------------------- */
    /* ローカル変数の宣言と初期化 */
    /* ---------------------------------------------------- */
    // システム例外例外
    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");

    /* ---------------------------------------------------- */
    /* 403エラーの処理 */
    /* ---------------------------------------------------- */
    if (response.getStatus() == HttpServletResponse.SC_FORBIDDEN) {
      return "redirect:/error/403";
    }

    /* ---------------------------------------------------- */
    /* 404エラーの処理 */
    /* ---------------------------------------------------- */
    if (response.getStatus() == HttpServletResponse.SC_NOT_FOUND) {
      return "redirect:/error/404";
    }

    /* ---------------------------------------------------- */
    /* 例外処理 */
    /* ---------------------------------------------------- */
    // NestedServletExceptionでラップされている場合の対応
    if (throwable != null && throwable instanceof NestedServletException
        && throwable.getCause() != null) {
      throwable.getCause();
    }

    return "redirect:/error/500";
  }

  @Override
  public String getErrorPath() {
    return "error";
  }

  /**
   * システムエラー画面.
   */
  @GetMapping("500")
  public String status500(HttpServletRequest request, HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    return "common/500";
  }

  /**
   * 403エラー画面.
   */
  @GetMapping("403")
  public String status403(HttpServletRequest request, HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    // 403エラーは404画面と同じ
    return "common/403";
  }

  /**
   * 404エラー画面.
   */
  @GetMapping("404")
  public String status404(HttpServletRequest request, HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return "common/404";
  }
}