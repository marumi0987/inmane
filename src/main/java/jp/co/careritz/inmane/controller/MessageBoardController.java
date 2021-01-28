package jp.co.careritz.inmane.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
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
import jp.co.careritz.inmane.dto.MessageViewDto;
import jp.co.careritz.inmane.form.MessageForm;
import jp.co.careritz.inmane.form.MessageReplyForm;
import jp.co.careritz.inmane.model.security.SecurityUserModel;
import jp.co.careritz.inmane.service.MessageBoardService;
import jp.co.careritz.inmane.service.MessageReplyService;

/**
 * メッセージ機能用コントローラ.
 */
@Controller
@RequestMapping("webboard")
public class MessageBoardController extends AbstractAppController {

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
  private static Logger log = LoggerFactory.getLogger(MessageBoardController.class);


  private static final String MESSAGE_LIST = "messageList";
  private static final String MESSAGE_REPLY_LIST = "messageReplyList";
  private static final String MESSAGE_FORM = "messageForm";
  private static final String WEBBOARD = "webboard";
  private static final String WEBBOARD_EDIT = "webboard_edit";
  private static final String WEBBOARD_REPLY = "webboard_reply";
  private static final String REDIRECT_WEBBOARD = "redirect:/webboard/view/";



  // ----------------------------------------------------------------------
  // インスタンスメソッド
  // ----------------------------------------------------------------------

  /**
   * deleteflag=0のメッセージをを表示する.
   *
   * @param model モデル
   * @param form MessageForm
   * @param replyform MessageReplyForm
   * @param userDetails ユーザー情報
   * @return 投稿メッセージ一覧
   */
  @GetMapping("view") // formとかにaction名の指定がなければなにも書かなくてよい。⇒デフォでここが呼ばれる
  public String viewMessage(Model model, @ModelAttribute MessageForm form,
      @ModelAttribute MessageReplyForm replyform,
      @AuthenticationPrincipal SecurityUserModel userDetails) {


    // 検索条件を設定 値をHTMLに渡す。
    String userId = userDetails.getUserId();
    String roleName = userDetails.getRoleName();

    // ボタン表示用の変数
    model.addAttribute("roleName", roleName);
    model.addAttribute("userId", userId);
    model.addAttribute("checkRoleName", AppConst.ROLE_NAME_ADMIN.equals(roleName));


    // messegeリスト取り出し
    List<MessageViewDto> messageList = messageService.findAll();

    // 画面表示用のDtoを作成
    List<MessageViewDto> messageListForView = new ArrayList<MessageViewDto>();

    // Forを用いて、改ざん対策のため各idのハッシュ値を発行し、別途MessageListForViewに入れなおす処理
    for (MessageViewDto dtoFor : messageList) {
      if (log.isDebugEnabled()) {
        log.debug(String.format(dtoFor.getName()));
        log.debug(String.format(dtoFor.getCreatedId()));
      }
      // id+秘密の言葉でハッシュ値を発行
      String id = dtoFor.getId();
      String sha1 = messageService.makeHashCode(id);

      // 画面表示用のDtoにハッシュ値を格納
      dtoFor.setHashedId(sha1);
      messageListForView.add(dtoFor);
    }
    model.addAttribute(MESSAGE_LIST, messageListForView);


    return WEBBOARD;
  }

  /**
   * メッセージ作成処理を実行する.
   *
   * @param model モデル
   * @param form MessageForm
   * @param bindingResult MessageFormのバインド結果の作成(エラーの有無取得)
   * @param replyForm MessageReplyForm
   * @param userDetails ユーザー情報
   * @param redirectAttributes リダイレクト先にパラメータを渡す
   * @return メッセージ登録画面
   */
  @PostMapping("create")
  public String create(Model model, @ModelAttribute @Valid MessageForm form,
      BindingResult bindingResult, @ModelAttribute MessageReplyForm replyForm,
      @AuthenticationPrincipal SecurityUserModel userDetails,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      String userId = userDetails.getUserId();
      String roleName = userDetails.getRoleName();

      // ボタン表示用の変数
      model.addAttribute("roleName", roleName);
      model.addAttribute("userId", userId);
      model.addAttribute("checkRoleName", AppConst.ROLE_NAME_ADMIN.equals(roleName));

      List<MessageViewDto> messageList = messageService.findAll();

      model.addAttribute(MESSAGE_LIST, messageList);

      return WEBBOARD;
    }

