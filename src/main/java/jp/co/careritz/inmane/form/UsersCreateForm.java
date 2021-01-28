package jp.co.careritz.inmane.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import jp.co.careritz.inmane.validator.SizeIfNotNull;
import lombok.Data;

/**
 * ユーザ情報登録用フォーム.
 */
@Data
public class UsersCreateForm {

  /** ユーザID. */
  @NotEmpty(message = "{errors.validation.Required.message}")
  @Size(min = 5, max = 10, message = "{errors.validation.SizeRange.message}")
  @Pattern(regexp = "[a-zA-Z0-9]*", message = "{errors.validation.HalfWidthAlphanumeric.message}")
  private String userId;
  /** パスワード. */
  @Pattern(regexp = "[a-zA-Z0-9]*", message = "{errors.validation.HalfWidthAlphanumeric.message}")
  @SizeIfNotNull(min = 4, max = 10, message = "{errors.validation.SizeRange.message}")
  private String password;
  /** ユーザ名. */
  @Size(min = 1, max = 20, message = "{errors.validation.SizeRange.message}")
  private String userName;
  /** ロール名. */
  @NotEmpty(message = "{errors.validation.Required.message}")
  @Pattern(regexp = "USER|ADMIN", message = "{errors.validation.IllegalData.message}")
  private String roleName;
  /** ログイン失敗回数. */
  private int loginFailureCount;
  /** ログイン拒否時間. */
  private String loginDeniedAt;

  /** 更新者ID. */
  private String updaterId;
  /** 更新日時. */
  private String updatedAt;
  /** 作成者ID. */
  private String createrId;
  /** 作成日時. */
  private String createdAt;


}
