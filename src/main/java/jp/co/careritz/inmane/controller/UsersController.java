package jp.co.careritz.inmane.controller;

import static jp.co.careritz.inmane.constant.AppConst.*;
import static jp.co.careritz.inmane.util.AppUtil.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jp.co.careritz.inmane.config.PropertyConfig;
import jp.co.careritz.inmane.constant.AppConst;
import jp.co.careritz.inmane.controller.commons.AbstractAppController;
import jp.co.careritz.inmane.dto.UsersDto;
import jp.co.careritz.inmane.form.UsersCreateForm;
import jp.co.careritz.inmane.form.UsersSearchForm;
import jp.co.careritz.inmane.model.security.SecurityUserModel;
import jp.co.careritz.inmane.service.UsersService;

/**
 * ユーザ管理コントローラ.
 */
@Controller
@RequestMapping("maintenance/users")
public class UsersController extends AbstractAppController {


  // ----------------------------------------------------------------------
  // インスタンス変数
  // ----------------------------------------------------------------------
  @Autowired
  private PropertyConfig propertyConfig;
  @Autowired
  UsersService usersService;
  // ロガー
  static Logger log = LoggerFactory.getLogger(UsersController.class);



  private static final String USERS_CREATE_FORM = "usersCreateForm";
  private static final String USERS_DETAIL = "users_detail";
  private static final String USERS_DETAIL_USERID = "redirect: /maintenance/users/detail?userId=";
  private static final String USERS_CREATE = "users_create";
  private static final String OK = "ok.app.complete";
  private static final String FATAL = "error.app.fatal";
  private static final String DEPLICATED = "error.app.user.deplicated";

  private static final String REDIRECT_404 = "redirect: /error/404";
  // ----------------------------------------------------------------------
  // インスタンスメソッド
  // ----------------------------------------------------------------------


  /**
   * ユーザー検索.
   *
   * @param model モデル
   * @param form UsersSearchForm
   * @param bindingResult バインド結果の作成(エラーの有無取得)
   * @return ユーザ検索画面
   */
  @GetMapping("search")
  public String viewSearch(Model model, @ModelAttribute @Valid UsersSearchForm form,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("UsersSearchForm", form);
      model.addAttribute("usersList", new ArrayList<UsersDto>());
      return "users_search";
    }

    UsersDto dto = new UsersDto();

    dto.setUserId(form.getUserId());
    dto.setUserName(form.getUserName());
    dto.setRoleName(AppConst.ROLE_NAME_ALL.equals(form.getRoleName()) ? EMPTY : form.getRoleName());

    List<UsersDto> usersList = usersService.find(dto);

    // 検索条件を設定
    model.addAttribute("usersSearchForm", form);
    // 検索結果を設定
    model.addAttribute("usersList", usersList);

