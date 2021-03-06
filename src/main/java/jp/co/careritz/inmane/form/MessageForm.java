package jp.co.careritz.inmane.form;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * メッセージFORM.
 */
@Data
public class MessageForm {

  /** ID. */
  private String id;

  /** ハッシュ化したID. */
  private String hashedId;

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

  /** データファイル. */
  private Byte[] fileData;



}
