package jp.co.careritz.inmane.controller.commons;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * ログインコントローラ.
 */
@Controller

public class LoginController extends AbstractAppController {

  // ----------------------------------------------------------------------
  // インスタンスメソッド
  // ----------------------------------------------------------------------
  /**
   * 初期読込処理.
   */
  @GetMapping("login")
  public String index(Principal principal) {

    return "common/login";
  }

  @GetMapping("top")
  public String top(Model model) {
    return "common/top";
  }
}
