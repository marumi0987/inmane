package jp.co.careritz.inmane.form;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;


/**
 * メッセージ返信用Form.
 */
@Data
public class MessageReplyForm {

  /** ID. */
  private String id;

  /** メッセージID. */
  @NotEmpty(message = "{errors.validation.Required.message}")
  private String messageId;

  /** ユーザ名. */
  @NotEmpty(message = "{errors.validation.Required.message}")
  @Size(min = 1, max = 50, message = "{errors.validation.SizeRange.message}")
  private String name;

  /** メッセージ. */
  @NotEmpty(message = "{errors.validation.Required.message}")
  @Size(min = 1, max = 100, message = "{errors.validation.SizeRange.message}")
  private String message;

  /** 作成者ID. */
  private String createdId;

  /** 作成日時. */
  private Date createdAt;



}
