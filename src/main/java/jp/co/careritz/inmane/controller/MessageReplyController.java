package jp.co.careritz.inmane.controller;

import java.util.Date;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jp.co.careritz.inmane.config.PropertyConfig;
import jp.co.careritz.inmane.constant.AppConst;
import jp.co.careritz.inmane.controller.commons.AbstractAppController;
import jp.co.careritz.inmane.dto.MessageBoardDto;
import jp.co.careritz.inmane.dto.MessageReplyDto;
import jp.co.careritz.inmane.form.MessageForm;
import jp.co.careritz.inmane.model.security.SecurityUserModel;
import jp.co.careritz.inmane.service.MessageBoardService;
import jp.co.careritz.inmane.service.MessageReplyService;

/**
 * メッセージリプライ用コントローラ.
 */
@Controller

// Controllerの切り分けとして
// 掲示板画面から呼ばれる動き→MessageReplyController
// 返信画面から呼ばれる動き→MessageReplyController として切り分けて
// 共通となる 返信機能 は serviceの同じメソッドを呼び出すようにするのがよい
@RequestMapping("webboard_reply") // このURLのものが呼ばれたらここが動くよ

public class MessageReplyController extends AbstractAppController {

  // ----------------------------------------------------------------------
  // インスタンス変数
  // ----------------------------------------------------------------------

  @Autowired
  private PropertyConfig propertyConfig;
  @Autowired
  private MessageBoardService messageService;
  @Autowired
  private MessageReplyService messageReplyService;

  // ロガー
  static Logger log = LoggerFactory.getLogger(MessageReplyController.class);

  private static final String MESSAGE_FORM = "messageForm";
  private static final String WEBBOARD_REPLY = "webboard_reply";
  private static final String REDIRECT_WEBBOARD_REPLY = "redirect:/webboard";


  // ----------------------------------------------------------------------
  // インスタンスメソッド
  // ----------------------------------------------------------------------



  /**
   * メッセージ編集画面を表示する.(以下は実装途中です↓)
   *
   * @param model モデル
   * @param form メッセージフォーム
   * @return メッセージ編集画面
   */
  @GetMapping("editReply")
  public String viewEdit(Model model, @ModelAttribute @Valid MessageForm form) {

    String id = form.getId();
    if (AppConst.EMPTY.equals(id)) {
      return AppConst.REDIRECT_404;
    }
    if (log.isDebugEnabled() && id != null) {
      log.debug("-------------------------edit---------------");
      log.debug(String.format("id: %s", id));
    }
    MessageReplyDto dto = messageReplyService.findByPk(id);

    if (dto == null) {
      return AppConst.REDIRECT_404;
    }

    MessageForm editform = new MessageForm();
    editform.setId(dto.getId());
    editform.setName(dto.getName());
    editform.setMessage(dto.getMessage());
    editform.setCreatedId(dto.getCreatedId());
    editform.setCreatedAt(dto.getCreatedAt());

    model.addAttribute(MESSAGE_FORM, editform);

    return "webboard_edit";
  }


  /**
   * メッセージ更新処理を実行する.
   *
   * @param model モデル
   * @param form メッセージForm
   * @param bindingResult バインド結果の作成(エラーの有無取得)
   * @param userDetails ユーザー情報取得
   * @param redirectAttributes リダイレクト先にパラメータを渡す
   * @return メッセージ編集画面
   */
  @PostMapping("editReply")
  public String update(Model model, @ModelAttribute @Valid MessageForm form,
      BindingResult bindingResult, @AuthenticationPrincipal SecurityUserModel userDetails,
      RedirectAttributes redirectAttributes) {

    String id = form.getId();
    // エラーの有無を取得
    if (bindingResult.hasErrors() || !StringUtils.isNumeric(id)) {
      bindingResult.getFieldErrors().stream()
          .forEach(f -> log.debug(f.getField(), ": ", f.getDefaultMessage()));
      model.addAttribute(MESSAGE_FORM, form);
      return WEBBOARD_REPLY;
    }

    Date now = new Date(System.currentTimeMillis());
    MessageReplyDto dto = new MessageReplyDto();
    dto.setId(form.getId());
    dto.setName(form.getName());
    dto.setMessage(form.getMessage());

    dto.setCreatedId(userDetails.getUserId());
    dto.setCreatedAt(now);


    int result = messageReplyService.updateByPk(dto);

    if (result == 0) {
      // Flashスコープでメッセージを設定
      setCompleteMessageSuccess(redirectAttributes, propertyConfig.get(AppConst.OK));
    } else {
      String errMsg = (result == 1) ? propertyConfig.get(AppConst.DEPLICATED)
          : propertyConfig.get(AppConst.FATAL);
      // 入力内容を再表示するためフォームを再設定

      model.addAttribute(AppConst.APP_COMPLETE_MESSAGE_ID_FAILURE, errMsg);
      return "webboard_edit";
    }
    return REDIRECT_WEBBOARD_REPLY;

  }


  /**
   * メッセージ削除処理を実行する.
   *
   * @param model モデル
   * @param form メッセージフォーム
   * @param userDetails ユーザー情報
   * @param redirectAttributes リダイレクト先にパラメータを渡す
   * @return メッセージ削除画面
   */
  @PostMapping("deleteReply")
  public String delete(Model model, @ModelAttribute MessageForm form,
      @AuthenticationPrincipal SecurityUserModel userDetails,
      RedirectAttributes redirectAttributes) {

    String id = form.getId();
    // IDが空や数字でないときに404に飛ばす
    if (AppConst.EMPTY.equals(id) || !StringUtils.isNumeric(id)) {
      return AppConst.REDIRECT_404;
    }
    MessageBoardDto dto = new MessageBoardDto();
    int result = messageService.deleteByPk(dto);

    if (result == 0) {
      // Flashスコープでメッセージを設定
      setCompleteMessageSuccess(redirectAttributes, propertyConfig.get(AppConst.OK));
    } else {
      // Flashスコープでメッセージを設定
      setCompleteMessageFailure(redirectAttributes, propertyConfig.get(AppConst.FATAL));
    }

    return REDIRECT_WEBBOARD_REPLY;
  }
}
