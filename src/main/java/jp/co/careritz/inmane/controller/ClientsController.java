package jp.co.careritz.inmane.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jp.co.careritz.inmane.controller.commons.AbstractAppController;

/**
 * 取引先管理コントローラ.
 */
@Controller
@RequestMapping("clients")
public class ClientsController extends AbstractAppController {
  @GetMapping
  public String showStaffList(Model model) {
    return "clients_search";
  }
}