    // メッセージ格納用
    MessageBoardDto dto = new MessageBoardDto();
    dto.setId(form.getId());
    dto.setName(form.getName());
    dto.setMessage(form.getMessage());
    dto.setCreatedId(userDetails.getUserId());
    dto.setUpdatedId(userDetails.getUserId());

    int result = messageService.create(dto);

    if (result == 0) {
      // Flashスコープでメッセージを設定
      setCompleteMessageSuccess(redirectAttributes, propertyConfig.get(AppConst.OK));

    } else {
      String errMsg = (result == 1) ? propertyConfig.get(AppConst.DEPLICATED)
          : propertyConfig.get(AppConst.FATAL);

      // 入力内容を再表示するためフォームを再設定
      model.addAttribute(AppConst.APP_COMPLETE_MESSAGE_ID_FAILURE, errMsg);

    }
    return REDIRECT_WEBBOARD;
  }


  /**
   * メッセージIDをもとに該当メッセージの編集画面を表示する.
   *
   * @param model モデル
   * @param form MessageForm
   * @return ユーザ編集画面
   */
  @GetMapping("edit")
  public String viewEdit(Model model, @ModelAttribute MessageForm form,
      @AuthenticationPrincipal SecurityUserModel userDetails,
      RedirectAttributes redirectAttributes) {

    String userId = userDetails.getUserId();
    String roleName = userDetails.getRoleName();
    boolean isAdmin = AppConst.ROLE_NAME_ADMIN.equals(roleName);
    String id = form.getId();
    String createId = form.getCreatedId();



    // idが取得できなかったり、ユーザーが作成者でない場合 かつ 管理者でない場合に404にとぶ
    if (AppConst.EMPTY.equals(id) || !userId.equals(createId) && !isAdmin) {
      // Flashスコープでメッセージを設定
      setCompleteMessageFailure(redirectAttributes, propertyConfig.get(AppConst.INVALID));
      return REDIRECT_WEBBOARD;
    }

    // 該当1件表示
    MessageBoardDto dto = messageService.findByPk(id);

    if (dto == null) {
      return AppConst.REDIRECT_404;
    }


    // formにdtoの値をセットする
    MessageForm editForm = new MessageForm();
    editForm.setId(dto.getId());
    editForm.setName(dto.getName());
    editForm.setMessage(dto.getMessage());
    editForm.setCreatedId(dto.getCreatedId());

    String hashedId = form.getHashedId();
    editForm.setHashedId(hashedId);

    if (log.isDebugEnabled()) {
      log.debug(String.format("Id: %s", editForm.getId()));
      log.debug(String.format("Name: %s", editForm.getName()));
      log.debug(String.format("Message: %s", editForm.getMessage()));
    }
    model.addAttribute(MESSAGE_FORM, editForm);

    return WEBBOARD_EDIT;
  }


  /**
   * メッセージIDをもとに更新処理を実行する.
   *
   * @param model モデル
   * @param form MessageForm
   * @param bindingResult バインド結果の作成(エラーの有無取得)
   * @param userDetails ユーザー詳細が格納されているモデル
   * @param redirectAttributes リダイレクト先にパラメータを渡す
   * @return ユーザ編集画面
   */
  @PostMapping("edit")
  public String update(Model model, @ModelAttribute @Valid MessageForm form,
      BindingResult bindingResult, @AuthenticationPrincipal SecurityUserModel userDetails,
      RedirectAttributes redirectAttributes) {

    String id = form.getId();
    String hashedId = form.getHashedId();

    // id+文字列のハッシュ値を発行
    String sha1 = messageService.makeHashCode(id);

    // formのid+秘密文字列のハッシュ値と、本来のid＋文字列のハッシュ値を比較
    // ハッシュ値が一致しない（別の値を入力された場合） or idが数字じゃなかった場合４０４に飛ぶ
    if (!hashedId.equals(sha1) || !StringUtils.isNumeric(id)) {
      log.debug(sha1);
      log.debug(hashedId);
      // Flashスコープでメッセージを設定
      setCompleteMessageFailure(redirectAttributes, propertyConfig.get(AppConst.INVALID));
      return REDIRECT_WEBBOARD;
    }

    // validエラーがあったらエラー文を表示
    if (bindingResult.hasErrors()) {
      if (log.isDebugEnabled()) {
        bindingResult.getFieldErrors().stream()
            .forEach(f -> log.debug(f.getField(), ": ", f.getDefaultMessage()));
      }
      model.addAttribute(MESSAGE_FORM, form);
      return WEBBOARD_EDIT;
    }


    // formからdtoに値を格納
    MessageBoardDto dto = new MessageBoardDto();
    dto.setId(form.getId());
    dto.setName(form.getName());
    dto.setMessage(form.getMessage());
    dto.setUpdatedId(userDetails.getUserId());


    // dtoの値を元に投稿する
    int result = messageService.updateByPk(dto);


    if (result == 0) {
      // Flashスコープでメッセージを設定
      setCompleteMessageSuccess(redirectAttributes, propertyConfig.get(AppConst.OK));
    } else {
      String errMsg = (result == 1) ? propertyConfig.get(AppConst.DEPLICATED)
          : propertyConfig.get(AppConst.FATAL);
      // 入力内容を再表示するためフォームを再設定

      model.addAttribute(AppConst.APP_COMPLETE_MESSAGE_ID_FAILURE, errMsg);
      return WEBBOARD_EDIT;

    }
    return REDIRECT_WEBBOARD;
  }


  /**
   * 論理削除処理を実行する.
   *
   * @param model モデル
   * @param form MessageForm
   * @param userDetails ユーザー詳細が格納されているモデル
   * @param redirectAttributes リダイレクト先にパラメータを渡す
   * @return 投稿メッセージ一覧
   */
  @PostMapping("delete")
  public String delete(Model model, @ModelAttribute MessageForm form,
      @AuthenticationPrincipal SecurityUserModel userDetails,
      RedirectAttributes redirectAttributes) {

    String userId = userDetails.getUserId();
    String roleName = userDetails.getRoleName();
    boolean isAdmin = AppConst.ROLE_NAME_ADMIN.equals(roleName);
    String id = form.getId();
    String createId = form.getCreatedId();

    // idが取得できなかったり、ユーザーが作成者でない場合 かつ 管理者でない場合にエラーをだす
    if (AppConst.EMPTY.equals(id) || !userId.equals(createId) && !(isAdmin)
        || !StringUtils.isNumeric(id)) {
      // Flashスコープでメッセージを設定
      setCompleteMessageFailure(redirectAttributes, propertyConfig.get(AppConst.INVALID));
      return REDIRECT_WEBBOARD;
    }

    Date now = new Date(System.currentTimeMillis());
    MessageBoardDto dto = new MessageBoardDto();
    dto.setId(id);
    dto.setDeletedId(userDetails.getUserId());
    dto.setDeletedAt(now);

    int result = messageService.deleteByPk(dto);

    if (result == 0) {
      // Flashスコープでメッセージを設定
      setCompleteMessageSuccess(redirectAttributes, propertyConfig.get(AppConst.OK));
    } else {
      // Flashスコープでメッセージを設定
      setCompleteMessageFailure(redirectAttributes, propertyConfig.get(AppConst.FATAL));
    }

    return REDIRECT_WEBBOARD;
  }



  /**
   * 返信画面を表示する.
   *
   * @param model モデル
   * @paramform メッセージIDうけとるフォーム
   *
   * @return 返信画面
   */
  @GetMapping("reply")
  public String viewReply(Model model, @ModelAttribute MessageForm form) {

    String id = form.getId();

    if (AppConst.EMPTY.equals(id) || !StringUtils.isNumeric(id)) {
      return AppConst.REDIRECT_404;
    }

    MessageBoardDto dto = messageService.findByPk(id);

    if (dto == null) {
      return AppConst.REDIRECT_404;
    }

    MessageForm parentForm = new MessageForm();
    parentForm.setId(dto.getId());
    parentForm.setName(dto.getName());
    parentForm.setMessage(dto.getMessage());
    parentForm.setCreatedId(dto.getCreatedId());
    parentForm.setCreatedAt(dto.getCreatedAt());


    model.addAttribute(MESSAGE_FORM, parentForm);
    MessageReplyForm replyForm = new MessageReplyForm();
    replyForm.setMessageId(id);
    model.addAttribute("messageReplyForm", replyForm);


    // replyMessage表示用
    List<MessageReplyDto> messageReplyList = messageReplyService.findAll(id);
    model.addAttribute(MESSAGE_REPLY_LIST, messageReplyList);



    return WEBBOARD_REPLY;
  }

  /**
   * リプライ処理を実行する.
   *
   * @param model モデル
   * @param form MessageReplyForm
   * @param bindingResult バインド結果の作成(エラーの有無取得)
   * @param userDetails ユーザー詳細
   * @param redirectAttributes リダイレクト先にパラメータを渡す
   * @return メッセージ投稿画面
   */
  @PostMapping("reply")
  public String reply(Model model, @ModelAttribute @Valid MessageReplyForm replyform,
      BindingResult bindingResult, @ModelAttribute MessageForm form,
      @AuthenticationPrincipal SecurityUserModel userDetails,
      RedirectAttributes redirectAttributes) {

    String id = replyform.getMessageId();
    if (!StringUtils.isNumeric(id)) {
      // Flashスコープでメッセージを設定
      setCompleteMessageFailure(redirectAttributes, propertyConfig.get(AppConst.INVALID));
      return REDIRECT_WEBBOARD;
    }

    if (bindingResult.hasErrors()) {

      MessageReplyForm replyForm = new MessageReplyForm();
      replyForm.setMessageId(id);

      MessageBoardDto dto = messageService.findByPk(id);
      form.setId(dto.getId());
      form.setName(dto.getName());
      form.setMessage(dto.getMessage());
      form.setCreatedId(dto.getCreatedId());
      form.setCreatedAt(dto.getCreatedAt());

      model.addAttribute(MESSAGE_FORM, form);


      // replyMessage表示用
      List<MessageReplyDto> messageReplyList = messageReplyService.findAll(id);
      model.addAttribute(MESSAGE_REPLY_LIST, messageReplyList);

      return WEBBOARD_REPLY;
    }
    MessageBoardDto dto = messageService.findByPk(id);
    form.setId(dto.getId());
    form.setName(dto.getName());
    form.setMessage(dto.getMessage());
    form.setCreatedId(dto.getCreatedId());
    form.setCreatedAt(dto.getCreatedAt());

    model.addAttribute(MESSAGE_FORM, form);


    MessageReplyDto replyDto = new MessageReplyDto();
    replyDto.setId(replyform.getId());
    replyDto.setName(replyform.getName());
    replyDto.setMessageId(replyform.getMessageId());
    replyDto.setMessage(replyform.getMessage());
    replyDto.setCreatedId(userDetails.getUserId());

    int result = messageReplyService.create(replyDto);


    if (result == 0) {
      // Flashスコープでメッセージを設定
      setCompleteMessageSuccess(redirectAttributes, propertyConfig.get(AppConst.OK));


    } else {
      String errMsg = (result == 1) ? propertyConfig.get(AppConst.DEPLICATED)
          : propertyConfig.get(AppConst.FATAL);

      // 入力内容を再表示するためフォームを再設定
      model.addAttribute(AppConst.APP_COMPLETE_MESSAGE_ID_FAILURE, errMsg);

    }

    return "redirect:/webboard/reply?id=" + dto.getId();
  }


  /**
   * ログインしているユーザ宛の返信画面を表示する.
   *
   * @param model モデル
   * @paramform メッセージIDうけとるフォーム
   *
   * @return 返信画面
   */
  @GetMapping("replyToMe")
  public String viewReplyToMe(Model model, @ModelAttribute MessageForm form,
      @AuthenticationPrincipal SecurityUserModel userDetails,
      RedirectAttributes redirectAttributes) {

    String userId = userDetails.getUserId();

    if (AppConst.EMPTY.equals(userId) || !StringUtils.isNumeric(userId)) {
      setCompleteMessageFailure(redirectAttributes, propertyConfig.get(AppConst.INVALID));
      return REDIRECT_WEBBOARD;
    }

    // replyMessage表示用
    List<MessageReplyDto> messageReplyList = messageReplyService.findReplyToMe(userId);
    model.addAttribute(MESSAGE_REPLY_LIST, messageReplyList);

    return WEBBOARD_REPLY + "_to_me";
  }


}

