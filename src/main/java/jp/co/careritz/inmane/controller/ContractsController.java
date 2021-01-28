package jp.co.careritz.inmane.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jp.co.careritz.inmane.controller.commons.AbstractAppController;

/**
 * 契約管理コントローラ.
 */
@Controller
@RequestMapping(value = "contracts")
public class ContractsController extends AbstractAppController {
  @GetMapping
  public String showContractsList(Model model) {
    return "contracts_search";
  }
}
