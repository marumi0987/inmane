package jp.co.careritz.inmane.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jp.co.careritz.inmane.controller.commons.AbstractAppController;

/**
 * 稼働状況情報コントローラ.
 */
@Controller
@RequestMapping("assignments")
public class AssignmentsController extends AbstractAppController {

  @GetMapping("assignments")
  public String showStaffList(Model model) {
    return "assignments_search";
  }
}
