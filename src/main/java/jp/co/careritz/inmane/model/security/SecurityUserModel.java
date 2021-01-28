package jp.co.careritz.inmane.model.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jp.co.careritz.inmane.dto.UsersDto;

public class SecurityUserModel extends UsersDto implements Serializable, UserDetails {

  /** シリアルバージョンUID. */
  private static final long serialVersionUID = 1L;

  /** ロック状態. */
  private boolean accountLocked = false;

  private List<SimpleGrantedAuthority> authorities;

  /** ユーザIDを返す. */
  @Override
  public String getUsername() {
    return super.getUserId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !accountLocked;
  }

  public void setAccountLocked(boolean accountLocked) {
    this.accountLocked = accountLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
    this.authorities = authorities;
  }

  /** ユーザ名（IDではない）を返す. */
  @Override
  public String getUserName() {
    return super.getUserName();
  }
}
