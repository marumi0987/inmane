package jp.co.careritz.inmane.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jp.co.careritz.inmane.controller.commons.AbstractAppController;

/**
 * 案件管理コントローラ.
 */
@Controller
@RequestMapping(value = "projects")
public class ProjectsController extends AbstractAppController {
  @GetMapping
  public String showStaffList(Model model) {
    return "projects_search";
  }
}
