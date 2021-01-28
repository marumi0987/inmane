package jp.co.careritz.inmane.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * ユーザ情報検索用フォーム.
 */
@Data
public class UsersSearchForm {

  /** ユーザID. */
  @Size(max = 10, message = "{errors.validation.MaxSize.message}")
  @Pattern(regexp = "[a-zA-Z0-9]*", message = "{errors.validation.HalfWidthAlphanumeric.message}")
  private String userId;
  /** ユーザ名. */
  @Size(max = 20, message = "{errors.validation.MaxSize.message}")
  private String userName;
  /** ロール名. */
  @NotEmpty(message = "{errors.validation.Required.message}")
  @Pattern(regexp = "ALL|USER|ADMIN", message = "{errors.validation.IllegalData.message}")
  private String roleName;



}
