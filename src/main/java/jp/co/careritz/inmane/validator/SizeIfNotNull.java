package jp.co.careritz.inmane.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import jp.co.careritz.inmane.validator.SizeIfNotNull.SizeIfNullValidator;

@Documented
@Constraint(validatedBy = {SizeIfNullValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface SizeIfNotNull {
  /**
   * エラーメッセージ（デフォルト）.
   */
  String message() default "{javax.validation.constraints.Size.message}";

  /**
   * グループ（デフォルト）.
   */
  Class<?>[] groups() default {};

  /**
   * ペイロード（デフォルト）.
   */
  Class<? extends Payload>[] payload() default {};

  /**
   * 最小サイズ.
   * 
   * @return size the element must be higher or equal to
   */
  int min() default 0;

  /**
   * 最大サイズ.
   * 
   * @return size the element must be lower or equal to
   */
  int max() default Integer.MAX_VALUE;

  /**
   * Defines several {@link Size} annotations on the same element.
   *
   * @see Size
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    SizeIfNotNull[] value();
  }

  /**
   * nullまたは空文字除外のサイズチェックバリデーター.
   */
  class SizeIfNullValidator implements ConstraintValidator<SizeIfNotNull, String> {

    private int min;
    private int max;

    /**
     * 初期処理.
     * 
     * @param annotation アノテーションからの入力値
     */
    @Override
    public void initialize(SizeIfNotNull annotation) {
      min = annotation.min();
      max = annotation.max();
    }

    /**
     * 検証処理.
     * 
     * @param value 入力値
     * @param ctx コンテキスト
     * @return true - 制約を満たす。 false - 制約を満たさない。
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
      // nullまたは空文字は対象外。nullではない場合はサイズをチェック
      return value == null || value.isEmpty() ? true
          : min <= value.length() && value.length() <= max;
    }
  }
}