    return "users_search";
  }


  /**
   * ユーザ登録画面を表示する.
   *
   * @param model モデル
   * @return ユーザ作成画面
   */
  @GetMapping("new")
  public String viewCreate(Model model) {

    UsersCreateForm form = new UsersCreateForm();
    form.setRoleName(ROLE_NAME_USER);

    model.addAttribute(USERS_CREATE_FORM, form);

    return USERS_CREATE;
  }


  /**
   * ユーザー登録.
   *
   * @param model モデル
   * @param form UsersCreateForm
   * @param bindingResult UsersCreateFormのバインド結果の作成(エラーの有無取得)
   * @param userDetails ユーザー情報
   * @param redirectAttributes リダイレクト先にパラメータを渡す
   * @param req HttpServletRequest
   * @param res HttpServletResponse
   * @return
   */
  @PostMapping("new")
  public String create(Model model, @ModelAttribute @Valid UsersCreateForm form,
      BindingResult bindingResult, @AuthenticationPrincipal SecurityUserModel userDetails,
      RedirectAttributes redirectAttributes, HttpServletRequest req, HttpServletResponse res) {

    if (bindingResult.hasErrors()) {
      model.addAttribute(USERS_CREATE_FORM, form);
      return USERS_CREATE;
    }

    final String prefix = propertyConfig.get("user.defaultpass.prefix");
    final String password = new BCryptPasswordEncoder().encode(prefix + form.getUserId());

    Date now = new Date(System.currentTimeMillis());
    UsersDto dto = new UsersDto();
    dto.setUserId(form.getUserId());
    dto.setPassword(password);
    dto.setUserName(form.getUserName());
    dto.setRoleName(form.getRoleName());

    dto.setCreaterId(userDetails.getUserId());
    dto.setCreatedAt(now);

    int result = usersService.create(dto);

    if (result == 0) {
      // Flashスコープでメッセージを設定
      setCompleteMessageSuccess(redirectAttributes, propertyConfig.get(OK));
    } else {
      String errMsg = (result == 1) ? propertyConfig.get(DEPLICATED) : propertyConfig.get(FATAL);
      // 入力内容を再表示するためフォームを再設定
      model.addAttribute(USERS_CREATE_FORM, form);
      model.addAttribute(APP_COMPLETE_MESSAGE_ID_FAILURE, errMsg);
      return USERS_CREATE;
    }
    return USERS_DETAIL_USERID + dto.getUserId();
  }

  /**
   * ユーザ詳細画面を表示する.
   *
   * @param model モデル
   * @param userId ユーザーID
   * @return ユーザ詳細画面
   */
  @GetMapping("detail")
  public String viewDetail(Model model,
      // 画面の操作？URL?でuserIdを別画面に引き渡したいときにRequestPramを用いる
      @RequestParam(name = "userId", defaultValue = EMPTY) String userId) {

    if (EMPTY.equals(userId)) {
      return REDIRECT_404;
    }

    UsersDto dto = usersService.findByPk(userId);

    UsersCreateForm form = new UsersCreateForm();
    form.setUserId(dto.getUserId());
    form.setUserName(dto.getUserName());
    form.setRoleName(dto.getRoleName());
    form.setLoginFailureCount(dto.getLoginFailureCount());
    form.setLoginDeniedAt(convDateToStr(dto.getLoginDeniedAt(), DATE_PATTERN_STD));
    form.setUpdaterId(dto.getUpdaterId());
    form.setUpdatedAt(convDateToStr(dto.getUpdatedAt(), DATE_PATTERN_STD));
    form.setCreaterId(dto.getCreaterId());
    form.setCreatedAt(convDateToStr(dto.getCreatedAt(), DATE_PATTERN_STD));

    model.addAttribute(USERS_CREATE_FORM, form);

    return USERS_DETAIL;
  }


  /**
   * ユーザ編集画面を表示する.
   *
   * @param model モデル
   * @param userId ユーザーID
   * @return ユーザ編集画面
   */
  @GetMapping("edit")
  public String viewEdit(Model model,
      @RequestParam(name = "userId", defaultValue = EMPTY) String userId) {

    return viewDetail(model, userId);
  }


  /**
   * ユーザ更新処理を実行する.
   *
   * @param model モデル
   * @param form UsersCreateForm
   * @param bindingResult UsersCreateFormのバインド結果の作成(エラーの有無取得)
   * @param userDetails ユーザー情報
   * @param redirectAttributes リダイレクト先にパラメータを渡す
   * @return ユーザ編集画面
   */
  @PostMapping("edit")
  public String update(Model model, @ModelAttribute @Valid UsersCreateForm form,
      BindingResult bindingResult, @AuthenticationPrincipal SecurityUserModel userDetails,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      bindingResult.getFieldErrors().stream()
          .forEach(f -> log.debug(f.getField(), ": ", f.getDefaultMessage()));
      model.addAttribute(USERS_CREATE_FORM, form);
      return USERS_DETAIL;
    }

    final String password = new BCryptPasswordEncoder().encode(form.getPassword());

    Date now = new Date(System.currentTimeMillis());
    UsersDto dto = new UsersDto();
    dto.setUserId(form.getUserId());
    dto.setPassword(password);
    dto.setUserName(form.getUserName());
    dto.setRoleName(form.getRoleName());

    dto.setUpdaterId(userDetails.getUserId());
    dto.setUpdatedAt(now);

    int result = usersService.updateByPk(dto);

    if (result == 0) {
      // Flashスコープでメッセージを設定
      setCompleteMessageSuccess(redirectAttributes, propertyConfig.get(OK));
    } else {
      String errMsg = (result == 1) ? propertyConfig.get(DEPLICATED) : propertyConfig.get(FATAL);
      // 入力内容を再表示するためフォームを再設定
      model.addAttribute(USERS_CREATE_FORM, form);
      model.addAttribute(APP_COMPLETE_MESSAGE_ID_FAILURE, errMsg);
      return USERS_DETAIL;
    }
    return USERS_DETAIL_USERID + dto.getUserId();
  }


  /**
   * ユーザ削除 処理を実行する.
   *
   * @param model モデル
   * @param userId ユーザーID
   * @param userDetails ユーザー情報
   * @param redirectAttributes リダイレクト先にパラメータを渡す
   * @return ユーザ削除画面
   */
  @PostMapping("delete")
  public String delete(Model model,
      @RequestParam(name = "userId", defaultValue = EMPTY) String userId,
      @AuthenticationPrincipal SecurityUserModel userDetails,
      RedirectAttributes redirectAttributes) {

    if (EMPTY.equals(userId)) {
      return REDIRECT_404;
    }

    int result = usersService.deleteByPk(userId);

    if (result == 0) {
      // Flashスコープでメッセージを設定
      setCompleteMessageSuccess(redirectAttributes, propertyConfig.get(OK));
      return "redirect: /maintenance/users/search?roleName=ALL";
    } else {
      // Flashスコープでメッセージを設定
      setCompleteMessageFailure(redirectAttributes, propertyConfig.get(FATAL));
      return USERS_DETAIL_USERID + userId;
    }
  }



}
